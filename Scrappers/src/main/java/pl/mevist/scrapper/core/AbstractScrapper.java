package pl.mevist.scrapper.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mevist.scrapper.core.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScrapper {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected AbstractSearch search;
    protected AbstractVehicleMapper vehicleMapper;
    protected AbstractPriceMapper priceMapper;

    protected final List<UnparsedVehicleError> failedVehicles = new ArrayList<>();
    protected final List<BaseOffer> invalidOffers = new ArrayList<>();

    private final String USER_AGENT = "Mozilla/5.0";

    private final List<BaseOffer> offers = new ArrayList<>();
    private final List<BaseVehicle> vehicles = new ArrayList<>();
    private int maxOffersLimit = -1;
    private final int globalCount = 0;


    // Abstract
    protected abstract Elements getElementsInPage(Document doc);
    protected abstract BaseOffer parseOffer(Element element);
    protected abstract BaseVehicleDetails parseVehicleDetails(Element element);
    protected abstract BaseVehicle parseVehicleName(Element element);

    protected abstract int findMaxPageIndex(Document doc);

    // Provided
    protected Document getBaseDocument(int page) throws IOException {
        String searchUrl = search.buildSearchUrl(page);
        return Jsoup.connect(searchUrl).userAgent(USER_AGENT).get();
    }

    protected Document getFirstDocumentPage() throws IOException {
        return getBaseDocument(1);
    }

    protected BaseVehicle parseVehicle(Element element) {
        BaseVehicle vehicle = parseVehicleName(element);
        BaseVehicleDetails vehicleDetails = parseVehicleDetails(element);
        vehicle.setDetails(vehicleDetails);
        return vehicle;
    }

    public final void processAll() {
        try {
            Document first = getFirstDocumentPage();
            search.setMaxPage(findMaxPageIndex(first));
            int max = search.getMaxPage();

            logger.info("Starting scrape: up to {} pages, limit {} offers", max, maxOffersLimit);

            for (int page = 1; page <= max; page++) {
                if (maxOffersLimit > 0 && offers.size() >= maxOffersLimit) {
                    logger.info("Offer limit {} reached before page {}", maxOffersLimit, page);
                    break;
                }
                search.setPage(page);
                boolean limitReached = processPage(page);

                if (limitReached) {
                    logger.info("Offer limit {} reached on page {}", maxOffersLimit, page);
                    break;
                }
            }

            logger.info("Found {} offers after processing {} pages", offers.size(), search.getPage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            failedVehicleParsingLogging();
            invalidOfferParsingLogging();
        }
    }

    public final boolean processPage(int page) {
        try {
            Document document = getBaseDocument(page);
            Elements elements = getElementsInPage(document);

            logger.debug("Page {}: {} elements to process", page, elements.size());

            int count = 0;
            for (Element el : elements) {
                // Early-stop guard
                if (maxOffersLimit > 0 && offers.size() >= maxOffersLimit) {
                    return true; // limit reached
                }

                count++;
                if (logger.isTraceEnabled()) {
                    logger.trace("Processing element {}/{} on page {}", count, elements.size(), page);
                }

                BaseOffer offer = parseOffer(el);
                BaseVehicle vehicle = parseVehicle(el);
                offer.setVehicle(vehicle);

                if (offer.isValid()) {
                    offers.add(offer);
                } else {
                    invalidOffers.add(offer);
                }

                // Optional: throttle verbose progress logs
                if (logger.isDebugEnabled() && (count % 50 == 0 || count == elements.size())) {
                    logger.debug("Processed {}/{} elements on page {}", count, elements.size(), page);
                }
            }

            saveIntoFile();
            return false; // limit not reached
        } catch (IOException e) {
            throw new RuntimeException("Scraping failed", e);
        }
    }

    protected void logParseError(Elements htmlContext, BaseRawVehicleDetails rawDetails, Exception e) {
        StringBuilder builder = new StringBuilder();
        htmlContext.stream().map(Element::text).forEach(builder::append);
        failedVehicles.add(new UnparsedVehicleError(rawDetails, e.getMessage(), e));
        logger.warn("Failed to sanitize raw data. Html context: '{}'", builder, e);
    }

    // Private
    private void failedVehicleParsingLogging() {
        StringBuilder error = new StringBuilder();
        if (!failedVehicles.isEmpty()) {
            error.append("------- Failed Vehicles -------\n");
            failedVehicles.forEach(failedVehicle -> {
                error.append("-------START-------\n")
                        .append(failedVehicle.toString())
                        .append("\n-------END-------\n\n");
            });
            logger.warn(error.toString());
        }
    }

    private void invalidOfferParsingLogging() {
        StringBuilder error = new StringBuilder();
        if (!invalidOffers.isEmpty()) {
            error.append("------- Invalid Offers -------\n");
            invalidOffers.forEach(invalidOffer -> {
                error.append("-------START-------\n")
                        .append(invalidOffer.toString())
                        .append("\n-------END-------\n\n");
            });
            logger.warn(error.toString());
        }
    }

    private void saveIntoFile(){
        // TODO
        //  Use offers to store data into external file
    }

    // Constructors
    public AbstractScrapper(AbstractSearch search) {
        this.search = search;
    }


    // Getters, Setters
    public int getMaxOffersLimit() {
        return maxOffersLimit;
    }

    public void setMaxOffersLimit(int maxOffersLimit) {
        this.maxOffersLimit = maxOffersLimit;
    }

    public List<BaseOffer> getOffers() {
        return offers;
    }
}

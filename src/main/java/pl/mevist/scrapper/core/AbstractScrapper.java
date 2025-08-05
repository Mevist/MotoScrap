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

    public final void processAll(){
        try {
            Document document = getFirstDocumentPage();
            search.setMaxPage(findMaxPageIndex(document));

            for(int page = 1; page <= search.getMaxPage(); page++){
                search.setPage(page);
                processPage(search.getPage());
            }

            logger.debug("Found {} offers after processing {} pages", offers.size(), search.getMaxPage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            failedVehicleParsingLogging();
            invalidOfferParsingLogging();
        }
    }

    public final void processPage(int page) {
        try {
            Document document = getBaseDocument(page);
            Elements elements = getElementsInPage(document);

            logger.debug(String.valueOf(elements.size()));
            Integer count = 1;
            for (Element element : elements) {
                logger.debug("Processing element: " + count + "/" + elements.size());

                //TODO
                // refactor this part so that there will be provided link in case of error
                BaseOffer offer = parseOffer(element);
                logger.debug(offer.toString());
                BaseVehicle vehicle = parseVehicle(element);

                offer.setVehicle(vehicle);

                logger.debug(vehicle.toString());
                logger.debug("End of processing element: {}/{}", count, elements.size());
                if (offer.isValid()) {
                    offers.add(offer);
                }else{
                    invalidOffers.add(offer);
                }
                count++;
            }

            saveIntoFile();

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
}

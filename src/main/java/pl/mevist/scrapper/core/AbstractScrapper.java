package pl.mevist.scrapper.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mevist.scrapper.core.model.BaseVehicle;
import pl.mevist.scrapper.core.model.BaseVehicleDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScrapper {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected AbstractSearch search;
    protected AbstractVehicleMapper vehicleMapper;

    protected final String USER_AGENT = "Mozilla/5.0";
    protected List<BaseVehicle> vehicles = new ArrayList<>();

    // Abstract
    protected abstract Elements getElementsInPage(Document doc);
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

            logger.debug("Found " + vehicles.size() + " vehicles" + "after processing " + search.getMaxPage() + " pages");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                BaseVehicle vehicle = parseVehicle(element);
                logger.debug(vehicle.toString());
                if (vehicle.isValid()) {
                    vehicles.add(vehicle);
                }
                count++;
            }

            saveIntoFile();

        } catch (IOException e) {
            throw new RuntimeException("Scraping failed", e);
        }
    }

    protected void saveIntoFile(){
        // TODO
        //  Use vehicles to store data into external file
    }

    protected void logParseError(Elements htmlContext, Exception e) {
        StringBuilder builder = new StringBuilder();

        htmlContext.stream().map(Element::text).forEach(builder::append);

        logger.warn("Failed to sanitize raw data. Html context: '{}'", builder, e);
    }

    // Constructors
    public AbstractScrapper(AbstractSearch search) {
        this.search = search;
    }
}

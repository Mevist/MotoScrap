package pl.mevist.scrapper.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.mevist.scrapper.core.model.BaseRawVehicleDetails;
import pl.mevist.scrapper.core.model.BaseVehicle;
import pl.mevist.scrapper.core.model.BaseVehicleDetails;
import pl.mevist.scrapper.utils.QueryUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractScrapper {
    protected AbstractSearch search;
    protected AbstractVehicleMapper vehicleMapper;

    protected final String USER_AGENT = "Mozilla/5.0";
    protected List<BaseVehicle> vehicles = new ArrayList<>();

    // Abstract
    protected abstract Elements getElementsInPage(Document doc);
    protected abstract BaseVehicleDetails parseVehicleDetails(Element element);
    protected abstract BaseVehicle parseVehicleName(Element element);

    // Provided
    protected Document getBaseDocument() throws IOException {
        String searchUrl = search.buildSearchUrl();
        return Jsoup.connect(searchUrl).userAgent(USER_AGENT).get();
    }

    protected BaseVehicle parseVehicle(Element element) {
        BaseVehicle vehicle = parseVehicleName(element);
        BaseVehicleDetails vehicleDetails = parseVehicleDetails(element);
        vehicle.setDetails(vehicleDetails);
        return vehicle;
    }

    public final void process() {
        try {
            Document document = getBaseDocument();
            Elements elements = getElementsInPage(document);

            System.out.println(elements.size());
            Integer count = 1;
            for (Element element : elements) {
                System.out.println("Processing element: " + count + "/" + elements.size());
                BaseVehicle vehicle = parseVehicle(element);
                System.out.println(vehicle);
                if (vehicle != null) {
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

    // Constructors
    public AbstractScrapper(AbstractSearch search) {
        this.search = search;
    }
}

package pl.mevist.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.mevist.scrapper.model.vehicle.RawVehicleDetails;
import pl.mevist.scrapper.model.vehicle.Vehicle;
import pl.mevist.scrapper.model.vehicle.VehicleDetails;
import pl.mevist.scrapper.model.vehicle.VehicleDetailsMapper;
import pl.mevist.scrapper.soupData.Search;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class CarFinder implements Callable<Integer>{

    private final Search search;

    public static final Map<String, String> DETAILS_MAPPING = Map.ofEntries(
            Map.entry("VehicleDetails-mileage_road","mileage"),
            Map.entry("VehicleDetails-transmission", "transmission"),
            Map.entry("VehicleDetails-calendar", "firstRegister"),
            Map.entry("VehicleDetails-gas_pump", "gasPump"),
            Map.entry("VehicleDetails-speedometer", "speedoMeter"));

    public static final String  BROWSER_AGENT= "Mozilla/5.0";

    public CarFinder(Search search) {
        this.search = search;
    }

    @Override
    public Integer call() throws Exception {
        System.out.printf("Searching for '%s'...\n", search.getModel());

        String searchUrl = buildSearchUrl(search);
        System.out.println("Fetching: " + searchUrl);

        Elements articles = getElements(searchUrl);

        parseCurrentPage(articles);
        return 0;
    }

    private Elements getElements(String searchUrl) throws IOException {
        Document doc = Jsoup.connect(searchUrl).userAgent(BROWSER_AGENT).get();
        Elements articles = doc.select(cssQueryBuilder("article", "class", "cldt-summary-full-item listing-impressions-tracking"));
        return articles;
    }

    private void parseCurrentPage(Elements articles) {
        for (Element article : articles) {
            Element elementA = article.selectFirst(cssQueryBuilder("a", "class", "ListItem_title"));
            assert elementA != null;
            Element vehicleTitle = elementA.selectFirst("h2");
            Elements spans = elementA.select("span");

            List<String> listSpans = spans.stream().map(Element::text).collect(Collectors.toList());
            String brand = listSpans.get(0);
            String model = listSpans.get(1);
            System.out.printf("Brand: %s\n", brand);
            System.out.printf("Model: %s\n", model);

            Elements details = article.select(cssQueryBuilder("span", "data-testId", "VehicleDetails"));
            String VEHICLE_DETAILS = "VehicleDetails";
            String DETAIL_FIELD_IDENTIFIER = "data-testid";

            RawVehicleDetails rawDetails = getRawVehicleDetails(details, DETAIL_FIELD_IDENTIFIER);
            VehicleDetails vehicleDetails = VehicleDetailsMapper.toVehicleDetails(rawDetails);
            Vehicle vehicle = new Vehicle(brand, model, vehicleDetails);


            System.out.printf("RawVehicleDetails: %s\n", rawDetails);
            System.out.printf("VehicleDetails: %s\n", vehicleDetails);
            System.out.printf("Vehicle: %s\n", vehicle);
            System.out.println("Found " + details.size() + " results");
        }
    }

    private RawVehicleDetails getRawVehicleDetails(Elements details, String DETAIL_FIELD_IDENTIFIER) {
        RawVehicleDetails rawDetails = new RawVehicleDetails();
        for(Element detail : details) {
            String identifier = detail.attributes().get(DETAIL_FIELD_IDENTIFIER);
            String mappedField = DETAILS_MAPPING.get(identifier);

            if (mappedField != null) {
                String value = detail.text();

                switch (mappedField) {
                    case "mileage" -> rawDetails.setMileage(value);
                    case "transmission" -> rawDetails.setTransmission(value);
                    case "firstRegister" -> rawDetails.setFirstRegister(value);
                    case "gasPump" -> rawDetails.setGasPump(value);
                    case "speedoMeter" -> rawDetails.setSpeedoMeter(value);
                }
            }
        }
        return rawDetails;
    }

    private String buildSearchUrl(Search search) {
        String baseUrl = "https://www.autoscout24.pl/lst/";

        StringBuilder url = new StringBuilder(baseUrl);

        return urlFiller(url, search);
    }

    private String urlFiller(StringBuilder url, Search search) {
        Optional.ofNullable(search.getBrand()).ifPresent(brand -> url.append(brand).append('/'));
        Optional.ofNullable(search.getModel()).ifPresent(model -> url.append(model).append('/'));
        return url.toString();
    }

    private String cssQueryBuilder(String base, String searchBy, String searchValue, boolean startsWith){
        StringBuilder cssQueryBuilder = new StringBuilder();
        cssQueryBuilder
                .append(base)
                .append("[")
                .append(searchBy);

        if (startsWith){
            cssQueryBuilder
                    .append("^=");
        }else{
            cssQueryBuilder
                    .append("$=");
        }

        cssQueryBuilder
                .append(searchValue)
                .append("]");

        return cssQueryBuilder.toString();
    }

    private String cssQueryBuilder(String base, String searchBy, String searchValue) {
        return cssQueryBuilder(base, searchBy, searchValue, true);
    }
}
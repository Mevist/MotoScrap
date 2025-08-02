package pl.mevist.scrapper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.mevist.scrapper.soupData.Search;

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

    public CarFinder(Search search) {
        this.search = search;
    }

    @Override
    public Integer call() throws Exception {
        System.out.printf("Searching for '%s'...\n", search.getModel());

        String searchUrl = buildCraigslistSearchUrl(search);
        System.out.println("Fetching: " + searchUrl);

        Document doc = Jsoup.connect(searchUrl).userAgent("Mozilla/5.0").get();
        String title = doc.title();
        Elements main = doc.select(cssQueryBuilder("main", "class", "Listpage_main"));
        Elements articles = doc.select(cssQueryBuilder("article", "class", "cldt-summary-full-item listing-impressions-tracking"));

        System.out.println("Found " + main.size() + " results");
        System.out.println("Found " + articles.size() + " results");

        for (Element article : articles) {
//            Element vehicleDetail = article.select(cssQueryBuilder("div", "class", "VehicleDetailTable_container")).first();
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


            System.out.printf("VehicleDetails: %s\n", rawDetails);
            System.out.println("Found " + details.size() + " results");
        }
        return 0;
    }

    private String buildCraigslistSearchUrl(Search search) {
        String baseUrl = "https://www.autoscout24.pl/lst/";

        StringBuilder url = new StringBuilder(baseUrl);

        return urlFiller(url, search);
    }

    public class RawVehicleDetails {
        private String transmission;
        private String mileage;
        private String firstRegister;
        private String gasPump;
        private String speedoMeter;

        // Temp


        @Override
        public String toString() {
            return "RawVehicleDetails{" +
                    "gasPump='" + gasPump + '\'' +
                    ", speedoMeter='" + speedoMeter + '\'' +
                    ", firstRegister='" + firstRegister + '\'' +
                    ", mileage='" + mileage + '\'' +
                    ", transmission='" + transmission + '\'' +
                    '}';
        }

        public void setMileage(String mileage) {
            this.mileage = mileage;
        }

        public void setTransmission(String transmission) {
            this.transmission = transmission;
        }

        public void setFirstRegister(String firstRegister) {
            this.firstRegister = firstRegister;
        }

        public void setGasPump(String gasPump) {
            this.gasPump = gasPump;
        }

        public void setSpeedoMeter(String speedoMeter) {
            this.speedoMeter = speedoMeter;
        }
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
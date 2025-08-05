package pl.mevist.scrapper.impl.autoscout;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.mevist.scrapper.core.AbstractScrapper;
import pl.mevist.scrapper.core.AbstractSearch;
import pl.mevist.scrapper.core.AbstractVehicleMapper;
import pl.mevist.scrapper.core.exception.VehicleMappingException;
import pl.mevist.scrapper.core.model.BaseRawVehicleDetails;
import pl.mevist.scrapper.core.model.BaseVehicle;
import pl.mevist.scrapper.core.model.BaseVehicleDetails;
import pl.mevist.scrapper.utils.QueryUtils;

import java.util.List;
import java.util.Map;


public class AutoScoutScrapper extends AbstractScrapper {
    public static final Map<String, String> DETAILS_MAPPING = Map.ofEntries(
            Map.entry("VehicleDetails-mileage_road","mileage"),
            Map.entry("VehicleDetails-transmission", "transmission"),
            Map.entry("VehicleDetails-calendar", "firstRegister"),
            Map.entry("VehicleDetails-gas_pump", "gasPump"),
            Map.entry("VehicleDetails-speedometer", "speedoMeter"));

    public static final String DETAIL_FIELD_IDENTIFIER = "data-testid";

    public AutoScoutScrapper(AbstractSearch search, AbstractVehicleMapper  vehicleMapper) {
        super(search);
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    protected Elements getElementsInPage(Document doc) {
        return doc.select(
                QueryUtils.cssQueryBuilder(
                        "article",
                        "class",
                        "cldt-summary-full-item listing-impressions-tracking"));
    }

    @Override
    protected BaseVehicleDetails parseVehicleDetails(Element article) {
        Elements details = article.select(QueryUtils.cssQueryBuilder("span", "data-testId", "VehicleDetails"));
        String VEHICLE_DETAILS = "VehicleDetails";

        BaseRawVehicleDetails rawDetails = new BaseRawVehicleDetails();
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
        try {
            return vehicleMapper.toVehicleDetails(rawDetails);
        } catch (VehicleMappingException e) {
            logParseError(details, e);
        }
        return null;
    }

    @Override
    protected BaseVehicle parseVehicleName(Element element) {
        Element elementA = element.selectFirst(QueryUtils.cssQueryBuilder("a", "class", "ListItem_title"));
        assert elementA != null;

        Elements spans = elementA.select("span");

        List<String> listSpans = spans.stream().map(Element::text).toList();
        String brand = listSpans.get(0);
        String model = listSpans.get(1);
        return new BaseVehicle(brand, model);
    }

    @Override
    protected int findMaxPageIndex(Document doc) {
        Element element = doc.selectFirst("li.pagination-item--page-indicator span");
        String[] indicatorParts = element.text().split("/");
        return Integer.parseInt(indicatorParts[1].trim());
    }
}

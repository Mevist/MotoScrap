package pl.mevist;

import pl.mevist.scrapper.CarFinder;
import pl.mevist.scrapper.core.model.BaseSearchData;
import pl.mevist.scrapper.impl.autoscout.AutoScoutPriceMapper;
import pl.mevist.scrapper.impl.autoscout.AutoScoutScrapper;
import pl.mevist.scrapper.impl.autoscout.AutoScoutSearch;
import pl.mevist.scrapper.impl.autoscout.AutoScoutVehicleMapper;

import java.util.List;

public class Main{
    public static void main(String[] args) {
        BaseSearchData search = new BaseSearchData.Builder()
                .build();

        AutoScoutSearch autoScoutSearch = new AutoScoutSearch(search);
        AutoScoutVehicleMapper autoScoutVehicleMapper = new AutoScoutVehicleMapper();
        AutoScoutPriceMapper autoScoutPriceMapper = new AutoScoutPriceMapper();

        AutoScoutScrapper autoScoutScrapper = new AutoScoutScrapper(
                autoScoutSearch,
                autoScoutVehicleMapper,
                autoScoutPriceMapper);

        CarFinder test = new CarFinder(List.of(autoScoutScrapper));
        try {
            test.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

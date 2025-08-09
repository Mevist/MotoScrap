package pl.mevist;

import pl.mevist.scrapper.core.model.BaseSearchData;
import pl.mevist.scrapper.impl.autoscout.AutoScoutScrapper;
import pl.mevist.scrapper.impl.autoscout.AutoScoutSearch;

public class Main{
    public static void main(String[] args) {
        BaseSearchData search = new BaseSearchData.Builder()
                .build();

        AutoScoutSearch autoScoutSearch = new AutoScoutSearch(search);
        AutoScoutScrapper autoScoutScrapper = new AutoScoutScrapper(autoScoutSearch);
        autoScoutScrapper.setMaxOffersLimit(20);

        try {
            autoScoutScrapper.processAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

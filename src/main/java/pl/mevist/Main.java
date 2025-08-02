package pl.mevist;

import pl.mevist.scrapper.CarFinder;
import pl.mevist.scrapper.soupData.Search;

public class Main{
    public static void main(String[] args) {
        Search search = new Search.Builder()
                .build();
        CarFinder test = new CarFinder(search);
        try {
            test.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package pl.mevist.scrapper;

import org.jsoup.select.Elements;
import pl.mevist.scrapper.core.AbstractScrapper;
import java.util.List;
import java.util.concurrent.Callable;

public class CarFinder implements Runnable{
    List<AbstractScrapper> scrappers;

    public CarFinder(List<AbstractScrapper> scrappers) {
        this.scrappers = scrappers;
    }

    @Override
    public void run() {
        for (AbstractScrapper scrapper : scrappers) {
            scrapper.processAll();
        }
    }
}
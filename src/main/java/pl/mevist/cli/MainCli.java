package pl.mevist.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import pl.mevist.scrapper.CarFinder;
import pl.mevist.scrapper.Scrapper;
import pl.mevist.scrapper.core.AbstractScrapper;
import pl.mevist.scrapper.core.model.BaseSearchData;
import pl.mevist.scrapper.core.model.ScrapingParams;
import pl.mevist.scrapper.impl.autoscout.AutoScoutScrapper;
import pl.mevist.scrapper.impl.autoscout.AutoScoutSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "CarFinder",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Searches for car listings based on user input")
public class MainCli implements Callable<Integer> {
    @Option(names = {"--brand"}, description = "Car brand to search for")
    String brand;

    @Option(names = {"--model"}, description = "Car model to search for")
    String model;

    @Option(names = {"--maxYear"}, description = "Maximum production year")
    int maxYear = 0;

    @Option(names = {"--maxMileage"}, description = "Maximum mileage")
    float maxMileage = 0;

    @Option(names = {"--maxPrice"}, description = "Maximum price")
    float maxPrice = 0;

    @Option(names = {"--limit"}, description = "Maximum limit of offers to get (max 400)")
    int maxLimit = 0;

    @Option(
            names = {"--scrapper"},
            description = "Scrapper to use. Available: ${COMPLETION-CANDIDATES}",
            required = true
    )
    Scrapper scrapper;

    BaseSearchData search;
    ScrapingParams scrapingParams;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {

        BaseSearchData baseSearchData = prepareScrappingData();
        this.search = baseSearchData;

        if (baseSearchData == null) return 2;

        List<AbstractScrapper> scrappers = prepareScrappers(baseSearchData);
        CarFinder carFinder = new CarFinder(scrappers);

        carFinder.run();

        return 0; // success
    }

    private List<AbstractScrapper> prepareScrappers(BaseSearchData baseSearchData) {
        List<AbstractScrapper> scrapperList = new ArrayList<>();

        switch (scrapper) {
            case AUTOSCOUT24 -> {
                AutoScoutSearch autoScoutSearch = new AutoScoutSearch(baseSearchData);
                AutoScoutScrapper autoScoutScrapper = new AutoScoutScrapper(autoScoutSearch);
                if (this.scrapingParams != null) {
                    autoScoutScrapper.setMaxOffersLimit(this.scrapingParams.getMaxOffersLimit());
                }
                scrapperList.add(autoScoutScrapper);
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + scrapper);

        }

        return scrapperList;
    }

    private BaseSearchData prepareScrappingData() {
        // Basic validation (non-negative constraints)
        if (maxYear < 0 || maxMileage < 0 || maxPrice < 0) {
            System.err.println("Error: maxYear, maxMileage, and maxPrice must be >= 0.");
            return null;
        }

        BaseSearchData.Builder baseSearchBuilder = new BaseSearchData.Builder();

        if (brand != null) baseSearchBuilder.brand(brand);
        if (model != null) baseSearchBuilder.model(model);
        if (maxYear > 0) baseSearchBuilder.maxYear(maxYear);
        if (maxMileage > 0) baseSearchBuilder.maxMileage(maxMileage);
        if (maxPrice > 0) baseSearchBuilder.maxPrice(maxPrice);

        if (maxLimit > 400) {
            System.out.println("Limit exceeds 400, setting to 400.");
            maxLimit = 400;
        }
        if (maxLimit < 0) {
            System.out.println("Negative limit not allowed, setting to 0 (no limit).");
            maxLimit = 0;
        }

        this.scrapingParams = new ScrapingParams(maxLimit);

        System.out.println("Using limit: " + (maxLimit == 0 ? "(no limit)" : maxLimit));
        System.out.println("CarFinder — running with:");
        System.out.println("  model:      " + model);
        System.out.println("  maxYear:    " + (maxYear == 0 ? "(not set)" : maxYear));
        System.out.println("  maxMileage: " + (maxMileage == 0 ? "(not set)" : maxMileage));
        System.out.println("  maxPrice:   " + (maxPrice == 0 ? "(not set)" : maxPrice));

        BaseSearchData baseSearchData = baseSearchBuilder.build();
        return baseSearchData;
    }
}
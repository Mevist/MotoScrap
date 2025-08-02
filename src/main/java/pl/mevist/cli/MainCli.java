package pl.mevist.cli;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "CarFinder", mixinStandardHelpOptions = true, version = "1.0",
        description = "Searches for car listings based on user input")
public class MainCli {
    @CommandLine.Option(names = {"--model"}, description = "Car model to search for", required = true)
    String model;

    @CommandLine.Option(names = {"--maxYear"}, description = "Maximum production year")
    int maxYear = 0;

    @CommandLine.Option(names = {"--maxMileage"}, description = "Maximum mileage")
    int maxMileage = 0;

    @CommandLine.Option(names = {"--maxPrice"}, description = "Maximum price")
    int maxPrice = 0;
}
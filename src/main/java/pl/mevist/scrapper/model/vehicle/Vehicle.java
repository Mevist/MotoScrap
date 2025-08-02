package pl.mevist.scrapper.model.vehicle;

public class Vehicle {
    String brand;
    String model;
    VehicleDetails details;

    // Constructor
    public Vehicle(String brand, String model, VehicleDetails details) {
        this.brand = brand;
        this.model = model;
        this.details = details;
    }

    // Getters
    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public VehicleDetails getDetails() {
        return details;
    }
}

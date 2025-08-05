package pl.mevist.scrapper.core.model;

public class BaseVehicle {
    private String brand;
    private String model;
    private BaseVehicleDetails details;

    // Constructor
    public BaseVehicle(String brand, String model, BaseVehicleDetails details) {
        this.brand = brand;
        this.model = model;
        this.details = details;
    }

    public BaseVehicle(String brand, String model) {
        this.brand = brand;
        this.model = model;
    }

    // Methods
    public boolean isValid(){
        return brand != null &&
                model != null &&
                details != null &&
                details.isValid();
    }


    // Getters, Setters
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BaseVehicleDetails getDetails() {
        return details;
    }

    public void setDetails(BaseVehicleDetails details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", details=" + details +
                '}';
    }
}

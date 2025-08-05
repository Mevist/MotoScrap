package pl.mevist.scrapper.core.model;

public class BaseOffer {
    private String url;
    private Price price;
    private BaseVehicle vehicle;

    public boolean isValid(){
        return url != null && price != null && vehicle != null && vehicle.isValid();
    }

    // Constructor
    public BaseOffer(String url, Price price) {
        this.url = url;
        this.price = price;
    }


    // Getters, Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public BaseVehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(BaseVehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "BaseOffer{" +
                "url='" + url +
                ", price=" + price +
                ", vehicle=" + vehicle +
                '}';
    }
}

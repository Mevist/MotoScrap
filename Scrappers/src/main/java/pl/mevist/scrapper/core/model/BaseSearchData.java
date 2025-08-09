package pl.mevist.scrapper.core.model;

public class BaseSearchData {
    private final String brand;
    private final String model;
    private final Integer maxYear;
    private final Float maxMileage;
    private final Float maxPrice;

    // Constructor
    public BaseSearchData(String brand, String model, Integer maxYear, Float maxMileage, Float maxPrice) {
        this.brand = brand;
        this.model = model;
        this.maxYear = maxYear;
        this.maxMileage = maxMileage;
        this.maxPrice = maxPrice;
    }


    public static class Builder {
        private String brand;
        private String model;
        private Integer maxYear;
        private Float maxMileage;
        private Float maxPrice;

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }
        public Builder model(String model) {
            this.model = model;
            return this;
        }
        public Builder maxYear(Integer maxYear) {
            this.maxYear = maxYear;
            return this;
        }
        public Builder maxMileage(Float maxMileage) {
            this.maxMileage = maxMileage;
            return this;
        }
        public Builder maxPrice(Float maxPrice) {
            this.maxPrice = maxPrice;
            return this;
        }

        public BaseSearchData build() {
            return new BaseSearchData(brand, model, maxYear, maxMileage, maxPrice);
        }
    }

    // Getters, Setters

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public Float getMaxMileage() {
        return maxMileage;
    }

    public Float getMaxPrice() {
        return maxPrice;
    }
}

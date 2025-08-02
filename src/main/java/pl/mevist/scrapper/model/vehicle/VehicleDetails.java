package pl.mevist.scrapper.model.vehicle;

import java.util.List;

public class VehicleDetails {
        private final Float mileage;
        private final String transmission;
        private final Float firstRegister;
        private final String gasPump;
        private final String speedoMeter;

        // Constructor
        private VehicleDetails(Float mileage, String transmission, Float firstRegister, String gasPump, String speedoMeter) {
                this.mileage = mileage;
                this.transmission = transmission;
                this.firstRegister = firstRegister;
                this.gasPump = gasPump;
                this.speedoMeter = speedoMeter;
        }

        // Builder
        public static class Builder {
                private Float mileage;
                private String transmission;
                private Float firstRegister;
                private String gasPump;
                private String speedoMeter;

                private Builder setMileage(Float mileage) {
                        this.mileage = mileage;
                        return this;
                }
                private Builder setTransmission(String transmission) {
                        this.transmission = transmission;
                        return this;
                }
                private Builder setFirstRegister(Float firstRegister) {
                        this.firstRegister = firstRegister;
                        return this;
                }
                private Builder setGasPump(String gasPump) {
                        this.gasPump = gasPump;
                        return this;
                }
                private Builder setSpeedoMeter(String speedoMeter) {
                        this.speedoMeter = speedoMeter;
                        return this;
                }

                public VehicleDetails build() {
                        return new VehicleDetails(mileage, transmission, firstRegister, gasPump, speedoMeter);
                }
        }

        // Getters, Setters

        public Float getMileage() {
                return mileage;
        }

        public String getTransmission() {
                return transmission;
        }

        public Float getFirstRegister() {
                return firstRegister;
        }

        public String getGasPump() {
                return gasPump;
        }

        public String getSpeedoMeter() {
                return speedoMeter;
        }
}

package pl.mevist.scrapper.core.model;

import java.time.YearMonth;

public class BaseVehicleDetails {
        private final Float mileage;
        private final String transmission;
        private final YearMonth firstRegister;
        private final String gasPump;
        private final String speedoMeter;

        public boolean isValid(){
                return transmission != null &&
                        gasPump != null &&
                        speedoMeter != null;
        }

        // Constructor
        private BaseVehicleDetails(Float mileage, String transmission, YearMonth firstRegister, String gasPump, String speedoMeter) {
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
                private YearMonth firstRegister;
                private String gasPump;
                private String speedoMeter;

                public Builder setMileage(Float mileage) {
                        this.mileage = mileage;
                        return this;
                }
                public Builder setTransmission(String transmission) {
                        this.transmission = transmission;
                        return this;
                }
                public Builder setFirstRegister(YearMonth firstRegister) {
                        this.firstRegister = firstRegister;
                        return this;
                }
                public Builder setGasPump(String gasPump) {
                        this.gasPump = gasPump;
                        return this;
                }
                public Builder setSpeedoMeter(String speedoMeter) {
                        this.speedoMeter = speedoMeter;
                        return this;
                }

                public BaseVehicleDetails build() {
                        return new BaseVehicleDetails(mileage, transmission, firstRegister, gasPump, speedoMeter);
                }
        }

        // Getters, Setters

        public Float getMileage() {
                return mileage;
        }

        public String getTransmission() {
                return transmission;
        }

        public YearMonth getFirstRegister() {
                return firstRegister;
        }

        public String getGasPump() {
                return gasPump;
        }

        public String getSpeedoMeter() {
                return speedoMeter;
        }

        @Override
        public String toString() {
                return "VehicleDetails{" +
                        "mileage=" + mileage +
                        ", transmission='" + transmission + '\'' +
                        ", firstRegister=" + firstRegister +
                        ", gasPump='" + gasPump + '\'' +
                        ", speedoMeter='" + speedoMeter + '\'' +
                        '}';
        }
}

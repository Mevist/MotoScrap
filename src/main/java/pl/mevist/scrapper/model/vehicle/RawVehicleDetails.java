package pl.mevist.scrapper.model.vehicle;

public class RawVehicleDetails {
    private String transmission;
    private String mileage;
    private String firstRegister;
    private String gasPump;
    private String speedoMeter;

    // Temp


    @Override
    public String toString() {
        return "RawVehicleDetails{" +
                "gasPump='" + gasPump + '\'' +
                ", speedoMeter='" + speedoMeter + '\'' +
                ", firstRegister='" + firstRegister + '\'' +
                ", mileage='" + mileage + '\'' +
                ", transmission='" + transmission + '\'' +
                '}';
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public void setFirstRegister(String firstRegister) {
        this.firstRegister = firstRegister;
    }

    public void setGasPump(String gasPump) {
        this.gasPump = gasPump;
    }

    public void setSpeedoMeter(String speedoMeter) {
        this.speedoMeter = speedoMeter;
    }

    public String getTransmission() {
        return transmission;
    }

    public String getMileage() {
        return mileage;
    }

    public String getFirstRegister() {
        return firstRegister;
    }

    public String getGasPump() {
        return gasPump;
    }

    public String getSpeedoMeter() {
        return speedoMeter;
    }
}

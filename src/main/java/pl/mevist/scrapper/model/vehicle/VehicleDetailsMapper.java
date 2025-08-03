package pl.mevist.scrapper.model.vehicle;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public final class VehicleDetailsMapper {
//    VehicleDetails:
//    RawVehicleDetails{
//    gasPump='Benzyna',
//    speedoMeter='51 kW (69 KM)',
//    firstRegister='04/2012',
//    mileage='136 627 km',
//    transmission='Manualna'}

    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/yyyy");

    public static VehicleDetails toVehicleDetails(RawVehicleDetails rawDetails) {
        Float mileage = sanitizeMileage(rawDetails.getMileage());
        YearMonth registerDate = sanitizeFirstRegister(rawDetails.getFirstRegister());
        String fuel = rawDetails.getGasPump();
        String speedo = rawDetails.getSpeedoMeter();
        String transmission = rawDetails.getTransmission();

        return  new VehicleDetails.Builder()
                .setMileage(mileage)
                .setFirstRegister(registerDate)
                .setGasPump(fuel)
                .setTransmission(transmission)
                .setSpeedoMeter(speedo)
                .build();

    }

    private static Float sanitizeMileage(String mileage) {
        String rawMileage = mileage.replaceAll("[^\\d.]", "");
        return Float.parseFloat(rawMileage);
    }

    private static YearMonth sanitizeFirstRegister(String firstRegister) {
        String registerCleared = firstRegister.replaceAll("[^\\d.]", "");
        return YearMonth.parse(firstRegister, dateFormat);
    }
}

package pl.mevist.scrapper.core;

import pl.mevist.scrapper.core.model.BaseRawVehicleDetails;
import pl.mevist.scrapper.core.model.BaseVehicleDetails;
import java.time.YearMonth;

public abstract class AbstractVehicleMapper {

    // Abstract
    protected abstract Float sanitizeMileage(String mileage);
    protected abstract YearMonth sanitizeFirstRegister(String firstRegister);
    protected abstract String sanitizeGasPump(String gasPump);
    protected abstract String sanitizeSpeedoMeter(String speedoMeter);
    protected abstract String sanitizeTransmission(String transmission);

    // Provided
    public BaseVehicleDetails toVehicleDetails(BaseRawVehicleDetails rawDetails) {
        System.out.println("Sanitizing vehicle details" + rawDetails);
        Float mileage = sanitizeMileage(rawDetails.getMileage());
        YearMonth registerDate = sanitizeFirstRegister(rawDetails.getFirstRegister());
        String gasPump = sanitizeGasPump(rawDetails.getGasPump());
        String speedoMeter = sanitizeSpeedoMeter(rawDetails.getSpeedoMeter());
        String transmission = sanitizeTransmission(rawDetails.getTransmission());

        return  new BaseVehicleDetails.Builder()
                .setMileage(mileage)
                .setFirstRegister(registerDate)
                .setGasPump(gasPump)
                .setSpeedoMeter(speedoMeter)
                .setTransmission(transmission)
                .build();

    }
}

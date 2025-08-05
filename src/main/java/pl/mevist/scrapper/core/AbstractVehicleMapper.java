package pl.mevist.scrapper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mevist.scrapper.core.exception.VehicleMappingException;
import pl.mevist.scrapper.core.model.BaseRawVehicleDetails;
import pl.mevist.scrapper.core.model.BaseVehicleDetails;
import java.time.YearMonth;

public abstract class AbstractVehicleMapper {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // Abstract
    protected abstract Float sanitizeMileage(String mileage) throws VehicleMappingException;
    protected abstract YearMonth sanitizeFirstRegister(String firstRegister) throws VehicleMappingException;
    protected abstract String sanitizeGasPump(String gasPump)  throws VehicleMappingException;
    protected abstract String sanitizeSpeedoMeter(String speedoMeter)  throws VehicleMappingException;
    protected abstract String sanitizeTransmission(String transmission) throws VehicleMappingException;

    // Provided
    public BaseVehicleDetails toVehicleDetails(BaseRawVehicleDetails rawDetails) throws VehicleMappingException {
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

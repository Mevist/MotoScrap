package pl.mevist.scrapper.impl.autoscout;

import pl.mevist.scrapper.core.AbstractVehicleMapper;
import pl.mevist.scrapper.core.model.BaseRawVehicleDetails;
import pl.mevist.scrapper.core.model.BaseVehicleDetails;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class AutoScoutVehicleMapper extends AbstractVehicleMapper {
//    VehicleDetails:
//    RawVehicleDetails{
//    gasPump='Benzyna',
//    speedoMeter='51 kW (69 KM)',
//    firstRegister='04/2012',
//    mileage='136 627 km',
//    transmission='Manualna'}

    private final static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/yyyy");

    @Override
    protected Float sanitizeMileage(String mileage) {
        String rawMileage = mileage.replaceAll("[^\\d.]", "");
        return Float.parseFloat(rawMileage);
    }

    @Override
    protected YearMonth sanitizeFirstRegister(String firstRegister) {
        String registerCleared = firstRegister.replaceAll("[^\\d.]", "");
        return YearMonth.parse(firstRegister, dateFormat);
    }

    @Override
    protected String sanitizeGasPump(String gasPump) {
        return gasPump;
    }

    @Override
    protected String sanitizeSpeedoMeter(String speedoMeter) {
        return speedoMeter;
    }

    @Override
    protected String sanitizeTransmission(String transmission) {
        return transmission;
    }
}

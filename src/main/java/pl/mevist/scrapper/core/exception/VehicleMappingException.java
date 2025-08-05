package pl.mevist.scrapper.core.exception;

public class VehicleMappingException extends Exception {
    public VehicleMappingException(String message) {
        super(message);
    }

    public VehicleMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}

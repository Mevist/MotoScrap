package pl.mevist.scrapper.core.model;

public class UnparsedVehicleError {
    private final BaseRawVehicleDetails rawDetails;
    private final String errorMessage;
    private final Throwable exception;

    public UnparsedVehicleError(BaseRawVehicleDetails rawDetails, String errorMessage, Throwable exception) {
        this.rawDetails = rawDetails;
        this.errorMessage = errorMessage;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "UnparsedVehicleError{" +
                "errorMessage='" + errorMessage + '\'' +
                ", rawDetails=" + rawDetails +
                ", exception=" + (exception != null ? exception.getMessage() : "none") +
                '}';
    }
}


package pl.mevist.scrapper.core.exception;

public class PriceMappingException extends RuntimeException {
    public PriceMappingException(String message) {
        super(message);
    }
    public PriceMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}

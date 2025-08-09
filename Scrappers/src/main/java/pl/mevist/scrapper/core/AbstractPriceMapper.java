package pl.mevist.scrapper.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.mevist.scrapper.core.exception.PriceMappingException;
import pl.mevist.scrapper.core.model.Price;

import java.util.Currency;

public abstract class AbstractPriceMapper {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // Abstract
    protected abstract Price sanitizePrice(String rawPrice) throws PriceMappingException;

    // Provided
    public Price toPrice(String rawPrice) throws PriceMappingException {
        return sanitizePrice(rawPrice);
    }

    // Provided inner
    protected static Currency currencyFromSymbol(String symbol) {
        return switch (symbol) {
            case "PLN" -> Currency.getInstance("PLN");
            case "€" -> Currency.getInstance("EUR");
            case "$" -> Currency.getInstance("USD");
            case "£" -> Currency.getInstance("GBP");
            default -> throw new IllegalArgumentException("Unsupported currency symbol");
        };
    }
}

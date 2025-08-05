package pl.mevist.scrapper.core.model;

import pl.mevist.scrapper.core.exception.PriceMappingException;

import java.util.Currency;

public record Price(float price, Currency currency) {

    // Constructor
    public Price {
        if (currency == null) {
            throw new PriceMappingException("Currency can't be null");
        }
    }

    @Override
    public String toString() {
        return price + " " + currency;
    }
}

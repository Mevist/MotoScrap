package pl.mevist.scrapper.impl.autoscout;

import pl.mevist.scrapper.core.AbstractPriceMapper;
import pl.mevist.scrapper.core.exception.PriceMappingException;
import pl.mevist.scrapper.core.model.Price;

import java.util.Currency;

public class AutoScoutPriceMapper extends AbstractPriceMapper {
    // Format:
    // € 2 450

    // Constructor
    public AutoScoutPriceMapper() {
    }

    @Override
    protected Price sanitizePrice(String rawPrice) throws PriceMappingException {
        String[] priceParts = rawPrice.split(" ", 2);

        if (priceParts.length != 2) {
            throw new PriceMappingException("Price part is invalid, raw value: " + rawPrice);
        }

        String symbol =  priceParts[0].trim();
        String priceString = priceParts[1].replace(" ", "");

        Currency currency = null;
        float price;

        try {
            currency = currencyFromSymbol(symbol);
        } catch (Exception e) {
            throw new PriceMappingException("Currency symbol parsing did not succeed, raw symbol: " + symbol, e);
        }

        try {
            price = Float.parseFloat(priceString);
        } catch (NumberFormatException e) {
            throw new PriceMappingException("Price value parsing did not succeed, raw value: " + priceString ,e);
        }

        return new Price(price, currency);
    }
}

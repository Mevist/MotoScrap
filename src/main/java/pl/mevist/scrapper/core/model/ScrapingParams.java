package pl.mevist.scrapper.core.model;

public class ScrapingParams {
    private int maxOffersLimit;

    public ScrapingParams(int maxOffersLimit) {
        this.maxOffersLimit = maxOffersLimit;
    }

    public int getMaxOffersLimit() {
        return maxOffersLimit;
    }

    public void setMaxOffersLimit(int maxOffersLimit) {
        this.maxOffersLimit = maxOffersLimit;
    }
}

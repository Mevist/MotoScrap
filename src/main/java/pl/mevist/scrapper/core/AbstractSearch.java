package pl.mevist.scrapper.core;


import pl.mevist.scrapper.core.model.BaseSearchData;

public abstract class AbstractSearch {
    private final BaseSearchData baseSearchData;

    // Abstract
    protected abstract String buildSearchUrl();
    protected abstract String buildUrlParams(StringBuilder baseUrl);

    // Constructor
    public AbstractSearch(BaseSearchData baseSearchData) {
        this.baseSearchData = baseSearchData;
    }

    // Getters, Setters
    public BaseSearchData getBaseSearch() {
        return baseSearchData;
    }
}

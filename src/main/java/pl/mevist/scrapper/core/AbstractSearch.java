package pl.mevist.scrapper.core;


import pl.mevist.scrapper.core.model.BaseSearchData;

public abstract class AbstractSearch {
    private final BaseSearchData baseSearchData;

    private int maxPage;
    private int page;

    // Abstract
    protected abstract String buildSearchUrl(int page);
    protected abstract String buildUrlParams(StringBuilder baseUrl);

    // Constructor
    public AbstractSearch(BaseSearchData baseSearchData) {
        this.baseSearchData = baseSearchData;
    }

    // Getters, Setters
    public BaseSearchData getBaseSearch() {
        return baseSearchData;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

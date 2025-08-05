package pl.mevist.scrapper.impl.autoscout;

import org.jsoup.Connection;
import pl.mevist.scrapper.core.AbstractSearch;
import pl.mevist.scrapper.core.model.BaseSearchData;

import java.util.Optional;

public class AutoScoutSearch extends AbstractSearch {
    static final String URL = "https://www.autoscout24.pl";

    public AutoScoutSearch(BaseSearchData baseSearchData) {
        super(baseSearchData);
        BaseSearchData searchData = this.getBaseSearch();
    }

    @Override
    protected String buildSearchUrl(int page) {
        StringBuilder url = new StringBuilder(URL).append("/lst/").append("?page=").append(page);
        return this.buildUrlParams(url);
    }

    @Override
    protected String buildUrlParams(StringBuilder url){
        BaseSearchData searchData = getBaseSearch();

        Optional.ofNullable(searchData.getBrand()).ifPresent(brand -> url.append(brand).append('/'));
        Optional.ofNullable(searchData.getModel()).ifPresent(model -> url.append(model).append('/'));
        return url.toString();
    }
}

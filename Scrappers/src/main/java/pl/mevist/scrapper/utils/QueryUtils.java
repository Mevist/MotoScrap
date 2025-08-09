package pl.mevist.scrapper.utils;

public final class QueryUtils {

    public QueryUtils() {
    }

    public static String cssQueryBuilder(String base, String searchBy, String searchValue, boolean startsWith){
        StringBuilder cssQueryBuilder = new StringBuilder();
        cssQueryBuilder
                .append(base)
                .append("[")
                .append(searchBy);

        if (startsWith){
            cssQueryBuilder
                    .append("^=");
        }else{
            cssQueryBuilder
                    .append("$=");
        }

        cssQueryBuilder
                .append(searchValue)
                .append("]");

        return cssQueryBuilder.toString();
    }

    public static String cssQueryBuilder(String base, String searchBy, String searchValue) {
        return cssQueryBuilder(base, searchBy, searchValue, true);
    }
}

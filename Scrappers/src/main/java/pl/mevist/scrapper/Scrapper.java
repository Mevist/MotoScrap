package pl.mevist.scrapper;

public enum Scrapper {
    AUTOSCOUT24("AutoScout24"),
    OTHERPORTAL("OtherPortal"); // example of adding more

    private final String displayName;

    Scrapper(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Returns all available scrapers as a comma-separated list for CLI help */
    public static String getAvailableScrappersList() {
        StringBuilder sb = new StringBuilder();
        for (Scrapper s : Scrapper.values()) {
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(s.name()); // or s.displayName
        }
        return sb.toString();
    }

    /** Try to parse ignoring case */
    public static Scrapper fromString(String name) {
        for (Scrapper s : Scrapper.values()) {
            if (s.name().equalsIgnoreCase(name) || s.displayName.equalsIgnoreCase(name)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown scrapper: " + name);
    }
}


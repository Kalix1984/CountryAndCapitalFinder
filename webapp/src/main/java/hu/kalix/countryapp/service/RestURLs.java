package hu.kalix.countryapp.service;

public enum RestURLs {
    COUNTRY("rest:3000/country/"),
    CAPITAL("rest:3000/capital/");

    private final String url;

    RestURLs(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}

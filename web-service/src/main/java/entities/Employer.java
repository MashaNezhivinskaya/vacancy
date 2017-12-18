package entities;

/**
 * Created by Maria on 26.11.17.
 */
public class Employer {
    String name;
    String url;
    LogoUrl logo_urls;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LogoUrl getLogo_urls() {
        return logo_urls;
    }

    public void setLogo_urls(LogoUrl logo_urls) {
        this.logo_urls = logo_urls;
    }
}

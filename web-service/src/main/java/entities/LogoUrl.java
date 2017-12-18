package entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maria on 26.11.17.
 */
public class LogoUrl {
    @SerializedName("90")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

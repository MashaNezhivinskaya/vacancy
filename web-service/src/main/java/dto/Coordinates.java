package dto;

import java.text.DecimalFormat;

/**
 * Created by Voronovich Viacheslav on 10.01.2018.
 */
public class Coordinates {
    private Double lat;
    private Double lng;

    public Coordinates(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return formatDouble(lat);
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getLng() {
        return formatDouble(lng);
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    private static String formatDouble(Double value) {
        return new DecimalFormat("#.#####").format(value);
    }
}

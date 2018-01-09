package dto;

import java.util.List;

/**
 * Created by Voronovich Viacheslav on 09.01.2018.
 */
public class DateAndCountAndPoints {

    private List<DateAndCount> values;
    private Integer x1;
    private Double y1;
    private Integer x2;
    private Double y2;

    public List<DateAndCount> getValues() {
        return values;
    }

    public void setValues(List<DateAndCount> values) {
        this.values = values;
    }

    public Integer getX1() {
        return x1;
    }

    public void setX1(Integer x1) {
        this.x1 = x1;
    }

    public Double getY1() {
        return y1;
    }

    public void setY1(Double y1) {
        this.y1 = y1;
    }

    public Integer getX2() {
        return x2;
    }

    public void setX2(Integer x2) {
        this.x2 = x2;
    }

    public Double getY2() {
        return y2;
    }

    public void setY2(Double y2) {
        this.y2 = y2;
    }
}

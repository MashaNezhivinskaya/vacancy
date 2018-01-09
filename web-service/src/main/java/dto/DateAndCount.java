package dto;

import java.time.LocalDate;

/**
 * Created by Voronovich Viacheslav on 09.01.2018.
 */
public class DateAndCount {
    private LocalDate date;
    private Integer count;

    public DateAndCount(LocalDate date, Integer count) {
        this.date = date;
        this.count = count;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

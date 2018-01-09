package dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public String getDate() {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    public Integer getCount() {
        return count;
    }
}

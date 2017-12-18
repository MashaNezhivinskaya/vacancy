package entities;

import java.util.List;

/**
 * Created by Voronovich Viacheslav on 28.11.2017.
 */
public class VacanciesItem {
    List<Vacancy> items;

    public List<Vacancy> getItems() {
        return items;
    }

    public void setItems(List<Vacancy> items) {
        this.items = items;
    }
}

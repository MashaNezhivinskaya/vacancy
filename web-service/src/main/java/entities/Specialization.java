package entities;

import java.util.List;

/**
 * Created by Maria on 24.11.17.
 */
public class Specialization {
    Integer id;
    String name;
    List<DictionaryEntity> specializations;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DictionaryEntity> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<DictionaryEntity> specializations) {
        this.specializations = specializations;
    }
}

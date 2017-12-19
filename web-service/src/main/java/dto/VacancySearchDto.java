package dto;

import java.util.List;

/**
 * Created by Voronovich Viacheslav on 19.12.2017.
 */
public class VacancySearchDto {
    private Integer id;
    private String name;
    private Boolean premium;
    private List<String> specializations;

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

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }
}

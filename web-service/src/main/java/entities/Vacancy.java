package entities;

import java.util.List;

/**
 * Created by Maria on 26.11.17.
 */
public class Vacancy {
    private Integer id;
    private String description;
    private List<KeySkill> key_skills;
    private DictionaryEntity schedule;
    private Boolean accept_handicapped;
    private Boolean accept_kids;
    private DictionaryEntity experience;
    private Address address;
    private String alternate_url;
    private DictionaryEntity employment;
    private Salary salary;
    private String name;
    private Test test;
    private Boolean premium;
    //TODO проверить
    private String published_at;
    private Employer employer;
    private List<VacancySpecialization> specializations;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<KeySkill> getKey_skills() {
        return key_skills;
    }

    public void setKey_skills(List<KeySkill> key_skills) {
        this.key_skills = key_skills;
    }

    public DictionaryEntity getSchedule() {
        return schedule;
    }

    public void setSchedule(DictionaryEntity schedule) {
        this.schedule = schedule;
    }

    public Boolean getAccept_handicapped() {
        return accept_handicapped;
    }

    public void setAccept_handicapped(Boolean accept_handicapped) {
        this.accept_handicapped = accept_handicapped;
    }

    public Boolean getAccept_kids() {
        return accept_kids;
    }

    public void setAccept_kids(Boolean accept_kids) {
        this.accept_kids = accept_kids;
    }

    public DictionaryEntity getExperience() {
        return experience;
    }

    public void setExperience(DictionaryEntity experience) {
        this.experience = experience;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getAlternate_url() {
        return alternate_url;
    }

    public void setAlternate_url(String alternate_url) {
        this.alternate_url = alternate_url;
    }

    public DictionaryEntity getEmployment() {
        return employment;
    }

    public void setEmployment(DictionaryEntity employment) {
        this.employment = employment;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public List<VacancySpecialization> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<VacancySpecialization> specializations) {
        this.specializations = specializations;
    }
}

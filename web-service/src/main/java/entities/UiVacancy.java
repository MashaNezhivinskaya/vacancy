package entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Maria on 13.12.17.
 */
public class UiVacancy implements Serializable {
    private Integer id;
    private String description;
    private Boolean acceptHandicapped;
    private Boolean acceptKids;
    private String alternateUrl;
    private String name;
    private Boolean testRequired;
    private Boolean premium;
    private String publishedAt;
    private Integer addressId;
    private Integer salaryId;
    private Integer scheduleId;
    private Integer employmentId;
    private Integer employerId;
    private Integer experienceId;

    private List<String> specializations;
    private List<String> keySkills;

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

    public Boolean getAcceptHandicapped() {
        return acceptHandicapped;
    }

    public void setAcceptHandicapped(Boolean acceptHandicapped) {
        this.acceptHandicapped = acceptHandicapped;
    }

    public Boolean getAcceptKids() {
        return acceptKids;
    }

    public void setAcceptKids(Boolean acceptKids) {
        this.acceptKids = acceptKids;
    }

    public String getAlternateUrl() {
        return alternateUrl;
    }

    public void setAlternateUrl(String alternateUrl) {
        this.alternateUrl = alternateUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTestRequired() {
        return testRequired;
    }

    public void setTestRequired(Boolean testRequired) {
        this.testRequired = testRequired;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getSalaryId() {
        return salaryId;
    }

    public void setSalaryId(Integer salaryId) {
        this.salaryId = salaryId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getEmploymentId() {
        return employmentId;
    }

    public void setEmploymentId(Integer employmentId) {
        this.employmentId = employmentId;
    }

    public Integer getEmployerId() {
        return employerId;
    }

    public void setEmployerId(Integer employerId) {
        this.employerId = employerId;
    }

    public Integer getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Integer experienceId) {
        this.experienceId = experienceId;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    public List<String> getKeySkills() {
        return keySkills;
    }

    public void setKeySkills(List<String> keySkills) {
        this.keySkills = keySkills;
    }
}

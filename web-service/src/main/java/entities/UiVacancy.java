package entities;

import com.google.gson.annotations.SerializedName;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maria on 13.12.17.
 */
public class UiVacancy implements Serializable {

    private static final long serialVersionUID = 2326662048259945883L;
    @SerializedName("description")
    private String description;
    @SerializedName("accept_handicapped")
    private Boolean acceptHandicapped;
    @SerializedName("accept_kids")
    private Boolean acceptKids;
    @SerializedName("name")
    private String name;
    @SerializedName("test_required")
    private Boolean testRequired;
    @SerializedName("premium")
    private Boolean premium;
    @SerializedName("address")
    private Address address;
    @SerializedName("salary")
    private Salary salary;
    @SerializedName("schedule")
    private Integer scheduleId;
    @SerializedName("employment")
    private Integer employmentId;
    @SerializedName("employer")
    private Integer employerId;
    @SerializedName("experience")
    private Integer experienceId;

    @SerializedName("specialization")
    private String specialization;
    @SerializedName("keySkill")
    private String keySkill;

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getKeySkill() {
        return keySkill;
    }

    public void setKeySkill(String keySkill) {
        this.keySkill = keySkill;
    }

    public List<String> getKeySkills() {
        if (StringUtils.isEmpty(keySkill)) {
            return Collections.emptyList();
        } else {
            return Arrays.stream(keySkill.split(",")).map(String::trim).collect(Collectors.toList());
        }
    }
}

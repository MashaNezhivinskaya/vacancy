package jdbc;

import dto.NameAndCount;
import dto.VacancySearchDto;
import entities.UiVacancy;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Voronovich Viacheslav on 18.12.2017.
 */
public class DataQueryManager {
    public static List<NameAndCount> getSpecializationGroups() {
        try {
            return MySqlManager.getInstance().getList("select sp.name, vs.count from (select specialization_id as id, count(vacancy_id) as count " +
                    "from vacancy_schema.vacancyspecializations group by specialization_id) vs " +
                    "join vacancy_schema.specialization sp on sp.id = vs.id", resultSet -> {
                NameAndCount specializationGroup = new NameAndCount();
                specializationGroup.setCount(resultSet.getInt("count"));
                specializationGroup.setName(resultSet.getString("name"));
                return specializationGroup;
            });
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<NameAndCount> getProfareaGroups() {
        try {
            return MySqlManager.getInstance().getList("select pr.name, p.count from (select sp.profarea_id as id, sum(vs.count) as count from (select specialization_id as id, count(vacancy_id) as count" +
                    " from vacancy_schema.vacancyspecializations group by specialization_id) vs" +
                    " join vacancy_schema.specialization sp on sp.id = vs.id group by sp.profarea_id) p" +
                    " join vacancy_schema.profarea pr on pr.id = p.id", resultSet -> {
                NameAndCount profareaGroup = new NameAndCount();
                profareaGroup.setCount(resultSet.getInt("count"));
                profareaGroup.setName(resultSet.getString("name"));
                return profareaGroup;
            });
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public static List<VacancySearchDto> getVacanciesSearch(UiVacancy vacancy) {
        StrBuilder builder = new StrBuilder("select * from (SELECT vac.id_vacancy as id, " +
                "vac.name, " +
                "vac.premium, vac.published_at " +
                "FROM vacancy_schema.vacancies vac " +
                "left join vacancy_schema.salary s on s.id = vac.salary_id " +
                "where " +
                "true ");
        boolean acceptHandicaped = vacancy.getAcceptHandicapped() != null;
        boolean acceptKids = vacancy.getAcceptKids() != null;
        boolean testRequired = vacancy.getTestRequired() != null;
        boolean premium = vacancy.getPremium() != null;
        boolean schedule = vacancy.getScheduleId() != null;
        boolean employment = vacancy.getEmploymentId() != null;
        boolean experience = vacancy.getExperienceId() != null;
        boolean dateFrom = vacancy.getSalary() != null && vacancy.getSalary().getFrom() != null;
        boolean dateTo = vacancy.getSalary() != null && vacancy.getSalary().getTo() != null;
        boolean dateAll = vacancy.getSalary() != null && vacancy.getSalary().getFrom() != null && vacancy.getSalary().getTo() != null;
        boolean currency = vacancy.getSalary() != null && vacancy.getSalary().getCurrency() != null;
        boolean specialization = vacancy.getSpecialization() != null;
        builder.add("and vac.accept_handicapped = %d ", acceptHandicaped, () -> vacancy.getAcceptHandicapped() ? 1 : 0)
                .add("and vac.accept_kids = %d ", acceptKids, () ->vacancy.getAcceptKids() ? 1 : 0)
                .add("and vac.`test.required`= %d ", testRequired, () ->vacancy.getTestRequired() ? 1 : 0)
                .add("and vac.premium = %d ", premium, () ->vacancy.getPremium() ? 1 : 0)
                .add("and vac.schedule_id = '%s' ", schedule, vacancy::getScheduleId)
                .add("and vac.employment_id = '%s' ", employment, vacancy::getEmploymentId)
                .add("and vac.experience_id = '%s' ", experience, vacancy::getExperienceId)
                .add("and (s.from >= %d or s.to >= %d) ", dateFrom, () -> new Object[] {vacancy.getSalary().getFrom(), vacancy.getSalary().getFrom()})
                .add("and (s.to <= %d or s.from <= %d) ", dateTo, () -> new Object[] {vacancy.getSalary().getTo(), vacancy.getSalary().getTo()})
                .add("and (s.from >= %d and s.to <= %d) ", dateAll, () -> new Object[] {vacancy.getSalary().getFrom(), vacancy.getSalary().getTo()})
                .add("and s.currency_id = %d ", currency, () -> vacancy.getSalary().getCurrency())
                .add(") search ", true, () -> new Object[0])
                .add("join vacancy_schema.vacancyspecializations vs on vs.vacancy_id = search.id and vs.specialization_id = %s", specialization, vacancy::getSpecialization)
                .add("order by published_at desc limit 6 offset %d", true, vacancy::getOffset);

        List<VacancySearchDto> result = MySqlManager.getInstance().getList(builder.getValue(), resultSet -> {
            VacancySearchDto dto = new VacancySearchDto();
            dto.setId(resultSet.getInt("id"));
            dto.setName(resultSet.getString("name"));
            dto.setPremium(resultSet.getBoolean("premium"));
            return dto;
        });

        result.forEach(dto -> {
            dto.setSpecializations(MySqlManager.getInstance().getList("select distinct(name) from vacancy_schema.vacancyspecializations vs " +
                    "join vacancy_schema.specialization s on s.id = vs.specialization_id " +
                    "where vacancy_id = " + dto.getId(), resultSet -> resultSet.getString(1)));
        });

        return result;
    }

    private static class StrBuilder {
        private StringBuilder value = new StringBuilder();

        public StrBuilder(String value) {
            this.value.append(value);
        }

        public<T extends Object> StrBuilder add(String value, boolean needToAdd, Supplier<T> values) {
            if (needToAdd) {
                this.value.append(String.format(value, values.get()));
            }
            return this;
        }

        public String getValue() {
            return value.toString();
        }
    }

    public static Integer getVacanciesCount() {
        return MySqlManager.getInstance().getObject("select count(*) from `vacancy_schema`.`vacancies`", resultSet -> resultSet.getInt(1));
    }
}

package jdbc;

import dto.DateAndCount;
import dto.DateAndCountAndPoints;
import dto.NameAndCount;
import dto.NameAndCountAndColor;
import dto.VacancySearchDto;
import entities.UiVacancy;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Voronovich Viacheslav on 18.12.2017.
 */
public class DataQueryManager {
    private static final Map<Integer, String> monthNames = new HashMap<Integer, String>() {{
        put(1, "Январь");
        put(2, "Февраль");
        put(3, "Март");
        put(4, "Апрель");
        put(5, "Май");
        put(6, "Июнь");
        put(7, "Июль");
        put(8, "Август");
        put(9, "Сентябрь");
        put(10, "Октябрь");
        put(11, "Ноябрь");
        put(12, "Декабрь");
    }};

    private static final Map<Integer, String> colorsMap = new HashMap<Integer, String>() {{
        put(1, "#FF0F00");
        put(2, "#FF6600");
        put(3, "#FF9E01");
        put(4, "#FCD202");
        put(5, "#F8FF01");
        put(6, "#B0DE09");
        put(7, "#04D215");
        put(8, "#0D8ECF");
        put(9, "#0D52D1");
        put(10, "#2A0CD0");
        put(11, "#8A0CCF");
        put(12, "#CD0D74");
    }};

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

    public static List<NameAndCount> getProfareaDetail(Integer id) {
        return MySqlManager.getInstance().getList("select sp.name, count(*) as count from vacancy_schema.vacancyspecializations vs " +
                "join vacancy_schema.specialization sp on sp.id = vs.specialization_id and sp.profarea_id = ? " +
                "group by sp.id", preparedStatement -> preparedStatement.setInt(1, id), resultSet -> {
            NameAndCount value = new NameAndCount();
            value.setName(resultSet.getString("name"));
            value.setCount(resultSet.getInt("count"));
            return value;
        });
    }

    public static List<NameAndCountAndColor> getVacancyYearDetail() {
        LocalDateTime explicitRightBorder = LocalDate.now().withDayOfMonth(1).plusMonths(1).atStartOfDay();
        LocalDateTime implicitLeftBorder = explicitRightBorder.minusYears(1);
        return MySqlManager.getInstance().getList("select MONTH(published_at) as month, count(*) as count from vacancy_schema.vacancies " +
                "where published_at >= ? and published_at < ? group by MONTH(published_at)", preparedStatement -> {
            preparedStatement.setTimestamp(1, Timestamp.valueOf(implicitLeftBorder));
            preparedStatement.setTimestamp(2, Timestamp.valueOf(explicitRightBorder));
        },resultSet -> {
            NameAndCountAndColor value = new NameAndCountAndColor();
            int month = resultSet.getInt("month");
            value.setName(monthNames.get(month));
            value.setCount(resultSet.getInt("count"));
            value.setColor(colorsMap.get(month));
            return value;
        });
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
        builder.add("and vac.accept_handicapped = %s ", acceptHandicaped, () -> vacancy.getAcceptHandicapped() ? 1 : 0)
                .add("and vac.accept_kids = %s ", acceptKids, () ->vacancy.getAcceptKids() ? 1 : 0)
                .add("and vac.`test.required`= %s ", testRequired, () ->vacancy.getTestRequired() ? 1 : 0)
                .add("and vac.premium = %s ", premium, () ->vacancy.getPremium() ? 1 : 0)
                .add("and vac.schedule_id = '%s' ", schedule, vacancy::getScheduleId)
                .add("and vac.employment_id = '%s' ", employment, vacancy::getEmploymentId)
                .add("and vac.experience_id = '%s' ", experience, vacancy::getExperienceId)
                .add("and (s.from >= %s or s.to >= %s) ", dateFrom, () -> new Object[] {vacancy.getSalary().getFrom(), vacancy.getSalary().getFrom()})
                .add("and (s.to <= %s or s.from <= %s) ", dateTo, () -> new Object[] {vacancy.getSalary().getTo(), vacancy.getSalary().getTo()})
                .add("and (s.from >= %s and s.to <= %s) ", dateAll, () -> new Object[] {vacancy.getSalary().getFrom(), vacancy.getSalary().getTo()})
                .add("and s.currency_id = %s ", currency, () -> vacancy.getSalary().getCurrency())
                .add(") search ", true, () -> new Object[0])
                .add("join vacancy_schema.vacancyspecializations vs on vs.vacancy_id = search.id and vs.specialization_id = '%s'", specialization, vacancy::getSpecialization)
                .add("order by published_at desc limit 6 offset %s", true, vacancy::getOffset);

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

    public static DateAndCountAndPoints getVacaniesCountForLastMonthByProfarea(Integer profareaId) {
        List<DateAndCount> values = MySqlManager.getInstance().getList("select count(*) as count , published_at from (select distinct(vac.id) as id, " +
                "vac.published_at " +
                "from (select vac.id_vacancy as id, date(vac.published_at) as published_at " +
                "from vacancy_schema.vacancies vac " +
                "where date(vac.published_at) between ? and ? " +
                "group by published_at) as vac " +
                "join vacancy_schema.vacancyspecializations vs on vac.id = vs.vacancy_id  " +
                "join vacancy_schema.specialization sp on sp.id = vs.specialization_id and sp.profarea_id = ?) res " +
                "group by published_at order by published_at desc", preparedStatement -> {
            LocalDate now = LocalDate.now();
            preparedStatement.setDate(1, Date.valueOf(now.minusDays(31)));
            preparedStatement.setDate(2, Date.valueOf(now.minusDays(1)));
            preparedStatement.setInt(3, profareaId);
        }, rs -> new DateAndCount(rs.getDate("published_at").toLocalDate(), rs.getInt("count")));
        int n = values.size();
        Integer xySym = 0;
        int xSum = 0;
        int x2Sum = 0;
        int ySum = 0;
        for (int i = 0, x = 1; i < n; i++, x++) {
            Integer y = values.get(i).getCount();
            xySym += x * y;
            xSum += x;
            ySum += y;
            x2Sum += x * x;
        }
        int xSumYSym = xSum * ySum;
        double a1 = n * xySym - xSumYSym * 1.0;
        int a2 = n * x2Sum - xSum*xSum;
        double a = tryGetDouble(() -> a1 / a2);
        double b1 = ySum - a*xSum;
        double b =  tryGetDouble(() -> b1 / n);
        DateAndCountAndPoints result = new DateAndCountAndPoints();
        result.setValues(values);
        int x1 = values.get(0).getCount();
        double y1 = getFunctionResult(x1, a, b);
        int x2 = values.get(values.size()-1).getCount();
        double y2 = getFunctionResult(x2, a, b);
        result.setX1(x1);
        result.setY1(y1);
        result.setX2(x2);
        result.setY2(y2);
        return result;
    }

    private static double tryGetDouble(Supplier<Double> sup) {
        try {
            return sup.get();
        } catch (Exception e) {
            return 0;
        }
    }

    private static double getFunctionResult(int x, double a, double b) {
        return a * x + b;
    }
}

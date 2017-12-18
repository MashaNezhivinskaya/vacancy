package jdbc;

import dto.NameAndCount;

import java.util.Collections;
import java.util.List;

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

    public static Integer getVacanciesCount() {
        return MySqlManager.getInstance().getObject("select count(*) from `vacancy_schema`.`vacancies`", resultSet -> resultSet.getInt(1));
    }
}

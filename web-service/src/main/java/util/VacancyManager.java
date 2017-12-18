package util;

import entities.UiVacancy;
import entities.Vacancy;
import jdbc.MySqlManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Voronovich Viacheslav on 27.11.2017.
 */
public class VacancyManager {

    public static void insertVacancy(Vacancy vacancy) {
        try {
            MySqlManager.getInstance().executePreparedStatement("INSERT INTO `vacancy_schema`.`vacancies` (`id_vacancy`, `description`, " +
                    "`accept_handicapped`, `accept_kids`, `alternate_url`, `name`, `test.required`, `premium`, `published_at`," +
                    " `address_id`, `salary_id`, `schedule_id`, `employment_id`, `employer_id`, `experience_id`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", preparedStatement -> {
                preparedStatement.setInt(1, vacancy.getId());
                preparedStatement.setString(2, vacancy.getDescription());
                preparedStatement.setBoolean(3, vacancy.getAccept_handicapped());
                preparedStatement.setBoolean(4, vacancy.getAccept_kids());
                preparedStatement.setString(5, vacancy.getAlternate_url());
                preparedStatement.setString(6, vacancy.getName());
                if (vacancy.getTest() != null) {
                    preparedStatement.setBoolean(7, vacancy.getTest().getRequired());
                } else {
                    preparedStatement.setNull(7, Types.BOOLEAN);
                }
                preparedStatement.setBoolean(8, vacancy.getPremium());
                preparedStatement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.parse(vacancy.getPublished_at().split("\\+")[0],
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
                Integer addressId = MySqlManager.getInstance().insertAddress(vacancy.getAddress());
                if (addressId == null) {
                    preparedStatement.setNull(10, Types.INTEGER);
                } else {
                    preparedStatement.setInt(10, addressId);
                }
                Integer salaryId = MySqlManager.getInstance().insertSalary(vacancy.getSalary());
                if (salaryId == null) {
                    preparedStatement.setNull(11, Types.INTEGER);
                } else {
                    preparedStatement.setInt(11, salaryId);
                }
                preparedStatement.setString(12, vacancy.getSchedule() != null ? vacancy.getSchedule().getId() : null);
                preparedStatement.setString(13, vacancy.getEmployment() != null ? vacancy.getEmployment().getId() : null);
                Integer employerId = MySqlManager.getInstance().insertEmployer(vacancy.getEmployer());
                if (employerId == null) {
                    preparedStatement.setNull(14, Types.INTEGER);
                } else {
                    preparedStatement.setInt(14, employerId);
                }
                preparedStatement.setString(15, vacancy.getExperience() != null ? vacancy.getExperience().getId() : null);
            });
            MySqlManager.getInstance().executeBatchStatement("INSERT INTO `vacancy_schema`.`vacancyspecializations` (`vacancy_id`, `specialization_id`)" +
                    " VALUES (?, ?)", vacancy.getSpecializations(), (preparedStatement, object) -> {
                preparedStatement.setInt(1, vacancy.getId());
                preparedStatement.setString(2, object.getId());
            });
            MySqlManager.getInstance().executeBatchStatement("INSERT INTO `vacancy_schema`.`key_skills` (`id`, `name`, `vacancy_id`) " +
                    "VALUES (null, ?, ?)", vacancy.getKey_skills(), (preparedStatement, object) -> {
                preparedStatement.setString(1, object.getName());
                preparedStatement.setInt(2, vacancy.getId());
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void instertVacancyNullValid(UiVacancy vacancy) {
        try {
            int vacancyId = MySqlManager.getInstance().executePreparedStatement("INSERT INTO `vacancy_schema`.`vacancies` (`id_vacancy`, `description`, " +
                    "`accept_handicapped`, `accept_kids`, `alternate_url`, `name`, `test.required`, `premium`, `published_at`," +
                    " `address_id`, `salary_id`, `schedule_id`, `employment_id`, `employer_id`, `experience_id`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", ps -> {

                setIfNotNull(ps, ps::setInt, 1, () -> null, Types.INTEGER);
                setIfNotNull(ps, ps::setString, 2, vacancy::getDescription, Types.VARCHAR);
                setIfNotNull(ps, ps::setBoolean, 3, vacancy::getAcceptHandicapped, Types.BOOLEAN);
                setIfNotNull(ps, ps::setBoolean, 4, vacancy::getAcceptKids, Types.BOOLEAN);
                setIfNotNull(ps, ps::setString, 5, () -> null, Types.VARCHAR);
                setIfNotNull(ps, ps::setString, 6, vacancy::getName, Types.VARCHAR);
                setIfNotNull(ps, ps::setBoolean, 7, vacancy::getTestRequired, Types.BOOLEAN);
                setIfNotNull(ps, ps::setBoolean, 8, vacancy::getPremium, Types.BOOLEAN);
                setIfNotNull(ps, ps::setTimestamp, 9, () -> Timestamp.valueOf(LocalDateTime.now()), Types.TIMESTAMP);
                Integer addressId = MySqlManager.getInstance().insertAddress(vacancy.getAddress());
                Integer salaryId = MySqlManager.getInstance().insertSalary(vacancy.getSalary());
                setIfNotNull(ps, ps::setInt, 10, () -> addressId, Types.INTEGER);
                setIfNotNull(ps, ps::setInt, 11, () -> salaryId, Types.INTEGER);
                setIfNotNull(ps, ps::setInt, 12, vacancy::getScheduleId, Types.INTEGER);
                setIfNotNull(ps, ps::setInt, 13, vacancy::getEmploymentId, Types.INTEGER);
                setIfNotNull(ps, ps::setInt, 14, vacancy::getEmployerId, Types.INTEGER);
                setIfNotNull(ps, ps::setInt, 15, vacancy::getExperienceId, Types.INTEGER);
            });
            MySqlManager.getInstance().executePreparedStatement("INSERT INTO `vacancy_schema`.`vacancyspecializations` (`vacancy_id`, `specialization_id`)" +
                    " VALUES (?, ?)", preparedStatement -> {
                preparedStatement.setInt(1, vacancyId);
                preparedStatement.setString(2, vacancy.getSpecialization());
            });
            MySqlManager.getInstance().executeBatchStatement("INSERT INTO `vacancy_schema`.`key_skills` (`id`, `name`, `vacancy_id`) " +
                    "VALUES (null, ?, ?)", vacancy.getKeySkills(), (ps, skill) -> {
                ps.setString(1, skill);
                ps.setInt(2, vacancyId);
            });
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @FunctionalInterface
    private interface BiConsumerSqlException<T, U> {

        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         */
        void accept(T t, U u) throws SQLException;
    }


    private static <T> void setIfNotNull(PreparedStatement ps, BiConsumerSqlException<Integer, T> ifNonNull, int index, Supplier<T> value, int type) throws SQLException {
        if (value.get() == null) {
            ps.setNull(index, type);
        } else {
            ifNonNull.accept(index, value.get());
        }
    }


}

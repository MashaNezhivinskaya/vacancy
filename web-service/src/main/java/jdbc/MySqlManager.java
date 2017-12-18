package jdbc;

import entities.Address;
import entities.Employer;
import entities.Salary;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class MySqlManager {

    private static final String jdbcDriverStr = "com.mysql.jdbc.Driver";
    private static final String jdbcURL = "jdbc:mysql://localhost/vacancy_schema?user=root&password=root";

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;

    static {
        try {
            Class.forName(jdbcDriverStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            connection = DriverManager.getConnection(jdbcURL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> List<T> getList(String query, RowMapper<T> rowMapper) {
        try {
            List<T> list = new LinkedList<T>();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                list.add(rowMapper.mapEntity(resultSet));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            close();
        }
        return new LinkedList<T>();
    }

    public <T> T getObject(String query, RowMapper<T> mapper) {
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return mapper.mapEntity(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }

    public int executePreparedStatement(String query, QueryFiller filler) {
        try {
            preparedStatement = connection.prepareStatement(query);
            filler.fillQuery(preparedStatement);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
            ResultSet rs = connection.createStatement().executeQuery("select LAST_INSERT_ID()");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return -1;
    }

    public <T> void executeBatchStatement(String query, List<T> objects, BatchQueryFiller<T> filler) {
        try {
            if (objects == null || objects.isEmpty()) {
                return;
            }
            objects.forEach(object -> {
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = connection.prepareStatement(query);
                    filler.fillQuery(preparedStatement, object);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    private void close(){
        try {
            if(resultSet!=null) resultSet.close();
            if(statement!=null) statement.close();
        } catch(Exception e){}
    }

    public void closeConnection() {
        if(connection!=null) try {
            connection.close();
        } catch (SQLException e) {
        }
    }

    public Integer insertAddress(Address address) {
        try {
            if (address == null || address.isEmpty()) {
                return null;
            }
            preparedStatement = connection.prepareStatement("INSERT INTO `vacancy_schema`.`address` (`id`, `city`, `street`," +
                    " `building`, `description`, `lat`, `lng`) VALUES (null, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            setDataOrNull(preparedStatement::setString, address.getCity(), 1, Types.VARCHAR);
            setDataOrNull(preparedStatement::setString, address.getStreet(), 2, Types.VARCHAR);
            setDataOrNull(preparedStatement::setString, address.getBuilding(), 3, Types.VARCHAR);
            setDataOrNull(preparedStatement::setString, address.getDescription(), 4, Types.VARCHAR);
            setDataOrNull(preparedStatement::setDouble, address.getLat(), 5, Types.DOUBLE);
            setDataOrNull(preparedStatement::setDouble, address.getLng(), 6, Types.DOUBLE);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    public Integer insertSalary(Salary salary) {
        try {
            if (salary == null) {
                return null;
            }
            preparedStatement = connection.prepareStatement("INSERT INTO `vacancy_schema`.`salary` (`id`, `from`, `to`, `currency_id`) " +
                    "VALUES (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            setDataOrNull(preparedStatement::setDouble, salary.getFrom(), 1, Types.DOUBLE);
            setDataOrNull(preparedStatement::setDouble, salary.getTo(), 2, Types.DOUBLE);
            setDataOrNull(preparedStatement::setString, salary.getCurrency(), 3, Types.VARCHAR);
            preparedStatement.setString(3, salary.getCurrency());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    public Integer insertEmployer(Employer employer) {
        try {
            if (employer == null) {
                return null;
            }
            preparedStatement = connection.prepareStatement("INSERT INTO `vacancy_schema`.`employer` (`id`, `name`, " +
                    "`logo_urls_90`, `site_url`) VALUES (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, employer.getName());
            if (employer.getLogo_urls() != null) {
                preparedStatement.setString(2, employer.getLogo_urls().getUrl());
            } else {
                preparedStatement.setNull(2, Types.VARCHAR);
            }
            preparedStatement.setString(3, employer.getUrl());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            close();
        }
    }

    private <T> void setDataOrNull(BiConsumerSqlException<Integer, T> consumer, T object, int index, int typeIfNull) throws SQLException {
        if (object == null) {
            preparedStatement.setNull(index, typeIfNull);
        } else {
            consumer.accept(index, object);
        }
    }

    @FunctionalInterface
    public interface RowMapper<T> {
        T mapEntity(ResultSet resultSet) throws SQLException;
    }

    @FunctionalInterface
    public interface QueryFiller {
        void fillQuery(PreparedStatement preparedStatement) throws SQLException;
    }

    @FunctionalInterface
    public interface BatchQueryFiller<T> {
        void fillQuery(PreparedStatement preparedStatement, T object) throws SQLException;
    }

    @FunctionalInterface
    public interface BiConsumerSqlException<T, U> {

        /**
         * Performs this operation on the given arguments.
         *
         * @param t the first input argument
         * @param u the second input argument
         */
        void accept(T t, U u) throws SQLException;
    }
}
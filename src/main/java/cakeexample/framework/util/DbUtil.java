package cakeexample.framework.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static cakeexample.framework.util.Throwables.propagate;

public class DbUtil {
    private final Connection connection;

    public DbUtil(String driverClass, String url) {
        connection = Throwables.propagate(() -> {
            Class.forName(driverClass);
            return DriverManager.getConnection(url);
        });
    }

    public void insertSingleColumn(String table, String value) {
        try {
            PreparedStatement statement = connection.prepareStatement("insert into " + table + " values(?)");
            statement.setString(1, value);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTableIfNotExists(String tableName, String... columnNames) {
        String sql = "";
        for (String columnName : columnNames) {
            if (sql.length() != 0)
                sql += ", ";
            sql += "  " + columnName + " varchar";
        }
        sql = "create table if not exists " + tableName + " (\n" + sql + ")\n";
        final String s = sql;
        Throwables.propagate(() -> connection.createStatement().execute(s));
    }

    public <T> List<T> select(String table, Function<ResultSet, T> function) {
        return Throwables.propagate(() -> {
            List<T> list = new ArrayList<>();
            Statement statement = Throwables.propagate(connection::createStatement);
            ResultSet resultSet = Throwables.propagate(() -> statement.executeQuery("select * from " + table));
            while (resultSet.next()) {
                list.add(function.apply(resultSet));
            }
            return list;
        });
    }

}

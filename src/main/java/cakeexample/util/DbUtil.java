package cakeexample.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static cakeexample.util.Throwables.propagate;

public class DbUtil {
    private final Connection connection;

    public DbUtil(String driverClass, String url) {
        connection = propagate(() -> {
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
        propagate(() -> connection.createStatement().execute(s));
    }

    public <T> List<T> select(String table, Function<ResultSet, T> function) {
        return propagate(() -> {
            List<T> list = new ArrayList<>();
            Statement statement = propagate(connection::createStatement);
            ResultSet resultSet = propagate(() -> statement.executeQuery("select * from " + table));
            while (resultSet.next()) {
                list.add(function.apply(resultSet));
            }
            return list;
        });
    }

    public Long countRows(String name) {
        return propagate(() -> {
            Statement statement = propagate(connection::createStatement);
            ResultSet resultSet = propagate(() -> statement.executeQuery("select count(*) from " + name));
            resultSet.next();
            return resultSet.getLong(1);
        });
    }
}

package cakeexample.framework.util;

import fj.data.List;

import java.sql.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static cakeexample.framework.util.Throwables.propagate;

public class DbUtil {
    private final Connection connection;
    private final Supplier<Boolean> showSql;

    public DbUtil(String driverClass, String url, Supplier<Boolean> showSql) {
        this.showSql = showSql;
        connection = propagate(() -> {
            Class.forName(driverClass);
            return DriverManager.getConnection(url);
        });
    }

    public void insertSingleColumn(String table, String value) {
        try {
            String sql = "insert into " + table + " values(?)";
            printSql(sql);
            PreparedStatement statement = connection.prepareStatement(sql);
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
        printSql(sql);
        propagate(() -> connection.createStatement().execute(s));
    }

    private void printSql(String sql) {
        if (showSql.get()) {
            System.out.println("SQL: " + sql);
        }
    }

    public <T> List<T> select(String table, Function<ResultSet, T> function) {
        return propagate(() -> {
            List<T> list = List.nil();
            Statement statement = propagate(connection::createStatement);
            ResultSet resultSet = propagate(() -> statement.executeQuery("select * from " + table));
            while (resultSet.next()) {
                list = list.cons(function.apply(resultSet));
            }
            return list;
        });
    }

}

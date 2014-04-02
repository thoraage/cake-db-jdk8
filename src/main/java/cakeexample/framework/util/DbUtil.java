package cakeexample.framework.util;

import cakeexample.framework.gnurf.Column;
import fj.F2;
import fj.data.List;

import java.sql.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static cakeexample.framework.util.Throwables.propagate;

public class DbUtil {
    private final Connection connection;
    private final Supplier<Boolean> showSql;

    public DbUtil(String driverClass, String url, Supplier<Boolean> showSql) {
        this.showSql = showSql;
        connection = propagate(() -> {
            Class.forName(driverClass);
            Connection connection = DriverManager.getConnection(url);
            connection.setAutoCommit(true);
            return connection;
        });
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
            Statement statement = connection.createStatement();
            String sql = "select * from " + table;
            printSql(sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list = list.cons(function.apply(resultSet));
            }
            return list;
        });
    }

    public <C> Optional<Long> insert(String table, List<Column<C, ?>> columns) {
        return propagate(() -> {
            F2<String,String,String> concatWithCommas = (s1, s2) -> s1 + (s1.isEmpty() ? "" : ", ") + s2;
            String sql = "insert into " + table + " (" +
                    columns.map(c -> c.name).foldLeft(concatWithCommas, "") + ") values (" +
                    columns.map(c -> "?").foldLeft(concatWithCommas, "") + ")";
            printSql(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            columns.zipIndex().foreachDo(p2 -> propagate(() -> preparedStatement.setString(p2._2() + 1, (String) p2._1().field.value.get())));
            preparedStatement.execute();
            return Optional.empty();
        });
    }
}

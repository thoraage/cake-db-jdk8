package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Field;
import fj.F2;
import fj.P2;
import fj.data.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static cakeexample.framework.util.Throwables.propagate;

public class DbUtil {
    private final static Logger logger = LoggerFactory.getLogger(DbUtil.class);
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

    public ColumnResultMapper columnMapper(Column<?, ?> column) {
        // TODO create these outside and map them here?
        if (column.field instanceof Field) {
            //noinspection unchecked
            return (cr) -> cr._2().field.as(getValue(cr._1(), cr._2()));
        }
        throw new RuntimeException("Column mapping for column " + column + " not found");
    }

    private <T> T getValue(ResultSet r, Column<?, T> c) {
        //noinspection unchecked
        return (T) propagate(() -> r.getString(c.name));
    }

    private void printSql(String sql) {
        if (showSql.get()) {
            logger.info("SQL: " + sql);
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
            F2<String, String, String> concatWithCommas = (s1, s2) -> s1 + (s1.isEmpty() ? "" : ", ") + s2;
            String sql = "insert into " + table + " (" +
                    columns.map(c -> c.name).foldLeft(concatWithCommas, "") + ") values (" +
                    columns.map(c -> "?").foldLeft(concatWithCommas, "") + ")";
            printSql(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            columns.zipIndex().foreachDo(p2 -> propagate(() -> setColumnValue(preparedStatement, p2)));
            preparedStatement.execute();
            return Optional.<Long>empty();
        });
    }

    private <C> void setColumnValue(PreparedStatement preparedStatement, P2<Column<C, ?>, Integer> p2) throws SQLException {
        // TODO some abstraction needed
        Object value = p2._1().field.value().get();
        int index = p2._2() + 1;
        setColumnValue(preparedStatement, index, value);
    }

    private void setColumnValue(PreparedStatement preparedStatement, int index, Object value) throws SQLException {
        if (value == null) {
            preparedStatement.setObject(index, null);
        } else if (value instanceof Optional) {
            //noinspection unchecked
            setColumnValue(preparedStatement, index, ((Optional) value).orElse(null));
        } else if (String.class.isAssignableFrom(value.getClass())) {
            preparedStatement.setString(index, (String) value);
        } else {
            throw new RuntimeException("Unable to handle type " + value.getClass());
        }
    }
}

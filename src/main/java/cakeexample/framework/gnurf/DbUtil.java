package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import cakeexample.framework.domain.Field;
import cakeexample.framework.domain.OptionalField;
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

    public <C> void createTableIfNotExists(String tableName, List<AbstractColumn<C, ?>> columns) {
        String sql = "";
        for (AbstractColumn<?, ?> column : columns) {
            if (sql.length() != 0)
                sql += ", ";
            final String columnType;
            Class<?> clazz = column.field().clazz();
            if (column instanceof Column) {
                columnType = getH2ColumnType(clazz);
            } else if (column instanceof OneToOneColumn) {
                columnType = getH2ColumnType(((OneToOneColumn) column).foreignPrimaryKey().field().clazz());
            } else {
                throw new NotImplementedException();
            }
            String name = column.name();
            String primaryKeyText = column.primaryKey() ? " primary key" : "";
            String autoIncrementText = column.autoIncrement() ? " auto_increment" : "";
            sql += "  " + name + " " + columnType + primaryKeyText + autoIncrementText;
        }
        sql = "create table if not exists " + tableName + " (\n" + sql + ")\n";
        final String s = sql;
        printSql(sql);
        propagate(() -> connection.createStatement().execute(s));
    }

    private String getH2ColumnType(Class<?> clazz) {
        String columnType;
        if (String.class.isAssignableFrom(clazz)) {
            columnType = "varchar";
        } else if (Integer.class.isAssignableFrom(clazz)) {
            columnType = "integer";
        } else if (Long.class.isAssignableFrom(clazz)) {
            columnType = "bigint";
        } else {
            throw new RuntimeException("Unknown type for column creation " + clazz);
        }
        return columnType;
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

    public <C> Optional<Long> insert(String table, List<AbstractColumn<C, ?>> columns) {
        return propagate(() -> {
            F2<String, String, String> concatWithCommas = (s1, s2) -> s1 + (s1.isEmpty() ? "" : ", ") + s2;
            String sql = "insert into " + table + " (" +
                    columns.map(AbstractColumn::name).foldLeft(concatWithCommas, "") + ") values (" +
                    columns.map(c -> "?").foldLeft(concatWithCommas, "") + ")";
            printSql(sql);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            columns.zipIndex().foreachDo(p2 -> propagate(() -> setColumnValue(preparedStatement, p2)));
            preparedStatement.execute();
            return Optional.<Long>empty();
        });
    }

    private <C> void setColumnValue(PreparedStatement preparedStatement, P2<AbstractColumn<C, ?>, Integer> p2) throws SQLException {
        // TODO some abstraction needed (value type plugins)
        Object value = p2._1().field().value().get();
        int index = p2._2() + 1;
        setColumnValue(preparedStatement, index, value);
    }

    private void setColumnValue(PreparedStatement preparedStatement, int index, Object value) throws SQLException {
        if (value == null) {
            preparedStatement.setObject(index, null);
        } else if (value instanceof Optional) {
            //noinspection unchecked
            setColumnValue(preparedStatement, index, ((Optional) value).orElse(null));
        } else {
            preparedStatement.setObject(index, value);
        }
    }

    public <V extends Number> Optional<V> getLastGeneratedValue() {
        // TODO Only handles single generated values. Can multiple occur; any dimension, rows or columns?
        return propagate(() -> {
            ResultSet resultSet = connection.createStatement().getGeneratedKeys();
            if (resultSet.next()) {
                //noinspection unchecked
                V number = (V) resultSet.getObject(1);
                // In H2 appearently, we get result even if there has not been generated any keys yet
                // (that result will be zero)
                if (number.longValue() == 0) {
                    return Optional.empty();
                }
                return Optional.of(number);
            }
            return Optional.<V>empty();
        });
    }
}

package cakeexample.framework.gnurf;

import fj.F2;
import fj.P2;
import fj.data.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

import static cakeexample.framework.util.Throwables.propagate;

public class DbUtil {
    private final static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private static <C, V> AbstractColumn<C, V> addEntityValue(AbstractColumn<C, V> c, C entity) {
        // TODO more information in error (which table for example or get position from stack trace during creation)
        //noinspection unchecked
        return c.columnValue(entity)
                .map(value -> c.withField(c.field().as((V) value)))
                .orElseThrow(() -> new RuntimeException("Field of column " + c.name() + " missing getter function"));
    }

    public static <C> void createTableIfNotExists(DatabaseSession session, TableCharacteristics<C> table) {
        String sql = "";
        for (AbstractColumn<?, ?> column : table.columns()) {
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
        sql = "create table if not exists " + table.name() + " (\n" + sql + ")\n";
        final String s = sql;
        printSql(session, sql);
        propagate(() -> session.connection().createStatement().execute(s));
    }

    private static String getH2ColumnType(Class<?> clazz) {
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

    private static void printSql(DatabaseSession session, String sql) {
        if (session.showSqlEnabled()) {
            logger.info("SQL: " + sql);
        }
    }

    public static <C> List<C> selectAll(DatabaseSession session, TableCharacteristics<C> table) {
        return propagate(() -> {
            try (Connection connection = session.connection()) {
                try (Statement statement = connection.createStatement()) {
                    String sql = "select * from " + table.name();
                    printSql(session, sql);
                    try (ResultSet resultSet = statement.executeQuery(sql)) {
                        List<C> list = List.nil();
                        while (resultSet.next()) {
                            list = list.cons(table.entityConstructor().apply(table.columns().map(c -> c.withResult(resultSet))));
                        }
                        return list;
                    }
                }
            }
        });
    }

    public static <C> Optional<Long> insert(DatabaseSession session, TableCharacteristics<C> table, List<AbstractColumn<C, ?>> columns) {
        return propagate(() -> {
            F2<String, String, String> concatWithCommas = (s1, s2) -> s1 + (s1.isEmpty() ? "" : ", ") + s2;
            String sql = "insert into " + table.name() + " (" +
                    columns.map(AbstractColumn::name).foldLeft(concatWithCommas, "") + ") values (" +
                    columns.map(c -> "?").foldLeft(concatWithCommas, "") + ")";
            printSql(session, sql);
            PreparedStatement preparedStatement = session.connection().prepareStatement(sql);
            columns.zipIndex().foreachDo(p2 -> propagate(() -> setColumnValue(preparedStatement, p2)));
            preparedStatement.execute();
            return Optional.<Long>empty();
        });
    }

    private static <C> void setColumnValue(PreparedStatement preparedStatement, P2<AbstractColumn<C, ?>, Integer> p2) throws SQLException {
        // TODO some abstraction needed (value type plugins)
        Object value = p2._1().field().value().get();
        int index = p2._2() + 1;
        setColumnValue(preparedStatement, index, value);
    }

    private static void setColumnValue(PreparedStatement preparedStatement, int index, Object value) throws SQLException {
        if (value == null) {
            preparedStatement.setObject(index, null);
        } else if (value instanceof Optional) {
            //noinspection unchecked
            setColumnValue(preparedStatement, index, ((Optional) value).orElse(null));
        } else {
            preparedStatement.setObject(index, value);
        }
    }

    public static <V extends Number> Optional<V> getLastGeneratedValue(DatabaseSession session) {
        // TODO Only handles single generated values. Can multiple occur; any dimension, rows or columns?
        return propagate(() -> {
            ResultSet resultSet = session.connection().createStatement().getGeneratedKeys();
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

    public static <C> InsertContinuation<C, Long> insert(DatabaseSession session, TableCharacteristics<C> table, C entity) {
        return new InsertContinuation<C, Long>(session, insert(session, table, table.columns().map(c -> addEntityValue(c, entity))));
    }

    public static class InsertContinuation<C, V> {
        private final DatabaseSession session;
        private final Optional<Long> autogeneratedKey;

        public InsertContinuation(DatabaseSession session, Optional<Long> autogeneratedKey) {
            this.session = session;
            this.autogeneratedKey = autogeneratedKey;
        }

        public C retrieve() {
            throw new RuntimeException("Not implemented");
        }

        public <V extends Number> Optional<V> id() {
            return getLastGeneratedValue(session);
        }
    }

}

package cakeexample.framework.gnurf;

import cakeexample.framework.domain.AbstractField;
import fj.F;
import fj.F2;
import fj.P2;
import fj.data.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;
import java.util.function.Function;

import static cakeexample.framework.util.Throwables.propagate;

public class DbUtil {
    private final static Logger logger = LoggerFactory.getLogger(DbUtil.class);

    private static <C, V> AbstractColumn<C, V> addEntityValue(AbstractColumn<C, V> column, C entity) {
        // TODO more information in error (which table for example or get position from stack trace during creation)
        //noinspection unchecked
        return column.extractFieldValue(entity)
                .map(value -> column.withField(column.field().as((V) value)))
                .orElseThrow(() -> new RuntimeException("Field of column " + column.name() + " missing getter function"));
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
        propagate(() -> {
            try (Connection connection = session.connection()) {
                try (Statement statement = connection.createStatement()) {
                    return statement.execute(s);
                }
            }
        });
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
        Function<Iterable<AbstractField<C, ?>>, C> entityConstructor = table.entityConstructor();
        F<List<AbstractColumn<C, ?>>, List<AbstractField<C, ?>>> findFields = columns -> columns.map(AbstractColumn::field);
        F<List<AbstractColumn<C, ?>>, List<AbstractColumn<C, ?>>> subEntities = columns -> columns.map(c -> c.retrieveEntity(session));
        return selectAllFields(session, table).map(subEntities).map(findFields).map(entityConstructor::apply);
    }

    private static <C> List<List<AbstractColumn<C, ?>>> selectAllFields(DatabaseSession session, TableCharacteristics<C> table) {
        return propagate(() -> {
            try (Connection connection = session.connection()) {
                try (Statement statement = connection.createStatement()) {
                    String sql = "select * from " + table.name();
                    printSql(session, sql);
                    try (ResultSet resultSet = statement.executeQuery(sql)) {
                        List<List<AbstractColumn<C, ?>>> list = List.nil();
                        while (resultSet.next()) {
                            list = list.cons(table.columns().map(c -> c.withResult(resultSet)));
                        }
                        return list;
                    }
                }
            }
        });
    }

    public static <C> Optional<Long> insert(DatabaseSession session, TableCharacteristics<C> table, List<AbstractColumn<C, ?>> preColumns) {
        // TODO ignores multiple generated primary keys
        List<AbstractColumn<C, ?>> columns = preColumns.filter(column -> !column.autoIncrement()).map(column -> column.preInsert(session));
        return propagate(() -> {
            F2<String, String, String> concatWithCommas = (s1, s2) -> s1 + (s1.isEmpty() ? "" : ", ") + s2;
            String sql = "insert into " + table.name() + " (" +
                    columns.map(AbstractColumn::name).foldLeft(concatWithCommas, "") + ") values (" +
                    columns.map(c -> "?").foldLeft(concatWithCommas, "") + ")";
            printSql(session, sql);
            try (Connection connection = session.connection()) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    columns.zipIndex().foreachDo(p2 -> propagate(() -> setColumnValue(preparedStatement, p2)));
                    preparedStatement.execute();
                    return DbUtil.<Long> getLastGeneratedKeys(preparedStatement);
                }
            }
        });
    }

    private static <C> void setColumnValue(PreparedStatement preparedStatement, P2<AbstractColumn<C, ?>, Integer> p2) throws SQLException {
        // TODO some abstraction needed (value type plugins)
        Object value = p2._1().columnValue().get();
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

    private static <V extends Number> Optional<V> getLastGeneratedKeys(Statement statement) throws SQLException {
        // TODO Only handles single generated values. Can multiple occur; any dimension, rows or columns?
        ResultSet resultSet = statement.getGeneratedKeys();
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
    }

    public static <C, TCTO extends TableCharacteristics<C> & TableOperations<C>> InsertContinuation<C, Long> insert(DatabaseSession session, TCTO table, C entity) {
        return new InsertContinuation<C, Long>(session, table, insert(session, table, table.columns().map(c -> addEntityValue(c, entity))));
    }

    public static class InsertContinuation<C, V> {
        private final DatabaseSession session;
        private final TableOperations<C> table;
        private final Optional<V> autogeneratedKey;

        public InsertContinuation(DatabaseSession session, TableOperations<C> table, Optional<V> autogeneratedKey) {
            this.session = session;
            this.table = table;
            this.autogeneratedKey = autogeneratedKey;
        }

        public C retrieve() {
            // TODO just select på primary key
            return table.selectAll(session).head();
        }

        public Optional<V> id() {
            return autogeneratedKey;
        }
    }

    public static Connection createConnection(String databaseDriverClass, String databaseUrl, boolean autoCommit) {
        return propagate(() -> {
            Class.forName(databaseDriverClass);
            Connection connection = DriverManager.getConnection(databaseUrl);
            connection.setAutoCommit(autoCommit);
            return connection;
        });
    }

}

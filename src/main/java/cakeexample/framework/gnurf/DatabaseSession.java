package cakeexample.framework.gnurf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

import static cakeexample.framework.util.Throwables.propagate;

public class DatabaseSession {

    private final String databaseDriverClass;
    private final String databaseUrl;
    private Supplier<Boolean> showSql;

    public DatabaseSession(String databaseDriverClass, String databaseUrl, Supplier<Boolean> showSql) {
        this.databaseDriverClass = databaseDriverClass;
        this.databaseUrl = databaseUrl;
        this.showSql = showSql;
    }

    private Connection createConnection(String databaseDriverClass, String databaseUrl) {
        return propagate(() -> {
            Class.forName(databaseDriverClass);
            Connection connection = DriverManager.getConnection(databaseUrl);
            connection.setAutoCommit(true);
            return connection;
        });
    }

    public boolean showSqlEnabled() {
        return showSql.get();
    }

    public Connection connection() {
        return createConnection(databaseDriverClass, databaseUrl);
    }
}

package cakeexample.framework.gnurf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Supplier;

import static cakeexample.framework.util.Throwables.propagate;

public class GnurfDbSession {

    private final Connection connection;
    private Supplier<Boolean> showSql;

    public GnurfDbSession(String databaseDriverClass, String databaseUrl, Supplier<Boolean> showSql) {
        this.showSql = showSql;
        this.connection = propagate(() -> {
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
        return connection;
    }
}

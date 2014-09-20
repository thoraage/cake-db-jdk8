package cakeexample.framework.gnurf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

import static cakeexample.framework.util.Throwables.propagate;

public class NewConnectionPerRequestSession implements DatabaseSession {

    private final String databaseDriverClass;
    private final String databaseUrl;
    private Supplier<Boolean> showSql;

    public NewConnectionPerRequestSession(String databaseDriverClass, String databaseUrl, Supplier<Boolean> showSql) {
        this.databaseDriverClass = databaseDriverClass;
        this.databaseUrl = databaseUrl;
        this.showSql = showSql;
    }

    public boolean showSqlEnabled() {
        return showSql.get();
    }

    public Connection connection() {
        return DbUtil.createConnection(databaseDriverClass, databaseUrl, true);
    }
}

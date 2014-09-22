package cakeexample.framework.gnurf;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.reflect.Proxy.newProxyInstance;

public class PerpetualConnectionSession implements DatabaseSession {

    private final String databaseDriverClass;
    private final String databaseUrl;
    private final Supplier<Boolean> showSql;
    private Optional<Connection> connection = Optional.empty();

    public PerpetualConnectionSession(String databaseDriverClass, String databaseUrl, Supplier<Boolean> showSql) {
        this.databaseDriverClass = databaseDriverClass;
        this.databaseUrl = databaseUrl;
        this.showSql = showSql;
    }

    @Override
    public boolean showSqlEnabled() {
        return showSql.get();
    }

    @Override
    public Connection connection() {
        return connection.orElseGet(() -> {
            Connection connection = (Connection) newProxyInstance(getClass().getClassLoader(), new Class<?>[]{Connection.class}, new ConnectionProxy(DbUtil.createConnection(databaseDriverClass, databaseUrl, false)));
            this.connection = Optional.of(connection);
            return connection;
        });
    }
}

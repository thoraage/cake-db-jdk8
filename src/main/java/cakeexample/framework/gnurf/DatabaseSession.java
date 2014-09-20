package cakeexample.framework.gnurf;

import java.sql.Connection;

public interface DatabaseSession {

    default boolean showSqlEnabled() {
        return false;
    }

    Connection connection();

}

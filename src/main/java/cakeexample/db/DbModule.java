package cakeexample.db;

import java.util.List;

public interface DbModule {

    interface Db {
        void create(String name);
        List<String> getCakes();
    }

    Db getDb();

}

package cakeexample.db;

import java.util.List;

public interface Db {
    void create(String name);
    List<String> getCakes();
}

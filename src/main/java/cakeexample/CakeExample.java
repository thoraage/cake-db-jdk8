package cakeexample;

public class CakeExample {
    static public void main(String[] args) {
        for (String name : new CakeModuleImpl().getDb().getCakes()) {
            System.out.println(name);
        }
    }
}

class CakeModuleImpl extends SingletonModuleImpl implements H2MemConfigModule, JdbcDbModule {
}


public class CakeExample {
    static public void main(String[] args) {
        System.out.println(new CakeImpl().getDb().getData());
        System.out.println(new MockCake().getDb().getData());
    }
}

interface Db {
    public String getData();
}

interface DbModule {
    public Db getDb();
}

abstract class Cake implements DbModule {

    public void printThingy() {
        System.out.println(getDb().getData());
    }

}

interface RealDbModule extends DbModule {
    class RealDb implements Db {
        @Override
        public String getData() {
            return "Real cake tastes goooood!";
        }
    }

    @Override
    default public Db getDb() {
        return new RealDb();
    }
}

class CakeImpl implements RealDbModule {
}

interface MockDbModule extends DbModule {
    class MockDb implements Db {
        @Override
        public String getData() {
            return "*Yuck* plastic fantastic";
        }
    }
    @Override
    default public Db getDb() {
        return new MockDb();
    }
}

class MockCake implements MockDbModule {
}

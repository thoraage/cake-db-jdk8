package cakeexample;

public interface RealDbModule extends DbModule {
    class RealDb implements Db {
        @Override
        public String getData() {
            return "Real cake tastes goooood!";
        }
    }

    @Override
    default Db getDb() {
        return new RealDb();
    }
}

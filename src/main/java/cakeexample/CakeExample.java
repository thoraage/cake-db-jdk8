package cakeexample;

public class CakeExample {
    static public void main(String[] args) {
        for (String name : new CakeImpl().getDb().getNames()) {
            System.out.println(name);
        }
    }
}

class CakeImpl implements RealDbModule {
}


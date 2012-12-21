package cakeexample;

public class CakeExample {
    static public void main(String[] args) {
        System.out.println(new CakeImpl().getDb().getData());
    }
}

class CakeImpl implements RealDbModule {
}


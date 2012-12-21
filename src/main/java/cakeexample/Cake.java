package cakeexample;

abstract class Cake implements DbModule {

    public void printThingy() {
        System.out.println(getDb().getNames());
    }

}

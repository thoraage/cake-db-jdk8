package cakeexample;

import cakeexample.db.DbModule;

abstract class Cake implements DbModule {

    public void printThingy() {
        System.out.println(getDb().getCakes());
    }

}

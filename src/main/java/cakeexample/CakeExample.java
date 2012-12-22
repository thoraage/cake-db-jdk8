package cakeexample;

public class CakeExample {
    static public void main(String[] args) {
        CakeStack stack = new CakeStack();
        for (String name : stack.getDb().getCakes()) {
            System.out.println(name);
        }
        stack.getWebHandler().start();
        stack.getWebHandler().join();
        stack.getWebHandler().stop();
    }
}


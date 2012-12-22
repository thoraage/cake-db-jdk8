package cakeexample;

import cakeexample.util.HttpUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class CakeStackTest {

    private CakeStack stack = new CakeStack();

    @Before
    public void setUpStack() {
        stack.getWebHandler().start();
    }

    @After
    public void shutDownStack() {
        stack.getWebHandler().stop();
    }

    @Test
    public void listCakes() {
        List<String> cakes = Arrays.asList("Biscuit", "Pancake cake");
        cakes.stream().forEach(s -> stack.getDb().create(s));
        assertCakes(cakes);
    }

    @Test
    public void addCake() {
        throw new RuntimeException("implement");
    }

    private void assertCakes(List<String> cakes) {
        String html = HttpUtil.get("http://localhost:" + stack.getWebHandler().getPort());
        cakes.stream().forEach(s -> assertTrue(html.contains(s)));
    }


}

package cakeexample;

import org.junit.Assert;
import org.junit.Test;

public class CakeTest {

    @Test
    public void getData() {
        Assert.assertEquals("*Yuck* plastic fantastic", new MockCake().getDb().getNames()[0]);
    }

}

class MockCake implements MockDbModule {
}

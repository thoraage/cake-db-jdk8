package cakeexample;

import cakeexample.db.MockDbModule;
import org.junit.Assert;
import org.junit.Test;

public class CakeTest {

    @Test
    public void getData() {
        Assert.assertEquals("*Yuck* plastic fantastic", new MockCake().getDb().getCakes().iterator().next());
    }

}

class MockCake implements MockDbModule {
}

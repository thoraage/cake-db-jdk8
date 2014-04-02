package cakeexample.framework.domain;

import org.junit.Test;

import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FieldComparatorTest {

    private FieldComparator<Entity> fieldComparator = FieldComparator.create(list(Entity.NAME));

    @Test
    public void equals() {
        assertThat(fieldComparator.equals(new Entity("a"), new Entity("a")), equalTo(true));
    }

    @Test
    public void nullEqualsNull() {
        assertThat(fieldComparator.equals(null, null), equalTo(true));
    }

    @Test
    public void notEquals() {
        assertThat(fieldComparator.equals(new Entity("a"), new Entity("b")), equalTo(false));
    }

    @Test
    public void notEqualsNull() {
        assertThat(fieldComparator.equals(new Entity("a"), null), equalTo(false));
        assertThat(fieldComparator.equals(null, new Entity("a")), equalTo(false));
    }

}

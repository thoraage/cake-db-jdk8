package cakeexample.framework.domain;

import org.junit.Test;

import java.util.Optional;

import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FieldComparatorTest {

    private FieldComparator<Entity> fieldComparator = FieldComparator.create(list(Entity.NAME, Entity.DESCRIPTION));

    @Test
    public void equals() {
        assertThat(fieldComparator.equals(new Entity("a", Optional.empty(), 27), new Entity("a", Optional.empty(), 27)), equalTo(true));
    }

    @Test
    public void nullEqualsNull() {
        assertThat(fieldComparator.equals(null, null), equalTo(true));
    }

    @Test
    public void notEquals() {
        assertThat(fieldComparator.equals(new Entity("a", Optional.empty(), 27), new Entity("a", Optional.of("tull"), 27)), equalTo(false));
    }

    @Test
    public void notEqualsNull() {
        assertThat(fieldComparator.equals(new Entity("a", Optional.empty(), 27), null), equalTo(false));
        assertThat(fieldComparator.equals(null, new Entity("a", Optional.empty(), 27)), equalTo(false));
    }

}

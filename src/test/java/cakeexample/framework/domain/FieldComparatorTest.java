package cakeexample.framework.domain;

import org.junit.Test;

import java.util.Optional;

import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FieldComparatorTest {

    private FieldComparator<EntityNoPK> fieldComparator = FieldComparator.create(list(EntityNoPK.NAME, EntityNoPK.DESCRIPTION));

    @Test
    public void equals() {
        assertThat(fieldComparator.equals(new EntityNoPK("a", Optional.empty(), 27), new EntityNoPK("a", Optional.empty(), 27)), equalTo(true));
    }

    @Test
    public void nullEqualsNull() {
        assertThat(fieldComparator.equals(null, null), equalTo(true));
    }

    @Test
    public void notEquals() {
        assertThat(fieldComparator.equals(new EntityNoPK("a", Optional.empty(), 27), new EntityNoPK("a", Optional.of("tull"), 27)), equalTo(false));
    }

    @Test
    public void notEqualsNull() {
        assertThat(fieldComparator.equals(new EntityNoPK("a", Optional.empty(), 27), null), equalTo(false));
        assertThat(fieldComparator.equals(null, new EntityNoPK("a", Optional.empty(), 27)), equalTo(false));
    }

}

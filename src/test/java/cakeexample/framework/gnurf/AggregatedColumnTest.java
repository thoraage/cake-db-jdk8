package cakeexample.framework.gnurf;

import cakeexample.framework.domain.OneToOneEntity;
import cakeexample.framework.domain.Entity;
import org.h2.Driver;
import org.junit.Before;
import org.junit.Test;

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AggregatedColumnTest {

    private GnurfDbSession session;

    @Before
    public void create() {
        session = new GnurfDbSession(Driver.class.getName(), "jdbc:h2:mem:", () -> false);
        session.create(Entity.TABLE, OneToOneEntity.TABLE);
    }

    @Test
    public void insertAndSelect() {
        OneToOneEntity entity = new OneToOneEntity(empty(), new Entity(empty(), "tull"));
        session.into(OneToOneEntity.TABLE).insert(entity);
        assertThat(session.from(OneToOneEntity.TABLE).selectAll().length(), equalTo(1));
    }

}

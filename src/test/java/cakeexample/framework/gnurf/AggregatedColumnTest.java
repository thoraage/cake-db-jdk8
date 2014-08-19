package cakeexample.framework.gnurf;

import cakeexample.framework.domain.OneToOneEntity;
import cakeexample.framework.domain.Entity;
import fj.data.List;
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
    public void insertEntityWithInsertAggregate() {
        OneToOneEntity entity = new OneToOneEntity(empty(), new Entity(empty(), "tull"));
        session.into(OneToOneEntity.TABLE).insert(entity);
        assertThat(session.from(OneToOneEntity.TABLE).selectAll().length(), equalTo(1));
    }

    @Test
    public void insertEntityWithAggregateInserted() {
        Entity aggregate = new Entity(empty(), "tull");
        session.into(Entity.TABLE).insert(aggregate);
        List<Entity> aggregates = session.from(Entity.TABLE).selectAll();
        assertThat(aggregates.length(), equalTo(1));
        aggregate = aggregates.head();
        OneToOneEntity entity = new OneToOneEntity(empty(), aggregate);
        session.into(OneToOneEntity.TABLE).insert(entity);
        assertThat(session.from(OneToOneEntity.TABLE).selectAll().length(), equalTo(1));
    }

}

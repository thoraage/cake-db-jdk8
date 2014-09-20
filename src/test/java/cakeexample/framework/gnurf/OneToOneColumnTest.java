package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Entity;
import cakeexample.framework.domain.OneToOneEntity;
import fj.data.List;
import org.h2.Driver;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class OneToOneColumnTest {

    private DatabaseSession session;

    @Before
    public void create() {
        // TODO create a solution that rollbacks instead
        session = new NewConnectionPerRequestSession(Driver.class.getName(), "jdbc:h2:mem:" + UUID.randomUUID().toString() + ";DB_CLOSE_DELAY=-1", () -> false);
        Entity.TABLE.createTableIfNotExists(session);
        OneToOneEntity.TABLE.createTableIfNotExists(session);
    }

    @Test
    public void insertEntityWithInsertAggregate() {
        OneToOneEntity entity = new OneToOneEntity(empty(), new Entity(empty(), "tull"));
        OneToOneEntity.TABLE.insert(session, entity);
        assertThat(OneToOneEntity.TABLE.selectAll(session).length(), equalTo(1));
    }

    @Test
    public void insertEntityWithAggregateInserted() {
        Entity aggregate = new Entity(empty(), "tull");
        Entity.TABLE.insert(session, aggregate);
        List<Entity> aggregates = Entity.TABLE.selectAll(session);
        assertThat(aggregates.length(), equalTo(1));
        aggregate = aggregates.head();
        OneToOneEntity entity = new OneToOneEntity(empty(), aggregate);
        OneToOneEntity.TABLE.insert(session, entity);
        assertThat(OneToOneEntity.TABLE.selectAll(session).length(), equalTo(1));
    }

}

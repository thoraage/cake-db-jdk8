package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Entity;
import cakeexample.framework.domain.EntityNoPK;
import org.h2.Driver;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ColumnTest {

    private DatabaseSession session;

    @Before
    public void create() {
        session = new NewConnectionPerRequestSession(Driver.class.getName(), "jdbc:h2:mem:" + UUID.randomUUID().toString() + ";DB_CLOSE_DELAY=-1", () -> false);
        EntityNoPK.TABLE.createTableIfNotExists(session);
        Entity.TABLE.createTableIfNotExists(session);
    }

    @Test
    public void insertAndSelectNoPK() {
        EntityNoPK.TABLE.insert(session, new EntityNoPK("tull", Optional.empty(), 27));
        assertThat(EntityNoPK.TABLE.selectAll(session), equalTo(list(new EntityNoPK("tull", Optional.empty(), 27))));
    }

    @Test
    public void insert() {
        Entity.TABLE.insert(session, new Entity(Optional.empty(), "tull"));
        assertThat(Entity.TABLE.selectAll(session), equalTo(list(new Entity(Optional.of(1L), "tull"))));
    }

    @Test
    public void insertAndGetAutoIncrementedPK() {
        Entity entity = new Entity(Optional.empty(), "tull");
        assertThat(Entity.TABLE.insert(session, entity).id(), equalTo(Optional.of(1L)));
    }

    @Test
    public void insertAndGetNoPK() {
        EntityNoPK entity = new EntityNoPK("tull", Optional.of("tøys"), 27);
        assertThat(EntityNoPK.TABLE.insert(session, entity).id(), equalTo(Optional.empty()));
    }

}
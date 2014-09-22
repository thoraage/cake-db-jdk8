package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Entity;
import cakeexample.framework.domain.EntityNoPK;
import org.h2.Driver;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static fj.data.List.list;
import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ColumnTest {

    private DatabaseSession session;

    @Before
    public void create() throws Exception {
        session = new PerpetualConnectionSession(Driver.class.getName(), "jdbc:h2:mem:test", () -> false);
        EntityNoPK.TABLE.createTableIfNotExists(session);
        Entity.TABLE.createTableIfNotExists(session);
    }

    @After
    public void rollback() throws Exception {
        session.connection().rollback();
    }

    @Test
    public void insertAndSelectNoPK() {
        EntityNoPK.TABLE.insert(session, new EntityNoPK("tull", empty(), 27));
        assertThat(EntityNoPK.TABLE.selectAll(session), equalTo(list(new EntityNoPK("tull", empty(), 27))));
    }

    @Test
    public void insert() {
        Entity.TABLE.insert(session, new Entity(empty(), "tull"));
        assertThat(Entity.TABLE.selectAll(session), equalTo(list(new Entity(Optional.of(1L), "tull"))));
    }

    @Test
    public void insertAndGetAutoIncrementedPK() {
        Entity entity = new Entity(empty(), "tull");
        Optional<Long> id = Entity.TABLE.insert(session, entity).retrieve().id();
        assertThat(id, not(empty()));
    }

    @Test
    public void insertAndGetNoPK() {
        EntityNoPK entity = new EntityNoPK("tull", Optional.of("tøys"), 27);
        Optional<Long> id = EntityNoPK.TABLE.insert(session, entity).id();
        assertThat(id, equalTo(empty()));
    }

}
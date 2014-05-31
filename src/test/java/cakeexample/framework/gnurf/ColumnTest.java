package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Entity;
import cakeexample.framework.domain.EntityNoPK;
import org.h2.Driver;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ColumnTest {

    private GnurfDbSession session;

    @Before
    public void create() {
        session = new GnurfDbSession(Driver.class.getName(), "jdbc:h2:mem:", () -> false);
        session.create(EntityNoPK.TABLE, Entity.TABLE);
    }

    @Test
    public void insertAndSelectNoPK() {
        session.into(EntityNoPK.TABLE).insert(new EntityNoPK("tull", Optional.empty(), 27));
        assertThat(session.from(EntityNoPK.TABLE).selectAll(), equalTo(list(new EntityNoPK("tull", Optional.empty(), 27))));
    }

    @Test
    public void insert() {
        session.into(Entity.TABLE).insert(new Entity(Optional.empty(), "tull"));
        assertThat(session.from(Entity.TABLE).selectAll(), equalTo(list(new Entity(Optional.of(1L), "tull"))));
    }

    @Test
    public void insertAndGetAutoIncrementedPK() {
        Entity entity = new Entity(Optional.empty(), "tull");
        assertThat(session.into(Entity.TABLE).insert(entity).id(), equalTo(Optional.of(1L)));
    }

    @Test
    public void insertAndGetNoPK() {
        EntityNoPK entity = new EntityNoPK("tull", Optional.of("tøys"), 27);
        assertThat(session.into(EntityNoPK.TABLE).insert(entity).id(), equalTo(Optional.empty()));
    }

}
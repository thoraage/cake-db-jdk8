package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Entity;
import cakeexample.framework.domain.EntityNoPK;
import org.h2.Driver;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static cakeexample.framework.gnurf.Column.column;
import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ColumnTest {

    private GnurfDbSession session;
    private Table<EntityNoPK> entityNoPKTable;
    private Table<Entity> entityTable;

    @Before
    public void create() {
        entityNoPKTable = new Table<EntityNoPK>("tull", list(column("name", EntityNoPK.NAME), column("description", EntityNoPK.DESCRIPTION), column("age", EntityNoPK.AGE)), EntityNoPK::new);
        entityTable = new Table<Entity>("tullwpk", list(column("id", Entity.ID).primaryKey(true).autoIncrement(true), column("name", Entity.NAME)), Entity::new);
        session = new GnurfDbSession(Driver.class.getName(), "jdbc:h2:mem:", () -> false);
        session.create(entityNoPKTable, entityTable);
    }

    @Test
    public void insertAndSelectNoPK() {
        session.into(entityNoPKTable).insert(new EntityNoPK("tull", Optional.empty(), 27));
        assertThat(session.from(entityNoPKTable).selectAll(), equalTo(list(new EntityNoPK("tull", Optional.empty(), 27))));
    }

    @Test
    public void insert() {
        session.into(entityTable).insert(new Entity(Optional.empty(), "tull"));
        assertThat(session.from(entityTable).selectAll(), equalTo(list(new Entity(Optional.of(1L), "tull"))));
    }

    @Test
    public void insertAndGetAutoIncrementedPK() {
        Entity entity = new Entity(Optional.empty(), "tull");
        assertThat(session.into(entityTable).insert(entity).id(), equalTo(Optional.of(1L)));
    }

    @Test
    public void insertAndGetNoPK() {
        EntityNoPK entity = new EntityNoPK("tull", Optional.of("tøys"), 27);
        assertThat(session.into(entityNoPKTable).insert(entity).id(), equalTo(Optional.empty()));
    }

}
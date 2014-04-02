package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Entity;
import org.h2.Driver;
import org.junit.Before;
import org.junit.Test;

import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TableTest {

    private GnurfDbSession session;
    private Table<Entity> entityTable;

    @Before
    public void create() {
        entityTable = new Table<Entity>("tull", list(Column.column("name", Entity.NAME)), Entity::new);
        session = new GnurfDbSession(Driver.class.getName(), "jdbc:h2:mem:", () -> false);
        session.create(entityTable);
    }

    @Test
    public void insertAndSelect() {
        session.into(entityTable).insert(new Entity("tull"));
        assertThat(session.from(entityTable).selectAll(), equalTo(list(new Entity("tull"))));
    }

}

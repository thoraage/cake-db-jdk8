package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Entity;
import cakeexample.framework.domain.EntityWPK;
import org.h2.Driver;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static cakeexample.framework.gnurf.Column.column;
import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TableTest {

    private GnurfDbSession session;
    private Table<Entity> entityTable;
    private Table<EntityWPK> entityWPKTable;

    @Before
    public void create() {
        entityTable = new Table<Entity>("tull", list(column("name", Entity.NAME), column("description", Entity.DESCRIPTION), column("age", Entity.AGE)), Entity::new);
        entityWPKTable = new Table<EntityWPK>("tullwpk", list(column("id", EntityWPK.ID), column("name", EntityWPK.NAME)), EntityWPK::new);
        session = new GnurfDbSession(Driver.class.getName(), "jdbc:h2:mem:", () -> false);
        session.create(entityTable, entityWPKTable);
    }

    @Test
    public void insertAndSelect() {
        session.into(entityTable).insert(new Entity("tull", Optional.empty(), 27));
        assertThat(session.from(entityTable).selectAll(), equalTo(list(new Entity("tull", Optional.empty(), 27))));
    }

    @Test
    public void insertAndSelectWPK() {
        session.into(entityWPKTable).insert(new EntityWPK(Optional.empty(), "tull"));
        assertThat(session.from(entityWPKTable).selectAll(), equalTo(list(new EntityWPK(Optional.of(1L), "tull"))));
    }

}
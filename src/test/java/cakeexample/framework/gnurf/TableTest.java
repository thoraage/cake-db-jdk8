package cakeexample.framework.gnurf;

import cakeexample.framework.domain.Field;
import fj.data.List;
import org.h2.Driver;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import java.util.Optional;

import static fj.data.List.list;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class TableTest {

    private GnurfDbSession session;
    private Table<Entity> entityTable;

    @Before
    public void create() {
        entityTable = new Table<>("tull", list(Column.column("name", Entity.NAME)), Entity::new);
        session = new GnurfDbSession(Driver.class.getName(), "jdbc:h2:mem:", () -> false);
        session.create(entityTable);
    }

    @Test
    public void insertAndSelect() {
        session.into(entityTable).insert(new Entity("tull"));
        assertThat(session.from(entityTable).selectAll(), equalTo(list(new Entity("tull"))));
    }

    static class Entity {
        public static Field<Entity, String> NAME = new Field<>(e -> e.name);
        public final String name;

        public Entity(String name) {
            this.name = name;
        }

        public Entity(Iterable<Field<Entity, ?>> fields) {
            this(NAME.get(fields));
        }
    }

}

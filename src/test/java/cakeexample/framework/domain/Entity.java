package cakeexample.framework.domain;

import cakeexample.framework.gnurf.Table;
import fj.data.List;

import java.util.Optional;

import static cakeexample.framework.domain.Field.field;
import static cakeexample.framework.domain.OptionalField.optional;
import static cakeexample.framework.gnurf.Column.column;
import static fj.data.List.list;

public class Entity {
    public static OptionalField<Entity, Long> ID = optional(Field.<Entity, Long>field(Long.class), Entity::id);
    public static Field<Entity, String> NAME = field(String.class, Entity::name);
    public static final Table<Entity> TABLE = new Table<Entity>("tullwpk", list(column("id", ID).primaryKey(true).autoIncrement(true), column("name", NAME)), Entity::new);
    private final Optional<Long> id;
    private final String name;

    public Entity(Optional<Long> id, String name) {
        this.id = id;
        this.name = name;
    }

    public Entity(Iterable<AbstractField<Entity, ?>> fields) {
        this(ID.from(fields), NAME.from(fields));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity && FieldComparator.<Entity>create(List.list(ID, NAME)).equals(this, (Entity) obj);
    }

    public String name() {
        return name;
    }

    public Optional<Long> id() {
        return id;
    }

}


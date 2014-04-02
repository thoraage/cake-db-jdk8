package cakeexample.framework.domain;

import fj.data.List;

public class Entity {
    public static Field<Entity, String> NAME = new Field<>(e -> e.name);
    public final String name;

    public Entity(String name) {
        this.name = name;
    }

    public Entity(Iterable<Field<Entity, ?>> fields) {
        this(NAME.get(fields));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity && FieldComparator.<Entity>create(List.list(NAME)).equals(this, (Entity) obj);
    }
}


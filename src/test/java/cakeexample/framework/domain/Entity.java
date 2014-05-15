package cakeexample.framework.domain;

import fj.data.List;

import java.util.Optional;

public class Entity {
    public static AbstractField<Entity, String> NAME = Field.field(String.class, e -> e.name);
    public static AbstractField<Entity, Optional<String>> DESCRIPTION = OptionalField.optional(Field.<Entity, String>field(String.class), Entity::description);
    private final String name;
    private final Optional<String> description;

    public Entity(String name, Optional<String> description) {
        this.name = name;
        this.description = description;
    }

    public Entity(Iterable<AbstractField<Entity, ?>> fields) {
        this(NAME.from(fields), DESCRIPTION.from(fields));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity && FieldComparator.<Entity>create(List.list(NAME, DESCRIPTION)).equals(this, (Entity) obj);
    }

    public Optional<String> description() {
        return description;
    }
}


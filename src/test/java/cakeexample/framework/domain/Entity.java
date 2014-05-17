package cakeexample.framework.domain;

import fj.data.List;

import java.util.Optional;

import static cakeexample.framework.domain.Field.field;
import static cakeexample.framework.domain.OptionalField.optional;

public class Entity {
    public static Field<Entity, String> NAME = field(String.class, e -> e.name);
    public static OptionalField<Entity, String> DESCRIPTION = optional(Field.<Entity, String>field(String.class), Entity::description);
    public static Field<Entity, Integer> AGE = field(Integer.class, e -> e.age);
    private final String name;
    private final Optional<String> description;
    private final Integer age;

    public Entity(String name, Optional<String> description, Integer age) {
        this.name = name;
        this.description = description;
        this.age = age;
    }

    public Entity(Iterable<AbstractField<Entity, ?>> fields) {
        this(NAME.from(fields), DESCRIPTION.from(fields), AGE.from(fields));
    }

    private static <T> T print(T from) {
        System.out.println("from: " +from);
        return from;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entity && FieldComparator.<Entity>create(List.list(NAME, DESCRIPTION, AGE)).equals(this, (Entity) obj);
    }

    public Optional<String> description() {
        return description;
    }
}


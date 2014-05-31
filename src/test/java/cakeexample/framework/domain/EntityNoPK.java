package cakeexample.framework.domain;

import cakeexample.framework.gnurf.Table;
import fj.data.List;

import java.util.Optional;

import static cakeexample.framework.domain.Field.field;
import static cakeexample.framework.domain.OptionalField.optional;
import static cakeexample.framework.gnurf.Column.column;
import static fj.data.List.list;

public class EntityNoPK {
    public static Field<EntityNoPK, String> NAME = field(String.class, e -> e.name);
    public static OptionalField<EntityNoPK, String> DESCRIPTION = optional(Field.<EntityNoPK, String>field(String.class), EntityNoPK::description);
    public static Field<EntityNoPK, Integer> AGE = field(Integer.class, e -> e.age);
    public static final Table<EntityNoPK> TABLE = new Table<EntityNoPK>("tull", list(column("name", NAME), column("description", DESCRIPTION), column("age", AGE)), EntityNoPK::new);
    private final String name;
    private final Optional<String> description;
    private final Integer age;

    public EntityNoPK(String name, Optional<String> description, Integer age) {
        this.name = name;
        this.description = description;
        this.age = age;
    }

    public EntityNoPK(Iterable<AbstractField<EntityNoPK, ?>> fields) {
        this(NAME.from(fields), DESCRIPTION.from(fields), AGE.from(fields));
    }

    private static <T> T print(T from) {
        System.out.println("from: " +from);
        return from;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EntityNoPK && FieldComparator.<EntityNoPK>create(List.list(NAME, DESCRIPTION, AGE)).equals(this, (EntityNoPK) obj);
    }

    public Optional<String> description() {
        return description;
    }
}


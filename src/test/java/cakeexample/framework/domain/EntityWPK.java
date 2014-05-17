package cakeexample.framework.domain;

import fj.data.List;

import java.util.Optional;

import static cakeexample.framework.domain.Field.field;
import static cakeexample.framework.domain.OptionalField.optional;

public class EntityWPK {
    public static OptionalField<EntityWPK, Long> ID = optional(Field.<EntityWPK, Long>field(Long.class), EntityWPK::id);
    public static Field<EntityWPK, String> NAME = field(String.class, EntityWPK::name);
    private final Optional<Long> id;
    private final String name;

    public EntityWPK(Optional<Long> id, String name) {
        this.id = id;
        this.name = name;
    }

    public EntityWPK(Iterable<AbstractField<EntityWPK, ?>> fields) {
        this(ID.from(fields), NAME.from(fields));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EntityWPK && FieldComparator.<EntityWPK>create(List.list(ID, NAME)).equals(this, (EntityWPK) obj);
    }

    public String name() {
        return name;
    }

    public Optional<Long> id() {
        return id;
    }

}


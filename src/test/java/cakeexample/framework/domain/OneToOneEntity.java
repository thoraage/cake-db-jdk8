package cakeexample.framework.domain;

import cakeexample.framework.gnurf.Table;

import java.util.Optional;

import static cakeexample.framework.domain.Field.field;
import static cakeexample.framework.domain.OptionalField.optional;
import static cakeexample.framework.gnurf.Column.column;
import static cakeexample.framework.gnurf.OneToOneColumn.oneToOne;
import static fj.data.List.list;

public class OneToOneEntity {

    public static OptionalField<OneToOneEntity, Long> ID = optional(Field.<OneToOneEntity, Long>field(Long.class), OneToOneEntity::id);
    public static Field<OneToOneEntity, Entity> ENTITY = field(Entity.class, OneToOneEntity::entity);

    public static Table<OneToOneEntity> TABLE = new Table<OneToOneEntity>("aggregatingentity",
            list(column("id", ID).primaryKey(true).autoIncrement(true), oneToOne("entity_id", ENTITY, Entity.TABLE, Entity.PK)), OneToOneEntity::new);

    private final Optional<Long> id;
    private final Entity entity;

    public OneToOneEntity(Iterable<AbstractField<OneToOneEntity, ?>> fields) {
        this(ID.from(fields), ENTITY.from(fields));
    }

    public OneToOneEntity(Optional<Long> id, Entity entity) {
        this.id = id;
        this.entity = entity;
    }

    public Optional<Long> id() {
        return id;
    }

    public Entity entity() {
        return entity;
    }
}

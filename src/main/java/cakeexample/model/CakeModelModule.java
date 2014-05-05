package cakeexample.model;

import cakeexample.framework.domain.Field;
import cakeexample.framework.domain.ListField;
import cakeexample.framework.domain.OptionalField;
import fj.data.List;

import java.util.Optional;

import static cakeexample.framework.domain.ListField.list;
import static cakeexample.framework.domain.OptionalField.optional;

public interface CakeModelModule {


    public class Cake {
        public static OptionalField<Cake, Long> ID = optional(Field.<Cake, Long>field(Long.class), c -> c.id);
        public static Field<Cake, String> NAME = Field.field(String.class, c -> c.name);
        public static ListField<Cake, Ingredient> INGREDIENTS = list(Field.field(Ingredient.class), c -> c.ingredients);
        public final Optional<Long> id;
        public final String name;
        public final List<Ingredient> ingredients;
        //public final Person author;

        public Cake(Optional<Long> id, String name, List<Ingredient> ingredients) {//, Person author) {
            this.id = id;
            this.name = name;
            this.ingredients = ingredients;
            //this.author = author;
        }

        public Cake(Iterable<Field<Cake, ?>> fields) {
            this(ID.from(fields), NAME.from(fields), INGREDIENTS.from(fields));
        }
    }

    public class Ingredient {
        public final Optional<Long> id;
        public final String name;
        public final Measure measure;

        public Ingredient(Optional<Long> id, String name, Measure measure) {
            this.id = id;
            this.name = name;
            this.measure = measure;
        }
    }

    public class Measure {
        public final Optional<Long> id;
        public final float amount;
        public final MeasurementType measurementType;

        public Measure(Optional<Long> id, int amount, MeasurementType measurementType) {
            this.id = id;
            this.amount = amount;
            this.measurementType = measurementType;
        }
    }

    public class MeasurementType {
        public final Optional<Long> id;
        public final String name;
        public final String shorthand;
        public final Optional<MeasurementTypeRelation> measurementTypeRelationOptional;

        public MeasurementType(Optional<Long> id, String name, String shorthand, Optional<MeasurementTypeRelation> measurementTypeRelationOptional) {
            this.id = id;
            this.name = name;
            this.shorthand = shorthand;
            this.measurementTypeRelationOptional = measurementTypeRelationOptional;
        }
    }

    public class MeasurementTypeRelation {
        public final Optional<Long> id;
        public final Float ratio;
        public final MeasurementType measurementType;

        public MeasurementTypeRelation(Optional<Long> id, Float ratio, MeasurementType measurementType) {
            this.id = id;
            this.ratio = ratio;
            this.measurementType = measurementType;
        }
    }

    interface CakeDb {
        Cake save(Cake cake);
        List<Cake> getCakes();
    }

    CakeDb getCakeDb();

}

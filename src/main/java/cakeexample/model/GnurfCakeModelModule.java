package cakeexample.model;

import cakeexample.framework.gnurf.Column;
import cakeexample.framework.gnurf.GnurfDbSessionModule;
import cakeexample.framework.gnurf.Table;
import fj.data.List;

import static cakeexample.framework.gnurf.Column.column;
import static fj.data.List.list;

public interface GnurfCakeModelModule extends CakeModelModule, GnurfDbSessionModule {

    public class GnurfCakeDb implements CakeDb {
        private final GnurfCakeModelModule that;
        private static List<Column<Cake, ?>> cakeColumns = list(column("id", Cake.ID), column("name", Cake.NAME));
        public static Table<Cake> cakeTable = new Table<>("cakes", cakeColumns, Cake::new);

        public GnurfCakeDb(GnurfCakeModelModule that) {
            this.that = that;
            that.getGnurfDbSession().create(cakeTable);
        }

        @Override
        public Cake save(Cake cake) {
            if (cake.id.isPresent()) {
                return that.getGnurfDbSession().into(cakeTable).update(cake);
            } else {
                return that.getGnurfDbSession().into(cakeTable).insert(cake);
            }
        }

        @Override
        public List<Cake> getCakes() {
            return that.getGnurfDbSession().from(cakeTable).selectAll();
        }
    }

    @Override
    default CakeDb getCakeDb() {
        return new GnurfCakeDb(this);
    }
}

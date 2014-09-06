package cakeexample.model;

import cakeexample.framework.gnurf.AbstractColumn;
import cakeexample.framework.gnurf.GnurfDbSessionModule;
import cakeexample.framework.gnurf.Table;
import fj.data.List;

import static cakeexample.framework.gnurf.Column.column;
import static fj.data.List.list;

public interface GnurfCakeModelModule extends CakeModelModule, GnurfDbSessionModule {

    public class GnurfCakeDb implements CakeDb {
        private final GnurfCakeModelModule that;
        private static List<AbstractColumn<Cake, ?>> cakeColumns = list(column("id", Cake.ID), column("name", Cake.NAME));
        public static Table<Cake> cakeTable = new Table<>("cakes", cakeColumns, Cake::new);

        public GnurfCakeDb(GnurfCakeModelModule that) {
            this.that = that;
            cakeTable.createTableIfNotExists(that.getGnurfDbSession());
        }

        @Override
        public Cake save(Cake cake) {
            if (cake.id.isPresent()) {
                return cakeTable.update(that.getGnurfDbSession(), cake);
            } else {
                return cakeTable.insert(that.getGnurfDbSession(), cake).retrieve();
            }
        }

        @Override
        public List<Cake> getCakes() {
            return cakeTable.selectAll(that.getGnurfDbSession()).list();
        }
    }

    @Override
    default CakeDb getCakeDb() {
        return new GnurfCakeDb(this);
    }
}

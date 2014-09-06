package cakeexample.framework.gnurf;

import cakeexample.model.CakeModelModule;
import fj.data.List;

public interface TableOperations<C> extends TableCharacteristics<C> {

    default public List<C> selectAll(GnurfDbSession session) {
        return DbUtil.selectAll(session, this);
    }

    default public DbUtil.InsertContinuation<C, Long> insert(GnurfDbSession session, C entity) {
        return DbUtil.insert(session, this, entity);
    }

    default public void createTableIfNotExists(GnurfDbSession session) {
        DbUtil.createTableIfNotExists(session, this);
    }

    default C update(GnurfDbSession gnurfDbSession, C entity) {
        throw new NotImplementedException();
    }
}

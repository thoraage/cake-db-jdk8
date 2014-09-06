package cakeexample.framework.gnurf;

import fj.data.List;

public interface TableOperations<C> extends TableCharacteristics<C> {

    default public List<C> selectAll(DatabaseSession session) {
        return DbUtil.selectAll(session, this);
    }

    default public DbUtil.InsertContinuation<C, Long> insert(DatabaseSession session, C entity) {
        return DbUtil.insert(session, this, entity);
    }

    default public void createTableIfNotExists(DatabaseSession session) {
        DbUtil.createTableIfNotExists(session, this);
    }

    default C update(DatabaseSession databaseSession, C entity) {
        throw new NotImplementedException();
    }
}

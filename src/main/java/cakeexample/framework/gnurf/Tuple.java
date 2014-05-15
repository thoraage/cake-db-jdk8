package cakeexample.framework.gnurf;

public abstract class Tuple {
    public static <_1, _2> Tuple2 tuple(_1 _1, _2 _2) {
        return new Tuple2<>(_1, _2);
    }

    public static class Tuple2<_1, _2> {
        public final _1 _1;
        public final _2 _2;

        public Tuple2(_1 _1, _2 _2) {
            this._1 = _1;
            this._2 = _2;
        }
    }
}

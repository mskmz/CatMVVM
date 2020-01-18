package com.mskmz.catmvvm.core.Utils;

import java.util.Objects;

public class MapIndex<T1, T2> {
    T1 t1;
    T2 t2;

    MapIndex(T1 t1, T2 t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapIndex<?, ?> mapIndex = (MapIndex<?, ?>) o;
        return Objects.equals(t1, mapIndex.t1) &&
                Objects.equals(t2, mapIndex.t2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t1, t2);
    }
}

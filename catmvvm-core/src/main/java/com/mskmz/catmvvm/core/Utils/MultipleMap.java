package com.mskmz.catmvvm.core.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultipleMap<T1, T2, T3> {
    private HashMap<MapIndex<T1, T2>, List<T3>> map;

    public MultipleMap() {
        map = new HashMap<>();
    }

    public void put(T1 t1, T2 t2, T3 t3) {
        List<T3> list = get(t1, t2);
        if (list == null) {
            list = new ArrayList<>();
            map.put(new MapIndex<>(t1, t2), list);
        }
        list.add(t3);
    }

    public List<T3> get(T1 t1, T2 t2) {
        return map.get(new MapIndex<>(t1, t2));
    }
}


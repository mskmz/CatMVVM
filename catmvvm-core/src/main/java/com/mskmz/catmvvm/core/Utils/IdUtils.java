package com.mskmz.catmvvm.core.Utils;

public class IdUtils {
    private static int i = 1;

    public synchronized static int generateId() {
        return i++;
    }
}

package com.mskmz.catmvvm.core.constant;

import android.content.Context;

public class CatConfig {
    public static Context context;
    public static String PACKAGE_NAME;

    public static void init(Context context) {
        CatConfig.context = context;
        PACKAGE_NAME = context.getPackageName();
    }
}

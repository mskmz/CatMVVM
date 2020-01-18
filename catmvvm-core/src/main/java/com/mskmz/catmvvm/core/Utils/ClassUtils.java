package com.mskmz.catmvvm.core.Utils;

import java.lang.reflect.ParameterizedType;

public class ClassUtils {
    //反射获取第一个范型入参的Class
    public static Class getParaClass(Class c, int index) {
        ParameterizedType pt = (ParameterizedType) c.getGenericSuperclass();
        return (Class) pt.getActualTypeArguments()[index];
    }

}

package com.mskmz.catmvvm.core.annotaion;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

//注册通知事件
//现在仅支持变量
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CatAutoInjection {
    int id() default -1;

    int value() default -1;

    boolean forever() default false;

}

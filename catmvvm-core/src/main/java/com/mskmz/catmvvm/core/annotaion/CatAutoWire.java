package com.mskmz.catmvvm.core.annotaion;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//使用该注解表示该类启动反射器进行扫描
@Target(ElementType.TYPE) // 该注解作用在类之上
@Retention(RetentionPolicy.RUNTIME) // 要在编译时进行一些预处理操作，注解会在class文件中存在
public @interface CatAutoWire {
}

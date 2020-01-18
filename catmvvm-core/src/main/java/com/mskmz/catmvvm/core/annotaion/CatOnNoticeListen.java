package com.mskmz.catmvvm.core.annotaion;

import com.mskmz.catmvvm.core.notice.NoticeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
        ElementType.FIELD,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME) // 要在编译时进行一些预处理操作，注解会在class文件中存在
public @interface CatOnNoticeListen {

    int value() default -1;

    int tag() default -1;

    NoticeType[] allow() default {
            NoticeType.FragmentNotice,
            NoticeType.ActivityNotice,
            NoticeType.ViewNotice,
            NoticeType.ViewModelNotice,
            NoticeType.ModelNotice
    };
}


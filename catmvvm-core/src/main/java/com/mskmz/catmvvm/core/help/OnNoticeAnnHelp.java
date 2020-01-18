package com.mskmz.catmvvm.core.help;

import com.mskmz.catmvvm.core.Utils.MultipleMap;
import com.mskmz.catmvvm.core.annotaion.CatAutoWire;
import com.mskmz.catmvvm.core.annotaion.CatOnNoticeListen;
import com.mskmz.catmvvm.core.notice.NoticeType;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class OnNoticeAnnHelp {
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Static Final >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Static Final <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Field  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private WeakReference<Object> mO;
    private Class mClazz;

    private MultipleMap<NoticeType, Integer, Field> mFieldMap;
    private MultipleMap<NoticeType, Integer, Method> mMethodMap;

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Field  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Constructor  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public OnNoticeAnnHelp(Object o) {
        mO = new WeakReference<>(o);
        mClazz = o.getClass();
        scan();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Constructor  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method Override  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method Override  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method public  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void notice(NoticeType[] types, int msgId, Object obj) {
        if (types == null
                || msgId < 0
                || (mFieldMap == null && mMethodMap == null)
        ) {
            return;
        }
        for (NoticeType noticeType : types) {
            if (mFieldMap.get(noticeType, msgId) != null) {
                for (Field f : mFieldMap.get(noticeType, msgId)) {
                    noticeField(f, msgId, obj);
                }
            }
            if (mMethodMap.get(noticeType, msgId) != null) {
                for (Method m : mMethodMap.get(noticeType, msgId)) {
                    noticeMethod(m, msgId, obj);
                }
            }
        }
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method public  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method protect  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method protect  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method private  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private void scan() {
        //不需要扫描没有允许注解的类
        if (mClazz.getAnnotation(CatAutoWire.class) == null) {
            return;
        }
        mFieldMap = new MultipleMap<>();
        mMethodMap = new MultipleMap<>();
        //扫描参数
        scanPara();
        //扫描方法
        scanMethod();
    }

    private void scanPara() {
        Field[] fields = mClazz.getDeclaredFields();
        CatOnNoticeListen ann;
        for (Field field : fields) {
            ann = field.getAnnotation(CatOnNoticeListen.class);
            if (ann == null) {
                continue;
            }
            for (NoticeType type : ann.allow()) {
                mFieldMap.put(type, ann.tag() < 0 ? ann.value() : ann.tag(), field);
            }
        }
    }

    private void scanMethod() {
        Method[] methods = mClazz.getDeclaredMethods();
        CatOnNoticeListen ann;
        for (Method method : methods) {
            ann = method.getAnnotation(CatOnNoticeListen.class);
            if (ann == null) {
                continue;
            }
            for (NoticeType type : ann.allow()) {
                mMethodMap.put(type, ann.tag() < 0 ? ann.value() : ann.tag(), method);
            }
        }
    }

    //忽略msgId 使用msgObj 替换m的值
    private void noticeField(Field m, int msgId, Object msgObj) {
        setValue(m, msgObj);
    }

    private void noticeMethod(Method m, int msgId, Object msgObj) {
        try {
            invokeMethod(m, msgId, msgObj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setValue(Field f, Object inObj) {
        Object o = mO.get();
        f.setAccessible(true);
        Class objClass = inObj.getClass();
        //如果类型相同 直接赋值
        //如果obj是f的子类 也可以直接赋值
        if (f.getType() == objClass
                || f.getType().isAssignableFrom(objClass)) {
            try {
                f.set(o, inObj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return;
        }
        //这里开始冗余判断 列举Observable 相关类 调用其设置方法
        Object oF;
        Class cF = f.getType();
        try {
            oF = f.get(o);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        //ObservableField  -- 尝试获取set方法
        for (Method mcF : cF.getDeclaredMethods()) {
            if (!mcF.getName().equals("set")) {
                continue;
            }
            if (mcF.getParameterTypes().length != 1) {
                continue;
            }
            if (mcF.getParameterTypes()[0] != inObj.getClass()
                    && !mcF.getParameterTypes()[0].isAssignableFrom(inObj.getClass())
            ) {
                continue;
            }
            if (oF == null) {
                try {
                    oF = cF.newInstance();
                    f.set(o, oF);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                    return;
                }
            }
            try {
                mcF.invoke(oF, inObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

    private void invokeMethod(Method m, int msgId, Object msgObj)
            throws InvocationTargetException, IllegalAccessException {
        Class[] cpArray = m.getParameterTypes();
        Object oC = mO.get();

        if (oC == null) {
            return;
        }
        //尝试依次对几种情况判断
        switch (cpArray.length) {
            case 0:
                m.setAccessible(true);
                m.invoke(oC);
                break;
            case 1:
                invokeMethodFor1P(oC, cpArray, m, msgId, msgObj);
                break;
            case 2:
                invokeMethodFor2P(oC, cpArray, m, msgId, msgObj);
                break;
            default:
                break;
        }
    }

    private void invokeMethodFor1P(Object oC, Class[] cpArray, Method m, int tag, Object inObj)
            throws InvocationTargetException, IllegalAccessException {
        Class cInObj = null;
        if (inObj != null) {
            cInObj = inObj.getClass();
        }

        Class cp0 = cpArray[0];
        if (cp0 == int.class) {
            m.setAccessible(true);
            m.invoke(oC, tag);
        } else {
            if (inObj == null && Object.class.isAssignableFrom(cp0)) {
                m.setAccessible(true);
                m.invoke(oC, inObj);
            } else if (inObj != null && cp0.isAssignableFrom(cInObj)) {
                m.setAccessible(true);
                m.invoke(oC, inObj);
            }
        }
    }

    private void invokeMethodFor2P(Object oC, Class[] cpArray, Method m, int tag, Object inObj)
            throws InvocationTargetException, IllegalAccessException {
        Class cInObj = null;
        if (inObj != null) {
            cInObj = inObj.getClass();
        }
        Class cp0 = cpArray[0];
        Class cp1 = cpArray[1];
        if (cp0 != int.class && cp0 != Integer.class) {
            return;
        }
        if (!cp1.isAssignableFrom(cInObj)) {
            return;
        }
        m.setAccessible(true);
        m.invoke(oC, tag, inObj);
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method private  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Class inner  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Class inner  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

}


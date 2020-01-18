package com.mskmz.catmvvm.core.manager;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.mskmz.catmvvm.core.Utils.IdUtils;
import com.mskmz.catmvvm.core.annotaion.CatAutoInjection;
import com.mskmz.catmvvm.core.annotaion.CatAutoWire;
import com.mskmz.catmvvm.core.constant.CatConfig;
import com.mskmz.catmvvm.core.m.CatModel;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//使用该类暂存所有被写入的数据
public class ShardModelManager {

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>单例模式>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static ShardModelManager INSTANCE() {
        return Singleton.singleton;
    }

    private static class Singleton {
        private static ShardModelManager singleton = new ShardModelManager();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Static Final >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private static final int MESSAGE_CLEAR = 100011;
    private static final int DELAY_SEND = 1000;
    private static final int DELAY_CLEAR = 5000;


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Static Final <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Field  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //一级缓存 - 所有加入的都会在这边
    private SparseArray<Object> mCacheMap;

    //id记录 - 根据class名生成对应的id字段
    private ArrayMap<Class, Integer> mCacheIdMap;

    //永久标记 - 被加入该标记的不会从一级缓存中移出
    private Set<Integer> mIgnoreSet;

    //删除标记
    private SparseArray<Long> mDelayMap;

    //引用计数器 - 引用计数器
    private ArrayMap<Integer, List<WeakReference<Object>>> mCountMap;


    private Handler mHandler;

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Field  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Constructor  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private ShardModelManager() {
        mCacheMap = new SparseArray<>();
        mCacheIdMap = new ArrayMap<>();
        mIgnoreSet = new HashSet<>();
        mCountMap = new ArrayMap<>();
        mDelayMap = new SparseArray<>();
        mHandler = new Handler(CatConfig.context.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MESSAGE_CLEAR:
                        clear();
                        break;
                }
                return false;
            }
        });
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_CLEAR), DELAY_SEND);
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Constructor  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method Override  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method Override  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method public  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void registerData(Object clientSource, Object obj) {
        registerData(clientSource, generateId(obj.getClass()), obj);
    }

    public void registerData(Object clientSource, int tag, Object obj) {
        if (obj != null) {
            updateData(tag, obj);
        }
        recordCount(clientSource, tag);

    }

    public void registerData(Object clientSource, Class clazz) {
        registerData(clientSource, generateId(clazz), clazz);
    }

    public void registerData(Object clientSource, int tag, Class clazz) {
        if (clazz == null) {
            return;
        }
        if (checkoutData(tag) != null) {
            registerData(clientSource, tag, null);
        } else {
            registerData(clientSource, tag, newData(clazz));
        }

    }


    public Object checkoutData(int tag) {
        return mCacheMap.get(tag);
    }

    //直接解析一个类
    public void checkClass(Object obj) {
        Class clazz = obj.getClass();
        if (clazz.getAnnotation(CatAutoWire.class) == null) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        CatAutoInjection ann = null;
        for (Field f : fields) {
            ann = f.getAnnotation(CatAutoInjection.class);
            if (ann == null) {
                continue;
            }
            int tag = ann.id();
            if (tag < 0) {
                tag = ann.value();
            }
            if (tag < 0) {
                tag = generateId(clazz);
            }
            if (ann.forever()) {
                mIgnoreSet.add(tag);
            }
            registerData(obj, tag, f.getType());
            try {
                f.set(obj, checkoutData(tag));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method public  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method protect  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method protect  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method private  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    private int generateId(Class clazz) {
        if (!mCacheIdMap.containsKey(clazz)) {
            mCacheIdMap.put(clazz, IdUtils.generateId());
        }
        return mCacheIdMap.get(clazz);
    }

    private void updateData(int tag, Object obj) {
        if (obj == null) {
            return;
        }
        mCacheMap.put(tag, obj);
    }

    private Object newData(Class clazz) {
        Object obj = null;
        try {
            obj = clazz.newInstance();
            if (CatModel.class.isAssignableFrom(clazz)) {
                Method mPCreate = CatModel.class.getDeclaredMethod("singleCreate");
                mPCreate.invoke(obj);
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }

    // 将对象计入引用计数器中
    private void recordCount(Object clientSource, int tag) {
        if (clientSource == null) {
            return;
        }
        if (!mCountMap.containsKey(tag) || mCountMap.get(tag) == null) {
            mCountMap.put(tag, new ArrayList<WeakReference<Object>>());
        }
        updateWeakList(mCountMap.get(tag));
        mCountMap.get(tag).add(new WeakReference<>(clientSource));
    }

    private void updateWeakList(List<WeakReference<Object>> list) {
        for (WeakReference w : list) {
            if (w == null || w.get() == null) {
                list.remove(w);
            }
        }
    }

    private boolean checkAlive(int tag) {
        if (mCountMap.get(tag) == null) {
            return false;
        }
        if (mIgnoreSet.contains(tag)) {
            return true;
        }
        updateWeakList(mCountMap.get(tag));
        return mCountMap.get(tag).size() > 0;
    }

    //筛选过时的数据
    //筛选无效的数据
    //进入循环
    private void clear() {
        long curr = System.currentTimeMillis();
        for (int i = mDelayMap.size() - 1; i >= 0; i--) {
            if (checkAlive(mDelayMap.keyAt(i))) {
                mDelayMap.removeAt(i);
            } else {
                if (curr - mDelayMap.valueAt(i) > DELAY_CLEAR) {
                    mCacheMap.removeAt(i);
                    mDelayMap.removeAt(i);
                }
            }
        }
        for (int i = mCacheMap.size() - 1; i >= 0; i--) {
            if (mDelayMap.get(mCacheMap.keyAt(i)) != null) {
                continue;
            }
            if (!checkAlive(mCacheMap.keyAt(i))) {
                mDelayMap.put(mCacheMap.keyAt(i), curr);
            }
        }
        mHandler.removeMessages(MESSAGE_CLEAR);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_CLEAR), DELAY_SEND);
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method private  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Class inner  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Class inner  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


}

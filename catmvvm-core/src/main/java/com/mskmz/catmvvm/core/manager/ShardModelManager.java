package com.mskmz.catmvvm.core.manager;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import com.mskmz.catmvvm.core.Utils.IdUtils;
import com.mskmz.catmvvm.core.annotaion.CatAutoInjection;
import com.mskmz.catmvvm.core.annotaion.CatAutoWire;
import com.mskmz.catmvvm.core.constant.CatConfig;
import com.mskmz.catmvvm.core.inter.CatBaseModel;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
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


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  TAG  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private static final String TAG = "ShardModelManager>>>";
    private static final boolean isDebug = true;
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TAG  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>单例模式>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public static ShardModelManager INSTANCE() {
        return Singleton.singleton;
    }

    private static class Singleton {
        private static ShardModelManager singleton = new ShardModelManager();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Static Final >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private static final int MESSAGE_DELETE = 100011;
    private static final int MESSAGE_REMOVE = 100012;
    private static final int DELAY_SEND = 1000;

    private ReferenceQueue<Object> mRefQueue = new ReferenceQueue<>();


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Static Final <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Field  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //一级缓存 - 所有加入的都会在这边
    private SparseArray<Object> mCacheMap;

    //id记录 - 根据class名生成对应的id字段
    private ArrayMap<Class, Integer> mCacheIdMap;

    //永久标记 - 被加入该标记的不会从一级缓存中移出
    private Set<Integer> mIgnoreSet;


    //引用计数器 - 引用计数器
    //TODO 仿照weakListen 整改
    private SparseArray<List<WeakListen>> mCountMap;

    private Handler mHandler;

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Field  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Constructor  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    @SuppressLint("UseSparseArrays")
    private ShardModelManager() {
        mCacheMap = new SparseArray<>();
        mCacheIdMap = new ArrayMap<>();
        mIgnoreSet = new HashSet<>();
        mCountMap = new SparseArray<>();
        mHandler = new Handler(CatConfig.context.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int i;
                switch (msg.what) {
                    case MESSAGE_DELETE:
                        i = -1;
                        if (msg.obj instanceof Integer) {
                            i = (int) msg.obj;
                        }
                        if (i < 0) {
                            break;
                        }
                        mCacheMap.remove(i);
                        break;
                    case MESSAGE_REMOVE:
                        i = -1;
                        if (msg.obj instanceof Integer) {
                            i = (int) msg.obj;
                        }
                        if (i < 0) {
                            break;
                        }
                        clear(i);
                        if (!mIgnoreSet.contains(i)
                                && (
                                mCountMap.get(i) == null
                                        || mCountMap.get(i).size() <= 0)
                        ) {
                            mHandler.removeMessages(MESSAGE_DELETE, i);
                            mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_DELETE, i), DELAY_SEND);
                        }
                        break;
                }
                return false;
            }
        });
        processReferenceQueue();
    }

    private void processReferenceQueue() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Reference<?> reference;
                    for (; ; ) {
                        reference = mRefQueue.remove();
                        if (reference instanceof WeakListen) {
                            int i = ((WeakListen) reference).tag;
                            mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_REMOVE, i));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
        if (isDebug) Log.d("wzk>>>", TAG + ": checkClass: " + clazz.getSimpleName());
        if (clazz.getAnnotation(CatAutoWire.class) == null) {
            return;
        }
        Log.d("wzk>>>", TAG + ": checkClass: ");
        if (isDebug)
            Log.d("wzk>>>", TAG + ": checkClass: check class" + obj.getClass().getSimpleName());
        Field[] fields = clazz.getDeclaredFields();
        CatAutoInjection ann;
        for (Field f : fields) {
            ann = f.getAnnotation(CatAutoInjection.class);
            if (ann == null) {
                continue;
            }
            if (isDebug)
                Log.d("wzk>>>", TAG + ": checkClass: 尝试解析- class - " + clazz.getSimpleName() + " - field - " + f.getName());
            int tag = ann.id();
            if (tag < 0) {
                tag = ann.value();
            }
            if (tag < 0) {
                tag = generateId(f.getType());
            }
            if (isDebug) Log.d("wzk>>>", TAG + ":  tag - " + tag);
            if (isDebug) Log.d("wzk>>>", TAG + ": checkClass: tag -" + f.getType());
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
            if (CatBaseModel.class.isAssignableFrom(clazz)) {
                Method mPCreate = CatBaseModel.class.getDeclaredMethod("shardCreate");
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
        if (mCountMap.get(tag) == null) {
            mCountMap.put(tag, new ArrayList<WeakListen>());
        }
        mCountMap.get(tag).add(new WeakListen(clientSource, tag));
        mHandler.removeMessages(MESSAGE_DELETE, tag);
    }

    //筛选过时的数据
    private void clear(int tag) {
        for (WeakListen w : mCountMap.get(tag)) {
            if (w.get() == null) {
                mCountMap.get(tag).remove(w);
            }
        }
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method private  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Class inner  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Class inner  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    class WeakListen extends WeakReference<Object> {
        int tag;

        WeakListen(Object referent, int tag) {
            super(referent, mRefQueue);
            this.tag = tag;
        }
    }
}



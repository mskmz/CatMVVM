package com.mskmz.catmvvm.core.m;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;

import com.mskmz.catmvvm.core.inter.CatBaseModel;
import com.mskmz.catmvvm.core.manager.ShardModelManager;
import com.mskmz.catmvvm.core.manager.SupportShard;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CatModel
        extends BaseObservable
        implements CatBaseModel,
        SupportShard {

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  TAG  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private static final String TAG = "CatModel>>>";
    private static final boolean isDebug = true;
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TAG  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  解决内存泄露  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private List<OnPropertyChangedCallback> mDependCallBack;
    private List<WeakReference<OnPropertyChangedCallback>> mCallBacks;

    private OnPropertyChangedCallback mWeakCallBack = new OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (mCallBacks != null) {
                for (WeakReference<OnPropertyChangedCallback> callback : mCallBacks) {
                    OnPropertyChangedCallback onCall = callback.get();
                    if (onCall == null) {
                        mCallBacks.remove(callback);
                        continue;
                    }
                    onCall.onPropertyChanged(sender, propertyId);
                }
            }
        }
    };
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  解决内存泄露  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    public CatModel() {
        ShardModelManager.INSTANCE().checkClass(this);
        super.addOnPropertyChangedCallback(mWeakCallBack);
    }

    @Override
    public CatBaseModel singleCreate() {
        return this;
    }

    public void Log() {
        if (!isDebug) return;
        try {
            Class observable = Class.forName("androidx.databinding.BaseObservable");
            Field f = observable.getDeclaredField("mCallbacks");
            f.setAccessible(true);
            PropertyChangeRegistry p = (PropertyChangeRegistry) f.get(this);

            Class c = Class.forName("androidx.databinding.CallbackRegistry");
            Field f2 = c.getDeclaredField("mCallbacks");
            f2.setAccessible(true);
            ArrayList list = (ArrayList) f2.get(p);
            Log.d(TAG, "Log: " + list.size());

            for (WeakReference<OnPropertyChangedCallback> callback : mCallBacks) {
                OnPropertyChangedCallback onCall = callback.get();
                if (onCall == null) {
                    mCallBacks.remove(callback);
                }
            }
            Log.d(TAG, "Log: " + mCallBacks.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOnPropertyChangedCallback(SupportShard sso, @NonNull OnPropertyChangedCallback callback) {
        sso.addCallbackLink(callback);
        if (mCallBacks == null) {
            mCallBacks = new ArrayList<>();
        }
        addOnPropertyChangedCallback(callback);
    }

    @Override
    public void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {
        if (mCallBacks == null) {
            mCallBacks = new ArrayList<>();
        }
        mCallBacks.add(new WeakReference<>(callback));
    }

    @Override
    public void addCallbackLink(OnPropertyChangedCallback callback) {
        if (mDependCallBack == null) {
            mDependCallBack = new ArrayList<>();
        }
        mDependCallBack.add(callback);
    }
}

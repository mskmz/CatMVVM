package com.mskmz.catmvvm.core.vm;

import android.util.Log;

import androidx.databinding.Observable;
import androidx.lifecycle.LifecycleObserver;

import com.mskmz.catmvvm.core.help.OnNoticeAnnHelp;
import com.mskmz.catmvvm.core.inter.CatBaseView;
import com.mskmz.catmvvm.core.inter.CatBaseViewModel;
import com.mskmz.catmvvm.core.manager.ShardModelManager;
import com.mskmz.catmvvm.core.manager.SupportShard;
import com.mskmz.catmvvm.core.notice.NoticeType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CatViewModel<T extends CatBaseView>
        implements CatBaseViewModel<T>,
        LifecycleObserver,
        SupportShard {

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Static Final >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  TAG  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private static final String TAG = "CatViewModel>>>";
    private static final boolean isDebug = true;
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TAG  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Static Final <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Field  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private List<Observable.OnPropertyChangedCallback> mDependCallBack;
    private WeakReference<T> view;
    private OnNoticeAnnHelp mAnnNoticeHelp;
    private boolean isShard = false;

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Field  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Constructor  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public CatViewModel() {
        mAnnNoticeHelp = new OnNoticeAnnHelp(this);
        ShardModelManager.INSTANCE().checkClass(this);
        init();
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Constructor  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method Override  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public void onViewListen(int msgTag, Object msgObj) {
        mAnnNoticeHelp.notice(new NoticeType[]{NoticeType.ViewNotice}, msgTag, msgObj);
    }

    @Override
    public void noticeView(int msgTag, Object msgObj) {
        if (getView() == null) {
            return;
        }
        Log.d(TAG, "noticeView: " + getView().getClass());
        getView().onViewModelListen(msgTag, msgObj);
    }

    @Override
    public void bindView(T v) {
        view = new WeakReference<>(v);

    }

    @Override
    public T getView() {
        return view.get();
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method Override  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method public  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method public  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method protect  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    protected void init() {
        if (isDebug) Log.d(TAG, "init: start");
    }

    @Override
    public void addCallbackLink(Observable.OnPropertyChangedCallback callback) {
        if (mDependCallBack == null) {
            mDependCallBack = new ArrayList<>();
        }
        mDependCallBack.add(callback);
    }



    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method protect  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method private  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method private  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Class inner  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Class inner  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
}

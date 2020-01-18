package com.mskmz.catmvvm.core.vm;

import androidx.lifecycle.LifecycleObserver;

import com.mskmz.catmvvm.core.help.OnNoticeAnnHelp;
import com.mskmz.catmvvm.core.inter.CatBaseView;
import com.mskmz.catmvvm.core.inter.CatBaseViewModel;
import com.mskmz.catmvvm.core.manager.ShardModelManager;
import com.mskmz.catmvvm.core.notice.NoticeType;

import java.lang.ref.WeakReference;

public class CatViewModel<T extends CatBaseView> implements CatBaseViewModel<T>, LifecycleObserver {

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Static Final >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private WeakReference<T> view;
    private OnNoticeAnnHelp mAnnNoticeHelp;

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Static Final <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Field  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Field  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Constructor  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    CatViewModel() {
        mAnnNoticeHelp = new OnNoticeAnnHelp(this);
        ShardModelManager.INSTANCE().checkClass(this);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Constructor  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method Override  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public void onViewListen(int msgTag, Object msgObj) {
        mAnnNoticeHelp.notice(new NoticeType[]{NoticeType.ViewNotice}, msgTag, msgObj);
    }

    @Override
    public void noticeView(int msgTag, Object msgObj) {

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
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method protect  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method private  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method private  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Class inner  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Class inner  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
}
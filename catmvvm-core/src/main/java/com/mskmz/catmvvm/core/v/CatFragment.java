package com.mskmz.catmvvm.core.v;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.mskmz.catmvvm.core.Utils.ClassUtils;
import com.mskmz.catmvvm.core.Utils.ViewUtils;
import com.mskmz.catmvvm.core.constant.CatConfig;
import com.mskmz.catmvvm.core.help.OnNoticeAnnHelp;
import com.mskmz.catmvvm.core.inter.CatBaseView;
import com.mskmz.catmvvm.core.inter.CatBaseViewModel;
import com.mskmz.catmvvm.core.help.BaseViewHelp;
import com.mskmz.catmvvm.core.notice.NoticeType;

public class CatFragment
        <DB extends ViewDataBinding>
        extends Fragment
        implements CatBaseView<DB> {
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Static Final >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Static Final <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Field  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private DB mDb;
    private BaseViewHelp mViewHelp;
    private OnNoticeAnnHelp mAnnNoticeHelp;

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Field  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Constructor  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Constructor  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method Override  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewHelp = new BaseViewHelp(this);
        mAnnNoticeHelp = new OnNoticeAnnHelp(this);
        mDb = DataBindingUtil
                .inflate(inflater,
                        ViewUtils.getRes(
                                getContext() == null ? CatConfig.context : getContext(),
                                ClassUtils.getParaClass(this.getClass(), 0).getSimpleName()
                        ), container, false);
        bindVm();
        init();
        return mDb.getRoot();
    }


    @Override
    public DB getDb() {
        return mDb;
    }

    @Override
    public void onViewModelListen(int msgTag, Object msgObj) {
        mAnnNoticeHelp.notice(new NoticeType[]{NoticeType.ViewModelNotice}, msgTag, msgObj);
    }

    @Override
    public void addVM(String vmName, CatBaseViewModel vm) {
        mViewHelp.addVM(vmName, vm);
    }

    @Override
    public void removeVM(String vmName) {
        mViewHelp.removeVM(vmName);
    }

    @Override
    public void removeVM(CatBaseViewModel vm) {
        mViewHelp.removeVM(vm);
    }


    @Override
    public CatBaseViewModel getVm() {
        return mViewHelp.getVm();
    }

    @Override
    public CatBaseViewModel getVm(String vmName) {
        return mViewHelp.getVm(vmName);
    }

    @Override
    public void noticeVm(int msgTag, Object msgObj) {
        mViewHelp.noticeVm(msgTag, msgObj);
    }

    @Override
    public void noticeVm(String vmName, int msgTag, Object msgObj) {
        mViewHelp.noticeVm(msgTag, msgObj);
    }

    @Override
    public void noticeVm(CatBaseViewModel vm, int msgTag, Object msgObj) {
        mViewHelp.noticeVm(vm, msgTag, msgObj);
    }

    @Override
    public void noticeAllVm(int msgTag, Object msgObj) {
        mViewHelp.noticeAllVm(msgTag, msgObj);
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method Override  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method public  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    protected void init() {

    }

    public void noticeActivity(int tag, Object obj) {
        Activity activity = getActivity();
        if (activity instanceof CatActivity) {
            ((CatActivity) activity).onFragmentListen(tag, obj);
        }
    }

    public void onActivityListen(int msgTag, Object msgObj) {
        mAnnNoticeHelp.notice(new NoticeType[]{NoticeType.ActivityNotice}, msgTag, msgObj);
    }


    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method public  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method protect  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method protect  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method private  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private void bindVm() {
        mViewHelp.bindVm(mDb);
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method private  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Class inner  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Class inner  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


}

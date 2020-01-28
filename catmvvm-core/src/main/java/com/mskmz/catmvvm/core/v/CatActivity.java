package com.mskmz.catmvvm.core.v;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.mskmz.catmvvm.core.Utils.ClassUtils;
import com.mskmz.catmvvm.core.Utils.ViewUtils;
import com.mskmz.catmvvm.core.help.OnNoticeAnnHelp;
import com.mskmz.catmvvm.core.inter.CatBaseView;
import com.mskmz.catmvvm.core.inter.CatBaseViewModel;
import com.mskmz.catmvvm.core.help.BaseViewHelp;
import com.mskmz.catmvvm.core.notice.NoticeType;

//实现基础的view功能 如dataBinding
public class CatActivity
        <DB extends ViewDataBinding>
        extends AppCompatActivity
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewHelp = new BaseViewHelp(this);
        mAnnNoticeHelp = new OnNoticeAnnHelp(this);
        mDb = DataBindingUtil.setContentView(
                this,
                ViewUtils.getRes(
                        this,
                        ClassUtils.getParaClass(this.getClass(), 0).getSimpleName()
                ));
        bindVm();

        init();
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
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method public  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method protect  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    protected void init() {

    }

    protected void onFragmentListen(int msgTag, Object msgObj) {
        mAnnNoticeHelp.notice(new NoticeType[]{NoticeType.FragmentNotice}, msgTag, msgObj);
    }

    protected void noticeFragment(Fragment fragment, int tag, Object obj) {
        if (fragment instanceof CatFragment) {
            ((CatFragment) fragment).onActivityListen(tag, obj);
        }
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method protect  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Method private  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private void bindVm() {
        mViewHelp.bindVm(mDb);
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Method private  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  Class inner  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Class inner  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<


}

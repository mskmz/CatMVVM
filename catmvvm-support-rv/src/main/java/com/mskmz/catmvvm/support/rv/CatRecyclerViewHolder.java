package com.mskmz.catmvvm.support.rv;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.mskmz.catmvvm.core.help.BaseViewHelp;
import com.mskmz.catmvvm.core.inter.CatBaseView;
import com.mskmz.catmvvm.core.inter.CatBaseViewModel;
import com.mskmz.catmvvm.core.notice.CatNotice;

import java.lang.ref.WeakReference;

public class CatRecyclerViewHolder
        extends RecyclerView.ViewHolder
        implements CatBaseView {

    private ViewDataBinding mDb;
    private CatRecyclerAdapter mAdapter;
    private BaseViewHelp mViewHelp;
    private WeakReference<Object> mData;


    public CatRecyclerViewHolder(CatRecyclerAdapter adapter, ViewDataBinding db) {
        super(db.getRoot());
        mAdapter = adapter;
        mDb = db;
        mViewHelp = new BaseViewHelp(this);
        mViewHelp.bindVm(mDb);

    }

    public void bindAdapter(Object obj, int position) {
        mData = new WeakReference<>(obj);
        noticeAllVm(CatRecyclerConstant.NOTICE_BIND_ADAPTER,
                CatNotice.builder()
                        .put(CatRecyclerConstant.PAMA_INDEX, position)
                        .put(CatRecyclerConstant.PAMA_DATA, obj)
                        .build()
        );
    }

    @Override
    public ViewDataBinding getDb() {
        return mDb;
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
    public void onViewModelListen(int msgTag, Object msgObj) {
        mAdapter.onViewModelListen(getAdapterPosition(), msgTag, msgObj);
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
        mViewHelp.noticeVm(vmName, msgTag, msgObj);
    }

    @Override
    public void noticeVm(CatBaseViewModel vm, int msgTag, Object msgObj) {
        mViewHelp.noticeVm(vm, msgTag, msgObj);
    }

    @Override
    public void noticeAllVm(int msgTag, Object msgObj) {
        mViewHelp.noticeAllVm(msgTag, msgObj);
    }
}

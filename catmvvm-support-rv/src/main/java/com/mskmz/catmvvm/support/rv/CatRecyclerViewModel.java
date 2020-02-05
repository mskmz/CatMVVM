package com.mskmz.catmvvm.support.rv;

import androidx.databinding.ObservableField;

import com.mskmz.catmvvm.core.notice.CatNotice;
import com.mskmz.catmvvm.core.vm.CatViewModel;

public class CatRecyclerViewModel<T>
        extends CatViewModel {
    ObservableField<T> mData = new ObservableField<>();
    int mIndex = -1;

    @Override
    public void onViewListen(int msgTag, Object msgObj) {
        super.onViewListen(msgTag, msgObj);
        switch (msgTag) {
            case CatRecyclerConstant.NOTICE_BIND_ADAPTER:
                if (msgObj instanceof CatNotice) {
                    T t = null;
                    try {
                        t = (T) ((CatNotice) msgObj).get(CatRecyclerConstant.PAMA_DATA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    bindVm(t);
                    try {
                        bindIndex((Integer) ((CatNotice) msgObj).get(CatRecyclerConstant.PAMA_INDEX));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public ObservableField<T> getData() {
        return mData;
    }

    public int getIndex() {
        if (getView() instanceof CatRecyclerViewHolder) {
            return ((CatRecyclerViewHolder) getView()).getAdapterPosition();
        }
        return mIndex;
    }

    protected void bindIndex(int index) {
        mIndex = index;
    }

    protected void bindVm(T obj) {
        mData.set(obj);
    }
}

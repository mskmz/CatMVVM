package com.mskmz.catmvvm.support.rv;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public abstract class CatRecyclerAdapter<T> extends RecyclerView.Adapter {

    ObservableList<T> mDataList;
    private SparseArray<List<OnItemNoticeListen>> mItemNoticeMap;

    public CatRecyclerAdapter(@NonNull ObservableList<T> data) {
        mDataList = data;
        initDataList();
    }

    private void initDataList() {
        mDataList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<T>>() {
            @Override
            public void onChanged(ObservableList<T> sender) {
                CatRecyclerAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<T> sender, int positionStart, int itemCount) {
                CatRecyclerAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(ObservableList<T> sender, int positionStart, int itemCount) {
                CatRecyclerAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(ObservableList<T> sender, int fromPosition, int toPosition, int itemCount) {
                for (int i = 0; i < itemCount; i++) {
                    CatRecyclerAdapter.this.notifyItemMoved(fromPosition + i, toPosition + i);
                }
            }

            @Override
            public void onItemRangeRemoved(ObservableList<T> sender, int positionStart, int itemCount) {
                CatRecyclerAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int res = onCreateViewHolderForRes(parent, viewType);
        if (res > 0) {
            return buildHolderForRes(res, parent);
        }
        return null;
    }

    protected abstract int onCreateViewHolderForRes(@NonNull ViewGroup parent, int viewType);

    private RecyclerView.ViewHolder buildHolderForRes(int res, ViewGroup parent) {
        ViewDataBinding db =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        res,
                        parent,
                        false);
        return new CatRecyclerViewHolder(this, db);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CatRecyclerViewHolder) {
            ((CatRecyclerViewHolder) holder).bindAdapter(mDataList.get(position), position);
        }
    }

    public void registerItemNoticeListen(int noticeKey, OnItemNoticeListen onItemNoticeListen) {
        if (mItemNoticeMap == null) {
            mItemNoticeMap = new SparseArray<>();
        }
        if (mItemNoticeMap.get(noticeKey) == null) {
            mItemNoticeMap.put(noticeKey, new ArrayList<OnItemNoticeListen>());
        }
        mItemNoticeMap.get(noticeKey).add(onItemNoticeListen);
    }

    public void removeItemNoticeListen(int noticeKey) {
        if (mItemNoticeMap == null) {
            return;
        }
        mItemNoticeMap.remove(noticeKey);
    }

    public void removeItemNoticeListen(int noticeKey, OnItemNoticeListen onItemNoticeListen) {
        if (mItemNoticeMap == null
                || mItemNoticeMap.get(noticeKey) == null) {
            return;
        }
        for (OnItemNoticeListen listen : mItemNoticeMap.get(noticeKey)) {
            if (listen == onItemNoticeListen) {
                mItemNoticeMap.get(noticeKey).remove(listen);
            }
        }
    }

    public void onViewModelListen(int postion, int noticeKey, Object obj) {
        if (mItemNoticeMap != null
                && mItemNoticeMap.get(noticeKey) != null) {
            for (OnItemNoticeListen listen : mItemNoticeMap.get(noticeKey)) {
                listen.onItemNotice(postion, noticeKey, obj);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface OnItemNoticeListen {
        void onItemNotice(int postion, int noticeKey, Object obj);
    }

}


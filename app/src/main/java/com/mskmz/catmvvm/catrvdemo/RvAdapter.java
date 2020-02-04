package com.mskmz.catmvvm.catrvdemo;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import com.mskmz.catmvvm.R;
import com.mskmz.catmvvm.support.rv.CatRecyclerAdapter;

public class RvAdapter extends CatRecyclerAdapter {
    public RvAdapter(@NonNull ObservableList data) {
        super(data);
    }

    @Override
    protected int onCreateViewHolderForRes(@NonNull ViewGroup parent, int viewType) {
        return R.layout.item_rv;
    }
}

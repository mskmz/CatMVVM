package com.mskmz.catmvvm.core.m;

import androidx.databinding.BaseObservable;

import com.mskmz.catmvvm.core.inter.CatBaseModel;
import com.mskmz.catmvvm.core.manager.ShardModelManager;

public class CatModel
        extends BaseObservable
        implements CatBaseModel {

    public CatModel() {
        ShardModelManager.INSTANCE().checkClass(this);
    }

    @Override
    public CatBaseModel singleCreate() {
        return this;
    }
}

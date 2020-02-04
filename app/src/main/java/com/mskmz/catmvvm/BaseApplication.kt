package com.mskmz.catmvvm

import android.app.Application
import com.mskmz.catmvvm.core.constant.CatConfig
import com.mskmz.catmvvm.support.rv.CatRecyclerConfig

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CatConfig.init(this)
        CatRecyclerConfig.init(this)
    }
}
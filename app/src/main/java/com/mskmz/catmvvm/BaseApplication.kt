package com.mskmz.catmvvm

import android.app.Application
import com.mskmz.catmvvm.core.constant.CatConfig

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        CatConfig.init(this)
    }
}
package com.mskmz.catmvvm

import android.util.Log
import com.mskmz.catmvvm.core.v.CatActivity
import com.mskmz.catmvvm.databinding.ActivityMainBinding

class MainActivity : CatActivity<ActivityMainBinding>() {
    override fun onViewModelListen(msgTag: Int, msgObj: Any?) {
        Log.d("MainActivity", "$msgTag")
        noticeAllVm(msgTag, msgObj)
    }
}

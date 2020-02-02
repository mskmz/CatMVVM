package com.mskmz.catmvvm

import android.util.Log
import com.mskmz.catmvvm.core.v.CatActivity
import com.mskmz.catmvvm.databinding.ActivityNewBinding
import java.lang.ref.WeakReference

class NewActivity : CatActivity<ActivityNewBinding>() {
    override fun init() {
        Log.d("new", "" + TestCheckLive.INSTANCE().mActivity.get())
        Log.d("new", "" + TestCheckLive.INSTANCE().mAmb.get())

    }
}

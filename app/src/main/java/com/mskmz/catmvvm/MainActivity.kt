package com.mskmz.catmvvm

import android.content.Intent
import android.util.Log
import com.mskmz.catmvvm.core.v.CatActivity
import com.mskmz.catmvvm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : CatActivity<ActivityMainBinding>() {
    override fun init() {
        super.init()
        Log.d("MainActivity", "init")
        btn_next.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    NewActivity::class.java
                )
            )
            this@MainActivity.finish()
        }
    }

    override fun onViewModelListen(msgTag: Int, msgObj: Any?) {
        Log.d("MainActivity", "$msgTag")
        noticeAllVm(msgTag, msgObj)

    }
}

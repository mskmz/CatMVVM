package com.mskmz.catmvvm

import android.content.Intent
import android.util.Log
import com.mskmz.catmvvm.core.v.CatActivity
import com.mskmz.catmvvm.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CatActivity<ActivityMainBinding>() {
    override fun init() {
        super.init()
        btn_next.setOnClickListener {
            var i = Intent(this@MainActivity, NewActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onViewModelListen(msgTag: Int, msgObj: Any?) {
        Log.d("MainActivity", "$msgTag")
        noticeAllVm(msgTag, msgObj)

    }
}

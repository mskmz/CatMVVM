package com.mskmz.catmvvm

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mskmz.catmvvm.databinding.ActivityNewBinding
import com.mskmz.catmvvm.databinding.ActivityTestNewBinding
import java.lang.ref.WeakReference

class NewActivity2 : AppCompatActivity() {
    lateinit var db: ActivityTestNewBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DataBindingUtil.setContentView(
            this,
            R.layout.activity_test_new
        )
        db.btnLog.setOnClickListener {
        }
    }
}

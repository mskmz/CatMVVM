package com.mskmz.catmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mskmz.catmvvm.core.annotaion.OnNoticeListen


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    @OnNoticeListen(11)
    fun a(){

    }
}

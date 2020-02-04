package com.mskmz.catmvvm

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mskmz.catmvvm.databinding.ActivityTestMainBinding
import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

class MainActivity2 : AppCompatActivity() {

    lateinit var db: ActivityTestMainBinding
    lateinit var tweak: TestListen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("wzk>>>", "MainActivity" + "oncreate")
        db = DataBindingUtil.setContentView(
            this,
            R.layout.activity_test_main
        )
        tweak = TestListen(Ta(), 1)
        Thread(Runnable {
            var ref: Reference<out Ta?>?
            while (true) {
                ref = TBASE.queue.remove()
                Log.d("wzk>>>", "MainActivity" + "ref=")
                if (ref != null && ref is TestListen) {
                    Log.d("wzk>>>", "MainActivity" + "ref=" + ref.i)
                }
            }
        }).start()
//        db.btnNext.setOnClickListener {
//            startActivity(
//                Intent(
//                    this@MainActivity2,
//                    NewActivity2::class.java
//                )
//            )
//            this@MainActivity2.finish()
//        }
    }
}

object TBASE {
    val queue = ReferenceQueue<Ta>()
}

class Ta
class TestListen(referent: Ta, var i: Int) : WeakReference<Ta>(referent, TBASE.queue)

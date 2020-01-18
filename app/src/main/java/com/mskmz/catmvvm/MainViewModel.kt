package com.mskmz.catmvvm

import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import com.mskmz.catmvvm.core.annotaion.CatAutoWire
import com.mskmz.catmvvm.core.annotaion.CatOnNoticeListen
import com.mskmz.catmvvm.core.inter.CatBaseView
import com.mskmz.catmvvm.core.v.CatActivity
import com.mskmz.catmvvm.core.vm.CatViewModel
import com.mskmz.catmvvm.databinding.ActivityMainBinding

@CatAutoWire
class MainViewModel : CatViewModelIgnore() {
    companion object {
        const val NOTICE_MSG = 11;
    }

    @CatOnNoticeListen(NOTICE_MSG)
    var testName = ObservableField<String>("11")
}

package com.mskmz.catmvvm

import android.util.Log
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.mskmz.catmvvm.core.annotaion.CatAutoInjection
import com.mskmz.catmvvm.core.annotaion.CatAutoWire
import com.mskmz.catmvvm.core.annotaion.CatOnNoticeListen

@CatAutoWire
class MainViewModel : CatViewModelIgnore() {
    companion object {
        const val NOTICE_MSG = 11;
    }

    @CatAutoInjection
    lateinit var model: BaseModel

    @CatOnNoticeListen(NOTICE_MSG)
    var testName = ObservableField<String>("11")

    override fun init() {
        super.init()
        Log.d("MainViewModel", "baseModel - ${model.name}")
        Log.d("MainViewModel", "baseModel - ${model}")

        model.addOnPropertyChangedCallback(this,object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                Log.d("MainViewModel", "baseModel - ${model.name}")
                model.Log()
            }
        })
        model.Log()
    }
}

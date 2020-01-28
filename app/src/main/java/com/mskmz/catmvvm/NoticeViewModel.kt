package com.mskmz.catmvvm


import android.util.Log
import com.mskmz.catmvvm.core.annotaion.CatAutoInjection
import com.mskmz.catmvvm.core.annotaion.CatAutoWire


@CatAutoWire
class NoticeViewModel : CatViewModelIgnore() {

    @CatAutoInjection
    lateinit var baseModel: BaseModel

    override fun init() {
        super.init()
        Log.d("NoticeViewModel", "baseModel - ${baseModel}")
    }

    override fun noticeView(msgTag: Int, msgObj: Any?) {
        Log.d("NoticeViewModel", "baseModel - ${baseModel.name}")
        super.noticeView(msgTag, msgObj)
        baseModel.name = "$msgTag"
        baseModel.notifyChange()
        Log.d("NoticeViewModel", "$msgTag")
        Log.d("NoticeViewModel", "baseModel - ${baseModel.name}")
        Log.d("NoticeViewModel", "$view")
    }
}

package com.mskmz.catmvvm


import android.util.Log
import com.mskmz.catmvvm.core.annotaion.CatAutoWire


@CatAutoWire
class NoticeViewModel : CatViewModelIgnore() {
    override fun noticeView(msgTag: Int, msgObj: Any?) {
        super.noticeView(msgTag, msgObj)
        Log.d("NoticeViewModel", "$msgTag")
        Log.d("NoticeViewModel", "$view")
    }
}

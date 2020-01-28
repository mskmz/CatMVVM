package com.mskmz.catmvvm

import com.mskmz.catmvvm.core.annotaion.CatAutoInjection
import com.mskmz.catmvvm.core.annotaion.CatAutoWire


@CatAutoWire
class NoticeViewModel : CatViewModelIgnore() {

    @CatAutoInjection
    lateinit var baseModel: BaseModel

    override fun noticeView(msgTag: Int, msgObj: Any?) {
        super.noticeView(msgTag, msgObj)
        baseModel.name = "$msgTag"
        baseModel.notifyChange()
    }
}

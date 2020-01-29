package com.mskmz.catmvvm

import com.mskmz.catmvvm.core.annotaion.CatAutoWire

@CatAutoWire
class BaseModel : CatModelIgnore() {
    public var name = "str"
}

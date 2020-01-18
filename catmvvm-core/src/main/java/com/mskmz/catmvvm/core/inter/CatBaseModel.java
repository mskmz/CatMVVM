package com.mskmz.catmvvm.core.inter;

import java.util.Map;

//m中的特性
// - 是一对多的关系
// - 部分m是单例的
//m中应该具有的功能
// - 相关的v的索引
// - 实现全局复用的机制
public interface CatBaseModel {
    public CatBaseModel singleCreate();
}

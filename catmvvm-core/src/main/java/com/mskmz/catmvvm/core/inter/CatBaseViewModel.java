package com.mskmz.catmvvm.core.inter;

//vm中的特性
// - 一个vm对象中应该只有一个唯一固定的v （创建时传入索引）
//vm中应该具有的功能
// - 绑定view的方法
// - 从全局变量中拿出m的方法
// - 接受v中通知的方法
public interface CatBaseViewModel<T extends CatBaseView> {

    void onViewListen(int msgTag, Object msgObj);

    void noticeView(int msgTag, Object msgObj);

    void bindView(T v);

    T getView();

}

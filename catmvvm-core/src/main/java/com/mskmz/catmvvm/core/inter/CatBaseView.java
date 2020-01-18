package com.mskmz.catmvvm.core.inter;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

//v中的特性
// - 具有唯一绑定的db对象
// - 可以具有多个vm对象
// - 不应该具备m对象
//v中应该具备的功能
// - 添加和删除对应的vm
// - 消息允许转发给响应的vm对象
public interface CatBaseView<T extends ViewDataBinding> {
    T getDb();

    void addVM(String vmName, CatBaseViewModel vm);

    void removeVM(String vmName);

    void removeVM(CatBaseViewModel vm);

    void onViewModelListen(int msgTag, Object msgObj);

    CatBaseViewModel getVm();

    CatBaseViewModel getVm(String vmName);

    void noticeVm(int msgTag, Object msgObj);

    void noticeVm(String vmName, int msgTag, Object msgObj);

    void noticeVm(CatBaseViewModel vm, int msgTag, Object msgObj);

    void noticeAllVm(int msgTag, Object msgObj);
}
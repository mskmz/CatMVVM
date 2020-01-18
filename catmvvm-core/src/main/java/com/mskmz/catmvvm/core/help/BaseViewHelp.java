package com.mskmz.catmvvm.core.help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import com.mskmz.catmvvm.core.Utils.ViewUtils;
import com.mskmz.catmvvm.core.inter.CatBaseView;
import com.mskmz.catmvvm.core.inter.CatBaseViewModel;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BaseViewHelp implements CatBaseView {
    private CatBaseView mV;
    private HashMap<String, CatBaseViewModel> mVmMap;

    public BaseViewHelp(CatBaseView catBaseView) {
        mV = catBaseView;
        mVmMap = new HashMap<>();
    }

    @Override
    public ViewDataBinding getDb() {
        throw new UnsupportedOperationException("不应该使用help类实现该方法");
    }

    public Map<String, CatBaseViewModel> getVmMap() {
        return mVmMap;
    }

    public void bindVm(ViewDataBinding db) {
        ViewUtils.scanDatabing(db);
        try {
            Map<String, CatBaseViewModel> map = ViewUtils.bindVmList(db, this, ViewUtils.scanDatabing(db));
            for (Map.Entry<String, CatBaseViewModel> entry : map.entrySet()) {
                registerLife(mV, entry.getValue());
            }
            mVmMap.putAll(map);
        } catch (java.lang.InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void registerLife(Object obj, CatBaseViewModel catBaseViewModel) {
        if (!(catBaseViewModel instanceof LifecycleObserver)) {
            return;
        }
        if (obj instanceof AppCompatActivity) {

        } else if (obj instanceof Fragment) {
            ((Fragment) obj).getLifecycle().addObserver((LifecycleObserver) catBaseViewModel);
        }
    }

    @Override
    public void addVM(String vmName, CatBaseViewModel vm) {
        vm.bindView(mV);
        registerLife(mV, vm);
        mVmMap.put(vmName, vm);
    }

    @Override
    public void removeVM(String vmName) {
        mVmMap.remove(vmName);
    }

    @Override
    public void removeVM(CatBaseViewModel vm) {
        for (Map.Entry<String, CatBaseViewModel> entry : mVmMap.entrySet()) {
            if (entry.getValue() == vm) {
                mVmMap.remove(entry.getKey());
            }
        }
    }

    @Override
    public void onViewModelListen(int msgTag, Object msgObj) {
        throw new UnsupportedOperationException("不应该使用help类实现该方法");
    }

    @Override
    public CatBaseViewModel getVm() {
        return getVm("vm");
    }

    @Override
    public CatBaseViewModel getVm(String vmName) {
        return mVmMap.get(vmName);
    }


    @Override
    public void noticeVm(int msgTag, Object msgObj) {
        noticeVm(getVm(), msgTag, msgObj);
    }

    @Override
    public void noticeVm(String vmName, int msgTag, Object msgObj) {
        noticeVm(getVm(vmName), msgTag, msgObj);
    }

    @Override
    public void noticeVm(CatBaseViewModel vm, int msgTag, Object msgObj) {
        if (vm == null) {
            return;
        }
        vm.onViewListen(msgTag, msgObj);
    }

    @Override
    public void noticeAllVm(int msgTag, Object msgObj) {
        for (CatBaseViewModel vm : mVmMap.values()) {
            noticeVm(vm, msgTag, msgObj);
        }
    }
}

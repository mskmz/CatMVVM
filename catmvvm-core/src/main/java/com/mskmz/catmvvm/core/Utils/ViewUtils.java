package com.mskmz.catmvvm.core.Utils;

import android.content.Context;
import android.util.Log;

import androidx.databinding.ViewDataBinding;

import com.google.common.base.CaseFormat;
import com.mskmz.catmvvm.core.inter.CatBaseView;
import com.mskmz.catmvvm.core.inter.CatBaseViewModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewUtils {

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  TAG  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    private static final String TAG = "ViewUtils>>>";
    private static final boolean isDebug = false;
    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  TAG  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    //尝试扫描DB获取其中的DB对象
    public static List<VmBinderBean> scanDatabing(ViewDataBinding db) {
        List<VmBinderBean> vmBinderBeanList = null;
        Class cDb = db.getClass();
        Method[] mDbArray = cDb.getDeclaredMethods();
        String methodName;
        Class cVm;
        for (Method m : mDbArray) {
            methodName = m.getName().toLowerCase();
            if (methodName.contains("set") && methodName.contains("vm")) {
                try {
                    cVm = m.getParameterTypes()[0];
                    if (!CatBaseViewModel.class.isAssignableFrom(cVm)) {
                        continue;
                    }
                    if (vmBinderBeanList == null) {
                        vmBinderBeanList = new ArrayList<>();
                    }
                    vmBinderBeanList.add(new VmBinderBean(m, cVm));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return vmBinderBeanList;
    }

    public static Map<String, CatBaseViewModel> bindVmList(ViewDataBinding db, CatBaseView view, List<VmBinderBean> vmBinderBeanList) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Map<String, CatBaseViewModel> vmMap = null;
        if (vmBinderBeanList == null) {
            return vmMap;
        }
        Object oVm;
        Method mVmBindView = null;
        String vmName;
        for (VmBinderBean vmBinderBean : vmBinderBeanList) {
            //获取到cVm
            oVm = vmBinderBean.clazz.newInstance();
            //假定他是vm类
            if (mVmBindView == null) {
                mVmBindView = CatBaseViewModel.class.getDeclaredMethod("bindView", CatBaseView.class);
                mVmBindView.setAccessible(true);
            }
            //vm绑定v
            mVmBindView.invoke(oVm, view);
            //v绑定vm
            vmBinderBean.method.invoke(db, oVm);
            //赋值
            if (isDebug) Log.d(TAG, "bindVmList: " + vmBinderBean.method);
            vmName = CaseFormat.UPPER_CAMEL.to(
                    CaseFormat.LOWER_CAMEL,
                    vmBinderBean.method.getName().replaceFirst("set", "")
            );
            if (vmMap == null) {
                vmMap = new HashMap<>();
            }
            vmMap.put(vmName, (CatBaseViewModel) oVm);
        }
        return vmMap;
    }

    public static class VmBinderBean {
        Method method;
        Class clazz;

        public VmBinderBean(Method method, Class clazz) {
            this.method = method;
            this.clazz = clazz;
        }
    }

    //获取资源路径
    public static int getRes(Context context, String className) {
        try {
            String name = CaseFormat.LOWER_CAMEL.to(
                    CaseFormat.LOWER_UNDERSCORE,
                    className).replace(
                    "_binding",
                    ""
            );
            if (isDebug) Log.d(TAG, "getRes: " + name);
            String type = "layout";
            String pake = context.getPackageName();
            return context.getResources().getIdentifier(name, type, pake);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}

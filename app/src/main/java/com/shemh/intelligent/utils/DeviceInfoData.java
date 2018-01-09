package com.shemh.intelligent.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.shemh.intelligent.APP;
import com.shemh.intelligent.bean.AllDeviceInfoBean;
import com.shemh.intelligent.bean.DeviceInfoBean;

import java.util.List;

/**
 * Created by shemh on 2017/4/25.
 */

public class DeviceInfoData {

    public static AllDeviceInfoBean getDeviceInfo(){
        String info=(String) PreferencesUtils.getString(APP.getInstance(),"AllDeviceInfoBean","");
        if (!TextUtils.isEmpty(info)) {
            return (AllDeviceInfoBean) new Gson().fromJson(info, AllDeviceInfoBean.class);
        }
        return null;
    }

    public static void saveDeviceInfo(List<DeviceInfoBean> obj){
        AllDeviceInfoBean allDeviceInfoBean = new AllDeviceInfoBean();
        allDeviceInfoBean.setDeviceInfoList(obj);
        if (null != allDeviceInfoBean) {
            PreferencesUtils.putString(APP.getInstance(), "AllDeviceInfoBean", new Gson().toJson(allDeviceInfoBean));
        }
    }

    public static void clearDeviceInfo(){
        PreferencesUtils.removeValue(APP.getInstance(), "AllDeviceInfoBean");
    }

}

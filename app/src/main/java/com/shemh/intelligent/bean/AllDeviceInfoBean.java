package com.shemh.intelligent.bean;

import java.util.List;

/**
 * Created by shemh on 2018/1/9.
 */

public class AllDeviceInfoBean {

    private List<DeviceInfoBean> deviceInfoList;

    private int row;

    private int num;

    public List<DeviceInfoBean> getDeviceInfoList() {
        return deviceInfoList;
    }

    public void setDeviceInfoList(List<DeviceInfoBean> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}

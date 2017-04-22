package com.ling.jibonetposa.entities;

import java.util.List;

/**
 * Created by mhz小志 on 2017/4/22.
 */

public class BrandBean {
    private String key;
    private String name;
    private List<DeviceBean> val;

    @Override
    public String toString() {
        return "Brand{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", val=" + val +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeviceBean> getVal() {
        return val;
    }

    public void setVal(List<DeviceBean> val) {
        this.val = val;
    }
}
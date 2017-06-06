package com.github.beansoftapp.reatnative.idea.models.ios;

/**
 * An iOS device item info, see parseIOSDevicesList.js of RN.
 * Created by beansoft on 2017/6/3.
 */
public class IOSDeviceInfo {
    public String state, availability;// simulator extra info
    public String version, name, udid;// common device properties
    public boolean simulator;// is simulator or not

    @Override
    public String toString() {
        return "IOSDeviceInfo{" +
                "state='" + state + '\'' +
                ", availability='" + availability + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", udid='" + udid + '\'' +
                ", simulator=" + simulator +
                '}';
    }
}

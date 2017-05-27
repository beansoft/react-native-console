package com.github.beansoftapp.reatnative.idea.models.ios.devices;

import java.util.Map;

/**
 * An iOS devices object class.
 * Created by beansoft on 2017/5/26.
 */
public class Devices {
    public Map<String, DeviceItem[]> devices;

    public static class DeviceItem {
        public String state, availability, name, udid;
    }
}


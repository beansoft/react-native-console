package com.github.beansoftapp.reatnative.idea.models;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by beansoft on 17/3/16.
 */
@State(
        name = "RNConsoleConfiguration",
        storages = @Storage(file = "rnconsole-configuration.xml", roamingType = RoamingType.DISABLED)
)
public class RNConsoleConfiguration implements PersistentStateComponent<RNConsoleConfiguration> {


    @Nullable
    @Override
    public RNConsoleConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(RNConsoleConfiguration freelineConfiguration) {
        XmlSerializerUtil.copyBean(freelineConfiguration, this);
    }

    public static RNConsoleConfiguration getInstance() {
        return ServiceManager.getService(RNConsoleConfiguration.class);
    }
}

package com.github.beansoft.reatnative.idea.models;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
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
        return ApplicationManager.getApplication().getService(RNConsoleConfiguration.class);
    }
}

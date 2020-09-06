package com.github.beansoft.reatnative.idea.configuration;

import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.intellij.execution.configurations.ConfigurationTypeBase;

import javax.swing.*;

/**
 * React Native run configuration type
 *
 * @author beansoft@126.com
 */
public class FreeConfigurationType extends ConfigurationTypeBase {

    private final static String ID = "com.github.beansoftapp.reatnative.run";
    private final static String DISPLAY_NAME = "ReactNative Run";
    private final static String DESC = "ReactNative Run Configuration";
    private final static Icon ICON = PluginIcons.ICON_ACTION_RUN;

    protected FreeConfigurationType() {
        super(ID, DISPLAY_NAME, DESC, ICON);
        this.addFactory(new FreeConfigurationFactory(this));
    }
}

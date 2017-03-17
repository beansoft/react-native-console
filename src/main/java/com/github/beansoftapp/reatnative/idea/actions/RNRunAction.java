package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeTerminal;

import javax.swing.*;

/**
 * Created by pengwei on 16/9/11.
 */
public class RNRunAction extends BaseAction {

    public RNRunAction() {
        super(PluginIcons.FreelineIcon);
    }

    public RNRunAction(Icon icon) {
        super(icon);
    }

    @Override
    public void actionPerformed() {
//            String python = Utils.getPythonLocation();
//            if (python == null) {
//                NotificationUtils.pythonNotFound();
//            } else {
                ReactNativeTerminal.getInstance(currentProject).initAndExecute(new String[]{
                        "echo", "React Native Console by https://github.com/beansoftapp", getArgs()});
//            }
    }

    /**
     * 设置参数
     *
     * @return
     */
    protected String getArgs() {
        return null;
    }
}

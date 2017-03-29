package com.github.beansoftapp.reatnative.idea.actions;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.Utils;

/**
 * Created by beansoft on 17/3/15.
 */
public class UpdateAction extends BaseAction {

    public UpdateAction() {
        super(PluginIcons.Help);
    }

    @Override
    public void actionPerformed() {
        Utils.openUrl("https://github.com/beansoftapp/react-native-console");
    }
}
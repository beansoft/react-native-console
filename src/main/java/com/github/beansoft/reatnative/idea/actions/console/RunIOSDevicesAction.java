package com.github.beansoft.reatnative.idea.actions.console;

import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoft.reatnative.idea.actions.BaseRNConsoleActionGroup;
import com.github.beansoft.reatnative.idea.entity.ios.IOSDeviceInfo;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.ui.RNConsole;
import com.github.beansoft.reatnative.idea.utils.NotificationUtils;
import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.beansoft.reatnative.idea.utils.ios.IOSDevicesParser;
import com.github.beansoft.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/** Show all ios devices, includes simuators, and let the user choose one item to run */
public class RunIOSDevicesAction extends BaseRNConsoleActionGroup {
    public RunIOSDevicesAction(ReactNativeConsole terminal) {
        super(terminal, "iOS Choose Devices", "Run on a Selected iOS Device", PluginIcons.IPhoneDevices);
        setPopup(true);
    }

    /**
     * @return true if {@link #actionPerformed(AnActionEvent)} should be called, in this mode method
     * AnAction[] getChildren(@Nullable AnActionEvent e)
     * will be ignored.
     * 默认可点击
     */
    public boolean canBePerformed(DataContext context) {
        return true;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // Running with background task and with a progress indicator
        ProgressManager.getInstance().run(new Task.Backgroundable(e.getProject(), "Reading IOS Devices List", false) {
            public void run(@NotNull final ProgressIndicator indicator) {
                indicator.setText("RN Console:Loading the iOS devices list...");
                try {
                    doRun(e);
                } catch (final Exception e) {

                    UIUtil.invokeLaterIfNeeded(() -> Messages.showErrorDialog(getProject(), e.getMessage(), getTitle()));
                }
            }
        });

//            ApplicationManager.getApplication().executeOnPooledThread(() -> {
//
//            });

    }

    void doRun(AnActionEvent e) {
        java.util.List<IOSDeviceInfo> devices = IOSDevicesParser.getAllIOSDevicesList(false);
        ApplicationManager.getApplication().invokeLater(() -> {
            if (devices == null) {
                NotificationUtils.errorMsgDialog("Sorry, no iOS simulator or physically connected iOS devices found!");
                return;
            }

            int x = 0;
            int y = 0;
            InputEvent inputEvent = e.getInputEvent();
            if (inputEvent instanceof MouseEvent) {
                x = ((MouseEvent) inputEvent).getX();
                y = ((MouseEvent) inputEvent).getY();
            }
            showDevicesPopup(inputEvent.getComponent(), x, y, createDevicesPopupGroup(devices));
        });

    }

    // Show a ios device list popup menu
    private void showDevicesPopup(Component component, int x, int y, DefaultActionGroup defaultActionGroup) {
        ActionPopupMenu popupMenu =
                ((ActionManagerImpl) ActionManager.getInstance())
                        .createActionPopupMenu("ToolwindowPopup", // ToolWindowContentUi.POPUP_PLACE should be relaced with ActionPlaces.TOOLWINDOW_POPUP
                            defaultActionGroup,
                                new MenuItemPresentationFactory(false));// don't set forceHide to true, otherwise icons will be hidden in menu item
        popupMenu.getComponent().show(component, x, y);
    }

    // Generate a ios device list
    private DefaultActionGroup createDevicesPopupGroup(java.util.List<IOSDeviceInfo> devices) {
        DefaultActionGroup group = new DefaultActionGroup();
        devices.forEach(iosDeviceInfo -> {
            if (iosDeviceInfo != null) {
                String deviceName = iosDeviceInfo.name + " " + iosDeviceInfo.version;
                group.add(new BaseRNConsoleAction(super.terminal, deviceName, "Run on iOS device: '" + deviceName + "'",
                        iosDeviceInfo.simulator ? PluginIcons.IPhoneSimulator
                                : PluginIcons.IPhoneDevice) {
                    @Override
                    public void doAction(AnActionEvent anActionEvent) {
                        RNConsole consoleView = terminal.getRNConsole(getText(), getIcon());
                        consoleView.runRawNPMCI(
                                RNPathUtil.getExecuteFileFullPath("react-native"),
                                "run-ios",
                                iosDeviceInfo.simulator ? "--simulator" : "--device",
                                iosDeviceInfo.name);
                    }
                });
            }
        });


        return group;
    }

}

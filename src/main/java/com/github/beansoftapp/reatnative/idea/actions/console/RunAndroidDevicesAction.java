package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleActionGroup;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.ui.RNConsole;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.github.pedrovgs.androidwifiadb.Device;
import com.github.pedrovgs.androidwifiadb.adb.ADB;
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
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;

/**
 * Show all connected android devices, includes simuators, and let the user choose one item to run
 * @author beansoft
 * @date 2019-8-20
 */
public class RunAndroidDevicesAction extends BaseRNConsoleActionGroup {
    public RunAndroidDevicesAction(ReactNativeConsole terminal) {
        super(terminal, "Android Debug On Devices", "Run on a Selected Android Device", PluginIcons.Android);
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
        ProgressManager.getInstance().run(new Task.Backgroundable(e.getProject(), "Reading Android Devices List", false) {
            public void run(@NotNull final ProgressIndicator indicator) {
                indicator.setText("RN Console:Loading the Android devices list...");
                try {
                    doRun(e);
                } catch (final Exception e) {

                    UIUtil.invokeLaterIfNeeded(() -> Messages.showErrorDialog(getProject(), e.getMessage(), getTitle()));
                }
            }
        });
    }

    void doRun(AnActionEvent e) {
        ADB adb = new ADB();
        Collection<Device> devices = adb.getDevicesConnectedByUSB();
        ApplicationManager.getApplication().invokeLater(() -> {
            if (devices == null || devices.size() == 0) {
                NotificationUtils.errorMsgDialog("Sorry, no Android simulator or physically connected Android devices found!");
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
                        .createActionPopupMenu(ToolWindowContentUi.POPUP_PLACE, defaultActionGroup,
                                new MenuItemPresentationFactory(false));// don't set forceHide to true, otherwise icons will be hidden in menu item
        popupMenu.getComponent().show(component, x, y);
    }

    // Generate a ios device list
    private DefaultActionGroup createDevicesPopupGroup(Collection<Device> devices) {
        DefaultActionGroup group = new DefaultActionGroup();
        devices.forEach(iosDeviceInfo -> {
            if (iosDeviceInfo != null) {
                String deviceName = iosDeviceInfo.getName();// + " " + (iosDeviceInfo.isConnected() ? "Connected" : "Disconnected");
                group.add(new BaseRNConsoleAction(super.terminal, "Debug on " + deviceName, "Debug on Android device: '" + deviceName + "'",
                    PluginIcons.Android
                        //iosDeviceInfo.isConnected() ? PluginIcons.Android : PluginIcons.Error
                ) {
                    @Override
                    public void doAction(AnActionEvent anActionEvent) {
//                        if(!iosDeviceInfo.isConnected()) {
//                            NotificationUtils.errorNotification(iosDeviceInfo.getName() + " is disconnected");
//                            return;
//                        }

                        RNConsole consoleView = terminal.getRNConsole(getText(), getIcon());
                        consoleView.runRawNPMCI(
                                RNPathUtil.getExecuteFullPathSingle("react-native"),
                                "run-android",
                                "--deviceId",
                                iosDeviceInfo.getId());
                    }
                });
            }
        });


        return group;
    }

}

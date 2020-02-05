package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleActionGroup;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.ui.RNConsole;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.android.AVDList;
import com.github.beansoftapp.reatnative.idea.utils.android.AndroidPathUtil;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * Show all android virtual devices, includes simuators, and let the user choose one item to run
 * @author beansoft
 * @date 2020-2-1
 */
public class AndroidAvdsAction extends BaseRNConsoleActionGroup {
    public AndroidAvdsAction(ReactNativeConsole terminal) {
        super(terminal, "Run Android Emulator", "Run Android Emulator", PluginIcons.IPhoneDevices);
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
        ProgressManager.getInstance().run(new Task.Backgroundable(e.getProject(), "Reading Android AVD List", false) {
            public void run(@NotNull final ProgressIndicator indicator) {
                indicator.setText("RN Console:Loading the Android AVD list...");
                try {
                    doRun(e);
                } catch (final Exception e) {
                    UIUtil.invokeLaterIfNeeded(() -> Messages.showErrorDialog(getProject(), e.getMessage(), getTitle()));
                }
            }
        });
    }

    void doRun(AnActionEvent e) {
        String path = RNPathUtil.getRNProjectPath(e.getProject());
        String gradleLocation = RNPathUtil.getAndroidProjectPath(path);

        ApplicationManager.getApplication().invokeLater(() -> {
            String[] list = AVDList.getAvdList(gradleLocation);

            if (list == null || list.length == 0) {
                NotificationUtils.errorMsgDialog("Sorry, no Android AVD found! You can add one by running  Android Studio through menu: Tools > AVD Manager");
                return;
            }

            int x = 0;
            int y = 0;
            InputEvent inputEvent = e.getInputEvent();
            if (inputEvent instanceof MouseEvent) {
                x = ((MouseEvent) inputEvent).getX();
                y = ((MouseEvent) inputEvent).getY();
            }
            showDevicesPopup(inputEvent.getComponent(), x, y, createDevicesPopupGroup(list));
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
    private DefaultActionGroup createDevicesPopupGroup(String[] devices) {
        DefaultActionGroup group = new DefaultActionGroup();

        for (String avd :
                devices ) {
            System.out.println("avd:" + avd);
            if(StringUtil.isNotEmpty(avd)) {
                group.add(new BaseRNConsoleAction(super.terminal, "Run emulator  " + avd, "Run emulator  " + avd,
                        PluginIcons.Android
                        //iosDeviceInfo.isConnected() ? PluginIcons.Android : PluginIcons.Error
                ) {
                    @Override
                    public void doAction(AnActionEvent anActionEvent) {
                        String path = RNPathUtil.getRNProjectPath(getProject());
                        String gradleLocation = RNPathUtil.getAndroidProjectPath(path);

                        RNConsole consoleView = terminal.getRNConsole(getText(), getIcon());
                        //emulator.exe  -avd "Nexus_4_API_22"
                        consoleView.runRawNPMCI(
                                AndroidPathUtil.getEmulatorPath(gradleLocation),
                                "-avd",
                                avd );
                    }
                });
            }
        }


        return group;
    }

}

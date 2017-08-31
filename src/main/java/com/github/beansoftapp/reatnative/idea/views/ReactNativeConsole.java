package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.actions.*;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.models.ios.IOSDeviceInfo;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.OSUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.Utils;
import com.github.beansoftapp.reatnative.idea.utils.ios.IOSDevicesParser;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.filters.BrowserHyperlinkInfo;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.ShowFilePathAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionManagerImpl;
import com.intellij.openapi.actionSystem.impl.MenuItemPresentationFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.openapi.wm.impl.content.ToolWindowContentUi;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.impl.ContentImpl;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.terminal.AbstractTerminalRunner;
import org.jetbrains.plugins.terminal.JBTabbedTerminalWidget;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * A React Native Console with console view as process runner, no more depends on terminal widget,
 * thus tabs could be reused.
 * Created by beansoft@126.com on 17/4/27.
 */
public class ReactNativeConsole implements FocusListener, ProjectComponent {
    private Project myProject;

    public static ReactNativeConsole getInstance(Project project) {
        return project.getComponent(ReactNativeConsole.class);
    }

    public ReactNativeConsole(Project project) {
        this.myProject = project;
    }

    private ToolWindow getToolWindow() {
        return ToolWindowManager.getInstance(myProject).getToolWindow(RNToolWindowFactory.TOOL_WINDOW_ID);
    }

    public void initAndActive() {
        ToolWindow toolWindow = getToolWindow();
        if (!toolWindow.isActive()) {
            toolWindow.activate(null);
        }
    }

    /**
     * Create a terminal panel
     *
     * @param terminalRunner
     * @param toolWindow
     * @return
     */
    private Content createTerminalInContentPanel(@NotNull AbstractTerminalRunner terminalRunner, @NotNull final ToolWindow toolWindow) {
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true);
        Content content = ContentFactory.SERVICE.getInstance().createContent(panel, "TestTerminal", false);
        content.setCloseable(true);
        JBTabbedTerminalWidget myTerminalWidget = terminalRunner.createTerminalWidget(content);
        panel.setContent(myTerminalWidget.getComponent());
        panel.addFocusListener(this);

        new Thread(() -> {
            try {
                // Wait 0.5 second for the terminal to show up, no wait works ok on WebStorm but not on Android Studio
                Thread.currentThread().sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Below code without ApplicationManager.getApplication().invokeLater() will throw exception
            // : IDEA Access is allowed from event dispatch thread only.
            ApplicationManager.getApplication().invokeLater(() -> {
                if (myTerminalWidget.getCurrentSession() != null) {
                    myTerminalWidget.getCurrentSession().getTerminalStarter().sendString("ls\n");
                }
            });
        }).start();

//        ApplicationManager.getApplication().invokeLater(() -> {
//            if (myTerminalWidget.getCurrentSession() != null) {
//                myTerminalWidget.getCurrentSession().getTerminalStarter().sendString("ls\n");
//            }
//        });


//        createToolbar(terminalRunner, myTerminalWidget, toolWindow, panel);// west toolbar

//        ActionToolbar toolbar = createTopToolbar(terminalRunner, myTerminalWidget, toolWindow);
//        toolbar.setTargetComponent(panel);
//        panel.setToolbar(toolbar.getComponent(), false);

        content.setPreferredFocusableComponent(myTerminalWidget.getComponent());
        return content;
    }

    public void initTerminal(final ToolWindow toolWindow) {
        toolWindow.setToHideOnEmptyContent(true);
        toolWindow.setStripeTitle("RN Console");
        toolWindow.setIcon(PluginIcons.React);
        Content content = createConsoleTabContent(toolWindow, true, "Welcome", null);
//        toolWindow.getContentManager().addContent(content);
//        toolWindow.getContentManager().addContent(new ContentImpl(new JButton("Test"), "Build2", false));

        // ======= test a terminal create ======
//        LocalTerminalDirectRunner terminalRunner = LocalTerminalDirectRunner.createTerminalRunner(myProject);
//        Content testTerminalContent = createTerminalInContentPanel(terminalRunner, toolWindow);
//        toolWindow.getContentManager().addContent(testTerminalContent);

//        SimpleTerminal term  = new SimpleTerminal();
//        term.sendString("ls\n");
//        toolWindow.getContentManager().addContent(new ContentImpl(term.getComponent(), "terminal", false));
        toolWindow.setShowStripeButton(true);// if set to false, then sometimes the window will be hidden from the dock area for ever 2017-05-26
//        toolWindow.setTitle(" - ");
        ((ToolWindowManagerEx) ToolWindowManager.getInstance(this.myProject)).addToolWindowManagerListener(new ToolWindowManagerListener() {
            @Override
            public void toolWindowRegistered(@NotNull String s) {
            }

            @Override
            public void stateChanged() {
                ToolWindow window = ToolWindowManager.getInstance(myProject).getToolWindow(RNToolWindowFactory.TOOL_WINDOW_ID);
                if (window != null) {
                    boolean visible = window.isVisible();
                    if (visible && toolWindow.getContentManager().getContentCount() == 0) {
                        initTerminal(window);
                    }
                }
            }
        });
        toolWindow.show(null);
    }

    /**
     * 执行shell
     *
     * @param shell
     */
    public void executeShell(String shell, String workDirectory, String displayName, Icon icon) {
        RNConsoleImpl rnConsole = getRNConsole(displayName, icon);
        if (rnConsole != null) {
            rnConsole.executeShell(shell, workDirectory);
        }
    }

    /**
     * 执行shell
     * 利用terminal换行即执行原理
     *
     * @param shell
     */
    public void runGradleCI(String shell, String displayName, Icon icon) {
        RNConsoleImpl rnConsole = getRNConsole(displayName, icon);
        if (rnConsole != null) {
            rnConsole.runGradleCI(shell);
        }
    }

    /**
     * 执行shell
     * 利用terminal换行即执行原理
     *
     * @param shell
     */
    public void runNPMCI(String shell, String displayName, Icon icon) {
        RNConsoleImpl rnConsole = getRNConsole(displayName, icon);
        if (rnConsole != null) {
            rnConsole.runNPMCI(shell);
        }
    }

    /**
     * 获取 RN Console实例.
     *
     * @param displayName - the tab's display name must be unique.
     * @param icon        - used to set a tab icon, not used for search
     * @return
     */
    public RNConsoleImpl getRNConsole(String displayName, Icon icon) {
        ToolWindow window = ToolWindowManager.getInstance(myProject).getToolWindow(RNToolWindowFactory.TOOL_WINDOW_ID);
        if (window != null) {
            Content existingContent = createConsoleTabContent(window, false, displayName, icon);
            if (existingContent != null) {
                final JComponent existingComponent = existingContent.getComponent();

                if (existingComponent instanceof SimpleToolWindowPanel) {
                    JComponent component = ((SimpleToolWindowPanel) existingComponent).getContent();
                    if (component instanceof RNConsoleImpl) {
                        RNConsoleImpl rnConsole = (RNConsoleImpl) component;
                        return rnConsole;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Create a console panel
     *
     * @param toolWindow
     * @return
     */
    private Content createConsoleTabContent(@NotNull final ToolWindow toolWindow, boolean firstInit,
                                            String displayName, Icon icon) {
        final ContentManager contentManager = toolWindow.getContentManager();
        final Content existingContent = contentManager.findContent(displayName);
        if (existingContent != null) {
            contentManager.setSelectedContent(existingContent);
            return existingContent;
        }

        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(true);
        ContentImpl content = new ContentImpl(panel, displayName, true);

        content.setCloseable(true);
        RNConsoleImpl consoleView = new RNConsoleImpl(myProject, true);
        consoleView.setDisplayName(displayName);
        content.setDisposer(consoleView);

        if (icon != null) {
            content.setIcon(icon);
            content.setPopupIcon(icon);
            content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);// Set to show tab icon
        }

        if (firstInit) {
            content.setCloseable(false);
            content.setDisplayName("Welcome");
            content.setDescription("");
            content.setIcon(PluginIcons.React);
            content.setPopupIcon(PluginIcons.React);
            content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
            consoleView.print(
                    "Welcome to React Native Console, now please click one button on top toolbar to start." +
                            "\n\n" +
                            "WARNING: if click one button for twice, then the console will be reused and\n" +
                            "the first running process will be terminated automatically then run the command again.\n",
                    ConsoleViewContentType.SYSTEM_OUTPUT);
            consoleView.print(
                    "Click here for more info and issue, suggestion:\n",
                    ConsoleViewContentType.NORMAL_OUTPUT);
            consoleView.printHyperlink("https://github.com/beansoftapp/react-native-console",
                    new BrowserHyperlinkInfo("https://github.com/beansoftapp/react-native-console"));
        }

        panel.setContent(consoleView.getComponent());
        panel.addFocusListener(this);

//        createToolbar(terminalRunner, myTerminalWidget, toolWindow, panel);// west toolbar

//        ActionToolbar toolbar = createTopToolbar(terminalRunner, myTerminalWidget, toolWindow);
//        toolbar.setTargetComponent(panel);
//        panel.setToolbar(toolbar.getComponent(), false);


        // welcome page don't show console action buttons
        if (!firstInit) {
            // Create left console and normal toolbars
            DefaultActionGroup toolbarActions = new DefaultActionGroup();
            AnAction[]
                    consoleActions = consoleView.createConsoleActions();// 必须在 consoleView.getComponent() 调用后组件真正初始化之后调用

            // Rerun current command
            toolbarActions.add(consoleView.getReRunAction());
            toolbarActions.addSeparator();
            // Stop and close tab
            StopProcessAction stopProcessAction = new StopProcessAction("Stop process", "Stop process", null);
            consoleView.setStopProcessAction(stopProcessAction);
            toolbarActions.add(stopProcessAction);

            content.setManager(toolWindow.getContentManager());
            toolbarActions.add(new CloseTabAction(content));
            toolbarActions.addSeparator();
            // Built in console action
            toolbarActions.addAll((AnAction[]) Arrays.copyOf(consoleActions, consoleActions.length));
            ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("unknown", (ActionGroup) toolbarActions, false);
            toolbar.setTargetComponent(consoleView.getComponent());
            panel.setToolbar(toolbar.getComponent(), true);
        }

        // top toolbars
        DefaultActionGroup group = new DefaultActionGroup();
        ActionToolbar toolbarNorth = ActionManager.getInstance().createActionToolbar("unknown", (ActionGroup) group, true);
        toolbarNorth.setTargetComponent(consoleView.getComponent());
        panel.setToolbar(toolbarNorth.getComponent(), false);

        group.add(new HelpAction(this));

        // Android
        group.addSeparator();
        group.add(new AndroidDevMenuAction(this));
        group.add(new AndroidRefreshAction(this));
        group.add(new AdbForwardAction(this));
        group.add(new NPMAndroidLogsAction(this));
        group.add(new RunAndroidAction(this));
        group.add(new AndroidReleaseApkAction(this));
        group.add(new AndroidDebugApkAction(this));
        group.add(new AndroidBundleAction(this));


        // NPM, yarn and test
        group.addSeparator();
        group.add(new NPMStartAction(this));
        group.add(new NPMInstallAction(this));
        group.add(new RNLinkAction(this));
        group.add(new YarnAction(this));
        group.add(new JestAction(this));
//        group.add(new ReWatchManAction(this));// TODO in next version

        if (OSUtils.isMacOSX() || OSUtils.isMacOS()) {// Only show on Mac OS
            // iOS
            group.addSeparator();

            group.add(new RunIOSAction(this));
            group.add(new NPMiOSLogsAction(this));
            group.add(new IOSBundleAction(this));
            group.add(new RunIOSDeviceAction(this));
            group.add(new RunIOSDevicesAction(this));
            group.add(new LocateInFinderAction(this));
        }

        // General
        group.addSeparator();
        group.add(new DebugUiAction(this));

        content.setPreferredFocusableComponent(consoleView.getComponent());

        toolWindow.getContentManager().addContent(content);
        contentManager.setSelectedContent(content);
//        consoleView.runGradleCI(myProject, "." + File.separator + "gradlew assembleDebug --configure-on-demand");
        return content;
    }

    @Override
    public void focusGained(FocusEvent e) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow(RNToolWindowFactory.TOOL_WINDOW_ID);
        if (toolWindow != null) {
            try {
                ContentManager contentManager = toolWindow.getContentManager();
                JComponent component = contentManager.getSelectedContent().getComponent();
                if (component != null) {
                    component.requestFocusInWindow();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "ReactNativeConsole";
    }

    /**
     * Open help message.
     */
    private static class HelpAction extends BaseRNConsoleAction {
        public HelpAction(ReactNativeConsole terminal) {
            super(terminal, "Help", "Show RN Console docs online", PluginIcons.Help);
        }

        @Override
        public void doAction(AnActionEvent anActionEvent) {
            Utils.openUrl("https://github.com/beansoftapp/react-native-console");
        }
    }

    private static class AndroidReleaseApkAction extends BaseRNConsoleAndroidAction {
        public AndroidReleaseApkAction(ReactNativeConsole terminal) {
            super(terminal, "Release APK", "Generate Release APK file", PluginIcons.Archive);
        }

        protected String command() {
            if (OSUtils.isWindows()) {// https://github.com/beansoftapp/react-native-console/issues/8
                return "gradlew.bat assembleRelease";
            }
            return "." + File.separator + "gradlew assembleRelease";
        }
    }

    private static class AndroidDebugApkAction extends BaseRNConsoleAndroidAction {
        public AndroidDebugApkAction(ReactNativeConsole terminal) {
            super(terminal, "Debug APK", "Generate Debug APK file", PluginIcons.StartDebugger);
        }

        protected String command() {
            if (OSUtils.isWindows()) {// https://github.com/beansoftapp/react-native-console/issues/8
                return "gradlew.bat assembleDebug";
            }
            return "." + File.separator + "gradlew assembleDebug";
        }
    }

    /**
     * 物理设备, 转发请求.
     * If you're on a physical device connected to the same machine,
     * run 'adb reverse tcp:8081 tcp:8081' to forward requests from your device
     */
    private static class AdbForwardAction extends BaseRNConsoleRunAction {
        public AdbForwardAction(ReactNativeConsole terminal) {
            super(terminal, "Forward Android Request",
                    "forward Android device request to this machine", PluginIcons.Link);
        }

        @Override
        protected String command() {
            return "adb reverse tcp:8081 tcp:8081";
        }
    }

    private static class AndroidBundleAction extends BaseRNConsoleNPMAction {
        public AndroidBundleAction(ReactNativeConsole terminal) {
            super(terminal, "Android RN Bundle",
                    "Create Release React Native Bundle File for Android ", PluginIcons.Deploy);
        }

        public boolean beforeAction() {

            String npmLocation = RNPathUtil.getRNProjectPath(getProject());

            if (npmLocation == null) {
                NotificationUtils.packageJsonNotFound();
            } else {
                try {
                    File f = new File(npmLocation, "android/app/src/main/assets/index.android.bundle");
                    if (f.exists())
                        f.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

        protected String command() {
            return "react-native bundle --platform android --entry-file index.android.js --reset-cache --bundle-output android/app/src/main/assets/index.android.bundle --dev false --assets-dest android/app/src/main/res/";
//            return "react-native bundle --platform android --dev false --entry-file index.android.js --bundle-output ./bundle-android/index.android.bundle --assets-dest ./bundle-android";
        }
    }

    private static class RunAndroidAction extends BaseRNConsoleNPMAction {
        public RunAndroidAction(ReactNativeConsole terminal) {
            super(terminal, "Debug Android", "react-native run-android", PluginIcons.Android);
        }

        protected String command() {
            return "react-native run-android";
        }
    }

    private static class IOSBundleAction extends BaseRNConsoleNPMAction {
        public IOSBundleAction(ReactNativeConsole terminal) {
            super(terminal, "iOS RN Bundle",
                    "Create Release React Native Bundle File for iOS", PluginIcons.Deploy);
        }

        public boolean beforeAction() {
            String npmLocation = RNPathUtil.getRNProjectPath(getProject());

            if (npmLocation == null) {
                NotificationUtils.packageJsonNotFound();
            } else {
                try {
                    File dir = new File(npmLocation, "ios/bundle");
                    if (!dir.exists())
                        dir.mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return true;
        }

        protected String command() {
            return "react-native bundle --platform ios --entry-file index.ios.js --reset-cache --bundle-output ios/bundle/main.jsbundle --dev false --assets-dest ios/bundle/";
        }
    }

    private static class RunIOSAction extends BaseRNConsoleNPMAction {
        public RunIOSAction(ReactNativeConsole terminal) {
            super(terminal, "iOS Run Simulator", "Run on Default iOS Simulator", PluginIcons.IPhoneSimulator);
        }

        protected String command() {
            return "react-native run-ios";
        }
    }

    // Show NPM project in finder/explorer
    private static class LocateInFinderAction extends BaseRNConsoleAction {
        public LocateInFinderAction(ReactNativeConsole terminal) {
            super(terminal, getActionName(), getActionName(), PluginIcons.Folder);
        }

        public void doAction(AnActionEvent anActionEvent) {
            String npmLocation = RNPathUtil.getRNProjectPath(getProject());

            if (npmLocation != null) {
                ShowFilePathAction.openFile(new File(npmLocation + File.separatorChar + "package.json"));
            }
        }

//        @Override
//        public void update(AnActionEvent e) {
//            String npmLocation = RNPathUtil.getRNProjectPath(getProject());
//            Presentation presentation = e.getPresentation();
//            presentation.setText(getActionName());
//            presentation.setEnabled(npmLocation != null);
//        }

        @NotNull
        public static String getActionName() {
            return SystemInfo.isMac ? "Reveal Project in Finder" : "Show Project in " + ShowFilePathAction.getFileManagerName();
        }
    }

    private static class RunIOSDeviceAction extends BaseRNConsoleNPMAction {
        public RunIOSDeviceAction(ReactNativeConsole terminal) {
            super(terminal, "iOS Run Device", "Run on Physical iOS Device", PluginIcons.IPhoneDevice);
        }

        public boolean beforeAction() {
            RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());
            if (consoleView != null) {
                consoleView.print(
                        "If you were first running this command, make sure you have ios-deploy installed globally.\n" +
                                "To install, please run in terminal with command: \n" +
                                "npm install -g ios-deploy\n" +
                                "And now please connect your iPhone to USB and enable developer mode.\n\n",
                        ConsoleViewContentType.SYSTEM_OUTPUT);
            }
            return true;
        }

        protected String command() {
            return "react-native run-ios --device";
        }
    }

    /** Show all iso devices, includes simuators, and let the user choose one item to run */
    private static class RunIOSDevicesAction extends BaseRNConsoleNPMAction {
        public RunIOSDevicesAction(ReactNativeConsole terminal) {
            super(terminal, "iOS Choose Devices", "Run on a Selected iOS Device", PluginIcons.IPhoneDevices);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            // Running with background task and with a progress indicator
            ProgressManager.getInstance().run(new Task.Backgroundable(getProject(), "Reading iOS devices list", false) {
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
                            .createActionPopupMenu(ToolWindowContentUi.POPUP_PLACE, defaultActionGroup,
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
                            RNConsoleImpl consoleView = terminal.getRNConsole(getText(), getIcon());
                            consoleView.runRawNPMCI(
                                    RNPathUtil.getExecuteFullPathSingle("react-native"),
                                    "run-ios",
                                    iosDeviceInfo.simulator ? "--simulator" : "--device",
                                    iosDeviceInfo.name);
                        }
                    });
                }
            });


            return group;
        }

        @Override
        protected String command() {
            return null;
        }
    }

    private static class RNLinkAction extends BaseRNConsoleNPMAction {
        public RNLinkAction(ReactNativeConsole terminal) {
            super(terminal, "RN link", "react-native link", PluginIcons.Lightning);
        }

        protected String command() {
            return "react-native link";
        }
    }

    private static class YarnAction extends BaseRNConsoleNPMAction {
        public YarnAction(ReactNativeConsole terminal) {
            super(terminal, "yarn", "yarn", PluginIcons.yarn);
        }

        protected String command() {
            return "yarn";
        }
    }

    private static class JestAction extends BaseRNConsoleNPMAction {
        public JestAction(ReactNativeConsole terminal) {
            super(terminal, "npm test", "run Jest test", PluginIcons.Jest);
        }

        protected String command() {
            return "npm test";
        }
    }

    private static class ReWatchManAction extends BaseRNConsoleNPMAction {
        public ReWatchManAction(ReactNativeConsole terminal) {
            super(terminal, "re-watch project", "re-watch", PluginIcons.Jest);
        }

//        @Override
//        public void doAction(AnActionEvent anActionEvent) {
//            beforeAction();
//            terminal.runNPMCI(command(), getText(), getIcon());
//            afterAction();
//        }

        protected String command() {
            return "watchman watch-del .;watchman watch-project .";
        }
    }

    // NPM Run start Task
    private static class NPMStartAction extends BaseRNConsoleNPMAction {
        public NPMStartAction(ReactNativeConsole terminal) {
            super(terminal, "React Packager", "start node server(npm run start)", PluginIcons.Execute);
        }

        protected String command() {
            return "npm run start";
        }
    }

    // NPM Run start Task
    private static class NPMInstallAction extends BaseRNConsoleNPMAction {
        public NPMInstallAction(ReactNativeConsole terminal) {
            super(terminal, "npm install", "npm install", PluginIcons.Install);
        }

        protected String command() {
            return "npm install";
        }
    }

    // view android log
    private static class NPMAndroidLogsAction extends BaseRNConsoleNPMAction {
        public NPMAndroidLogsAction(ReactNativeConsole terminal) {
            super(terminal, "log-android", "react-native log-android", PluginIcons.TrackTests);
        }

        protected String command() {
            return "adb logcat *:S ReactNative:V ReactNativeJS:V";
        }
    }

    // view ios log, we have GUI version: /Applications/Utilities/Console.app/Contents/MacOS/Console /var/log/system.log or
    // syslog -w 100(see: https://developer.apple.com/legacy/library/documentation/Darwin/Reference/ManPages/man1/syslog.1.html
    // and https://discussions.apple.com/thread/2285049?start=0&tstart=0, and the source code of RN logIOS.js shows:
    // syslog -w -F std '-d', logDir(logDir is the active device dir var: xcrun simctl list devices --json, the status should be:
    // device.availability === '(available)' && device.state === 'Booted', so the final solution is just using RN built-in command
    private static class NPMiOSLogsAction extends BaseRNConsoleNPMAction {
        public NPMiOSLogsAction(ReactNativeConsole terminal) {
            super(terminal, "log-ios", "react-native log-ios", PluginIcons.TrackTests);
        }

        protected String command() {
            return "react-native log-ios";
        }
    }

    private static class AndroidDevMenuAction extends BaseRNConsoleRunAction {
        public AndroidDevMenuAction(ReactNativeConsole terminal) {
            super(terminal, "Android Dev Menu", "Open Android Dev Menu", PluginIcons.DevMenu);
        }

        @Override
        protected String command() {
            return "adb shell input keyevent 82";
        }
    }

    /**
     * Reloading JavaScript on Android device, tested on Samsung and MOTO XStyle only.
     *
     * @since 1.0.6
     */
    private static class AndroidRefreshAction extends BaseRNConsoleRunAction {
        public AndroidRefreshAction(ReactNativeConsole terminal) {
            super(terminal, "Android Reload JS", "Android Reloading JavaScript(beta)", AllIcons.Actions.Refresh);
        }

        @Override
        protected String command() {
            return "adb shell input keyevent 82 20 66 66";//First toggle menu, then press down key to select first menu item
            // - Relead, final press enter will execute the action
        }
    }

    private static class DebugUiAction extends BaseRNConsoleAction {
        public DebugUiAction(ReactNativeConsole terminal) {
            super(terminal, "open debugger-ui", "open debugger-ui", PluginIcons.OpenChromeDebugger);
        }

        public void doAction(AnActionEvent anActionEvent) {
            String url = Messages.showInputDialog(anActionEvent.getData(PlatformDataKeys.PROJECT),
                    "input url",
                    "open debugger-ui",
                    new ImageIcon(anActionEvent.getData(PlatformDataKeys.PROJECT) + "/resources/icons/chrome.png"),
                    "http://localhost:8081/debugger-ui",
                    new InputValidator() {
                        @Override
                        public boolean checkInput(String url) {
                            Pattern pattern = Pattern
                                    .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");

                            return pattern.matcher(url).matches();
                        }

                        @Override
                        public boolean canClose(String s) {
                            return true;
                        }
                    });

            Utils.openUrl(url);
        }
    }
}
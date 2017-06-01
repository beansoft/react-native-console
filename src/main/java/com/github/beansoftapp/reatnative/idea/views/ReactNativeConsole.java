package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.actions.*;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.OSUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.Utils;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.filters.BrowserHyperlinkInfo;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.impl.ContentImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

    public void initTerminal(final ToolWindow toolWindow) {
        toolWindow.setToHideOnEmptyContent(true);
        toolWindow.setStripeTitle("RN Console");
        toolWindow.setIcon(PluginIcons.React);
        Content content = createConsoleTabContent(toolWindow, true, "Welcome", null);
//        toolWindow.getContentManager().addContent(content);
//        toolWindow.getContentManager().addContent(new ContentImpl(new JButton("Test"), "Build2", false));
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
     * @param shell
     */
    public void executeShell(String shell, String workDirectory, String displayName, Icon icon) {
        RNConsoleImpl rnConsole = getRNConsole(displayName, icon);
        if(rnConsole != null) {
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
        if(rnConsole != null) {
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
        if(rnConsole != null) {
            rnConsole.runNPMCI(shell);
        }
    }

    /**
     * 获取 RN Console实例.
     * @param displayName
     * @param icon
     * @return
     */
    public RNConsoleImpl getRNConsole(String displayName, Icon icon) {
        ToolWindow window = ToolWindowManager.getInstance(myProject).getToolWindow(RNToolWindowFactory.TOOL_WINDOW_ID);
        if (window != null) {
            Content existingContent = createConsoleTabContent(window, false, displayName, icon);
            if(existingContent != null) {
                final JComponent existingComponent = existingContent.getComponent();

                if(existingComponent instanceof  SimpleToolWindowPanel) {
                    JComponent component = ((SimpleToolWindowPanel)existingComponent).getContent();
                    if(component instanceof  RNConsoleImpl) {
                        RNConsoleImpl rnConsole = (RNConsoleImpl)component;
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

        if(icon != null) {
            content.setIcon(icon);
            content.setPopupIcon(icon);
            content.putUserData(ToolWindow.SHOW_CONTENT_ICON, Boolean.TRUE);
        }

        if(firstInit) {
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
        if(!firstInit) {
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
            toolbarActions.add(stopProcessAction );

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


        // NPM
        group.addSeparator();
        group.add(new NPMStartAction(this));
        group.add(new NPMInstallAction(this));

        if(OSUtils.isMacOSX() || OSUtils.isMacOS()) {// Only show on Mac OS
            // iOS
            group.addSeparator();
            group.add(new RunLinkAction(this));
            group.add(new RunIOSAction(this));
            group.add(new NPMiOSLogsAction(this));
            group.add(new IOSBundleAction(this));
        }

        // General
        group.addSeparator();
        group.add(new DebugUiAction(this));
        // TODO npm init, jest

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
    public void focusLost(FocusEvent e) {}

    @Override
    public void projectOpened() {}

    @Override
    public void projectClosed() {}

    @Override
    public void initComponent() {}

    @Override
    public void disposeComponent() {}

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

        public void beforeAction() {
            String path = getProject().getBasePath();
            String npmLocation = RNPathUtil.getRNProjectPath(getProject(), path);

            if (npmLocation == null) {
                NotificationUtils.packageJsonNotFound();
            } else {
                try {
                    File dir = new File(npmLocation, "bundle-android");
                    if(!dir.exists())
                        dir.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected String command() {
            return "react-native bundle --platform android --dev false --entry-file index.android.js --bundle-output ./bundle-android/index.android.bundle --assets-dest ./bundle-android";
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

        public void beforeAction() {
            String path = getProject().getBasePath();
            String npmLocation = RNPathUtil.getRNProjectPath(getProject(), path);

            if (npmLocation == null) {
                NotificationUtils.packageJsonNotFound();
            } else {
                try {
                    File dir = new File(npmLocation, "bundle-ios");
                    if(!dir.exists())
                        dir.mkdir();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        protected String command() {
            return "react-native bundle --platform ios --dev false --entry-file index.ios.js --bundle-output ./bundle-ios/index.ios.bundle --assets-dest ./bundle-ios";
        }
    }

    private static class RunIOSAction extends BaseRNConsoleNPMAction {
        public RunIOSAction(ReactNativeConsole terminal) {
            super(terminal, "Debug iOS", "react-native run-ios", PluginIcons.IPhone);
        }

        protected String command() {
            return "react-native run-ios";
        }
    }

    private static class RunLinkAction extends BaseRNConsoleNPMAction {
        public RunLinkAction(ReactNativeConsole terminal) {
            super(terminal, "RN link", "react-native link", PluginIcons.Lightning);
        }

        protected String command() {
            return "react-native link";
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
package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleRunAction;
import com.github.beansoftapp.reatnative.idea.actions.CloseTabAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.Utils;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.filters.BrowserHyperlinkInfo;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
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
                    "Welcome to React Native Console, now please click one button on top toolbar to start.\n\n" +
                            "WARNING: if click one button for twice, " +
                            "then the console will be reused and first running process will be terminated automatically then run the command again.",
                    ConsoleViewContentType.SYSTEM_OUTPUT);
            consoleView.print(
                    "Click here for more info and issue, suggestion: ",
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

        // Create toolbars
        DefaultActionGroup toolbarActions = new DefaultActionGroup();
        AnAction[]
                consoleActions = consoleView.createConsoleActions();// 必须在 consoleView.getComponent() 调用后组件真正初始化之后调用

        StopProcessAction stopProcessAction = new StopProcessAction("Stop process", "Stop process", null);
        consoleView.setStopProcessAction(stopProcessAction);
        toolbarActions.add(stopProcessAction );

        content.setManager(toolWindow.getContentManager());
        toolbarActions.add(new CloseTabAction(content));
        toolbarActions.addSeparator();
        toolbarActions.addAll((AnAction[]) Arrays.copyOf(consoleActions, consoleActions.length));

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("unknown", (ActionGroup) toolbarActions, false);
        toolbar.setTargetComponent(consoleView.getComponent());
        panel.setToolbar(toolbar.getComponent(), true);

        // top toolbars
        DefaultActionGroup toolbarActionsTop = new DefaultActionGroup();
        ActionToolbar toolbarNorth = ActionManager.getInstance().createActionToolbar("unknown", (ActionGroup) toolbarActionsTop, true);
        toolbar.setTargetComponent(consoleView.getComponent());
        panel.setToolbar(toolbarNorth.getComponent(), false);

        // Android
        toolbarActionsTop.add(new HelpAction(this));
        toolbarActionsTop.addSeparator();
        toolbarActionsTop.add(new AdbForwardAction(this));
        toolbarActionsTop.add(new AndroidBundleAction(this));
        toolbarActionsTop.add(new IOSBundleAction(this));

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

}

package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.actions.CloseTabAction;
import com.github.beansoftapp.reatnative.idea.actions.console.*;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.OSUtils;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.filters.BrowserHyperlinkInfo;
import com.intellij.execution.filters.HyperlinkInfoBase;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.editor.actions.ScrollToTheEndToolbarAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.content.impl.ContentImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.AbstractTerminalRunner;
import org.jetbrains.plugins.terminal.JBTabbedTerminalWidget;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * A React Native Console with console view as process runner, no more depends on terminal widget,
 * thus tabs could be reused.
 * Created by beansoft@126.com on 2017/4/27.
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

    public void initAndActiveRunRefresh(InputEvent e) {
        ToolWindow toolWindow = getToolWindow();
        if (!toolWindow.isActive()) {
            toolWindow.activate(new Runnable() {
                @Override
                public void run() {
                    ActionManager.getInstance().tryToExecute(new AndroidRefreshAction(ReactNativeConsole.this), e, null, ActionPlaces.UNKNOWN, true);
                }
            });
        } else {
            ActionManager.getInstance().tryToExecute(new AndroidRefreshAction(this), e, null, ActionPlaces.UNKNOWN, true);
        }
    }

    /**
     * @deprecated
     * Create a terminal panel, for test purpose only.
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

    /**
     * Init and show this console.
     * @param toolWindow
     */
    public void init(final ToolWindow toolWindow) {
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
                        init(window);
                    }
                }
            }
        });
        toolWindow.show(null);
    }

    /**
     * Execute some shell and get outputs
     *
     * @param shell the command
     * @param displayName the name to display on tab
     * @param icon the icon on tab
     */
    public void executeShell(String shell, String workDirectory, String displayName, Icon icon) {
        RNConsoleImpl rnConsole = getRNConsole(displayName, icon);
        if (rnConsole != null) {
            rnConsole.executeShell(shell, workDirectory);
        }
    }

    /**
     * Run gradle commands.
     *
     * @param shell the gradle command
     * @param displayName the name to display on tab
     * @param icon the icon on tab
     */
    public void runGradleCI(String shell, String displayName, Icon icon) {
        RNConsoleImpl rnConsole = getRNConsole(displayName, icon);
        if (rnConsole != null) {
            rnConsole.runGradleCI(shell);
        }
    }

    /**
     * Run npm commands.
     *
     * @param shell the gradle command
     * @param displayName the name to display on tab
     * @param icon the icon on tab
     */
    public void runNPMCI(String shell, String displayName, Icon icon) {
        RNConsoleImpl rnConsole = getRNConsole(displayName, icon);
        if (rnConsole != null) {
            rnConsole.runNPMCI(shell);
        }
    }

    /**
     * Get the RN Console instance.
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
                    "Welcome to React Native Console, now please click one button on top toolbar to start.\n",
                    ConsoleViewContentType.SYSTEM_OUTPUT);
            consoleView.print(
                    "Click here for more info and issue, suggestion:\n",
                    ConsoleViewContentType.NORMAL_OUTPUT);
            consoleView.printHyperlink("https://github.com/beansoftapp/react-native-console",
                    new BrowserHyperlinkInfo("https://github.com/beansoftapp/react-native-console"));

            consoleView.print(
                    "\n\nJs project working directory is not root directory? ",
                    ConsoleViewContentType.NORMAL_OUTPUT);
            consoleView.printHyperlink("CLICK HERE to EDIT",
                    new HyperlinkInfoBase() {
                        @Override
                        public void navigate(@NotNull Project project, @Nullable RelativePoint relativePoint) {
                            EditJsAppPathAction.doEditJsProjectPath(project);
                        }
                    });

            consoleView.print(
                    "\n\nModify metro port in React Native 0.56+? ",
                    ConsoleViewContentType.NORMAL_OUTPUT);
            consoleView.printHyperlink("CLICK HERE to VIEW/EDIT",
                    new HyperlinkInfoBase() {
                        @Override
                        public void navigate(@NotNull Project project, @Nullable RelativePoint relativePoint) {
                            EditMetroPortAction.doEditPort(project);
                        }
                    });

            if(SystemInfoRt.isLinux) {
                consoleView.print(
                        "\n\n===========Linux Users PLEASE README FIRST ===========\nIf you found issue when click " +
                                "on the \"Debug Android\" button, error message: \n" +
                                " \"SDK location not found \", please fix it like this :\n" +
                                "add a android local config file:\n" +
                                "yourapp/android/local.properties\n" +
                                "sdk.dir=/Users/xxxx/Documents/Java/android-sdk-macosx\n" +
                                "let sdk.dir point to your ANDROID_HOME environment \n" +
                                "if can't find adb, try this shell command:\n" +
                                "sudo ln -s ~/Android/Sdk/platform-tools/adb /usr/bin/adb\n" +
                                "More info please ref this issue:\n",
                        ConsoleViewContentType.LOG_WARNING_OUTPUT);
                consoleView.printHyperlink("https://github.com/beansoftapp/react-native-console/issues/17",
                        new BrowserHyperlinkInfo("https://github.com/beansoftapp/react-native-console/issues/17"));
            }
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
                    consoleActions = consoleView.createConsoleActions();
            // createConsoleActions() Must be called after consoleView.getComponent() was invoked, after the component really inited, otherwise will got NPE

            // resort console actions to move scroll to end and clear to top
            List<AnAction> resortActions = new ArrayList<>();
            if(consoleActions != null) {
                for (AnAction action : consoleActions) {
                    if (action instanceof ScrollToTheEndToolbarAction || action instanceof ConsoleViewImpl.ClearAllAction) {
                        resortActions.add(action);
                    }
                }

                for (AnAction action : consoleActions) {
                    if (!(action instanceof ScrollToTheEndToolbarAction || action instanceof ConsoleViewImpl.ClearAllAction)) {
                        resortActions.add(action);
                    }
                }
            }

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
            toolbarActions.addAll(resortActions.toArray(new AnAction[0]));
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
        group.add(new EditJsAppPathAction(this));
        group.addSeparator();

        group.add(new AndroidDevMenuAction(this));
        group.add(new AndroidRefreshAction(this));
        group.add(new AdbForwardAction(this));
        group.add(new NPMAndroidLogsAction(this));
        group.add(new RunAndroidAction(this));
        group.add(new AndroidReleaseApkAction(this));
        group.add(new AndroidDebugApkAction(this));
        group.add(new AndroidBundleAction(this));
        group.add(new AndroidCleanAction(this));

        // NPM, yarn and test
        group.addSeparator();
        group.add(new RNStartAction(this));
        group.add(new NPMStartAction(this));
        group.add(new NPMInstallAction(this));
        group.add(new RunRNScriptsAction(this));

        group.add(new YarnAction(this));
        group.add(new JestAction(this));
//        group.add(new ReWatchManAction(this));// TODO in next version

        group.add(new RunNPMScriptsAction(this));

        if (OSUtils.isMacOSX() || OSUtils.isMacOS()) {// Only show on Mac OS
            // iOS
            group.addSeparator();

            group.add(new RunIOSAction(this));
            group.add(new RunRNDebuggerAction(this));
            group.add(new NPMiOSLogsAction(this));
            group.add(new IOSBundleAction(this));
            group.add(new RunIOSDeviceAction(this));
            group.add(new RunIOSDevicesAction(this));
        }

        // General
        group.addSeparator();
        group.add(new LocateInFinderAction(this));
        group.add(new DebugUiAction(this));
        group.add(new ReactDevToolsAction(this));
        group.add(new RunMiscScriptsAction(this));

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

    /* ========= below 4 methods must be keeped otherwise old version IDEA 2016.3 will crash
    see: https://github.com/beansoftapp/react-native-console/issues/26
    ERROR - ij.components.ComponentManager - com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole.disposeComponent()V
java.lang.AbstractMethodError: com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole.disposeComponent()V
     */

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

    // ========== end old version support

    @NotNull
    @Override
    public String getComponentName() {
        return "ReactNativeConsole";
    }

}
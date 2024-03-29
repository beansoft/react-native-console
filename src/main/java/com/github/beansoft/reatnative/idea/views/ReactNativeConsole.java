package com.github.beansoft.reatnative.idea.views;

import com.github.beansoft.reatnative.idea.actions.CloseTabAction;
import com.github.beansoft.reatnative.idea.actions.console.AdbForwardAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidAvdsAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidBundleAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidCleanAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidDebugApkAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidDevMenuAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidRefreshAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidReleaseApkAction;
import com.github.beansoft.reatnative.idea.actions.console.AndroidReleaseOfflineApkAction;
import com.github.beansoft.reatnative.idea.actions.console.DebugUiAction;
import com.github.beansoft.reatnative.idea.actions.console.EditJsAppPathAction;
import com.github.beansoft.reatnative.idea.actions.console.EditMetroPortAction;
import com.github.beansoft.reatnative.idea.actions.console.HelpAction;
import com.github.beansoft.reatnative.idea.actions.console.IOSBundleAction;
import com.github.beansoft.reatnative.idea.actions.console.JestAction;
import com.github.beansoft.reatnative.idea.actions.console.LocateInFinderAction;
import com.github.beansoft.reatnative.idea.actions.console.NPMAndroidLogsAction;
import com.github.beansoft.reatnative.idea.actions.console.NPMInstallAction;
import com.github.beansoft.reatnative.idea.actions.console.NPMStartAction;
import com.github.beansoft.reatnative.idea.actions.console.NPMiOSLogsAction;
import com.github.beansoft.reatnative.idea.actions.console.RNStartAction;
import com.github.beansoft.reatnative.idea.actions.console.ReactDevToolsAction;
import com.github.beansoft.reatnative.idea.actions.console.RunAndroidAction;
import com.github.beansoft.reatnative.idea.actions.console.RunAndroidDevicesAction;
import com.github.beansoft.reatnative.idea.actions.console.RunCocoPodsAction;
import com.github.beansoft.reatnative.idea.actions.console.RunIOSAction;
import com.github.beansoft.reatnative.idea.actions.console.RunIOSDeviceAction;
import com.github.beansoft.reatnative.idea.actions.console.RunIOSDevicesAction;
import com.github.beansoft.reatnative.idea.actions.console.RunMiscScriptsAction;
import com.github.beansoft.reatnative.idea.actions.console.RunNPMScriptsAction;
import com.github.beansoft.reatnative.idea.actions.console.RunRNDebuggerAction;
import com.github.beansoft.reatnative.idea.actions.console.RunRNScriptsAction;
import com.github.beansoft.reatnative.idea.actions.console.YarnAction;
import com.github.beansoft.reatnative.idea.actions.console.android.RevealApkAction;
import com.github.beansoft.reatnative.idea.actions.console.hyperlink.EditRunAnroidOptions;
import com.github.beansoft.reatnative.idea.actions.console.hyperlink.EditRuniOSOptions;
import com.github.beansoft.reatnative.idea.actions.console.java.OpenCurrentActivityAction;
import com.github.beansoft.reatnative.idea.content.RNContentImpl;
import com.github.beansoft.reatnative.idea.icons.PluginIcons;
import com.github.beansoft.reatnative.idea.ui.RNConsole;
import com.github.beansoft.reatnative.idea.utils.OSUtils;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.filters.BrowserHyperlinkInfo;
import com.intellij.execution.filters.HyperlinkInfoBase;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfoRt;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;

/**
 * A React Native Console with console view as process runner, no more depends on terminal widget,
 * thus tabs could be reused.
 * Created by beansoft@126.com on 2017/4/27.
 */
public class ReactNativeConsole implements FocusListener {
    private Project myProject;
    private static ReactNativeConsole instance = null;

    public static ReactNativeConsole getInstance(Project project) {
//        return project.getComponent(ReactNativeConsole.class);
        if (instance == null) {
            instance = new ReactNativeConsole(project);
        }

        // Open another project in same window @since 2020.1.2
        if (instance.myProject != project) {
            instance = new ReactNativeConsole(project);
        }
        return instance;
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
            toolWindow.activate(() -> ActionManager.getInstance().tryToExecute(new AndroidRefreshAction(ReactNativeConsole.this), e, null, ActionPlaces.UNKNOWN, true));
        } else {
            ActionManager.getInstance().tryToExecute(new AndroidRefreshAction(this), e, null, ActionPlaces.UNKNOWN, true);
        }
    }


    /**
     * Init and show this console pane.
     * @param toolWindow
     */
    public void init(final ToolWindow toolWindow) {
        toolWindow.setToHideOnEmptyContent(true);
        toolWindow.setStripeTitle("RN Console Free");
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
        // TODO Change to Eventbus
        myProject.getMessageBus().connect().subscribe(ToolWindowManagerListener.TOPIC, new ToolWindowManagerListener() {
//        ((ToolWindowManagerEx) ToolWindowManager.getInstance(this.myProject)).addToolWindowManagerListener(new ToolWindowManagerListener() {
            @Override
            public void toolWindowRegistered(@NotNull String s) {
            }

            @Override
            public void stateChanged(@NotNull ToolWindowManager toolWindowManager) {
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
        RNConsole rnConsole = getRNConsole(displayName, icon);
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
        RNConsole rnConsole = getRNConsole(displayName, icon);
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
        RNConsole rnConsole = getRNConsole(displayName, icon);
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
    public RNConsole getRNConsole(String displayName, Icon icon) {
        ToolWindow window = ToolWindowManager.getInstance(myProject).getToolWindow(RNToolWindowFactory.TOOL_WINDOW_ID);
        if (window != null) {
            Content existingContent = createConsoleTabContent(window, false, displayName, icon);
            if (existingContent != null && existingContent instanceof RNContentImpl) {
//                final JComponent existingComponent = existingContent.getComponent();
//                if (existingComponent instanceof SimpleToolWindowPanel) {
//                    JComponent component = ((SimpleToolWindowPanel) existingComponent).getContent();
//                    if (component instanceof RNConsoleImpl) {
//                        RNConsoleImpl rnConsole = (RNConsoleImpl) component;
//                        return rnConsole;
//                    }
//                }
                return ((RNContentImpl)existingContent).getRnConsole();
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

        RNConsole consoleView = new RNConsoleImpl(myProject, false);
        // set viewer to false allow user to interaction with the shell
        consoleView.setDisplayName(displayName);

        RNContentImpl content = new RNContentImpl(panel, displayName, true, consoleView);

        content.setCloseable(true);
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
                    "Welcome to React Native Console Free, now please click one button on top toolbar to start.\n",
                    ConsoleViewContentType.SYSTEM_OUTPUT);
//            consoleView.print(
//                    "Give a Star or Suggestion:\n",
//                    ConsoleViewContentType.NORMAL_OUTPUT);
//            consoleView.printHyperlink("https://github.com/beansoft/react-native-console",
//                    new BrowserHyperlinkInfo("https://github.com/beansoft/react-native-console"));

            consoleView.print(
                    "\nFYI ",
                    ConsoleViewContentType.NORMAL_OUTPUT);
            consoleView.printHyperlink("React Native Console Pro",
                    new BrowserHyperlinkInfo("https://plugins.jetbrains.com/plugin/9564-react-native-console"));
            consoleView.print(
                    " which has many premium features.",
                    ConsoleViewContentType.NORMAL_OUTPUT);

            consoleView.print(
                    "\n\uD83C\uDD95FYI ",
                    ConsoleViewContentType.NORMAL_OUTPUT);
            consoleView.printHyperlink("Notes",
                    new BrowserHyperlinkInfo("https://plugins.jetbrains.com/plugin/17501-notes"));
            consoleView.print(
                    " a code-centric notes/bookmarks manager inside your IDE without altering the source file," +
                            " supports SQLite local storage or sync with Evernote.",
                    ConsoleViewContentType.NORMAL_OUTPUT);

            consoleView.print(
                    "\n\uD83C\uDD95FYI ",
                    ConsoleViewContentType.NORMAL_OUTPUT);
            consoleView.printHyperlink("Flutter Storm",
                    new BrowserHyperlinkInfo("https://plugins.jetbrains.com/plugin/14718-fluterstorm"));
            consoleView.print(
                    " a WebStorm/PhpStorm/GoLand/PyCharm/CLion/RubyMine plugin for developing Flutter applications.",
                    ConsoleViewContentType.NORMAL_OUTPUT);

            consoleView.printHyperlink(
                    "\n\nEdit Js project working directory (Optional, if it's not under root directory)",
                new HyperlinkInfoBase() {
                    @Override
                    public void navigate(@NotNull Project project, @Nullable RelativePoint relativePoint) {
                        EditJsAppPathAction.doEditJsProjectPath(project);
                    }
                });


            consoleView.printHyperlink("\nEdit metro port in React Native 0.56+(Optional, default value 8081)",
                new HyperlinkInfoBase() {
                    @Override
                    public void navigate(@NotNull Project project, @Nullable RelativePoint relativePoint) {
                        EditMetroPortAction.doEditPort(project);
                    }
                });

            consoleView.printHyperlink("\nEdit react-native run-android command options of this project",
                new EditRunAnroidOptions());

            consoleView.printHyperlink("\nEdit react-native run-ios command options of this project",
                new EditRuniOSOptions());

            if(SystemInfoRt.isLinux || SystemInfoRt.isMac) {
                consoleView.print(
                        "\n\n===========Linux Users PLEASE README FIRST ===========\n" +
                            "If you found issue when click " +
                                "on the \"Debug Android\" button, error message: \n" +
                                " \"SDK location not found \", or Task 'installDebug' not found in project ':app',\n" +
                            " please fix it like this :\n" +
                                "add a android local config file:\n" +
                                "yourapp/android/local.properties\n" +
                                "sdk.dir=/Users/xxxx/Documents/Java/android-sdk-macosx\n" +
                                "let sdk.dir point to your ANDROID_HOME environment \n" +
                                "if can't find adb, try this shell command:\n" +
                                "sudo ln -s ~/Android/Sdk/platform-tools/adb /usr/bin/adb\n" +
                            "If can't find avd list, try this shell command(please ensure your emulator path):\n" +
                            "sudo ln -s /Users/my_user/Library/Android/sdk/emulator /usr/bin/emulator\n" +
                                "More info please ref this issue:\n",
                        ConsoleViewContentType.SYSTEM_OUTPUT);
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
//            AnAction[]
//                    consoleActions = consoleView.createConsoleActions();
            // createConsoleActions() Must be called after consoleView.getComponent() was invoked, after the component really inited, otherwise will got NPE

            // resort console actions to move scroll to end and clear to top
//            List<AnAction> resortActions = new ArrayList<>();
//            if(consoleActions != null) {
//                for (AnAction action : consoleActions) {
//                    if (action instanceof ScrollToTheEndToolbarAction || action instanceof ConsoleViewImpl.ClearAllAction) {
//                        resortActions.add(action);
//                    }
//                }
//            }

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
            toolbarActions.addAll(consoleView.createConsoleActions());
            ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("unknown", (ActionGroup) toolbarActions, false);
            toolbar.setTargetComponent(consoleView.getComponent());
            panel.setToolbar(toolbar.getComponent(), true);
        }

        // top toolbars
        DefaultActionGroup group = new DefaultActionGroup();
        ActionToolbar toolbarNorth = ActionManager.getInstance().createActionToolbar("RNConsoleFree", (ActionGroup) group, true);
        toolbarNorth.setTargetComponent(consoleView.getComponent());
        panel.setToolbar(toolbarNorth.getComponent(), false);

        group.add(new HelpAction(this));


        // Config
        group.addSeparator();
        group.add(new EditJsAppPathAction(this));
        group.addSeparator();

      // Android
        group.add(new AndroidDevMenuAction(this));
        group.add(new AndroidRefreshAction(this));
        group.add(new AdbForwardAction(this));
        group.add(new NPMAndroidLogsAction(this));
        group.add(new RunAndroidAction(this));
        group.add(new AndroidReleaseApkAction(this));
        group.add(new AndroidDebugApkAction(this));
        group.add(new AndroidBundleAction(this));
        group.add(new AndroidCleanAction(this));
        group.add(new OpenCurrentActivityAction(this));
        group.add(new RunAndroidDevicesAction(this));
        group.add(new AndroidAvdsAction(this));
        group.add(new RevealApkAction(this));

        group.add(new AndroidReleaseOfflineApkAction(this));


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
            group.add(new RunCocoPodsAction(this));
            group.add(new RunIOSDeviceAction(this));
            group.add(new RunIOSDevicesAction(this));
        }

        // General
        group.addSeparator();
        group.add(new LocateInFinderAction(this));
        group.add(new DebugUiAction(this));
        group.add(new ReactDevToolsAction(this));
        group.add(new RunMiscScriptsAction(this));

        // Gradle
//        group.addSeparator();
//        group.add(new RunGradleTaskAction(this));

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
                component.requestFocusInWindow();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
    }


}

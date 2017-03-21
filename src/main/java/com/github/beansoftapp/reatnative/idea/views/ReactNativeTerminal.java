package com.github.beansoftapp.reatnative.idea.views;

import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.Utils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.jediterm.terminal.Terminal;
import com.jediterm.terminal.model.TerminalTextBuffer;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.TerminalWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.AbstractTerminalRunner;
import org.jetbrains.plugins.terminal.JBTabbedTerminalWidget;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

/**
 * A React Native terminal.
 * Created by beansoft@126.com on 2017/3/15.
 */
public class ReactNativeTerminal implements FocusListener, ProjectComponent {

    private JBTabbedTerminalWidget myTerminalWidget;
    private Project myProject;
    private LocalTerminalDirectRunner myTerminalRunner;

    public ReactNativeTerminal(Project project) {
        this.myProject = project;
    }

    public static ReactNativeTerminal getInstance(Project project) {
        return project.getComponent(ReactNativeTerminal.class);
    }

    public JBTabbedTerminalWidget getTerminalWidget(ToolWindow window) {
        window.show(null);
        if (myTerminalWidget == null) {
            JComponent parentPanel = window.getContentManager().getContents()[0].getComponent();
            if (parentPanel instanceof SimpleToolWindowPanel) {
                SimpleToolWindowPanel panel = (SimpleToolWindowPanel) parentPanel;
                JPanel jPanel = (JPanel) panel.getComponents()[0];
                myTerminalWidget = (JBTabbedTerminalWidget) jPanel.getComponents()[0];
            } else {
                NotificationUtils.infoNotification("Wait for React Native Console to initialize");
            }
        }
        return myTerminalWidget;
    }

    public JBTabbedTerminalWidget getTerminalWidget() {
        ToolWindow window = getToolWindow();
        return getTerminalWidget(window);
    }

    public JediTermWidget getCurrentSession() {
        if (getTerminalWidget() != null) {
            return getTerminalWidget().getCurrentSession();
        }
        return null;
    }

    /**
     * 在terminal输入shell
     */
    private void sendString(String shell) {
        if (getCurrentSession() != null) {
            getCurrentSession().getTerminalStarter().sendString(shell);
        }
    }

    public void initAndExecute(final String[] shell) {
        ToolWindow toolWindow = getToolWindow();
        if (toolWindow.isActive()) {
            executeShell(shell);
        } else {
            toolWindow.activate(new Runnable() {
                @Override
                public void run() {
                    executeShell(shell);
                }
            });
        }
    }

    /**
     * 执行shell
     * 利用terminal换行即执行原理
     *
     * @param shell
     */
    public void executeShell(String shell) {
        if (getCurrentSession() != null) {
            TerminalTextBuffer buffer = getTerminalWidget().getCurrentSession().getTerminalTextBuffer();
            String lastLineText = buffer.getLine(buffer.getScreenLinesCount() - 1).getText().trim();
            shell = shell + " " + Utils.BREAK_LINE;
            if (!lastLineText.endsWith("$") && lastLineText.trim().length() != 0) {
                shell = "#" + Utils.BREAK_LINE + shell;
            }
            sendString(shell);
        }
    }

    /**
     * 执行shell
     *
     * @param shell
     */
    public void executeShell(String[] shell) {
        StringBuilder build = new StringBuilder();
        if (shell != null && shell.length > 0) {
            for (String s : shell) {
                if (s == null) {
                    continue;
                }
                build.append(s + " ");
            }
        }
        executeShell(build.toString());
    }

    public void initTerminal(final ToolWindow toolWindow) {
        toolWindow.setToHideOnEmptyContent(true);
        LocalTerminalDirectRunner terminalRunner = LocalTerminalDirectRunner.createTerminalRunner(myProject);
        myTerminalRunner = terminalRunner;
        toolWindow.setStripeTitle("React Native");
        Content content = createTerminalInContentPanel(terminalRunner, toolWindow);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setShowStripeButton(true);
        toolWindow.setTitle("Console");
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
        JBTabbedTerminalWidget terminalWidget = getTerminalWidget(toolWindow);
        if (terminalWidget != null && terminalWidget.getCurrentSession() != null) {
            Terminal terminal = terminalWidget.getCurrentSession().getTerminal();
            if (terminal != null) {
                terminal.setCursorVisible(true);// 是否启用光标 BeanSoft
            }
        }
    }

    private ToolWindow getToolWindow() {
        return ToolWindowManager.getInstance(myProject).getToolWindow(RNToolWindowFactory.TOOL_WINDOW_ID);
    }

    public void createNewSession() {
        createNewSession(myProject, myTerminalRunner);
    }

    public void createNewSession(Project project, AbstractTerminalRunner terminalRunner) {
        ToolWindow toolWindow = getToolWindow();
        toolWindow.activate(() -> {
            this.openSession(toolWindow, terminalRunner);
        }, true);
    }

    private void openSession(@NotNull ToolWindow toolWindow, @NotNull AbstractTerminalRunner terminalRunner) {
        if (this.myTerminalWidget == null) {
            toolWindow.getContentManager().removeAllContents(true);
            Content content = this.createTerminalInContentPanel(terminalRunner, toolWindow);
            toolWindow.getContentManager().addContent(content);
        } else {
            terminalRunner.openSession(this.myTerminalWidget);
        }

        toolWindow.activate(() -> {
        }, true);
    }

    /**
     * 创建Terminal panel
     *
     * @param terminalRunner
     * @param toolWindow
     * @return
     */
    private Content createTerminalInContentPanel(@NotNull AbstractTerminalRunner terminalRunner, @NotNull final ToolWindow toolWindow) {
        SimpleToolWindowPanel panel = new SimpleToolWindowPanel(false, true);
        Content content = ContentFactory.SERVICE.getInstance().createContent(panel, "", false);
        content.setCloseable(true);
        myTerminalWidget = terminalRunner.createTerminalWidget(content);
        panel.setContent(myTerminalWidget.getComponent());
        panel.addFocusListener(this);
        ActionToolbar toolbar = createToolbar(terminalRunner, myTerminalWidget, toolWindow);
        toolbar.setTargetComponent(panel);
        panel.setToolbar(toolbar.getComponent());
        content.setPreferredFocusableComponent(myTerminalWidget.getComponent());
        return content;
    }

    /**
     * 创建左侧工具栏
     *
     * @param terminalRunner
     * @param terminal
     * @param toolWindow
     * @return
     */
    private ActionToolbar createToolbar(@Nullable AbstractTerminalRunner terminalRunner, @NotNull JBTabbedTerminalWidget terminal, @NotNull ToolWindow toolWindow) {
        DefaultActionGroup group = new DefaultActionGroup();
        if (terminalRunner != null) {
            // Termal basis
            group.add(new NewSession(terminalRunner, terminal));
            group.add(new CloseSession(terminal, toolWindow));
            group.add(new StopAction(this));
            group.addSeparator();

            // Android
            group.addSeparator();
            group.add(new DevMenuAction(this));
            group.add(new AdbForwardAction(this));
//            group.add(new ForceAction(this));
            group.add(new RunAndroidAction(this));

            // NPM
            group.addSeparator();
            group.add(new NPMStartAction(this));
            group.add(new NPMInstallAction(this));

            // iOS
            group.addSeparator();
            group.add(new RunLinkAction(this));
            group.add(new RunIOSAction(this));

            // General
            group.addSeparator();
            group.add(new ClearAction(this));
            group.add(new DebugUiAction(this));

        }
        return ActionManager.getInstance().createActionToolbar("unknown", group, false);
    }

    @Override
    public void focusGained(FocusEvent e) {
        JComponent component = myTerminalWidget != null ? myTerminalWidget.getComponent() : null;
        if (component != null) {
            component.requestFocusInWindow();
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
        return "ReactNativeTerminal";
    }

    /**
     * 停止执行
     */
    private static class StopAction extends BaseTerminalAction {
        private Robot robot;

        public StopAction(ReactNativeTerminal terminal) {
            super(terminal, "Stop Run Command", "Stop Run Command", PluginIcons.Suspend);
            try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void doAction(AnActionEvent anActionEvent) {
            if (terminal.getCurrentSession() != null) {
                terminal.getCurrentSession().getComponent().requestFocusInWindow();
                Utils.keyPressWithCtrl(robot, KeyEvent.VK_C);
            }
        }
    }

    /** Run rpm command, but with a new terminal session */
    private static class BaseRunNPMAction extends RunAction {
        String npmLocation;

        public BaseRunNPMAction(ReactNativeTerminal terminal) {
            this(terminal, "Run NPM", "Run NPM", PluginIcons.Execute);
        }

        public BaseRunNPMAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(terminal, text, description, icon);
        }

        @Override
        public void doAction(AnActionEvent anActionEvent) {
            Project project = anActionEvent.getData(PlatformDataKeys.PROJECT);
            String path = project.getBasePath();
            npmLocation = RNPathUtil.getRNProjectPath(project, path);

//            pythonLocation = Utils.getPythonLocation();
            if (npmLocation == null) {
                NotificationUtils.packageJsonNotFound();
            } else {
                terminal.createNewSession();
                // If direct call without this code, then the command will be sent to the original terminal session
//                terminal.executeShell("cd \"" + npmLocation + "\"");
//                terminal.executeShell(args());

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
                        terminal.executeShell("cd \"" + npmLocation + "\"");
                        terminal.executeShell(args());
                    });
// Background task
//                    ApplicationManager.getApplication().executeOnPooledThread(() -> ApplicationManager.getApplication().runReadAction(() -> {
//                        terminal.executeShell("cd \"" + npmLocation + "\"");
//                        terminal.executeShell(args());
//                    }));
                }).start();

            }
        }

        protected String args() {
            return null;
        }
    }

    private static class RunAndroidAction extends BaseRunNPMAction {
        public RunAndroidAction(ReactNativeTerminal terminal) {
            this(terminal, "Run Android", "Run Android", PluginIcons.Android);
        }

        public RunAndroidAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(terminal, text, description, icon);
        }

        protected String args() {
            return "react-native run-android";
        }
    }

    private static class RunIOSAction extends BaseRunNPMAction {
        public RunIOSAction(ReactNativeTerminal terminal) {
            this(terminal, "Run on iOS Simulator", "react-native run-ios", PluginIcons.IPhone);
        }

        public RunIOSAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(terminal, text, description, icon);
        }

        protected String args() {
            return "react-native run-ios";
        }
    }

    private static class RunLinkAction extends BaseRunNPMAction {
        public RunLinkAction(ReactNativeTerminal terminal) {
            this(terminal, "react-native link", "", PluginIcons.Lightning);
        }

        public RunLinkAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(terminal, text, description, icon);
        }

        protected String args() {
            return "react-native link";
        }
    }

    // NPM Run start Task
    private static class NPMStartAction extends BaseRunNPMAction {
        public NPMStartAction(ReactNativeTerminal terminal) {
            this(terminal, "start node server", "npm run start", PluginIcons.Execute);
        }

        public NPMStartAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(terminal, text, description, icon);
        }

        protected String args() {
            return "npm run start";
        }
    }

    // NPM Run start Task
    private static class NPMInstallAction extends BaseRunNPMAction {
        public NPMInstallAction(ReactNativeTerminal terminal) {
            this(terminal, "npm install", "npm install", PluginIcons.Install);
        }

        public NPMInstallAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(terminal, text, description, icon);
        }

        protected String args() {
            return "npm install";
        }
    }

    private static class RunAction extends BaseTerminalAction {
        String pythonLocation;

        public RunAction(ReactNativeTerminal terminal) {
            this(terminal, "Run Android", "Run Android", PluginIcons.Execute);
        }

        public RunAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(terminal, text, description, icon);
        }

        @Override
        public void doAction(AnActionEvent anActionEvent) {
//            pythonLocation = Utils.getPythonLocation();
//            if (pythonLocation == null) {
//                NotificationUtils.pythonNotFound();
//            } else {
            terminal.executeShell(args());//
//            }
        }

        protected String args() {
            return null;
        }
    }

    private static class DevMenuAction extends BaseRunNPMAction {
        public DevMenuAction(ReactNativeTerminal terminal) {
            super(terminal, "Open Dev Menu", "Open Dev Menu", PluginIcons.DevMenu);
        }

        @Override
        protected String args() {
            return "adb shell input keyevent 82";
        }
    }

    /**
     * 物理设备, 转发请求.
     * If you're on a physical device connected to the same machine,
     * run 'adb reverse tcp:8081 tcp:8081' to forward requests from your device
     */
    private static class AdbForwardAction extends BaseRunNPMAction {
        public AdbForwardAction(ReactNativeTerminal terminal) {
            super(terminal, "adb reverse tcp:8081 tcp:8081",
                    "forward Android device request to this machine", PluginIcons.Link);
        }

        @Override
        protected String args() {
            return "adb reverse tcp:8081 tcp:8081";
        }
    }


    private static class DebugUiAction extends RunAction {
        public DebugUiAction(ReactNativeTerminal terminal) {
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

    /**
     * Clear the terminal window
     */
    private static class ClearAction extends BaseTerminalAction {
        public ClearAction(ReactNativeTerminal terminal) {
            super(terminal, "Clear", "Clear", PluginIcons.GC);
        }

        @Override
        public void doAction(AnActionEvent anActionEvent) {
            if (terminal.getCurrentSession() != null) {
                terminal.getCurrentSession().getTerminal().reset();
                terminal.getCurrentSession().getTerminal().setCursorVisible(false);
            }
        }
    }

    private static abstract class BaseTerminalAction extends DumbAwareAction {
        protected ReactNativeTerminal terminal;

        public BaseTerminalAction(ReactNativeTerminal terminal, String text, String description, Icon icon) {
            super(text, description, icon);
            this.terminal = terminal;
        }

        @Override
        public void actionPerformed(AnActionEvent anActionEvent) {
//            DocumentUtil.saveDocument();
            doAction(anActionEvent);
        }

        public abstract void doAction(AnActionEvent anActionEvent);
    }

    // Original code taken from IDEA platform
    private static void hideIfNoActiveSessions(@NotNull ToolWindow toolWindow, @NotNull JBTabbedTerminalWidget terminal) {
        if(terminal.isNoActiveSessions()) {
            toolWindow.getContentManager().removeAllContents(true);
        }
    }

    private static class CloseSession extends DumbAwareAction {
        private final JBTabbedTerminalWidget myTerminal;
        private ToolWindow myToolWindow;

        public CloseSession(@NotNull JBTabbedTerminalWidget terminal, @NotNull ToolWindow toolWindow) {
            super("Close Session", "Close Terminal Session", AllIcons.Actions.Delete);
            this.myTerminal = terminal;
            this.myToolWindow = toolWindow;
        }

        public void actionPerformed(AnActionEvent e) {
            this.myTerminal.closeCurrentSession();
            hideIfNoActiveSessions(this.myToolWindow, this.myTerminal);
        }
    }


    private static class NewSession extends DumbAwareAction {
        private final AbstractTerminalRunner myTerminalRunner;
        private final TerminalWidget myTerminal;

        public NewSession(@NotNull AbstractTerminalRunner terminalRunner, @NotNull TerminalWidget terminal) {
            super("New Session", "Create New Terminal Session", AllIcons.General.Add);
            this.myTerminalRunner = terminalRunner;
            this.myTerminal = terminal;
        }

        public void actionPerformed(AnActionEvent e) {
            this.myTerminalRunner.openSession(this.myTerminal);
        }
    }
}
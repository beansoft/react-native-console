package com.github.beansoftapp.reatnative.idea.actions.console;

import com.github.beansoftapp.reatnative.idea.actions.BaseRNConsoleNPMAction;
import com.github.beansoftapp.reatnative.idea.icons.PluginIcons;
import com.github.beansoftapp.reatnative.idea.views.ReactNativeConsole;

// view ios log, we have GUI version: /Applications/Utilities/Console.app/Contents/MacOS/Console /var/log/system.log or
// syslog -w 100(see: https://developer.apple.com/legacy/library/documentation/Darwin/Reference/ManPages/man1/syslog.1.html
// and https://discussions.apple.com/thread/2285049?start=0&tstart=0, and the source code of RN logIOS.js shows:
// syslog -w -F std '-d', logDir(logDir is the active device dir var: xcrun simctl list devices --json, the status should be:
// device.availability === '(available)' && device.state === 'Booted', so the final solution is just using RN built-in command
public class NPMiOSLogsAction extends BaseRNConsoleNPMAction {
    public NPMiOSLogsAction(ReactNativeConsole terminal) {
        super(terminal, "log-ios", "react-native log-ios starts iOS device syslog tail", PluginIcons.TrackTests);
    }

    protected String command() {
        return "react-native log-ios";
    }
}

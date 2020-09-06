package com.github.beansoft.reatnative.idea.utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;

public class NotificationUtils {

    private static final String TITLE = "React Native Console";
    private static final NotificationGroup NOTIFICATION_GROUP = NotificationGroup.balloonGroup(TITLE);

    /**
     * show a Notification
     *
     * @param message
     * @param type
     */
    public static void showNotification(final String message,
                                        final NotificationType type) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Notification notification =
                        NOTIFICATION_GROUP.createNotification(TITLE, message, type, null);
                Notifications.Bus.notify(notification);
            }
        });
    }

    /**
     * show a error Notification
     *
     * @param message
     */
    public static void errorNotification(final String message) {
        showNotification(message, NotificationType.ERROR);
    }

    /**
     * error message dialog
     * @param message
     */
    public static void errorMsgDialog(String message) {
        Messages.showMessageDialog(message, "Error", Messages.getInformationIcon());
    }

    /**
     * show a info Notification
     *
     * @param message
     */
    public static void infoNotification(final String message) {
        showNotification(message, NotificationType.INFORMATION);
    }

    /**
     * show a info Notification
     *
     * @param message
     */
    public static void warning(final String message) {
        showNotification(message, NotificationType.WARNING);
    }

    /**
     * python提示
     */
    public static void pythonNotFound() {
        String tip = "command 'python' not found. ";
        if (SystemInfo.isWindows) {
            tip += "  if first installation python or Set Environment variable, Please restart your computer";
        }
        errorNotification(tip);
    }

    /**
     * package.json error message
     */
    public static void packageJsonNotFound() {
        String tip = "File 'package.json' not found.\n Make sure that you have run `npm install` and that you are inside a react-native project.";//找不到有效的React Native目录, 命令将停止执行
        errorNotification(tip);
    }

    /**
     * build.gradle error message
     */
    public static void gradleFileNotFound() {
        String tip = "File 'build.gradle' not found.\n Make sure that you have run `npm install` and that you are inside a android project.";
        if (SystemInfo.isWindows) {
            tip += "  if first installation React Native or Set Environment variable, Please restart your computer";
        }
        errorNotification(tip);
    }

    /**
     * cocoapods error message
     */
    public static void cocoapodsFileNotFound() {
        String tip = "File 'Podfile' not found.\n Make sure that you have setup cocoapods inside a iOS project.";
        errorNotification(tip);
    }

}

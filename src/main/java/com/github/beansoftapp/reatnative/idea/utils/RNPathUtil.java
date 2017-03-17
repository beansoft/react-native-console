package com.github.beansoftapp.reatnative.idea.utils;

import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.util.List;

/**
 * Created by beansoft on 2017/3/14.
 */
public class RNPathUtil {
    static String PACKAGE_JSON = "package.json";

    /**
     * 获取React Native的根目录, 在Android Studio中运行时可能会在android的上一级目录.
     * @param inputDir
     * @return
     */
    public static String getRNProjectPath(Project project, String inputDir) {
        File file = new File(inputDir, PACKAGE_JSON);
        if(file.exists()) {
            return inputDir;
        } else {
            file = new File(inputDir, ".." + File.separatorChar + PACKAGE_JSON);
            if(file.exists()) {
                return inputDir + File.separatorChar + "..";
            }
        }

//        Messages.showWarningDialog(project, "找不到有效的React Native目录, 命令将停止执行.\n目录" +
//                inputDir + "及其上级目录中找不到有效的package.json文件.", "警告");

        return null;
    }

    /**
     * Get the full path of an exe file by the IDEA platform.
     * On Mac sys, PATH might will not be directly access by the IDE code,
     * eg: Android Studio Runtime.exec('adb') will throw a no file or directory exception.
     * @param exeName
     * @return
     */
    public static String getExecuteFileFullPath(String exeName) {
        List<File> fromPath = PathEnvironmentVariableUtil.findAllExeFilesInPath(exeName);
        if(fromPath != null && fromPath.size() > 0) {
            return fromPath.get(0).toString();
        }

        return null;
    }

    public static String createAdbCommand(String args) {
        String adbFullPath = getExecuteFileFullPath("adb");
        if(adbFullPath != null) {
            return adbFullPath + " " + args;
        }
        return "adb" + " " + args;
    }
}
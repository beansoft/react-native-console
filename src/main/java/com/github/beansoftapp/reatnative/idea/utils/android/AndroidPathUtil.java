package com.github.beansoftapp.reatnative.idea.utils.android;

import com.github.beansoftapp.reatnative.idea.utils.OSUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.EnvironmentUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AndroidPathUtil {
    public static String getEmulatorPath(String inputDir) {
        String fullPath = RNPathUtil.getExecuteFileFullPath("emulator");
        if (StringUtil.isNotEmpty(fullPath)) {
            return fullPath;
        }

        String sdkHome = getAndroidSDKHome(inputDir);
        if(StringUtil.isNotEmpty(sdkHome)) {
            if (OSUtils.isWindows()) {
                return sdkHome + "\\emulator\\emulator.exe";
            } else {
                return sdkHome + "/emulator/emulator";
            }
        }

        return null;
    }

    public static String getAndroidSDKHome(String inputDir) {
        String localSDKHome = getAndroidSDKHomeFromLocalProperties(inputDir);
        if (StringUtil.isNotEmpty(localSDKHome)) {
            File dir = new File(localSDKHome);
            if(dir.isDirectory() && dir.exists() && dir.canRead())
            return localSDKHome;
        }
        String envHome = EnvironmentUtil.getValue("ANDROID_HOME");
        if (StringUtil.isNotEmpty(envHome)) {
            return envHome;
        }

        return null;
    }

    public static String getAndroidSDKHomeFromLocalProperties(String inputDir) {
        if (StringUtil.isEmpty(inputDir)) {
            return null;
        }

        String androidProjectPath = RNPathUtil.getAndroidProjectPath(inputDir);
        if (StringUtil.isNotEmpty(androidProjectPath)) {
            File file = new File(androidProjectPath + File.separatorChar + "local.properties");
            if (file.exists() && file.canRead()) {
                try {
                    Properties p =  new Properties();
                    p.load(new FileReader(file));
                    return p.getProperty("sdk.dir");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

}

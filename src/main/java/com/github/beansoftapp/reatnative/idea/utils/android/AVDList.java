package com.github.beansoftapp.reatnative.idea.utils.android;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.execution.ParametersListUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AVDList {
    @Nullable
    public static String[] getAvdList(String inputDir) {
        String emulator = AndroidPathUtil.getEmulatorPath(inputDir);
        if(StringUtil.isNotEmpty(emulator)) {
            GeneralCommandLine commandLine = new GeneralCommandLine(emulator);
            List<String> parameters = ParametersListUtil.parse("-list-avds", false);// keepQuotes: false
            commandLine.addParameters(parameters);
            String text = null;
            try {
                text = ExecUtil.execAndGetOutput(commandLine).getStdout();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (StringUtil.isNotEmpty(text)) {
                return text.split("\n");
            }
        }

        return null;
    }

    public static void main(String[] args) {
        String[] list = AVDList.getAvdList(null);
        if(list != null) {
            for (String s :
                   list ) {
                System.out.println("avd:" + s);
            }
        }

    }
}

package com.github.beansoftapp.reatnative.idea.utils.ios;

import com.github.beansoftapp.reatnative.idea.entity.ios.IOSDeviceInfo;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.js.JSExec;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * iOS 设备列表信息分析.
 * Created by beansoft on 2017/6/3.
 */
public class IOSDevicesParser {
    public static final String LIST_Simulator_JSON = "xcrun simctl list devices --json";
    public static final String LIST_DEVICES = "xcrun instruments -s";

    /**
     * All devices include simulator.
     * @param noSimulator 是否不包含模拟器
     * @return
     */
    public static List<IOSDeviceInfo> getAllIOSDevicesList(boolean noSimulator) {
        List<IOSDeviceInfo> deviceInfos = new ArrayList<>();

        GeneralCommandLine commandLine = RNPathUtil.createFullPathCommandLine(IOSDevicesParser.LIST_DEVICES, null);
        try {
            String json = ExecUtil.execAndGetOutput(commandLine).getStdout();
            String regex = "/(.*?) \\((.*?)\\) \\[(.*?)\\]/";
            String simulatorRegex = "/(.*?) \\((.*?)\\) \\[(.*?)\\] \\((.*?)\\)/";

            Arrays.asList(json.split("\n")).forEach(line -> {
//                System.out.println("parsing " + line);
                String[] device = JSExec.jsMatchExpr(line, regex);
                String[] noSimulatorDevice = null;
                if(device != null) {
//                    System.out.println("result = " + device[1]);
                    IOSDeviceInfo deviceInfo = new IOSDeviceInfo();
                    deviceInfo.name = device[1];
                    deviceInfo.version = device[2];
                    deviceInfo.udid = device[3];

//                    noSimulatorDevice = JSExec.jsMatchExpr(line, simulatorRegex);

//                    deviceInfo.simulator = (noSimulatorDevice != null);
                    deviceInfo.simulator = line.endsWith("(Simulator)");// To enable quick exec

                    if(noSimulator) {
                        if(!deviceInfo.simulator) {
                            deviceInfos.add(deviceInfo);
                        }
                    } else {
                        deviceInfos.add(deviceInfo);
                    }
                }
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
            NotificationUtils.errorNotification( "xcrun invocation failed. Please check that Xcode is installed." );
            return null;
        }

        return deviceInfos;
    }
}

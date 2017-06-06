import com.github.beansoftapp.reatnative.idea.models.ios.IOSDeviceInfo;
import com.github.beansoftapp.reatnative.idea.models.ios.Simulators;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.github.beansoftapp.reatnative.idea.utils.RNPathUtil;
import com.github.beansoftapp.reatnative.idea.utils.ios.IOSDevicesParser;
import com.google.gson.Gson;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by beansoft on 2017/6/3.
 */

public class TestParseIOSDevices {

    @Test
    public void testgetAllIOSDevicesList() {
        List<IOSDeviceInfo> devices = IOSDevicesParser.getAllIOSDevicesList(true);
        System.out.println(devices);
        List<IOSDeviceInfo> devicesPhysical = IOSDevicesParser.getAllIOSDevicesList(false);
        System.out.println(devicesPhysical);
    }
    @Test
    public void listDevices() {
        System.out.println(
                System.getProperty("user.home"));
//        System.getProperties().list(System.out);

        GeneralCommandLine commandLine = RNPathUtil.cmdToGeneralCommandLine(IOSDevicesParser.LIST_Simulator_JSON);

        try {
            String json = ExecUtil.execAndGetOutput(commandLine).getStdout();
            System.out.println("json=" + json);
            Simulators result = new Gson().fromJson(json, Simulators.class);
            System.out.println(result.devices.keySet());
            System.out.println(result.devices.get("iOS 10.3")[0]);
        } catch (ExecutionException e) {
            e.printStackTrace();
            NotificationUtils.errorNotification( "xcrun invocation failed. Please check that Xcode is installed." );
        }

    }

    @Test
    public void testExec() {
        //        System.out.println(
//                System.getProperty("user.home"));
//        System.getProperties().list(System.out);

        GeneralCommandLine commandLine = RNPathUtil.cmdToGeneralCommandLine(IOSDevicesParser.LIST_DEVICES);
        try {
            String json = ExecUtil.execAndGetOutput(commandLine).getStdout();
            System.out.println(json);
            Arrays.asList(json.split("\n")).forEach(line -> {
                System.out.println(line);
//                Pattern pattern = Pattern
//                        .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
                boolean device = line.matches("^(.*?) \\((.*?)\\)\\ \\[(.*?)\\]");
                System.out.println("device=" + device);
//                String noSimulator = line.match(/(.*?) \((.*?)\) \[(.*?)\] \((.*?)\)/);
            });

        } catch (ExecutionException e) {
            e.printStackTrace();
            NotificationUtils.errorNotification( "xcrun invocation failed. Please check that Xcode is installed." );
            return;
        }
    }

    @Test
    public void testJS() {
        // https://stackoverflow.com/questions/22492641/java8-js-nashorn-convert-array-to-java-array
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            engine.put("line", "刘长炯 微信号weblogic (10.3.2) [46a5432f8fdea99a6186a927e8da5db7a51854ac]");
//            engine.put("regex", )
            String regex = "/(.*?) \\((.*?)\\) \\[(.*?)\\]/";
            String[] value = (String[])engine.eval("Java.to(line.match(" + regex + "),\"java.lang.String[]\" );");
            System.out.println(value);
//            Collection<Object> val = value.values();
//            if(value.isArray()) {
//                System.out.println(value.getMember("1"));
//            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}

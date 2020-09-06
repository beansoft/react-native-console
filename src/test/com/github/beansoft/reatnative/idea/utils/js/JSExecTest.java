package com.github.beansoft.reatnative.idea.utils.js;


import org.junit.Test;

import java.util.Arrays;

/**
 * Created by beansoft on 2017/6/4.
 */
public class JSExecTest {
    @Test
    public void jsMatchExpr() {
        String json = "Known Devices:\n" +
                "刘长炯的MacBook Pro [794FB38A-D69F-5F26-92E6-5D49622CEE34]\n" +
                "刘长炯 微信号weblogic (10.3.2) [46a5432f8fdea99a6186a927e8da5db7a51854ac]\n" +
                "Apple TV 1080p (10.2) [ED57B27E-916B-4B63-91A3-2802BE5CC1A4] (Simulator)\n" +
                "iPad Air (10.3) [63348528-5214-4FEE-8287-1B9DBDACAFDB] (Simulator)\n" +
                "iPad Air 2 (10.3) [7A2D68CB-ECDB-4ABE-A426-77460AAF65DB] (Simulator)\n" +
                "iPad Pro (12.9 inch) (10.3) [7B68FA98-A86F-481F-B994-F7E21322F2CE] (Simulator)\n" +
                "iPad Pro (9.7 inch) (10.3) [F977F35F-C527-4C04-AFEF-4FE39FCB8F21] (Simulator)\n" +
                "iPhone 5 (10.3) [897F3B6B-94B4-43E9-A843-33B370264A63] (Simulator)\n" +
                "iPhone 5s (10.3) [235D3150-BBD2-44EB-A337-26E19288FB88] (Simulator)\n" +
                "iPhone 6 (10.3) [AE42269B-EDD8-40D8-AFA0-334D84E928AA] (Simulator)\n" +
                "iPhone 6 Plus (10.3) [AF7DF570-C735-48A5-87C2-E8E6F25C72E1] (Simulator)";
        String regex = "/(.*?) \\((.*?)\\) \\[(.*?)\\]/";

        Arrays.asList(json.split("\n")).forEach(line -> {
            System.out.println("parsing " + line);
            String[] result = JSExec.jsMatchExpr(line, regex);
            if(result != null) {
                System.out.println("result = " + result[1]);
            } else {
                System.out.println("result = null");
            }

        });

//        String[] result = JSExec.jsMatchExpr(json, regex);
//        Assert.assertNotNull("Result should not be null", result);
//        Assert.assertEquals("Should have this result:", result[1], "刘长炯 微信号weblogic");
    }
}

package com.github.beansoftapp.reatnative.idea.utils.npm.test;

import com.github.beansoftapp.reatnative.idea.utils.npm.NPMParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class NPMParserTest {
    @Test
    public void parseScripts() {
        java.util.List<String> devices = NPMParser.parseScripts(new File("src/test/package.json"));
        System.out.println(devices);
        Assert.assertTrue(devices.contains("rewatchman"));
        String version = NPMParser.parseRNVersion(new File("src/test/package.json"));
        System.out.println(version.compareTo("0.40"));
        System.out.println(version.compareTo("0.50"));
    }
}

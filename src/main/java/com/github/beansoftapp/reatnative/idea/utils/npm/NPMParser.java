package com.github.beansoftapp.reatnative.idea.utils.npm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse package.json for script list.
 */
public class NPMParser {
    public static final List<String> parseScripts(File f) {
        List<String> list = new ArrayList<>();
        list.add("test");

        return list;
    }
}

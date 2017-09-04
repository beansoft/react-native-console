package com.github.beansoftapp.reatnative.idea.utils.npm;

import com.github.beansoftapp.reatnative.idea.models.ios.Simulators;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parse package.json for script list.
 */
public class NPMParser {
    public static final List<String> parseScripts(File f) {
        List<String> list = new ArrayList<>();
        try {
            JsonObject result = new Gson().fromJson(FileUtil.loadFile(f, "UTF-8"), JsonObject.class);
            JsonObject scripts = result.getAsJsonObject("scripts");
            for (Map.Entry<String, JsonElement> obj:
                    scripts.entrySet()) {
                list.add(obj.getKey());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return list;
    }
}

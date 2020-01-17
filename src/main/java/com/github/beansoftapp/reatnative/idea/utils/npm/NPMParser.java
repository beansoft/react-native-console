package com.github.beansoftapp.reatnative.idea.utils.npm;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parse package.json for script list.
 */
public class NPMParser {
    public static List<String> parseScripts(File f) {
        List<String> list = new ArrayList<>();
        if(!f.exists()) {
            return list;
        }

        try {
            JsonObject result = new Gson().fromJson(FileUtil.loadFile(f, "UTF-8"), JsonObject.class);
            JsonObject scripts = result.getAsJsonObject("scripts");
            if(scripts == null) {
                return list;// Fix NPE when there is an empty or no scripts package.json file
            }
            for (Map.Entry<String, JsonElement> obj:
                    scripts.entrySet()) {
                list.add(obj.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static String parseRNVersion(File f) {
        if(!f.exists()) {
            return null;
        }

        try {
            JsonObject result = new Gson().fromJson(FileUtil.loadFile(f, "UTF-8"), JsonObject.class);
            JsonObject scripts = result.getAsJsonObject("dependencies");
            if(scripts == null) {
                return null;// Fix NPE when there is an empty or no scripts package.json file
            }
            JsonPrimitive obj = scripts.getAsJsonPrimitive("react-native");
            if(obj != null) {
                String versionStr = obj.getAsString();
                return versionStr.replace("^", "").replace(">=", "")
                    .replace(">", "").replace(">=", "")
                    .replace("<=", "").replace("<", "")
                    .replace("=", "").replace("~", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

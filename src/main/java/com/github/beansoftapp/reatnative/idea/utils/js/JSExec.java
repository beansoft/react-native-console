package com.github.beansoftapp.reatnative.idea.utils.js;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Java call some js function, no need to study stupid JavaScript regex conversion to Java.
 * Created by beansoft on 2017/6/4.
 */
public class JSExec {

    private static ScriptEngine engine = null;
    /**
     * Exec a string match function with JavaScript regex expression and return the match result.
     * @param str
     * @param regex
     * @return null if no match, otherwise a String[] result value
     */
    public static String[] jsMatchExpr(String str, String regex) {
        // https://stackoverflow.com/questions/22492641/java8-js-nashorn-convert-array-to-java-array
        if(engine == null) {
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName("javascript");
        }
        try {
            engine.put("str", str);
            String[] value = (String[])engine.eval("Java.to(str.match(" + regex + "),\"java.lang.String[]\" );");
            return value;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }
}

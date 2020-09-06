package com.github.beansoft.reatnative.idea.utils;

/**
 * Tools to show os type.
 */
public class OSUtils {
    private static String OS = System.getProperty("os.name").toLowerCase();  
      
    public static boolean isLinux(){  
        return OS.indexOf("linux")>=0;  
    }  
      
    public static boolean isMacOS(){  
        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")<0;  
    }  
      
    public static boolean isMacOSX(){  
        return OS.indexOf("mac")>=0&&OS.indexOf("os")>0&&OS.indexOf("x")>0;  
    }  
      
    public static boolean isWindows(){  
        return OS.indexOf("windows")>=0;  
    }  
      
    
}

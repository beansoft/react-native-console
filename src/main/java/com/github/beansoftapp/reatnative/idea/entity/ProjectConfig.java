package com.github.beansoftapp.reatnative.idea.entity;

/**
 * Project configuration of current project, stored in file .idea/.rnconsole.
 * @author beansoft@126.com
 * @date 2019-8-19
 */
public class ProjectConfig {
  private String currentPath;// Js Project root path
  private String metroPort;// Metro listener port
  private String androidParam = "";// react-native run-android command options
  private String iosParam = "";// react-native run-ios command options

  public String getCurrentPath() {
    return currentPath;
  }

  public void setCurrentPath(String currentPath) {
    this.currentPath = currentPath;
  }

  public String getMetroPort() {
    return metroPort;
  }

  public void setMetroPort(String metroPort) {
    this.metroPort = metroPort;
  }

  public String getAndroidParam() {
    return androidParam;
  }

  public void setAndroidParam(String androidParam) {
    this.androidParam = androidParam;
  }

  public String getIosParam() {
    return iosParam;
  }

  public void setIosParam(String iosParam) {
    this.iosParam = iosParam;
  }

  @Override
  public String toString() {
    return "ProjectConfig{" +
            "currentPath='" + currentPath + '\'' +
            ", metroPort='" + metroPort + '\'' +
            ", androidParam='" + androidParam + '\'' +
            ", iosParam='" + iosParam + '\'' +
            '}';
  }
}
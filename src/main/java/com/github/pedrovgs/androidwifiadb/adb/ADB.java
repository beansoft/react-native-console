/*
 * Copyright (C) 2015 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pedrovgs.androidwifiadb.adb;

import com.github.beansoft.reatnative.idea.utils.RNPathUtil;
import com.github.pedrovgs.androidwifiadb.Device;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ADB {

  private static final String TCPIP_PORT = "5555";
  private final CommandLine commandLine;
  private final ADBParser adbParser;
  private Project project;

  public ADB() {
    this(new CommandLine(), new ADBParser());
  }

  public ADB(CommandLine commandLine, ADBParser adbParser) {
    this.commandLine = commandLine;
    this.adbParser = adbParser;
  }

  public void updateProject(Project project) {
    this.project = project;
  }

  public Collection<Device> getDevicesConnectedByUSB() {
    try {
      GeneralCommandLine commandLine = RNPathUtil.createFullPathCommandLine("adb devices -l", null);
      String adbDevicesOutput = ExecUtil.execAndGetOutput(commandLine).getStdout();
      return adbParser.parseGetDevicesOutput(adbDevicesOutput);
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

    return new ArrayList<>();
  }

  public Collection<Device> connectDevices(Collection<Device> devices) {
    for (Device device : devices) {
      boolean connected = connectDeviceByIp(device);
      device.setConnected(connected);
    }
    return devices;
  }

  public List<Device> disconnectDevices(List<Device> devices) {
    for (Device device : devices) {
      boolean disconnected = disconnectDevice(device.getIp());
      device.setConnected(disconnected);
    }
    return devices;
  }

  private boolean connectDeviceByIp(Device device) {
    String deviceIp = getDeviceIp(device);
    if (deviceIp.isEmpty()) {
      return false;
    } else {
      return connectDevice(deviceIp);
    }
  }

  private boolean disconnectDevice(String deviceIp) {
    enableTCPCommand();
    String connectDeviceCommand = getCommand("disconnect " + deviceIp);
    return commandLine.executeCommand(connectDeviceCommand).isEmpty();
  }

  public String getDeviceIp(Device device) {
    String getDeviceIpCommand =
        getCommand("-s " + device.getId() + " shell ip -f inet addr show wlan0");
    String ipInfoOutput = commandLine.executeCommand(getDeviceIpCommand);
    return adbParser.parseGetDeviceIp(ipInfoOutput);
  }

  private void enableTCPCommand() {
    if (!checkTCPCommandExecuted()) {
      String enableTCPCommand = getCommand("tcpip " + TCPIP_PORT);
      commandLine.executeCommand(enableTCPCommand);
    }
  }

  private boolean checkTCPCommandExecuted() {
    String getPropCommand = getCommand("adb shell getprop | grep adb");
    String getPropOutput = commandLine.executeCommand(getPropCommand);
    String adbTcpPort = adbParser.parseAdbServiceTcpPort(getPropOutput);
    return TCPIP_PORT.equals(adbTcpPort);
  }

  private boolean connectDevice(String deviceIp) {
    String enableTCPCommand = getCommand("tcpip 5555");
    commandLine.executeCommand(enableTCPCommand);
    String connectDeviceCommand = getCommand("connect " + deviceIp);
    String connectOutput = commandLine.executeCommand(connectDeviceCommand);
    return connectOutput.contains("connected");
  }

  private String getAdbPath() {
    return RNPathUtil.getExecuteFileFullPath("adb");
  }

  private String getCommand(String command) {
    return getAdbPath() + " " + command;
  }
}

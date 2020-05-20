package com.github.beansoftapp.gc;

import beansoft.jvm.hotspot.util.GetProcessID;
import com.github.beansoftapp.reatnative.idea.utils.NotificationUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.sun.jvmstat.tools.visualgc.JpsHelper;

import javax.swing.*;

public class VisualGCAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    String[] args = null;
    // TODO: insert action logic here
    JList list = new JList(JpsHelper.getJvmPSList().toArray());
    String str = JOptionPane.showInputDialog(null, list, "Please select a PS", 3);
    if (str == null || str.length() == 0) {
      Object val = list.getSelectedValue();
      if (val == null) {
        args = new String[] { (new StringBuilder(String.valueOf(GetProcessID.getPid()))).toString() };
      } else {
        str = val.toString().substring(0, val.toString().indexOf(' '));
        args = new String[] { str };
      }
    } else {
      args = new String[] { str };
    }
    System.out.println(str);
    NotificationUtils.infoNotification("PS:" + str);
  }
}

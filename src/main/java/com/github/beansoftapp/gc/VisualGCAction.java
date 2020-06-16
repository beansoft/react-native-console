package com.github.beansoftapp.gc;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.sun.jvmstat.tools.visualgc.VisualGCPane;
import org.graalvm.visualvm.core.ui.components.DataViewComponent;

import javax.swing.*;
import java.awt.*;

public class VisualGCAction extends AnAction {

  static class VisualGCPaneIdea extends VisualGCPane {
    static {
      VisualGCPane.customizeColors();
    }
    public DataViewComponent createComponent(final Container container) {
      return super.createComponent(container);
    }
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    JFrame frame = new JFrame();
    frame.setIconImage(new ImageIcon(VisualGCPane.class.getResource("/visualgc.png")).getImage());
    frame.setTitle("VisualGC 3.0");
    VisualGCPaneIdea gcPane = new VisualGCPaneIdea();
    frame.getContentPane().add(gcPane.createComponent(frame.getContentPane()), BorderLayout.CENTER);
    frame.setSize(1024, 768);
    frame.setVisible(true);
  }
}

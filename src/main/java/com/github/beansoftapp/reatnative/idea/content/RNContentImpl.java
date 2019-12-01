package com.github.beansoftapp.reatnative.idea.content;

import com.github.beansoftapp.reatnative.idea.ui.RNConsole;

import javax.swing.*;

public class RNContentImpl extends com.intellij.ui.content.impl.ContentImpl {
  private RNConsole rnConsole;
  public RNContentImpl(JComponent component, String displayName, boolean isPinnable, RNConsole rnConsole) {
    super(component, displayName, isPinnable);
    this.rnConsole = rnConsole;
  }

  public RNConsole getRnConsole() {
    return rnConsole;
  }

  public void setRnConsole(RNConsole rnConsole) {
    this.rnConsole = rnConsole;
  }
}

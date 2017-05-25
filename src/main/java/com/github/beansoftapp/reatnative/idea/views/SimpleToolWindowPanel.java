/*
 * Copyright 2000-2012 JetBrains s.r.o.
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
package com.github.beansoftapp.reatnative.idea.views;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.ui.JBColor;
import com.intellij.ui.switcher.QuickActionProvider;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

/**
 * A simple tool window pane with multi row toolbar buttons, however we remove interface com.intellij.ui.switcher.QuickActionProvider,
 * because the method ActionToolbar.getActions() has different method signature VS the old version(163.12024.16), the
 * old version has this format:  ActionToolbar.getActions(boolean)
 *
 * 2017-03-24
 * @author beansoft@126.com
 */
public class SimpleToolWindowPanel extends JPanel implements DataProvider {

  private JComponent myWestToolbar;
  private JComponent myNorthToolbar;
  private JComponent myContent;

  private boolean myBorderless;
  private boolean myProvideQuickActions;


  public SimpleToolWindowPanel(boolean borderless) {
    setLayout(new BorderLayout(1, 1));
    myBorderless = borderless;
//    setProvideQuickActions(true);

    addContainerListener(new ContainerAdapter() {
      @Override
      public void componentAdded(ContainerEvent e) {
        Component child = e.getChild();

        if (child instanceof Container) {
          ((Container)child).addContainerListener(this);
        }
        if (myBorderless) {
          UIUtil.removeScrollBorder(SimpleToolWindowPanel.this);
        }
      }

      @Override
      public void componentRemoved(ContainerEvent e) {
        Component child = e.getChild();
        
        if (child instanceof Container) {
          ((Container)child).removeContainerListener(this);
        }
      }
    });
  }

//  public boolean isToolbarVisible() {
//    return myWestToolbar != null && myWestToolbar.isVisible();
//  }

  public void setToolbar(@Nullable JComponent c, boolean vertical) {
    if (c == null) {
      if (vertical) {
        remove(myWestToolbar);
      } else {
        remove(myNorthToolbar);
      }
      return;
    }


    if (c != null) {
      if (vertical) {
        add(c, BorderLayout.WEST);
        myWestToolbar = c;
      } else {
        add(c, BorderLayout.NORTH);
        myNorthToolbar = c;
      }
    }

    revalidate();
    repaint();
  }

  @Nullable
  public Object getData(@NonNls String dataId) {
    return QuickActionProvider.KEY.is(dataId) && myProvideQuickActions ? this : null;
  }

  public SimpleToolWindowPanel setProvideQuickActions(boolean provide) {
    myProvideQuickActions = provide;
    return this;
  }

//  @NotNull
//  public List<AnAction> getActions(boolean originalProvider) {
//    JBIterable<ActionToolbar> toolbars = UIUtil.uiTraverser(myWestToolbar).traverse().filter(ActionToolbar.class);
//    if (toolbars.size() == 0) return Collections.emptyList();
//
//    // 兼容旧版API
//    try {
//      ActionToolbar.class.getMethod("getActions", Boolean.class);
//    } catch (NoSuchMethodException e) {
//      e.printStackTrace();
//    }
//    return toolbars.flatten(toolbar -> toolbar.getActions()).toList();
//  }

  public JComponent getComponent() {
    return this;
  }

  public boolean isCycleRoot() {
    return false;
  }

  public JComponent getContent() {
    return myContent;
  }

  public void setContent(JComponent c) {
    myContent = c;
    add(c, BorderLayout.CENTER);

    if (myBorderless) {
      UIUtil.removeScrollBorder(c);
    }

    revalidate();
    repaint();
  }



  @Override
  protected void paintComponent(final Graphics g) {
    super.paintComponent(g);

    g.setColor(JBColor.border());//UIUtil.getBorderColor() is deprecated

    if (myWestToolbar != null && myWestToolbar.getParent() == this && myContent != null && myContent.getParent() == this) {
        int x = (int) myWestToolbar.getBounds().getMaxX();
        g.drawLine(x, 0, x, getHeight());
    }

    if (myNorthToolbar != null && myNorthToolbar.getParent() == this && myContent != null && myContent.getParent() == this) {
      final int y = (int) myNorthToolbar.getBounds().getMaxY();
      g.drawLine(0, y, getWidth(), y);
    }

  }
}
package com.github.beansoft.reatnative.idea.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapperPeer;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages.InputDialog;
import com.intellij.openapi.util.TextRange;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Show input dialog but hide the cancel button.
 * @author beansoft@126.com
 * @date 2019-8-20
 */
public class IdeaMessages {
  @Nullable
  public static String showInputDialog(@Nullable Project project, @Nls String message, @Nls(capitalization = Nls.Capitalization.Title) String title, @Nullable Icon icon, @Nullable String initialValue, @Nullable InputValidator validator) {
    return IdeaMessages.showInputDialog(project, (Component)null, message, title, icon, initialValue, validator, (TextRange)null);
  }

  // Hide the cancel button
    public static String showInputDialog(@Nullable Project project, Component parentComponent, String message, String title, @Nullable Icon icon, @Nullable String initialValue, @Nullable InputValidator validator, @Nullable TextRange selection) {
      InputDialog dialog = new InputDialog(project, message, title, icon, initialValue, validator,
          new String[]{com.intellij.openapi.ui.Messages.OK_BUTTON}, 0);// Fix support with WebStorm 173(2017.3)
      JTextComponent field = dialog.getTextField();
      if (selection != null) {
        field.select(selection.getStartOffset(), selection.getEndOffset());
        field.putClientProperty(DialogWrapperPeer.HAVE_INITIAL_SELECTION, true);
      }

      dialog.show();
      return dialog.getInputString();
  }
}

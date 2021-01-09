// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.github.beansoftapp.javascript.codeInsight;

import com.github.beansoftapp.javascript.codeInsight.daemon.impl.JavaScriptTelescope;
import com.github.beansoftapp.javascript.codeInsight.daemon.impl.JavaScriptUnusedSymbolUtil;
import com.github.beansoftapp.javascript.codeInsight.daemon.impl.PsiEditorUtil;
import com.intellij.codeInsight.hints.*;
import com.intellij.codeInsight.hints.presentation.InlayPresentation;
import com.intellij.codeInsight.hints.presentation.MouseButton;
import com.intellij.codeInsight.hints.presentation.PresentationFactory;
import com.intellij.codeInsight.hints.presentation.SequencePresentation;
import com.intellij.codeInsight.hints.settings.InlayHintsConfigurable;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.lang.Language;
import com.intellij.lang.ecmascript6.psi.ES6ClassExpression;
import com.intellij.lang.javascript.JavascriptLanguage;
import com.intellij.lang.javascript.psi.JSFunction;
import com.intellij.lang.javascript.psi.ecma6.impl.ES6FieldImpl;
import com.intellij.openapi.editor.BlockInlayPriority;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.SmartList;
import com.intellij.util.ui.UI;
import kotlin.Unit;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Provide inlay hints for JSX language.
 */
public class JavaScriptJSXCodeVisionProvider implements InlayHintsProvider<NoSettings> {
  private static final String CODE_LENS_ID = "JavaScriptUsages";

  private static final SettingsKey<NoSettings> KEY = new SettingsKey<>(CODE_LENS_ID);

  interface InlResult {
    void onClick(@NotNull Editor editor, @NotNull PsiElement element, @NotNull MouseEvent event);

    @NotNull
    String getRegularText();

    default Icon getIcon() {
      return null;
    }
  }

  @Nullable
  @Override
  public InlayHintsCollector getCollectorFor(@NotNull PsiFile file,
                                             @NotNull Editor editor,
                                             @NotNull NoSettings settings,
                                             @NotNull InlayHintsSink __) {
    return new FactoryInlayHintsCollector(editor) {
      @Override
      public boolean collect(@NotNull PsiElement element, @NotNull Editor editor, @NotNull InlayHintsSink sink) {
        if(!hintsEnabled()) {
          return true;
        }

        // Only apply to class and functions, ES6 functions so far
        if (!(element instanceof ES6ClassExpression || element instanceof JSFunction || element instanceof ES6FieldImpl)) return true;
//        if(! (element instanceof PsiElementBase)) {return true;}
        if (JavaScriptUnusedSymbolUtil.isEmptyOrIgnoreName(element)) return true;

        if (! (element instanceof ES6ClassExpression || element instanceof ES6FieldImpl)) {
          PsiElement prevSibling = element.getPrevSibling();
          if (!(prevSibling instanceof PsiWhiteSpace && prevSibling.textContains('\n'))) return true;
        }

//        PsiElementBase member = (PsiElementBase)element;
//        if (member.getName() == null) return true;

        List<InlResult> hints = new SmartList<>();

        String usagesHint = JavaScriptTelescope.usagesHint(element, file);
        if (usagesHint != null) {
          hints.add(new InlResult() {
            @Override
            public void onClick(@NotNull Editor editor, @NotNull PsiElement element, @NotNull MouseEvent event) {
              GotoDeclarationAction.startFindUsages(editor, file.getProject(), element, new RelativePoint(event));
            }

            @NotNull
            @Override
            public String getRegularText() {
              return usagesHint;
            }
          });
        }

        if (!hints.isEmpty()) {
          PresentationFactory factory = getFactory();
          Document document = editor.getDocument();
          int offset = PsiEditorUtil.getAnchorOffset(element);
          int line = document.getLineNumber(offset);
          int startOffset = document.getLineStartOffset(line);
          int column = offset - startOffset;
          List<InlayPresentation> presentations = new SmartList<>();
//          presentations.add(factory.textSpacePlaceholder(column, true));// 兼容 IDEA 2020.1
          for (InlResult inlResult : hints) {
            if(inlResult.getIcon() != null) {
              presentations.add(factory.icon(inlResult.getIcon()));//));
            }

            presentations.add(createPresentation(factory, element, editor, inlResult));
//            presentations.add(factory.textSpacePlaceholder(1, true));// 兼容 IDEA 2020.1
          }
          SequencePresentation shiftedPresentation = new SequencePresentation(presentations);
          InlayPresentation withSettings = addSettings(element.getProject(), factory, shiftedPresentation);
          // addBlockElement(offset: Int, relatesToPrecedingText: Boolean, showAbove: Boolean, priority: Int, presentation: InlayPresentation)
          sink.addBlockElement(startOffset, true, true, BlockInlayPriority.CODE_VISION, withSettings);// 代码行上新起一行的提示
//          sink.addInlineElement(startOffset,true, withSettings);// 加在行首
//          sink.addInlineElement(startOffset, (RootInlayPresentation<?>) withSettings,
//          new HorizontalConstraints(BlockInlayPriority.CODE_VISION, true)); // 加行尾的尚未调试通过
        }
        return true;
      }
    };
  }

  static boolean hintsEnabled() {
    return InlayHintsSettings.instance().hintsEnabled(KEY, JavascriptLanguage.INSTANCE);
  }


  @NotNull
  private static InlayPresentation createPresentation(@NotNull PresentationFactory factory,
                                                      @NotNull PsiElement element,
                                                      @NotNull Editor editor,
                                                      @NotNull InlResult result) {
    InlayPresentation text = factory.smallText(result.getRegularText());

    return factory.referenceOnHover(text, (event, translated) -> result.onClick(editor, element, event));
  }

  // Add Settings to inlay context menu
  private static @NotNull InlayPresentation addSettings(@NotNull Project project,
                                                        @NotNull PresentationFactory factory,
                                                        @NotNull InlayPresentation presentation) {
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem item = new JMenuItem("Settings");
    item.addActionListener(e -> {
      InlayHintsConfigurable.showSettingsDialogForLanguage(project, JavascriptLanguage.INSTANCE);
      //, model -> model.getId().equals(CODE_LENS_ID));
    });
    popupMenu.add(item);

    return factory.onClick(presentation, MouseButton.Right, (e, __) -> {
      popupMenu.show(e.getComponent(), e.getX(), e.getY());
      return Unit.INSTANCE;
    });
  }

  @NotNull
  @Override
  public NoSettings createSettings() {
    return new NoSettings();
  }

  @NotNull
  @Override
  public @Nls String getName() {
    return "Show Usages";
  }

  public static final String RELATED_PROBLEMS_ID = "Show Usages";

  @NotNull
  @Override
  public SettingsKey<NoSettings> getKey() {
    return KEY;
  }


  @Nullable
  @Override
  public String getPreviewText() {
    return null;
  }

  @NotNull
  @Override
  public ImmediateConfigurable createConfigurable(@NotNull NoSettings settings) {
    return new ImmediateConfigurable() {
      @NotNull
      @Override
      public JComponent createComponent(@NotNull ChangeListener listener) {
        JPanel panel = UI.PanelFactory.panel(new JLabel()).
                withComment("This will display JSX class and method usages in editor, " +
                    "provided by the React Native Console Free plugin.").createPanel();


//        JPanel panel = new JPanel();
//        panel.setVisible(true);
//        panel.add(new JLabel("This will display JSX class and method usages in editor"));
        return panel;
      }
    };
  }

  @Override
  public boolean isLanguageSupported(@NotNull Language language) {
    return true;
  }

  @Override
  public boolean isVisibleInSettings() {
    return true;
  }

}

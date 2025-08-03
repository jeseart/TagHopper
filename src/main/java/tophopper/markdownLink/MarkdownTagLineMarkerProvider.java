package tophopper.markdownLink;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.util.FunctionUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MarkdownTagLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public LineMarkerInfo getLineMarkerInfo(PsiElement psiElement) {
        MarkdownTagVisitor visitor = new MarkdownTagVisitor();
        psiElement.accept(visitor);
        PsiComment markdownTag = visitor.getMarkdownTag();
        Icon pumlIcon = IconLoader.getIcon("/markdown.svg", MarkdownTagLineMarkerProvider.class);
        if (markdownTag != null) {
            return new LineMarkerInfo<PsiElement>(
                    psiElement,
                    psiElement.getTextRange(),
                    pumlIcon,
                    FunctionUtil.constant("跳转到文档"),
                    new MarkdownGutterIconNavigationHandler(),
                    GutterIconRenderer.Alignment.LEFT
            );
        } else {
            return null;
        }
    }
}

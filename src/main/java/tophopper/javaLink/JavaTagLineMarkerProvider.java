package tophopper.javaLink;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.util.FunctionUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class JavaTagLineMarkerProvider implements LineMarkerProvider {
    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public LineMarkerInfo getLineMarkerInfo(PsiElement psiElement) {
        JavaTagVisitor visitor = new JavaTagVisitor();
        psiElement.accept(visitor);
        PsiComment javaTag = visitor.getJavaTag();
        Icon javaIcon = IconLoader.getIcon("/java.svg", JavaTagLineMarkerProvider.class);
        if (javaTag != null) {
//            System.out.println("找到图标，开始标记");
            return new LineMarkerInfo<PsiElement>(
                    psiElement,
                    psiElement.getTextRange(),
                    javaIcon,
                    FunctionUtil.constant("跳转到Java引用"),
                    new JavaGutterIconNavigationHandler(),
                    GutterIconRenderer.Alignment.LEFT);
        } else {
//            System.out.println("找不到图标");
            return null;
        }
    }

}

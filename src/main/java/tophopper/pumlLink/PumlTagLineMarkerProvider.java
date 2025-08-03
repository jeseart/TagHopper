package tophopper.pumlLink;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.util.FunctionUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PumlTagLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public LineMarkerInfo getLineMarkerInfo(PsiElement psiElement) {
        PumlTagVisitor visitor = new PumlTagVisitor();
        psiElement.accept(visitor);
        PsiComment pumlTag = visitor.getPumlTag();
        Icon pumlIcon = IconLoader.getIcon("/uml.svg", PumlTagLineMarkerProvider.class);
        if (pumlTag != null) {
            return new LineMarkerInfo<PsiElement>(
                    psiElement,
                    psiElement.getTextRange(),
                    pumlIcon,
                    FunctionUtil.constant("打开PlantUml文件"),
                    new PumlGutterIconNavigationHandler(),
                    GutterIconRenderer.Alignment.LEFT
            );
        } else {
//            System.out.println("找不到图标");
            return null;
        }
    }
}

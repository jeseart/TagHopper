package tophopper.folderLink;

//https://xujin.org/guides/idea-02/

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.util.FunctionUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FolderTagLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public LineMarkerInfo getLineMarkerInfo(PsiElement psiElement) {
        FolderTagVisitor visitor = new FolderTagVisitor();
        psiElement.accept(visitor);
        PsiComment folderTag = visitor.getFolderTag();
        Icon pumlIcon = IconLoader.getIcon("/folder.svg", FolderTagLineMarkerProvider.class);
        if (folderTag != null) {


//            System.out.println("找到图标，开始标记");
            //noinspection deprecation
            return new LineMarkerInfo<PsiElement>(
                    psiElement,
                    psiElement.getTextRange(),
                    pumlIcon,
                    FunctionUtil.constant("跳转到目录"),
                    new FolderGutterIconNavigationHandler(),
                    GutterIconRenderer.Alignment.LEFT
            );
        } else {
//            System.out.println("找不到图标");
            return null;
        }
    }
}

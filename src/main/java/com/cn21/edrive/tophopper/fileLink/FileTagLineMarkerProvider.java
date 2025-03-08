package com.cn21.edrive.tophopper.fileLink;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.util.FunctionUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class FileTagLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public LineMarkerInfo getLineMarkerInfo(PsiElement psiElement) {
        FileTagVisitor visitor = new FileTagVisitor();
        psiElement.accept(visitor);
        PsiComment fileTag = visitor.getFileTag();
        Icon pumlIcon = IconLoader.getIcon("/word2.svg", FileTagLineMarkerProvider.class);
        if (fileTag != null) {


//            System.out.println("找到图标，开始标记");
            //noinspection deprecation
            return new LineMarkerInfo<PsiElement>(
                    psiElement,
                    psiElement.getTextRange(),
                    pumlIcon,
                    FunctionUtil.constant("跳转到文档"),
                    new FileGutterIconNavigationHandler(),
                    GutterIconRenderer.Alignment.LEFT
            );
        } else {
//            System.out.println("找不到图标");
            return null;
        }
    }
}

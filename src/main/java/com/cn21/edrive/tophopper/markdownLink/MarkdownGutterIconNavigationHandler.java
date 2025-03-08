package com.cn21.edrive.tophopper.markdownLink;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;

import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownGutterIconNavigationHandler implements GutterIconNavigationHandler<PsiElement> {

    @Override
    public void navigate(MouseEvent e, PsiElement elt) {
        if (elt != null) {
            String markdownTag = getMarkdownTag(elt);
            if (markdownTag != null) {
                String filePath = extractMarkdownPath(markdownTag);
                openFile(elt.getProject(), filePath);
            }else {
                Messages.showMessageDialog(elt.getProject(), "找不到文件，格式错误!\n正确示范\n【根目录下全路径】\nedrive-prod-corp/doc/jump.md", "Error", Messages.getErrorIcon());
            }
        }
    }

    private String getMarkdownTag(PsiElement elt) {
        if (elt instanceof PsiComment) {
            String commentText = elt.getText();
            Pattern pattern = Pattern.compile("@md\\s+(\\S+(?:\\.\\S+)+)");
            Matcher matcher = pattern.matcher(commentText);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String extractMarkdownPath(String markdownTag) {
        return markdownTag;
    }

    private void openFile(Project project, String filePath) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(project.getBasePath() + "/" + filePath);
        if (file != null) {
            FileEditorManager.getInstance(project).openFile(file, true);
        } else {
            Messages.showMessageDialog(project, "找不到文件: " + filePath, "Error", Messages.getErrorIcon());
        }
    }
}
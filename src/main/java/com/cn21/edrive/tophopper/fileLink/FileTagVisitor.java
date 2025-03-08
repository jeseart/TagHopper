package com.cn21.edrive.tophopper.fileLink;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElementVisitor;

public class FileTagVisitor extends PsiElementVisitor {
    private PsiComment fileTag = null;

    @Override
    public void visitComment(PsiComment comment) {
        String commentText = comment.getText();
        if (commentText.contains("@doc")) {
            fileTag = comment;
        }
    }

    public PsiComment getFileTag() {
        return this.fileTag;
    }
}
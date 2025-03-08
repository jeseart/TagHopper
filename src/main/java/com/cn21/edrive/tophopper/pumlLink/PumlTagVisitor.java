package com.cn21.edrive.tophopper.pumlLink;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElementVisitor;

public class PumlTagVisitor extends PsiElementVisitor {
    private PsiComment pumlTag = null;

    @Override
    public void visitComment(PsiComment comment) {
        String commentText = comment.getText();
        if (commentText.contains("@puml")) {
            pumlTag = comment;
        }
    }

    public PsiComment getPumlTag() {
        return this.pumlTag;
    }
}
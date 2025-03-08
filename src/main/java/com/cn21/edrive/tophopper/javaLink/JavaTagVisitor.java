package com.cn21.edrive.tophopper.javaLink;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElementVisitor;

public class JavaTagVisitor extends PsiElementVisitor {

    private PsiComment javaTag = null;

    @Override
    public void visitComment(PsiComment comment) {
        String commentText = comment.getText();
        if (commentText.contains("@java")) {
            javaTag = comment;
        }
    }

    public PsiComment getJavaTag() {
        return this.javaTag;
    }

}

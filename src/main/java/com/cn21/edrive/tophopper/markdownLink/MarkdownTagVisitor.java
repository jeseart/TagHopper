package com.cn21.edrive.tophopper.markdownLink;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElementVisitor;

public class MarkdownTagVisitor extends PsiElementVisitor {
    private PsiComment markdownTag = null;

    @Override
    public void visitComment(PsiComment comment) {
        String commentText = comment.getText();
        if (commentText.contains("@md")) {
            markdownTag = comment;
        }
    }

    public PsiComment getMarkdownTag() {
        return this.markdownTag;
    }
}
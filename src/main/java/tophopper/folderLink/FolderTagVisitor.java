package tophopper.folderLink;

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElementVisitor;

public class FolderTagVisitor extends PsiElementVisitor {
    private PsiComment folderTag = null;

    @Override
    public void visitComment(PsiComment comment) {
        String commentText = comment.getText();
        if (commentText.contains("@folder")) {
            folderTag = comment;
        }
    }

    public PsiComment getFolderTag() {
        return this.folderTag;
    }
}
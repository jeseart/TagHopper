package tophopper.folderLink;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;

import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FolderGutterIconNavigationHandler implements GutterIconNavigationHandler<PsiElement> {
    @Override
    public void navigate(MouseEvent e, PsiElement elt) {
        if (elt != null) {
            String folderTag = getFolderTag(elt);
            if (folderTag != null) {
                String filePath = extractFolderPath(folderTag);
                openFolder(elt.getProject(), filePath);
            }else {
                Messages.showMessageDialog(elt.getProject(), "找不到文件，格式错误!\n正确示范\n【根目录下全路径】\nedrive-prod-corp/doc/", "Error", Messages.getErrorIcon());
            }
        }
    }

    private String getFolderTag(PsiElement elt) {
        if (elt instanceof PsiComment) {
            String commentText = elt.getText();
            Pattern pattern = Pattern.compile("@folder\\s+(.*)");
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

    private String extractFolderPath(String folderTag) {
        return folderTag;
    }

    private void openFolder(Project project, String folderPath) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(project.getBasePath() + "/" + folderPath);

        if (file != null && file.isDirectory()) {
            PsiDirectory psiDirectory = PsiManager.getInstance(project).findDirectory(file);
            if (psiDirectory != null) {
                psiDirectory.navigate(true);
            }
        } else {
            Messages.showMessageDialog(project, "找不到文件夹: " + folderPath, "Error", Messages.getErrorIcon());
        }


    }
}
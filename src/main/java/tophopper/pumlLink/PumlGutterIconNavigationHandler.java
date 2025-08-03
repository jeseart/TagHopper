package tophopper.pumlLink;

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

public class PumlGutterIconNavigationHandler implements GutterIconNavigationHandler<PsiElement> {

    @Override
    public void navigate(MouseEvent e, PsiElement elt) {
        if (elt != null) {
            String pumlTag = getPumlTag(elt);
            if (pumlTag != null) {
                String pumlFilePath = extractPumlFilePath(pumlTag);
                openPumlFile(elt.getProject(), pumlFilePath);
            }else {
                Messages.showMessageDialog(elt.getProject(), "找不到文件，格式错误!\n正确示范\n【根目录下全路径】\nedrive-prod-corp/doc/jump.puml", "Error", Messages.getErrorIcon());
            }
        }
    }

    private String getPumlTag(PsiElement elt) {
        if (elt instanceof PsiComment) {
            String commentText = elt.getText();
            Pattern pattern = Pattern.compile("@puml\\s+(.*)\\.puml");
            Matcher matcher = pattern.matcher(commentText);
            if (matcher.find()) {
                return matcher.group(1) + ".puml";
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String extractPumlFilePath(String pumlTag) {
        return pumlTag;
    }

    private void openPumlFile(Project project, String pumlFilePath) {
//        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();
//
//        VirtualFile projectFile = fileIndex.getContentRootForFile(project.getProjectFile(), true);
//        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(projectFile.getPath() + "/" + pumlFilePath);
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(project.getBasePath() + "/" + pumlFilePath);
        if (file != null) {
            FileEditorManager.getInstance(project).openFile(file, true);
        } else {
            Messages.showMessageDialog(project, "找不到文件: " + pumlFilePath, "Error", Messages.getErrorIcon());
        }
    }
}
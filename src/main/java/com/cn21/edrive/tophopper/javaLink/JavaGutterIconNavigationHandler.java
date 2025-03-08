package com.cn21.edrive.tophopper.javaLink;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.lang3.StringUtils;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaGutterIconNavigationHandler implements GutterIconNavigationHandler<PsiElement> {


    @Override
    public void navigate(MouseEvent e, PsiElement elt) {
        if (elt != null) {
            String commentText = elt.getText();
            if(!commentText.contains(".java") && (!commentText.contains("(") || !commentText.contains(")")) ){
                Messages.showMessageDialog(elt.getProject(), "格式错误!\n正确示范:\n【根目录下全路径】\n edrive-prod-corp/src/Jump.java;\n【命中的第一个方法引用】\nJump#jumpMethod(java.lang.Long)\n【模块名+方法引用】\n{corp-portal}Jump#jumpMethod(java.lang.Long)", "Error", Messages.getErrorIcon());
                return;
            }
            if(commentText.contains(".java") && (commentText.contains("(") && commentText.contains(")")) ){
                Messages.showMessageDialog(elt.getProject(), "格式错误!\n正确示范:\n【根目录下全路径】\n edrive-prod-corp/src/Jump.java;\n【命中的第一个方法引用】\nJump#jumpMethod(java.lang.Long)\n【模块名+方法引用】\n{corp-portal}Jump#jumpMethod(java.lang.Long)", "Error", Messages.getErrorIcon());
                return;
            }

            String javaTag = getJavaTag(elt);
            String moduleName = getModuleName(elt);
            if (javaTag != null) {
                try {
                    String javaFilePath = extractJavaFilePath(javaTag);
                    if (javaFilePath.endsWith(".java")) {
                        openJavaFile(elt.getProject(), javaFilePath);
                    }else if(javaFilePath.matches(".*\\.java:\\d+")) {
                        openJavaFileAndJumpToLine(elt.getProject(),javaFilePath);
                    }else {
                        navigateToMethod(elt.getProject(), javaFilePath, moduleName);
                    }
                }catch (Exception ext){
                    Messages.showMessageDialog(elt.getProject(), "解析出现异常！"+ext.getMessage(), "Error", Messages.getErrorIcon());
                }
            }else{
                Messages.showMessageDialog(elt.getProject(), "没有找到引用位置！", "Error", Messages.getErrorIcon());
            }
        }
    }

    private void openJavaFileAndJumpToLine(Project project, String javaFilePath) {

        ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

        VirtualFile projectFile = fileIndex.getContentRootForFile(project.getProjectFile(), true);

        String[] split = javaFilePath.split(":");
        String realJavaFilePath = split[0];
        int lineNum = Integer.valueOf(split[1]);

        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(projectFile.getPath() + "/" + realJavaFilePath);
        if (file != null) {
            // 从VirtualFile获取PsiFile
            PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
            if (psiFile != null) {
                // 获取文件对应的编辑器
                Editor editor = FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, file, lineNum - 1), true);
                if (editor != null) {
                    // 将光标移动到指定行号
                    editor.getCaretModel().moveToLogicalPosition(new LogicalPosition(lineNum - 1, 0));
                    // 滚动到可见位置
                    editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                }
            }
        } else {
            Messages.showMessageDialog(project, "找不到文件: " + javaFilePath, "Error", Messages.getErrorIcon());
        }



    }

    private String getJavaTag(PsiElement elt) {
        if (elt instanceof PsiComment) {
            String commentText = elt.getText();

            Pattern pattern = Pattern.compile("@java\\s+(.*)");
            int index = 1;
            if(commentText.contains("{") && commentText.contains("}")){
                pattern = Pattern.compile("@java\\s*\\{(.*?)\\}+(.*)");
                index=2;
            }

            Matcher matcher = pattern.matcher(commentText);
            if (matcher.find()) {
                return matcher.group(index);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private String getModuleName(PsiElement elt){
        if(elt instanceof PsiComment){
            String commentText = elt.getText();
            Pattern pattern = Pattern.compile("@java\\s*\\{(.*?)\\}");
            Matcher matcher = pattern.matcher(commentText);
            if (matcher.find()) {
                return matcher.group(1);
            } else {
                return null;
            }
        }else{
            return null;
        }

    }

    private String extractJavaFilePath(String javaTag) {
        return javaTag;
    }

    private void openJavaFile(Project project, String javaFilePath) {
        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(project.getBasePath() + "/" + javaFilePath);
        if (file != null) {
            FileEditorManager.getInstance(project).openFile(file, true);
        } else {
            Messages.showMessageDialog(project, "找不到文件: " + javaFilePath, "Error", Messages.getErrorIcon());
        }
    }


    public static void navigateToMethod(Project project, String fullMethodName,String moduleName) {

        JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);

        String[] parts = fullMethodName.split("#");
        String className = parts[0];
        String methodName = parts[1].split("\\(")[0];
        String[] paramArray = parts[1].split("\\(")[1].split("\\)");
        String methodParams = "";
        if (paramArray.length > 0) {
            methodParams = paramArray[0].replaceAll("\\s", "");
        }

        PsiClass[] psiClasses = psiFacade.findClasses(className, GlobalSearchScope.allScope(project));
        if(psiClasses != null && psiClasses.length>0){
            for (PsiClass psiClass : psiClasses) {
                PsiMethod[] methods = psiClass.findMethodsByName(methodName, false);
                for (PsiMethod method : methods) {
                    // 检查方法签名是否匹配
                    Module moduleForPsiElement = ModuleUtil.findModuleForPsiElement(method);
                    String currentModuleName = moduleForPsiElement.getName();

                    ///如果指定了模块名，对子模块进行匹配
                    if(StringUtils.isNotEmpty(moduleName) && !currentModuleName.endsWith(moduleName)){
                        continue;
                    }
                    //拿到方法入参类型列表
                    List<String> psiParams = Arrays.stream(method.getParameterList().getParameters()).map(psiParameter -> psiParameter.getType().getCanonicalText()).collect(Collectors.toList());

                    //注释中的参数列表
                    List<String> commentParams = "".equals(methodParams)?new ArrayList<>():Arrays.asList(methodParams.split(","));

                    if (matchParams(psiParams,commentParams)){
                        method.navigate(true);
                        return;
                    }
                }
            }
            Messages.showMessageDialog(project, "没有找到符合条件的引用,请检查格式或路径参数", "Error", Messages.getErrorIcon());
        }else {
            Messages.showMessageDialog(project, "没有找到指定类", "Error", Messages.getErrorIcon());
        }

    }

    /**
     * 检查两个List的元素是否模糊匹配
     * @param psiParams
     * @param commentParams
     * @return
     */
    private static boolean matchParams(List<String> psiParams, List<String> commentParams) {
        if(psiParams.size() != commentParams.size()){
            return false;
        }
        if (psiParams.isEmpty() && commentParams.isEmpty()){
            return true;
        }
        boolean match = true;
        for (int i = 0; i < commentParams.size(); i++) {
            String psiParam = psiParams.get(i);
            String commentParam = commentParams.get(i);

            if( !(psiParam.contains(commentParam) || commentParam.contains(psiParam)) ){
                match= false;
            }

        }

        return match;
    }


}

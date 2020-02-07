import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import mfix.common.util.Method;

import java.util.ArrayList;

public abstract class AnActionWithInit extends AnAction {
    Project project;
    PsiFile psiFile;
    String classPath;
    PsiElement psiCurrentUnit;
    PsiElement currentPsiElementContext;
    PsiElementFactory currentProjectFactory;

    void init(AnActionEvent e) throws GenpatInitException {
        project = e.getData(PlatformDataKeys.PROJECT);
        currentProjectFactory = JavaPsiFacade.getInstance(project).getElementFactory();
        psiCurrentUnit = e.getData(CommonDataKeys.PSI_ELEMENT);
        psiFile = e.getData(CommonDataKeys.PSI_FILE);
        classPath = psiFile.getVirtualFile().getPath();
        if (psiCurrentUnit == null) {
            Editor editor = e.getData(PlatformDataKeys.EDITOR);
            int caretOffset = editor.getCaretModel().getOffset();
            psiCurrentUnit = psiFile.findElementAt(caretOffset);
        }

        psiCurrentUnit = PsiTreeUtil.getParentOfType(psiCurrentUnit, PsiMethod.class, false);

//        if (psiCurrentUnit == null) {
//            Messages.showMessageDialog(project, "Failed!", "WARNING", null);
//            throw new GenpatInitException();
//        }

        if (psiCurrentUnit != null) {
            currentPsiElementContext = psiCurrentUnit.getContext();
        }
    }

    public Method methodTransPsi2Genpat(PsiMethod m) {
        String methodName = m.getName();
        ArrayList<String> paras = new ArrayList<>();
        for (PsiParameter para : m.getParameterList().getParameters()) {
            paras.add(para.getType().getPresentableText());
        }
        String retType = m.getReturnType().getPresentableText();
        return new Method(retType, methodName, paras);
    }

    public void replace(PsiElement oldElement, PsiElement newElement) {
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                oldElement.replace(newElement);
            }
        });
    }

    public PsiMethod str2PsiMethod(String str) {
        return currentProjectFactory.createMethodFromText(str, currentPsiElementContext);
    }

}

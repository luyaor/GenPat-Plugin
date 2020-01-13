import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;

public abstract class AnActionWithInit extends AnAction {
    Project project;
    PsiFile psiFile;
    String classPath;
    PsiElement psiCurrentUnit;

    void init(AnActionEvent e) throws GenpatInitException {
        project = e.getData(PlatformDataKeys.PROJECT);
        psiCurrentUnit = e.getData(CommonDataKeys.PSI_ELEMENT);
        psiFile = e.getData(CommonDataKeys.PSI_FILE);
        classPath = psiFile.getVirtualFile().getPath();
        if (psiCurrentUnit == null) {
            Editor editor = e.getData(PlatformDataKeys.EDITOR);
            int caretOffset = editor.getCaretModel().getOffset();
            psiCurrentUnit = psiFile.findElementAt(caretOffset);
        }
        psiCurrentUnit = PsiTreeUtil.getParentOfType(psiCurrentUnit, PsiMethod.class, false);
        if (psiCurrentUnit == null) {
            Messages.showMessageDialog(project, "Failed!", "WARNING", null);
            throw new GenpatInitException();
        }
    }
}

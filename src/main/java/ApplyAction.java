import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

public class ApplyAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
//        CaretModel caretModel = editor.getCaretModel();
//        String selectedText = caretModel.getCurrentCaret().getSelectedText();

        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();

        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        PsiElement psiElementContext = psiElement.getContext();
        try {
            String ret = TraceAction.newStr;
            PsiElement newpsielement = factory.createCodeBlockFromText(ret, psiElementContext);
//            PsiElement newpsielement = factory.createMethodFromText(ret, null);

            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
//                    psielement.replace(newpsielement);
                    if (psiElement instanceof PsiMethod) {
                        ((PsiMethod) psiElement).getBody().replace(newpsielement);
                    }
                }
            });

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}

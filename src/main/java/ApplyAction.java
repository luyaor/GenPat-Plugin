import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import mfix.tools.Transformer;

public class ApplyAction extends AnActionWithInit {
    static Transformer transformer = new Transformer();

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        PsiElement psiElementContext = psiCurrentUnit.getContext();
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();

        try {
            String ret = transformer.apply(psiCurrentUnit.getText(), classPath);

            PsiElement newpsielement = factory.createMethodFromText(ret, psiElementContext);

            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    if (newpsielement instanceof PsiMethod) {
                        psiCurrentUnit.replace(newpsielement);
                    }
                }
            });

        } catch (Exception err) {
            Messages.showMessageDialog(project, "Apply Failed!", "WARNING", null);
            err.printStackTrace();
        }
    }
}

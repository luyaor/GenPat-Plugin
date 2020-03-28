import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import mfix.tools.Transformer;

public class ApplyAction extends AnActionWithInit {
    static Transformer transformer;

    public static void setTransformer(Transformer transformer) {
        ApplyAction.transformer = transformer;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        try {
            String ret = transformer.apply(methodTransPsi2Genpat((PsiMethod) psiCurrentUnit), classPath);
            replace(psiCurrentUnit, str2PsiMethod(ret));
        } catch (Exception err) {
            Messages.showMessageDialog(project, "Apply Failed!", "WARNING", null);
            err.printStackTrace();
        }
    }
}

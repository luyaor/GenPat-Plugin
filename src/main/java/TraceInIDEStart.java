import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiMethod;
import mfix.tools.Transformer;

public class TraceInIDEStart extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        Messages.showMessageDialog(project, "Trace Start!", "GenPat-Plugin", Messages.getInformationIcon());

        ApplyAction.transformer = new Transformer();
        ApplyAction.transformer.loadPatternSrc(methodTransPsi2Genpat((PsiMethod) psiCurrentUnit), psiFile.getText(), classPath);
    }
}

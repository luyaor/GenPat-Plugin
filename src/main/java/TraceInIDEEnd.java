import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiMethod;
import mfix.tools.Transformer;

public class TraceInIDEEnd extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        Transformer newTrans = new Transformer();
        newTrans.loadPatternSrc(methodTransPsi2Genpat((PsiMethod) TraceInIDEStart.startPsiElement), TraceInIDEStart.startText, classPath);
        newTrans.loadPatternTar(methodTransPsi2Genpat((PsiMethod) psiCurrentUnit), psiFile.getText(), classPath);
        newTrans.extractPattern();

        ServiceManager.getService(GenpatService.class).addTrans(newTrans);
        ApplyAction.setTransformer(newTrans);

        Messages.showMessageDialog(project, "Trace Finish!", "GenPat-Plugin", Messages.getInformationIcon());
    }
}

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

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
        ApplyAction.transformer.loadPatternSrc(psiCurrentUnit.getText(), psiFile.getText(), classPath);
    }
}

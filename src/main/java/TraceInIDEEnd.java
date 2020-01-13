import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;

public class TraceInIDEEnd extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        ApplyAction.transformer.loadPatternTar(psiCurrentUnit.getText(), psiFile.getText(), classPath);
        ApplyAction.transformer.extractPattern();
        Messages.showMessageDialog(project, "Trace Finish!", "GenPat-Plugin", Messages.getInformationIcon());
    }
}

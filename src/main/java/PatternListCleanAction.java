import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.Messages;

public class PatternListCleanAction extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        GenpatService applicationService = ServiceManager.getService(GenpatService.class);
        applicationService.getState().transList.clear();
        Messages.showMessageDialog(project, "Clear Success!", "WARNING", Messages.getInformationIcon());
    }
}

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;

public class PostStartupActivity implements StartupActivity {
    public void runActivity(Project project) {
        GenpatService applicationService = ServiceManager.getService(GenpatService.class);
        applicationService.getState().transList.clear();
    }
}
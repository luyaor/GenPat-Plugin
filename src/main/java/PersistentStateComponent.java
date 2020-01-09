import com.intellij.openapi.components.ServiceManager;

public interface PersistentStateComponent {
    static PersistentStateComponent getInstance() {
        return ServiceManager.getService(PersistentStateComponent.class);
    }
}

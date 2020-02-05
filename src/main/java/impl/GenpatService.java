package impl;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import org.jetbrains.annotations.NotNull;

public class GenpatService implements PersistentStateComponent {
    static class State {
        public int maxChoices = 1;
    }

    State myState;

    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull Object state) {

    }

}

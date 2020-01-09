package impl;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import org.jetbrains.annotations.NotNull;

public class GenpatService implements PersistentStateComponent {
    static class State {
        public int editChoice = 0; // 0 for popup view, 1 for editing in IDE.
    }

    State myState;

    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull Object state) {

    }

    public void loadState(State state) {
        myState = state;
    }
}

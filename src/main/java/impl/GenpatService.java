package impl;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import mfix.core.pattern.Pattern;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GenpatService implements PersistentStateComponent {
    static class State {
        public int maxChoices = 1;
    }

    State myState;
    private static List<Pattern> patternList = new ArrayList<>();

    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull Object state) {

    }

    public static void savePattern(Pattern pattern) {
        patternList.add(pattern);
    }

    public static List<Pattern> getPatternList() {
        return patternList;
    }
}

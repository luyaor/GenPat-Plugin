import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.util.xmlb.XmlSerializerUtil;
import mfix.tools.Transformer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@State(name = "GenpatService", storages = {@Storage("GenpatService.xml")})
public class GenpatService implements PersistentStateComponent<GenpatService.State> {
    static class State {
        public int maxChoices;
        public List<Transformer> transList;
    }

    State myState;

    GenpatService() {
        myState = new State();
        myState.maxChoices = 1;
        myState.transList = new ArrayList<>();
    }

    public State getState() {
        return myState;
    }

    public void loadState(@NotNull State state) {
        myState = state;
    }


    public void addTrans(Transformer pattern) {
        myState.transList.add(pattern);
    }

}

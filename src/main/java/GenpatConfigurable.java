import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GenpatConfigurable implements SearchableConfigurable {

    GenpatConfigurableGUI mGUI;

    @Nls
    @Override
    public String getDisplayName() {
        return "GenPat Plugin";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preference.GenpatConfigurable";
    }

    @NotNull
    @Override
    public String getId() {
        return "preference.GenpatConfigurable";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mGUI = new GenpatConfigurableGUI();
        return mGUI.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {
        mGUI = null;
    }

}

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBScrollPane;
import mfix.tools.Transformer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PatternLoadAction extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        GenpatService applicationService = ServiceManager.getService(GenpatService.class);
        MultiTransViewer viewer = new MultiTransViewer(project);
        for (Transformer trans : applicationService.getState().transList) {
            try {
                viewer.addTrans(trans);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        viewer.addselectButton();

        JFrame frame = new JFrame("Pattern List");
        frame.getContentPane().add(viewer.getMultiTransViewer(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import mfix.tools.Transformer;

import javax.swing.*;
import java.awt.*;

public class PatternLoadAction extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        GenpatService applicationService = ServiceManager.getService(GenpatService.class);
        TransListViewer viewer = new TransListViewer(project);
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

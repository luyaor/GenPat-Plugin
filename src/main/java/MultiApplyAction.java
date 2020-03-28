import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.openapi.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MultiApplyAction extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        List<String> candidates = ApplyAction.transformer.applyAll(methodTransPsi2Genpat((PsiMethod) psiCurrentUnit), classPath);

        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);

        if (candidates == null) {
            Messages.showMessageDialog(project, "Apply Failed!", "WARNING", null);
            return;
        }

        TransListViewer viewer = new TransListViewer(project);
        for (String p : candidates) {
            PsiMethod targetMethod = str2PsiMethod(p);
            codeStyleManager.reformat(targetMethod);
            viewer.addMatchedElement(new Pair<>(psiCurrentUnit, targetMethod));
        }
        viewer.addConfirmButton();

        JFrame frame = new JFrame("Multi Apply Result");
        frame.getContentPane().add(viewer.getMultiTransViewer(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

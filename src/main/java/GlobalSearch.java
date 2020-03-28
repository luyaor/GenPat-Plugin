import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;

import javax.swing.*;
import java.awt.*;

public class GlobalSearch extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);

        TransListViewer viewer = new TransListViewer(project);

        psiFile.acceptChildren(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethod(PsiMethod curMethod) {
                String methodPath = curMethod.getContainingFile().getVirtualFile().getPath();
                String ret = ApplyAction.transformer.apply(methodTransPsi2Genpat(curMethod), methodPath);
                if (ret != null) {
                    PsiMethod targetMethod = str2PsiMethod(ret);
                    codeStyleManager.reformat(targetMethod);
                    viewer.addMatchedElement(new Pair<>(curMethod, targetMethod));
                }
            }
        });

        viewer.addConfirmButton();

        JFrame frame = new JFrame("Global Search Result");
        frame.getContentPane().add(viewer.getMultiTransViewer(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

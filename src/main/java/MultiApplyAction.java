import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import mfix.tools.Transformer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
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
        PsiElement psiElementContext = psiCurrentUnit.getContext();
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();

        if (candidates == null) {
            Messages.showMessageDialog(project, "Apply Failed!", "WARNING", null);
            return;
        }


        JPanel all = new JPanel();
        all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));

        JFrame frame = new JFrame("FrameDemo");

        int cnt = 0;
        for (String res : candidates) {
            PsiElement newpsielement;
            try {
                newpsielement = factory.createMethodFromText(res, psiElementContext);
                codeStyleManager.reformat(newpsielement);
                res = newpsielement.getText();
            } catch (Exception err) {
                err.printStackTrace();
                continue;
            }

            cnt += 1;
            Editor editor = Editors.createSourceEditor(project, "JAVA", res, true);
            JButton confirmButton = new JButton("Choice " + cnt, null);
            confirmButton.addActionListener(actionEvent -> {
                WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                    @Override
                    public void run() {
                        if (newpsielement instanceof PsiMethod) {
                            psiCurrentUnit.replace(newpsielement);
                        }
                    }
                });
                frame.dispose();
            });

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(editor.getContentComponent());
            panel.add(confirmButton);

            all.add(panel);
        }

        frame.getContentPane().add(all, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

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
import com.intellij.openapi.util.Pair;

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

        if (candidates == null) {
            Messages.showMessageDialog(project, "Apply Failed!", "WARNING", null);
            return;
        }

        List<Pair<PsiElement, PsiElement>> matchedElements = new ArrayList<>();
        for (String p : candidates) {
            PsiMethod targetMethod = str2PsiMethod(p);
            codeStyleManager.reformat(targetMethod);
            matchedElements.add(new Pair<>(psiCurrentUnit, targetMethod));
        }

        MultiTransViewer viewer = new MultiTransViewer(project, matchedElements);
        JFrame frame = new JFrame("Multi Apply Result");
        frame.getContentPane().add(viewer.getMultiTransViewer(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

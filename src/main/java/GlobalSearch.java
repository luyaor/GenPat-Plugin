import com.intellij.codeInsight.completion.AllClassesGetter;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBScrollBar;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.Processor;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

        List<Pair<PsiElement, PsiElement>> matchedElements = new ArrayList<>();

        psiFile.acceptChildren(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethod(PsiMethod curMethod) {
                String methodPath = curMethod.getContainingFile().getVirtualFile().getPath();
                String ret = ApplyAction.transformer.apply(methodTransPsi2Genpat(curMethod), methodPath);
                if (ret != null) {
                    matchedElements.add(new Pair<>(curMethod, str2PsiMethod(ret)));
                }
            }
        });

        JPanel all = new JPanel();
        all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
        JFrame frame = new JFrame("Global Search Result");

        int cnt = 0;
        for (Pair<PsiElement, PsiElement> p : matchedElements) {
            cnt += 1;
            codeStyleManager.reformat(p.second);

            Editor beforeEditor = Editors.createSourceEditor(project, "JAVA", p.first.getText(), true);
            Editor afterEditor = Editors.createSourceEditor(project, "JAVA", p.second.getText(), true);
            JButton confirmButton = new JButton("Replace", null);
            confirmButton.addActionListener(actionEvent -> {
                WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                    @Override
                    public void run() {
                        replace(p.first, p.second);
                    }
                });
            });

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(beforeEditor.getContentComponent());
            panel.add(afterEditor.getContentComponent());
            panel.add(confirmButton);
            all.add(panel);
        }

        JBScrollPane scroll = new JBScrollPane(all);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

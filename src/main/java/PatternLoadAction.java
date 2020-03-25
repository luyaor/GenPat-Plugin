import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBScrollPane;
import impl.GenpatService;
import mfix.core.pattern.Pattern;

import javax.swing.*;
import java.awt.*;

public class PatternLoadAction extends AnActionWithInit {

    @Override
    public void actionPerformed(AnActionEvent e) {

        JPanel all = new JPanel();
        all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
        JFrame frame = new JFrame("Pattern List");

        int cnt = 0;
        for (Pattern p : GenpatService.getPatternList()) {
            cnt += 1;
            Editor patternEditor = Editors.createSourceEditor(project, "JAVA", p.toString(), true);
            JButton confirmButton = new JButton("Load", null);
            confirmButton.addActionListener(actionEvent -> {
                WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                    @Override
                    public void run() {
//                        ApplyAction.transformer.loadPattern(p);
                    }
                });
            });

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(patternEditor.getContentComponent());
            panel.add(confirmButton);
            all.add(panel);
        }

        JBScrollPane scroll = new JBScrollPane(all);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

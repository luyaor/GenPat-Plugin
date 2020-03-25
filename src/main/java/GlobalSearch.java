import com.intellij.codeInsight.completion.AllClassesGetter;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManagerImpl;
import com.intellij.diff.DiffRequestPanel;
import com.intellij.diff.chains.DiffRequestChain;
import com.intellij.diff.chains.DiffRequestProducer;
import com.intellij.diff.chains.SimpleDiffRequestChain;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.impl.CacheDiffRequestChainProcessor;
import com.intellij.diff.impl.DiffRequestProcessor;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.diff.tools.simple.SimpleDiffViewer;
import com.intellij.diff.util.DiffUserDataKeys;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diff.SimpleContent;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBScrollBar;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.Processor;
import com.intellij.diff.DiffManager;

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
                    PsiMethod targetMethod = str2PsiMethod(ret);
                    codeStyleManager.reformat(targetMethod);
                    matchedElements.add(new Pair<>(curMethod, targetMethod));
                }
            }
        });

//        System.out.println("matched number = " + matchedElements.size());

        MultiTransViewer viewer = new MultiTransViewer(project, matchedElements);
        JFrame frame = new JFrame("Global Search Result");
        frame.getContentPane().add(viewer.getMultiTransViewer(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}

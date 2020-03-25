import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.DiffRequestPanel;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MultiTransViewer extends JPanel
        implements ListSelectionListener {

    private int curIndex;
    private DiffRequestPanel diffPanel;
    JPanel all;

    private Vector<String> transNames;
    private Vector<DiffRequest> diffList;

    public MultiTransViewer(Project project, List<Pair<PsiElement, PsiElement>> matchedElements) {
        transNames = new Vector<>();
        diffList = new Vector<>();

        int cnt = 0;
        for (Pair<PsiElement, PsiElement> p : matchedElements) {
            PsiMethod before = (PsiMethod) p.first;
            PsiMethod after = (PsiMethod) p.second;
            cnt += 1;
            String transName = "#" + cnt + ":" + before.getName();
            transNames.add(transName);
            final DiffContent beforeContent = DiffContentFactory.getInstance().create(before.getText());
            final DiffContent afterContent = DiffContentFactory.getInstance().create(after.getText());
            DiffRequest dr = new SimpleDiffRequest("Diff", beforeContent, afterContent, "Before", "After");
            diffList.add(dr);
        }

        //Create the selectList of images and put it in a scroll pane.
        JList selectList = new JList(transNames);
        selectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectList.setSelectedIndex(0);
        selectList.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(selectList);

        JPanel showDiff = new JPanel();
        showDiff.setLayout(new BoxLayout(showDiff, BoxLayout.X_AXIS));
        showDiff.setLayout(new BoxLayout(showDiff, BoxLayout.Y_AXIS));
        DialogBuilder diffBuilder = new DialogBuilder(project);
        diffPanel = DiffManager.getInstance().createRequestPanel(project, diffBuilder, diffBuilder.getWindow());
        showDiff.add(diffPanel.getComponent());

        JScrollPane pictureScrollPane = new JScrollPane(showDiff);

        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, pictureScrollPane);
        splitPane.setOneTouchExpandable(true);
//        splitPane.setPreferredSize(new Dimension(1500,500));
//        splitPane.setDividerLocation(150);


        JButton confirmButton = new JButton("Replace", null);
//        confirmButton.setPreferredSize(new Dimension(30,30));
        confirmButton.addActionListener(actionEvent -> {
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    matchedElements.get(curIndex).first.replace(matchedElements.get(curIndex).second);
                }
            });
        });

        all = new JPanel();
        all.add(splitPane);
        all.add(confirmButton);

        updateAfterSelect(selectList.getSelectedIndex());
    }

    //Listens to the selectList
    public void valueChanged(ListSelectionEvent e) {
        JList list = (JList) e.getSource();
        updateAfterSelect(list.getSelectedIndex());
    }

    public void updateAfterSelect(int index) {
        curIndex = index;
        diffPanel.setRequest(diffList.get(index));
    }

    public JPanel getMultiTransViewer() {
        return all;
    }
}

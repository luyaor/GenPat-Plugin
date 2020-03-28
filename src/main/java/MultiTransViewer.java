import com.intellij.diff.DiffContentFactory;
import com.intellij.diff.DiffManager;
import com.intellij.diff.DiffRequestPanel;
import com.intellij.diff.contents.DiffContent;
import com.intellij.diff.requests.DiffRequest;
import com.intellij.diff.requests.SimpleDiffRequest;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import mfix.tools.Transformer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MultiTransViewer extends JPanel
        implements ListSelectionListener {

    Project curProject;
    private int curIndex;
    private JPanel mainPanel;
    private DiffRequestPanel diffPanel;
    private DefaultListModel listModel;

    private Vector<DiffRequest> diffList;
    private List<Pair<PsiElement, PsiElement>> matchedElementList;
    private List<Transformer> transformerList;

    public void addDiff(String before, String after) {
        final DiffContent beforeContent = DiffContentFactory.getInstance().create(before);
        final DiffContent afterContent = DiffContentFactory.getInstance().create(after);
        DiffRequest dr = new SimpleDiffRequest("Diff", beforeContent, afterContent, "Before", "After");
        diffList.add(dr);
        if (diffList.size() == 1) {
            updateAfterSelect(0);
        }
    }

    public void addMatchedElement(Pair<PsiElement, PsiElement> matchedElement) {
        matchedElementList.add(matchedElement);
        PsiMethod before = (PsiMethod) matchedElement.first;
        PsiMethod after = (PsiMethod) matchedElement.second;
        String transName = "#" + diffList.size() + ":" + before.getName();
        listModel.addElement(transName);
        addDiff(before.getText(), after.getText());
    }

    public void addTrans(Transformer trans) {
        transformerList.add(trans);
        String transName = "#" + diffList.size();
        listModel.addElement(transName);
        addDiff(trans.getBeforeChangeCode(), trans.getAfterChangeCode());
    }

    public MultiTransViewer(Project project) {
        curProject = project;
        diffList = new Vector<>();
        matchedElementList = new ArrayList<>();
        transformerList = new ArrayList<>();

        listModel = new DefaultListModel();
        //Create the selectList of images and put it in a scroll pane.
        JList selectList = new JList(listModel);
        selectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectList.setSelectedIndex(0);
        selectList.addListSelectionListener(this);

        JScrollPane listScrollPane = new JScrollPane(selectList);

        JPanel showDiff = new JPanel();
        showDiff.setLayout(new BoxLayout(showDiff, BoxLayout.X_AXIS));
        showDiff.setLayout(new BoxLayout(showDiff, BoxLayout.Y_AXIS));
        DialogBuilder diffBuilder = new DialogBuilder(curProject);
        diffPanel = DiffManager.getInstance().createRequestPanel(curProject, diffBuilder, diffBuilder.getWindow());
        showDiff.add(diffPanel.getComponent());

        JScrollPane pictureScrollPane = new JScrollPane(showDiff);

        //Create a split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, pictureScrollPane);
        splitPane.setOneTouchExpandable(true);

        mainPanel = new JPanel();
        mainPanel.add(splitPane);
    }

    public void addConfirmButton() {
        JButton confirmButton = new JButton("Replace", null);
        confirmButton.addActionListener(actionEvent -> {
            WriteCommandAction.runWriteCommandAction(curProject, new Runnable() {
                @Override
                public void run() {
                    matchedElementList.get(curIndex).first.replace(matchedElementList.get(curIndex).second);
                }
            });
        });
        mainPanel.add(confirmButton);
    }

    public void addselectButton() {
        JButton selectButton = new JButton("Select", null);
        selectButton.addActionListener(actionEvent -> {
            WriteCommandAction.runWriteCommandAction(curProject, new Runnable() {
                @Override
                public void run() {
                    ApplyAction.setTransformer(transformerList.get(curIndex));
                    Messages.showMessageDialog(curProject, "Load Success!", "WARNING", Messages.getInformationIcon());
                }
            });
        });
        mainPanel.add(selectButton);
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
        return mainPanel;
    }
}

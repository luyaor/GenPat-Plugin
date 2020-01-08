import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.RunResult;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.InputException;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.psi.*;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TraceAction extends AnAction {

    static String newStr = null;
    private final static Logger LOGGER = Logger.getLogger(TraceAction.class.getName());

    private String changeEditor(Project project, String language, String content) {
        final Editor editor = Editors.createSourceEditor(project, language, content, false);
        try {
            final DialogBuilder builder = new DialogBuilder(project);
            builder.addCloseButton().setText("Save");
            builder.setCenterPanel(editor.getComponent());
            builder.setTitle("Editor");
            builder.show();
        } finally {
            String retSource = editor.getDocument().getText();
            Editors.release(editor);
            return retSource;
        }
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
//        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
//        CaretModel caretModel = editor.getCaretModel();
//        String selectedText = caretModel.getCurrentCaret().getSelectedText();
        Project project = e.getData(PlatformDataKeys.PROJECT);
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        PsiElement psielement = e.getData(CommonDataKeys.PSI_ELEMENT);

//        LOGGER.setLevel(Level.ALL);
//        ConsoleHandler handler = new ConsoleHandler();
//        LOGGER.addHandler(handler);
//        LOGGER.info(psielement.toString());
//        LOGGER.info(psielement.getText());
//        LOGGER.info(psielement.getContext().toString());
//        LOGGER.info(psielement.getChildren().toString());
//        LOGGER.info(psielement.getContainingFile().toString());

        try {
            String str = changeEditor(project, "JAVA", psielement.getText());
//            newStr = changeEditor(project, "JAVA", selectedText);

            PsiElement newMethod = factory.createMethodFromText(str, null);
            if (newMethod instanceof PsiMethod) {
                newStr = ((PsiMethod) newMethod).getBody().getText();
            } else {
                throw new Exception("Input is not a method!");
            }

            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    psielement.replace(newMethod);
                }
            });

            Messages.showMessageDialog(project, "Success!", "GenPat-Plugin", Messages.getInformationIcon());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}

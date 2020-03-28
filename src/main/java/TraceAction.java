import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import mfix.tools.Transformer;

public class TraceAction extends AnActionWithInit {
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
        try {
            init(e);
        } catch (Exception err) {
            err.printStackTrace();
            return;
        }

        Transformer newTrans = new Transformer();

        newTrans.loadPatternSrc(methodTransPsi2Genpat((PsiMethod) psiCurrentUnit), psiFile.getText(), classPath);
        PsiElement newUnit;
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        try {
            String inputStr = changeEditor(project, "JAVA", psiCurrentUnit.getText());
            newUnit = factory.createMethodFromText(inputStr, null);
            if (!(newUnit instanceof PsiMethod)) {
                throw new Exception("Input is not a method!");
            }
            WriteCommandAction.runWriteCommandAction(project, new Runnable() {
                @Override
                public void run() {
                    psiCurrentUnit.replace(newUnit);
                }
            });

            Messages.showMessageDialog(project, "Success!", "GenPat-Plugin", Messages.getInformationIcon());
        } catch (Exception err) {
            Messages.showMessageDialog(project, "Trace Failed!", "WARNING", Messages.getInformationIcon());
            err.printStackTrace();
            return;
        }

        newTrans.loadPatternTar(methodTransPsi2Genpat((PsiMethod) newUnit), psiFile.getText(), classPath);
        newTrans.extractPattern();

        ServiceManager.getService(GenpatService.class).addTrans(newTrans);
        ApplyAction.setTransformer(newTrans);
    }

}

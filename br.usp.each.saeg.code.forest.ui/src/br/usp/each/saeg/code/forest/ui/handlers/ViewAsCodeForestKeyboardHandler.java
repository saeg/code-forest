package br.usp.each.saeg.code.forest.ui.handlers;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.ui.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.project.*;
import br.usp.each.saeg.code.forest.ui.views.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ViewAsCodeForestKeyboardHandler extends OnlyAfterAnalysisHandler {

    @Override
    public Object execute(ExecutionEvent arg) throws ExecutionException {

        IProject project = ProjectUtils.getCurrentSelectedProject();
        try {
            CodeForestUIPlugin.getActiveWorkbenchWindow().getActivePage().showView(CodeForestKeyboardView.VIEW_ID, project.getName(), IWorkbenchPage.VIEW_VISIBLE);

        } catch (Exception e) {
            CodeForestUIPlugin.log(e);
        }
        CodeForestUIPlugin.ui(project, this, "code forest keyboard");
        return null;
    }
}

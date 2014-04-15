package br.usp.each.saeg.code.forest.ui.handlers;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.project.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class RemoveAnalysisHandler extends OnlyAfterAnalysisHandler {

    @Override
    public Object execute(ExecutionEvent arg) throws ExecutionException {
        IProject project = ProjectUtils.getCurrentSelectedProject();
        PluginCleanup.clean(project);
        CodeForestUIPlugin.ui(project, this, "removing views");
        return null;
    }
}

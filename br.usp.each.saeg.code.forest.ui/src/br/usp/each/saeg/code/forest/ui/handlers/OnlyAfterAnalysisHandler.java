package br.usp.each.saeg.code.forest.ui.handlers;

import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.ui.project.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public abstract class OnlyAfterAnalysisHandler extends AbstractHandler {

    @Override
    public boolean isEnabled() {
        IProject project = ProjectUtils.getCurrentSelectedProject();
        if (project == null || !project.isOpen()) {
            return false;
        }

        ProjectState state = ProjectPersistence.getStateOf(project);
        if (state == null) {
            return false;
        }
        if (state.isAnalyzed()) {
            return true;
        }
        return false;
    }
}

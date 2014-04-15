package br.usp.each.saeg.code.forest.ui.project;

import org.eclipse.core.resources.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ProjectPersistence {


    private static final String PROJECT_STATE_PROP_NAME = "project.state";

    public static ProjectState getStateOf(IProject project) {
        if (project == null || !project.isOpen()) {
            return null;
        }
        if (!ProjectUtils.containsProperty(project, PROJECT_STATE_PROP_NAME)) {
            ProjectUtils.setPropertyOf(project, PROJECT_STATE_PROP_NAME, new ProjectState());
        }
        return (ProjectState) ProjectUtils.getPropertyOf(project, PROJECT_STATE_PROP_NAME);
    }
}

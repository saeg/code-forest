package br.usp.each.saeg.code.forest.ui.listeners;

import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.ui.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class CloseProjectListener implements IResourceChangeListener {

    public void register() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        workspace.addResourceChangeListener(this);
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event) {
        IResource res = event.getResource();
        if (IResourceChangeEvent.PRE_CLOSE == event.getType() || IResourceChangeEvent.PRE_DELETE == event.getType()) {
            IProject project = res.getProject();
            PluginCleanup.clean(project);
            CodeForestUIPlugin.ui(project, this, "fechando o projeto");
        }
    }
}

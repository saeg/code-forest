package br.usp.each.saeg.code.forest.ui;

import java.util.*;
import org.apache.commons.lang3.*;
import org.eclipse.core.resources.*;
import org.eclipse.ui.*;
import br.usp.each.saeg.code.forest.ui.markers.*;
import br.usp.each.saeg.code.forest.ui.project.*;

public class PluginCleanup {

    public static void clean(IProject project) {
        PluginCleanup.removeMarkers(project);
        PluginCleanup.removeState(project);
        PluginCleanup.closeAllViews(project);
    }

    private static void closeAllViews(IProject project) {
        IWorkbenchWindow[] windows = CodeForestUIPlugin.getWorkbenchWindow();
        if (ArrayUtils.isEmpty(windows)) {
            return;
        }
        for (IWorkbenchWindow window : windows) {
            if (ArrayUtils.isEmpty(window.getPages())) {
                continue;
            }
            for (IWorkbenchPage page : window.getPages()) {
                IViewReference[] viewReferences = page.getViewReferences();
                for (IViewReference ivr : viewReferences) {
                    if (ivr.getId().startsWith("br.usp.each.saeg.code.forest.menu.view.code.forest") && project.getName().equals(ivr.getSecondaryId())) {
                        page.hideView(ivr);
                    }
                }
            }
        }
    }

    private static void removeState(IProject project) {
        if (!project.isOpen()) {
            return;
        }
        ProjectState state = ProjectPersistence.getStateOf(project);
        if (state == null) {
            return;
        }
        state.clear();
    }

    private static void removeMarkers(IProject project) {
        List<IMarker> toDelete = new ArrayList<IMarker>();
        for (List<IResource> files : ProjectUtils.javaFilesOf(project).values()) {
            for (IResource file : files) {
                for (IMarker marker : CodeMarkerFactory.findMarkers(file)) {
                    try {
                        toDelete.add(marker);
                    } catch (Exception e) {
                        CodeForestUIPlugin.log(e);
                    }
                }
            }
        }
        CodeMarkerFactory.removeMarkers(toDelete);
    }
}

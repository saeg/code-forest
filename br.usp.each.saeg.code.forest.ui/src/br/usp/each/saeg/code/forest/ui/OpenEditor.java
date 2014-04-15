package br.usp.each.saeg.code.forest.ui;

import org.eclipse.core.resources.*;
import org.eclipse.ui.*;
import org.eclipse.ui.ide.*;

public class OpenEditor {

    public static void at(final IMarker marker) {
        if (marker == null) {
            return;
        }
        try {

            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
                public void run() {
                    IWorkbenchWindow dwindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    IWorkbenchPage page = dwindow.getActivePage();
                    if (page != null) {
                        try {
                            IDE.openEditor(page, marker);
                        } catch (Exception e) {
                            CodeForestUIPlugin.log(e);
                        }
                    }
                }
            });
        } catch (Exception t) {
            CodeForestUIPlugin.log(t);
        }
    }
}

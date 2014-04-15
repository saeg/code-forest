package br.usp.each.saeg.code.forest.ui;

import java.util.*;
import org.eclipse.ui.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.source.parser.*;
import br.usp.each.saeg.code.forest.ui.project.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class EditorTracker {

    private final IWorkbench workbench;

    private IWindowListener windowListener = new IWindowListener() {
        public void windowOpened(IWorkbenchWindow window) {
            window.getPartService().addPartListener(partListener);
        }

        public void windowClosed(IWorkbenchWindow window) {
            window.getPartService().removePartListener(partListener);
        }

        public void windowActivated(IWorkbenchWindow window) {
        }

        public void windowDeactivated(IWorkbenchWindow window) {
        }
    };

    private IPartListener2 partListener = new IPartListener2() {
        public void partOpened(IWorkbenchPartReference partref) {
            annotateEditor(partref);
        }

        public void partActivated(IWorkbenchPartReference partref) {
        }

        public void partBroughtToTop(IWorkbenchPartReference partref) {
        }

        public void partVisible(IWorkbenchPartReference partref) {
            annotateEditor(partref);
        }

        public void partInputChanged(IWorkbenchPartReference partref) {
        }

        public void partClosed(IWorkbenchPartReference partref) {
        }

        public void partDeactivated(IWorkbenchPartReference partref) {
        }

        public void partHidden(IWorkbenchPartReference partref) {
        }
    };

    public EditorTracker(IWorkbench workbench) {
        this.workbench = workbench;
        for (final IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
            w.getPartService().addPartListener(partListener);
        }
        workbench.addWindowListener(windowListener);
        annotateAllEditors();
    }

    public synchronized void dispose() {
        workbench.removeWindowListener(windowListener);
        for (final IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
            w.getPartService().removePartListener(partListener);
        }
    }

    private void annotateAllEditors() {
        for (final IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
            for (final IWorkbenchPage p : w.getPages()) {
                for (final IEditorReference e : p.getEditorReferences()) {
                    annotateEditor(e);
                }
            }
        }
    }

    /**
     * Arrumar o TreeDataBuilder para fazer a inclusao em batch
     * Rodar o addAnnotations em batch
     *
     */
    public synchronized void annotateEditor(IWorkbenchPartReference partref) {
        ProjectToolkit toolkit = new ProjectToolkit(partref);
        if (!toolkit.isValid()) {
            return;
        }

        ProjectState state = ProjectPersistence.getStateOf(toolkit.getProject());
        if (state == null) {
            return;
        }
        String fileName = SourceCodeUtils.asString(toolkit.getFile());
        if (state.getMarked().contains(fileName)) {
            return;
        }

        Collection<TreeData> analysis = state.getAnalysisResult().get(fileName);
        if (analysis == null) {
            CodeForestUIPlugin.info("[" + SourceCodeUtils.asString(toolkit.getFile()) + "] no data found...");
            return;
        }
        state.getMarked().add(fileName);
    }
}

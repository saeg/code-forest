package br.usp.each.saeg.code.forest.ui;

import java.io.*;
import java.text.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.plugin.*;
import org.eclipse.ui.texteditor.*;
import org.osgi.framework.*;
import br.usp.each.saeg.code.forest.ui.listeners.*;
import br.usp.each.saeg.code.forest.ui.project.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class CodeForestUIPlugin extends AbstractUIPlugin {

    public static final String ID = "br.usp.each.saeg.codeforest.ui";
    private static CodeForestUIPlugin instance;
    private EditorTracker tracker;
    private static final IWorkbenchWindow[] NO_WINDOWS = new IWorkbenchWindow[0];

    /** Identifier for the 'cf' launch group. */
    public static final String ID_COVERAGE_LAUNCH_GROUP = ID + ".launchGroup.cf";

    public void start(BundleContext context) throws Exception {
        super.start(context);
        instance = this;
        instance.tracker = new EditorTracker(getWorkbench());
        new CloseAllViewsListener().register();;
        new CloseProjectListener().register();
        new BreakpointListener().register();
        new DebugListener().register();
    }

    public void stop(BundleContext context) throws Exception {
        instance = null;
        super.stop(context);
    }

    public static CodeForestUIPlugin getInstance() {
        return instance;
    }

    public static EditorTracker getEditorTracker() {
        return instance.tracker;
    }

    public static void log(Throwable t) {
        String message = t.getMessage();
        if (message == null) {
            message = "Internal Error";
        }
        instance.getLog().log(errorStatus(message, t));
    }

    public static void info(String message) {
        //instance.getLog().log(new Status(IStatus.INFO, ID, message));
    }

    public static void warn(String message) {
        // instance.getLog().log(new Status(IStatus.WARNING, ID, message));
    }

    public static void ui(IProject project, Object caller, Object arg) {
        try {
            ProjectState state = ProjectPersistence.getStateOf(project);
            if (state == null) {
                return;
            }
            File folder = new File(state.formatFolderFileName(project.getName()));
            folder.mkdirs();

            FileUtils.write(new File(state.formatLogFileName(project.getName())), "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis()) + "] ["+ project.getName() + "] [" + caller.getClass().getSimpleName() + "] " + arg.toString() + System.getProperty("line.separator"), true);
        } catch (Exception e) {
            errorStatus("error while writing log file", e);
        }
    }

    public static IStatus errorStatus(String message, Throwable t) {
        return new Status(IStatus.ERROR, ID, IStatus.ERROR, message, t);
    }

    public static void log(CoreException t) {
        instance.getLog().log(t.getStatus());
    }

    public static Shell getShell() {
        return getActiveWorkbenchWindow().getShell();
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    }

    public static IWorkbenchWindow[] getWorkbenchWindow() {
        IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
        if (ArrayUtils.isEmpty(windows)) {
            return NO_WINDOWS;
        }
        return windows;
    }

    public static ITextEditor getEditor() {
        return (ITextEditor) getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    }
}

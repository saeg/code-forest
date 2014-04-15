package br.usp.each.saeg.code.forest.ui.listeners;

import org.apache.commons.lang3.*;
import org.eclipse.core.resources.*;
import org.eclipse.debug.core.*;
import org.eclipse.debug.core.model.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.launching.*;
import br.usp.each.saeg.code.forest.ui.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 * http://programcreek.com/java-api-examples/index.php?example_code_path=codecover-org.codecover.eclipse.builder-CodeCoverDebugListener.java
 */
public class DebugListener implements IDebugEventSetListener {

    public void register() {
        DebugPlugin.getDefault().addDebugEventListener(this);
    }

    @Override
    public void handleDebugEvents(DebugEvent[] events) {
        if (ArrayUtils.isEmpty(events)) {
            return;
        }

        for (DebugEvent event : events) {
            if (event == null) {
                continue;
            }
            if (event.getKind() != DebugEvent.CREATE && event.getKind() != DebugEvent.TERMINATE) {
                continue;
            }

            String mensagem = event.getKind() == DebugEvent.CREATE ? "inicio" : "fim";

            if (!(event.getSource() instanceof IProcess)) {
                continue;
            }
            IProject project = from((IProcess) event.getSource());
            if (project == null || !project.isOpen()) {
                continue;
            }
            CodeForestUIPlugin.ui(project, this, mensagem + " da depuracao");
        }
    }

    private IProject from(IProcess process) {
        ILaunch launch = process.getLaunch();
        ILaunchConfiguration config = launch.getLaunchConfiguration();

        try {
            IJavaProject javaProject = JavaRuntime.getJavaProject(config);
            if (javaProject != null) {
                return javaProject.getProject();
            }

        } catch (Throwable e) {
            CodeForestUIPlugin.errorStatus("erro ao obter o projeto de um IProcess", e);
        }
        return null;
    }

}

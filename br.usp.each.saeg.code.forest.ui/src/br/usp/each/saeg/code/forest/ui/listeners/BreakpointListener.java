package br.usp.each.saeg.code.forest.ui.listeners;

import org.eclipse.core.resources.*;
import org.eclipse.debug.core.*;
import org.eclipse.debug.core.model.*;
import br.usp.each.saeg.code.forest.ui.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class BreakpointListener implements IBreakpointListener {

    public void register() {
        DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
    }

    @Override
    public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
        breakpointEvent(breakpoint, "removed");
    }

    @Override
    public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
    }

    @Override
    public void breakpointAdded(IBreakpoint breakpoint) {
        breakpointEvent(breakpoint, "added");
    }

    private void breakpointEvent(IBreakpoint breakpoint, final String type) {
        try {
            IMarker marker = breakpoint.getMarker();
            CodeForestUIPlugin.ui(marker.getResource().getProject(), this, "breakpoint [" + type + "] @ " + marker.getAttribute("org.eclipse.jdt.debug.core.typeName") + ":" + marker.getAttribute("lineNumber"));
        } catch (Exception e) {
            CodeForestUIPlugin.errorStatus("error inspecting marker", e);
        }

    }
}

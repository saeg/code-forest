package br.usp.each.saeg.code.forest.ui.listeners;

import org.eclipse.ui.*;
import br.usp.each.saeg.code.forest.ui.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class CloseAllViewsListener implements IWorkbenchListener {

    public void register() {
        PlatformUI.getWorkbench().addWorkbenchListener(this);
    }

    @Override
    public boolean preShutdown(IWorkbench workbench, boolean forced) {
        closeAllViews();
        return true;
    }

    @Override
    public void postShutdown(IWorkbench workbench) {
    }

    private static void closeAllViews() {
        IWorkbenchPage page = CodeForestUIPlugin.getActiveWorkbenchWindow().getActivePage();
        if (page != null) {
            IViewReference[] viewReferences = page.getViewReferences();
            for (IViewReference ivr : viewReferences) {
                if (ivr.getId().startsWith("br.usp.each.saeg.code.forest")) {
                    page.hideView(ivr);
                }
            }
        }
    }
}

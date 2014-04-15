package br.usp.each.saeg.code.forest.ui.project;

import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.ui.*;
import br.usp.each.saeg.code.forest.ui.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
@SuppressWarnings("restriction")
public class ProjectUtils {

    public static void setPropertyOf(IProject project, String name, Object value) {
        try {
            project.setSessionProperty(new QualifiedName("eclipse://br.usp.each.saeg.code.forest.ui.namespace", name), value);
        } catch (Exception e) {
            CodeForestUIPlugin.log(e);
        }
    }

    public static Object getPropertyOf(IProject project, String name) {
        try {
            return project.getSessionProperty(new QualifiedName("eclipse://br.usp.each.saeg.code.forest.ui.namespace", name));
        } catch (Exception e) {
            CodeForestUIPlugin.log(e);
            return null;
        }
    }

    public static boolean containsProperty(IProject project, String name) {
        try {
            return project.getSessionProperties().containsKey(new QualifiedName("eclipse://br.usp.each.saeg.code.forest.ui.namespace", name));
        } catch (Exception e) {
            CodeForestUIPlugin.log(e);
            return false;
        }
    }

    public static Map<String, List<IResource>> javaFilesOf(IProject project) {
        try {
            return visit(project, "java");
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_MAP;
        }
    }

    public static Map<String, List<IResource>> xmlFilesOf(IProject project) {
        try {
            return visit(project, "xml");
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.EMPTY_MAP;
        }
    }

    private static Map<String, List<IResource>> visit(IProject project, final String extension) throws CoreException {
        final Map<String, List<IResource>> result = new HashMap<String, List<IResource>>();
        project.accept(new IResourceVisitor() {
            @Override
            public boolean visit(IResource arg) throws CoreException {
                if (extension.equalsIgnoreCase(arg.getFileExtension())) {
                    if (!result.containsKey(arg.getName())) {
                        result.put(arg.getName(), new ArrayList<IResource>());
                    }
                    result.get(arg.getName()).add(arg);
                }
                return true;
            }
        });
        return result;
    }

    public static IProject getCurrentSelectedProject() {
        return getCurrentSelectedProject(CodeForestUIPlugin.getActiveWorkbenchWindow());
    }

    public static IProject getCurrentSelectedProject(IWorkbenchWindow window) {
        IProject project = null;
        ISelectionService selectionService =  window.getSelectionService();

        ISelection selection = selectionService.getSelection();

        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection)selection).getFirstElement();

            if (element instanceof IResource) {
                project= ((IResource) element).getProject();
            } else if (element instanceof PackageFragmentRoot) {
                IJavaProject jProject = ((PackageFragmentRoot) element).getJavaProject();
                project = jProject.getProject();
            } else if (element instanceof IJavaElement) {
                IJavaProject jProject = ((IJavaElement) element).getJavaProject();
                project = jProject.getProject();
            }
        }
        return project;
    }
}

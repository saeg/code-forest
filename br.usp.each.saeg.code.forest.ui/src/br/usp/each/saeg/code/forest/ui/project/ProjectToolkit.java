package br.usp.each.saeg.code.forest.ui.project;

import org.eclipse.core.resources.*;
import org.eclipse.jface.text.source.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;
import org.eclipse.ui.texteditor.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ProjectToolkit {

    private IWorkbenchPart part;
    private ITextEditor editor;
    private IDocumentProvider provider;
    private IAnnotationModel model;
    private IEditorInput input;
    private IFile file;
    private IProject project;
    private boolean valid;

    public ProjectToolkit(IWorkbenchPartReference partref) {
        this(partref.getPart(false));
    }

    public ProjectToolkit(IWorkbenchPart arg) {
        part = arg;
        if (!(part instanceof ITextEditor)) {
            return;
        }
        editor = (ITextEditor) part;
        provider = editor.getDocumentProvider();
        if (provider == null) {
            return;
        }
        model = provider.getAnnotationModel(editor.getEditorInput());
        if (!(model instanceof IAnnotationModelExtension)) {
            return;
        }
        input = editor.getEditorInput();
        file = ((FileEditorInput) input).getFile();
        project = file.getProject();
        if (project == null) {
            return;
        }
        valid = true;
    }

    public IWorkbenchPart getPart() {
        return part;
    }

    public ITextEditor getEditor() {
        return editor;
    }

    public IDocumentProvider getProvider() {
        return provider;
    }

    public IAnnotationModel getModel() {
        return model;
    }

    public IEditorInput getInput() {
        return input;
    }

    public IFile getFile() {
        return file;
    }

    public IProject getProject() {
        return project;
    }

    public boolean isValid() {
        return valid;
    }
}

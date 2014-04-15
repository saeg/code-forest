package br.usp.each.saeg.code.forest.ui.handlers;

import java.util.*;
import org.eclipse.core.commands.*;
import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.ui.*;
import org.eclipse.ui.handlers.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.source.parser.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.markers.*;
import br.usp.each.saeg.code.forest.ui.project.*;
import br.usp.each.saeg.code.forest.xml.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class RunAnalysisHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent arg) throws ExecutionException {
        final IProject project = ProjectUtils.getCurrentSelectedProject();
        if (!project.isOpen()) {
            return null;
        }

        Map<String, List<IResource>> xmlFiles = ProjectUtils.xmlFilesOf(project);
        XmlInput xmlInput = readXML(xmlFiles.get("codeforest.xml").get(0));

        ProjectState state = ProjectPersistence.getStateOf(project);
        if (state == null) {
            return null;
        }
        Map<IResource, List<Map<String, Object>>> resourceMarkerProps = new IdentityHashMap<IResource, List<Map<String,Object>>>();

        for (List<IResource> files : ProjectUtils.javaFilesOf(project).values()) {
            for (IResource file : files) {
                ParsingResult result = parse(file, xmlInput);
                TreeDataBuilderResult buildResult = TreeDataBuilder.from(result, SourceCodeUtils.read(file));
                resourceMarkerProps.put(buildResult.getResource(), buildResult.getMarkerProperties());
                state.getAnalysisResult().put(result.getURI(), buildResult.getTreeData());
            }
        }

        CodeMarkerFactory.scheduleMarkerCreation(resourceMarkerProps);
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(arg);
        IWorkbenchPage page = window.getActivePage();
        for (IEditorReference editorRef : page.getEditorReferences()) {
            CodeForestUIPlugin.getEditorTracker().annotateEditor(editorRef);
        }
        state.setAnalyzed(true);
        CodeForestUIPlugin.ui(project, this, "run analysis");

        return null;
    }

    private XmlInput readXML(IResource resource) {
        try {
            return XmlInput.unmarshal(resource.getLocation().toFile());
        } catch (Exception e) {
            CodeForestUIPlugin.log(e);
            return new XmlInput();
        }
    }

    private ParsingResult parse(final IResource file, final XmlInput input) {
        //quando abrir o arquivo no editor, adiciona as anotacoes... por isso existe o listener

        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        char[] trimmedSource = SourceCodeUtils.readAndTrim(file);
        parser.setSource(trimmedSource);
        parser.setResolveBindings(true);

        Hashtable<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.DISABLED);
        parser.setCompilerOptions(options);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        ParsingResult result = new ParsingResult(file);
        cu.accept(new SourceCodeParser(cu, trimmedSource, input, result));
        return result;
    }

    @Override
    public boolean isEnabled() {
        IProject project = ProjectUtils.getCurrentSelectedProject();
        if (project == null) {
            return false;
        }

        ProjectState state = ProjectPersistence.getStateOf(project);
        if (state == null) {
            return false;
        }
        if (!state.isAnalyzed()) {
            return true;
        }
        return false;
    }
}

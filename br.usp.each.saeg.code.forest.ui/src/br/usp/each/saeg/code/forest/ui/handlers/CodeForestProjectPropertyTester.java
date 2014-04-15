package br.usp.each.saeg.code.forest.ui.handlers;

import java.util.*;
import org.eclipse.core.expressions.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.ui.project.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class CodeForestProjectPropertyTester extends PropertyTester {

    /**
     * deve responder - posso ligar o handler? esqueca o valor esperado declarado no xml!
     */
    @Override
    public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
        if (!(receiver instanceof IProject)) {
            return false;
        }

        IProject project = (IProject) receiver;

        if (!project.isOpen()) {
            return false;
        }

        Map<String, List<IResource>> xmlFiles = ProjectUtils.xmlFilesOf(project);

        if (!xmlFiles.containsKey("codeforest.xml") || xmlFiles.get("codeforest.xml").size() > 1) {
            return false;
        }

        return ProjectUtils.javaFilesOf(project).size() > 0;
    }
}

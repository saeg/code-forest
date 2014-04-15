package br.usp.each.saeg.code.forest.domain;

import java.util.*;
import org.eclipse.core.resources.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class TreeDataBuilderResult {

    private final IResource resource;
    private final List<Map<String, Object>> markerProperties = new ArrayList<Map<String,Object>>();
    private final List<TreeData> treeData = new ArrayList<TreeData>();

    public TreeDataBuilderResult(IResource arg) {
        this.resource = arg;
    }

    public IResource getResource() {
        return resource;
    }

    public List<Map<String, Object>> getMarkerProperties() {
        return markerProperties;
    }

    public List<TreeData> getTreeData() {
        return treeData;
    }
}

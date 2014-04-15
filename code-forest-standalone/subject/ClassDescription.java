package domain;

import java.util.*;


/**
 * @author dmutti@gmail.com
 */
public class ClassDescription {

    private String name;
    private List<MethodDescription> methods = new ArrayList<MethodDescription>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<MethodDescription> getMethods() {
        return methods;
    }
    public int getNotEmptyMethodsCollectionSize() {
        int result = 0;
        for (MethodDescription desc : methods) {
            if (!desc.getLocScore().isEmpty()) {
                result++;
            }
        }
        return result;
    }
    public List<MethodDescription> getNotEmptyMethods() {
        List<MethodDescription> result = new ArrayList<MethodDescription>();
        for (MethodDescription desc : methods) {
            if (!desc.getLocScore().isEmpty()) {
                result.add(desc);
            }
        }
        return result;
    }

    public void setMethods(List<MethodDescription> methods) {
        if (null != methods) {
            this.methods.clear();
            this.methods.addAll(methods);
        }
    }
}

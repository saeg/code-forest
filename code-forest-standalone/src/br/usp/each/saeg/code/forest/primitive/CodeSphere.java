package br.usp.each.saeg.code.forest.primitive;

import javax.media.j3d.*;

import br.usp.each.saeg.code.forest.metaphor.*;

import com.sun.j3d.utils.geometry.*;


public class CodeSphere extends Sphere implements CodeInformation {

    private CodeGeometry geometry;

    public CodeSphere(float radius, int primflags, Appearance ap) {
        super(radius, primflags, ap);
    }

    @Override
    public CodeGeometry getGeometry() {
        return geometry;
    }
    public void setGeometry(CodeGeometry geometry) {
        this.geometry = geometry;
    }
}
package br.usp.each.saeg.code.forest.primitive;

import javax.media.j3d.*;

import br.usp.each.saeg.code.forest.metaphor.*;

import com.sun.j3d.utils.geometry.*;

public class CodeCone extends Cone implements CodeInformation {

    private CodeGeometry geometry;

    public CodeCone(float radius, float height, int primflags, Appearance ap) {
        super(radius, height, primflags, ap);
    }

    @Override
    public CodeGeometry getGeometry() {
        return geometry;
    }
    public void setGeometry(CodeGeometry geometry) {
        this.geometry = geometry;
    }
}

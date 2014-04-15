package br.usp.each.saeg.code.forest.metaphor;

import javax.media.j3d.*;
import javax.vecmath.*;
import org.apache.commons.lang3.*;

import br.usp.each.saeg.code.forest.image.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.primitive.*;

import com.sun.j3d.utils.geometry.*;


public class Leaf extends CodeGeometry {

    private LeafData data;
    private CodeCone shape;
    private final ForestRestrictions restrictions;
    private Branch parent;

    public Leaf(Branch parent, float radius, LeafData data, ForestRestrictions restr, Transform3D tr) {
        this.parent = parent;
        this.data = data;
        appearance = ImageUtils.getLeafAppearance(new Color3f(createColor(data.getScore())));
        this.restrictions = restr;

        shape = new CodeCone(radius/4, radius, Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS, appearance);
        shape.setGeometry(this);

        GroupDataItem item = new GroupDataItem(tr, shape);
        groupData.add(item);
        getTrunk().add(item.getTransformGroup());
    }

    public LeafData getData() {
        return data;
    }

    public CodeCone getShape() {
        return shape;
    }

    @Override
    public void changeStatus() {
        if (isEnabled()) {
            disable();
        } else {
            refresh();
        }
    }

    @Override
    public void refresh() {
        if (restrictions.isLeaves()) {
            if (!containsTerm() || !containsScore()) {
                disable();
                return;
            }
        }
        enable();
    }

    private boolean containsTerm() {
        if (StringUtils.isBlank(restrictions.getTerm())) {
            return true;
        }
        if (restrictions.isLeaves()) {
            if (StringUtils.isNotBlank(restrictions.getTerm()) && StringUtils.containsIgnoreCase(data.getValue(), StringUtils.trim(restrictions.getTerm()))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsScore() {
        if (restrictions.isLeaves()) {
            if (data.isScoreBetween(restrictions.getMinScore(), restrictions.getMaxScore())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Trunk getTrunk() {
        return parent.getTrunk();
    }
}

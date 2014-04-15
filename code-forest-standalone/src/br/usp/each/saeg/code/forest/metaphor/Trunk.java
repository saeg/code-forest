package br.usp.each.saeg.code.forest.metaphor;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.media.j3d.*;
import javax.vecmath.*;

import org.apache.commons.lang3.*;

import br.usp.each.saeg.code.forest.image.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;
import br.usp.each.saeg.code.forest.primitive.*;
import br.usp.each.saeg.code.forest.util.*;

import com.sun.j3d.utils.geometry.*;


public class Trunk extends CodeGeometry {

    private static final int OFFSET = 1;

    private TransformGroup baseTrunk = new TransformGroup();
    private TreeData data;
    private float radius;
    private float step;
    private CodeCylinder body;
    private CodeSphere tip;
    private BranchAssembler assembler;
    private final Color3f color;
    private final ForestRestrictions restrictions;

    public Trunk(TreeData data, float radius, float step, ForestRestrictions restr) {
        baseTrunk.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        color = new Color3f(createColor(new Color(102,0,0), new Color(102,102,0), data.getScore()));
        appearance = ImageUtils.getTrunkAppearance(getMaterial(color));
        this.data = data;
        this.radius = radius;
        this.radius = radius;
        this.step = step;
        this.restrictions = restr;

        drawBody();
        drawTip();
        addInformation();
        assembler = new BranchAssembler(this, data, restrictions);
    }

    private void drawBody() {
        Transform3D tr = new Transform3D();
        tr.setTranslation(new Vector3d(0, ((data.getHeight() + OFFSET) * step * .5), 0));
        body = new CodeCylinder(radius, ((data.getHeight() + OFFSET) * step), Cylinder.GENERATE_NORMALS | Cylinder.GENERATE_TEXTURE_COORDS, appearance);
        GroupDataItem item = new GroupDataItem(tr, body);
        groupData.add(item);
        add(item.getTransformGroup());
    }

    private void drawTip() {
        Transform3D tr = new Transform3D();
        tr.setTranslation(new Vector3d(0, ((data.getHeight() + OFFSET) * step), 0));
        tip = new CodeSphere(radius, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, appearance);
        GroupDataItem item = new GroupDataItem(tr, tip);
        groupData.add(item);
        add(item.getTransformGroup());
    }

    private void addInformation() {
        body.setGeometry(this);
        tip.setGeometry(this);
    }

    @Override
    public void changeStatus() {
        if (isEnabled()) {
            disable();
            for (Branch branch : assembler.getBranches()) {
                branch.disable();
                for (Leaf leaf : branch.getLeaves()) {
                    leaf.disable();
                }
            }
        } else {
            refresh();
        }
    }

    public TransformGroup getTransformGroup() {
        return baseTrunk;
    }

    public void add(TransformGroup arg) {
        baseTrunk.addChild(arg);
    }

    public float getHeight() {
        return (OFFSET * step) + (step * data.getHeight());
    }

    public float getRadius() {
        return radius;
    }

    public float getOffset() {
        return OFFSET;
    }

    public float getStep() {
        return step;
    }

    public double maxLeftBranchSize() {
        List<Double> sizes = new ArrayList<Double>();
        for (Branch branch : assembler.getBranches()) {
            if (branch.isLeft()) {
                sizes.add(branch.getSize());
            }
        }
        return sizes.isEmpty() ? 0 : CollectionUtils.max(sizes);
    }

    public double maxRightBranchSize() {
        List<Double> sizes = new ArrayList<Double>();
        for (Branch branch : assembler.getBranches()) {
            if (!branch.isLeft()) {
                sizes.add(branch.getSize());
            }
        }
        return sizes.isEmpty() ? 0 : CollectionUtils.max(sizes);
    }

    public Color3f getColor() {
        return color;
    }

    @Override
    public void refresh() {
        if (!containsTerm() || !containsScore()) {
            disable();
            for (Branch branch : assembler.getBranches()) {
                branch.disable();
                for (Leaf leaf : branch.getLeaves()) {
                    leaf.disable();
                }
            }
            return;
        }
        enable();
        for (Branch branch : assembler.getBranches()) {
            branch.refresh();
        }
    }

    private boolean containsTerm() {
        if (StringUtils.isBlank(restrictions.getTerm())) {
            return true;
        }
        if (restrictions.isTrees() && StringUtils.isNotBlank(restrictions.getTerm()) && StringUtils.containsIgnoreCase(data.getValue(), StringUtils.trim(restrictions.getTerm()))) {
            return true;
        }
        if (restrictions.isBranches()) {
            for (Branch branch : assembler.getBranches()) {
                if (StringUtils.isNotBlank(restrictions.getTerm()) && StringUtils.containsIgnoreCase(branch.getData().getValue(), StringUtils.trim(restrictions.getTerm()))) {
                    return true;
                }
            }
        }
        if (restrictions.isLeaves()) {
            for (Branch branch : assembler.getBranches()) {
                for (Leaf leaf : branch.getLeaves()) {
                    if (StringUtils.isNotBlank(restrictions.getTerm()) && StringUtils.containsIgnoreCase(leaf.getData().getValue(), StringUtils.trim(restrictions.getTerm()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean containsScore() {
        if (restrictions.isTrees() && data.isScoreBetween(restrictions.getMinScore(), restrictions.getMaxScore())) {
            return true;
        }
        if (restrictions.isBranches()) {
            for (Branch branch : assembler.getBranches()) {
                if (branch.getData().isScoreBetween(restrictions.getMinScore(), restrictions.getMaxScore())) {
                    return true;
                }
            }
        }
        if (restrictions.isLeaves()) {
            for (Branch branch : assembler.getBranches()) {
                for (Leaf leaf : branch.getLeaves()) {
                    if (leaf.getData().isScoreBetween(restrictions.getMinScore(), restrictions.getMaxScore())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public TreeData getData() {
        return data;
    }

    public double getLinearSize() {
        return maxLeftBranchSize() + (2 * getRadius()) + maxRightBranchSize();
    }

    @Override
    public Trunk getTrunk() {
        return this;
    }
}

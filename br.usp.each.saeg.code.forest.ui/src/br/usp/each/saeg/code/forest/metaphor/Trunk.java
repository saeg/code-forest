package br.usp.each.saeg.code.forest.metaphor;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.media.j3d.*;
import javax.vecmath.*;
import org.apache.commons.lang3.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.metaphor.assembler.*;
import br.usp.each.saeg.code.forest.metaphor.building.blocks.*;
import br.usp.each.saeg.code.forest.metaphor.util.*;
import br.usp.each.saeg.code.forest.util.*;
import com.sun.j3d.utils.geometry.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class Trunk extends CodeGeometry {

    private static final int OFFSET = 1;

    private TransformGroup baseTrunk = new TransformGroup();
    private TreeData data;
    private float radius;
    private float step;
    private float maxHorizontal;
    private CodeCylinder body;
    private CodeSphere tip;
    private BranchAssembler assembler;
    private final Color3f color;
    private final ForestRestrictions restrictions;

    public Trunk(TreeData data, ForestRestrictions restr) {
        baseTrunk.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        color = getColor(data.getScore());
        appearance = ImageUtils.getTrunkAppearance(getMaterial(color));
        this.data = data;
        this.radius = data.getSize().getRadius();
        this.step = data.getSize().getStep();
        this.maxHorizontal = data.getSize().getMaxHorizontal();
        this.restrictions = restr;

        drawBody();
        drawTip();
        addInformation();
        assembler = new BranchAssembler(this, data, restrictions);
    }

    private Color3f getColor(float score) {
        if (score < 0) {
            return new Color3f(Color.GRAY);
        }
        return new Color3f(createColor(new Color(102,0,0), new Color(102,102,0), score));
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

    public float getMaxHorizontal() {
        return maxHorizontal;
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

        if (restrictions.isTrees()) {
            if (restrictions.containsTerm(data.getValue())) {
                return true;
            }
        }
        if (restrictions.isBranches()) {
            for (Branch branch : assembler.getBranches()) {
                if (restrictions.containsTerm(branch.getData().getValue())) {
                    return true;
                }
            }
        }
        if (restrictions.isLeaves()) {
            for (Branch branch : assembler.getBranches()) {
                for (Leaf leaf : branch.getLeaves()) {
                    if (restrictions.containsTerm(leaf.getData().getValue())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean containsScore() {
        if (restrictions.isTrees()) {
            if (data.isScoreBetween(restrictions.getMinScore(), restrictions.getMaxScore())) {
                return true;
            }
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

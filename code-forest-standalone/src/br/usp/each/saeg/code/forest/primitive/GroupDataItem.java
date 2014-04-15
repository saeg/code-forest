package br.usp.each.saeg.code.forest.primitive;

import javax.media.j3d.*;

public class GroupDataItem {

    private final TransformGroup tg;
    private final BranchGroup bg;

    public GroupDataItem(Transform3D tr, CodeInformation info) {
        tg = new TransformGroup(tr);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        bg = new BranchGroup();
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        bg.addChild((Node) info);
        tg.addChild(bg);
    }

    public TransformGroup getTransformGroup() {
        return tg;
    }

    public BranchGroup getBranchGroup() {
        return bg;
    }

    public void enable() {
        tg.addChild(bg);
    }

    public void disable() {
        bg.detach();
    }
}

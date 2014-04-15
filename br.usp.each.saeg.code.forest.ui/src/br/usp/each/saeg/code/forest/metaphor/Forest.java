package br.usp.each.saeg.code.forest.metaphor;

import java.util.*;

import javax.media.j3d.*;

import br.usp.each.saeg.code.forest.metaphor.assembler.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public interface Forest {

    void resetForestRestrictions();
    void applyForestRestrictions();
    List<TransformGroup> getTrees();
    float getZ();
    float getX();
    float getY();
    List<Trunk> getTrunks();
    ForestRestrictions getRestrictions();

}
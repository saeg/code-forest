package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.metaphor.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class BranchAssembler {

    private List<Branch> branches = new ArrayList<Branch>();

    public BranchAssembler(Trunk trunk, TreeData data, ForestRestrictions restrictions) {
        List<BranchData> coveredBranches = BranchData.covered(data.getBranchData());

        for (int i = 0; i < coveredBranches.size(); i++) {
            branches.add(new Branch(trunk, coveredBranches.get(i), restrictions, i));
        }
    }

    public List<Branch> getBranches() {
        return branches;
    }
}

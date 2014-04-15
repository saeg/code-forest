package br.usp.each.saeg.code.forest.metaphor.assembler;

import java.util.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.data.*;

public class BranchAssembler {

    private List<Branch> branches = new ArrayList<Branch>();

    public BranchAssembler(Trunk trunk, TreeData data, ForestRestrictions restrictions) {
        Collections.sort(data.getBranchData());
        for (int i = 0; i < data.getNotEmptyBranches().size(); i++) {
            branches.add(new Branch(trunk, data.getNotEmptyBranches().get(i), restrictions, i));
        }
    }

    public List<Branch> getBranches() {
        return branches;
    }
}

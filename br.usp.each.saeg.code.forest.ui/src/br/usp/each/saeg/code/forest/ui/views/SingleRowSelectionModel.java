package br.usp.each.saeg.code.forest.ui.views;

import javax.swing.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class SingleRowSelectionModel extends DefaultListSelectionModel {

    private static final long serialVersionUID = 1L;

    public SingleRowSelectionModel () {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
    }
}

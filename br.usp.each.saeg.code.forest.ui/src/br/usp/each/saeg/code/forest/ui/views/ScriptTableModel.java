package br.usp.each.saeg.code.forest.ui.views;

import javax.swing.table.*;

public class ScriptTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private Object[][] data;
    private String[] columnNames;

    public ScriptTableModel(Object[][] data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}

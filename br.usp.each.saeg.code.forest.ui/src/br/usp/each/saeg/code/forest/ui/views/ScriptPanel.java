package br.usp.each.saeg.code.forest.ui.views;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.ui.*;
import br.usp.each.saeg.code.forest.ui.project.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class ScriptPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JScrollPane scriptScroll;
    private JTable scriptTable;
    private ProjectState state;
    private FilterPanel filterPanel;
    private IProject project;
    private Object parent;

    public ScriptPanel(IProject project, Object parent, ProjectState projectState, FilterPanel panel) {
        this.project = project;
        this.parent = parent;
        this.state = projectState;
        this.filterPanel = panel;

        if (!state.containsAnalysis()) {
            return;
        }

        Map<Integer, ScriptData> scriptElements = new TreeMap<Integer, ScriptData>();
        for (TreeData tree : state.getAllAnalysis()) {
            for (BranchData branch : tree.getBranchData()) {
                if (!ScriptEligibility.isEligible(branch)) {
                    continue;
                }
                ScriptData element = new ScriptData();
                element.setClassName(tree.getName());
                element.setMethodName(branch.getName());
                element.setScriptPosition(branch.getScriptPosition());
                element.setMarker(branch.getOpenMarker());
                element.setScore(branch.getScriptScore());
                if (!scriptElements.containsKey(element.getScriptPosition()) || scriptElements.get(element.getScriptPosition()).getScore() < element.getScore()) {
                    scriptElements.put(element.getScriptPosition(), element);
                }
            }
        }
        Object[][] data = new Object[scriptElements.size()][4];
        int i = 0;
        for (ScriptData scriptData : scriptElements.values()) {
            data[i][0] = scriptData.getClassName();
            data[i][1] = scriptData.getMethodName();
            data[i][2] = String.format("%.2f", scriptData.getScore());
            data[i][3] = scriptData;
            i++;
        }
        ScriptTableModel model = new ScriptTableModel(data, new String[] { "Class", "Method", "Score" });
        setUp(model);
    }

    private void setUp(final ScriptTableModel model) {
        scriptScroll = new JScrollPane();
        scriptTable = new JTable() {

            private static final long serialVersionUID = 1L;

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    ScriptData data = (ScriptData) getModel().getValueAt(row, 3);
                    int newScore = (int) (data.getScore() * 100);
                    if (newScore <= 25) {
                        c.setBackground(new Color(192, 255, 192));
                    } else if (newScore <= 50) {
                        c.setBackground(new Color(255, 255, 128));
                    } else if (newScore <= 75) {
                        c.setBackground(new Color(255, 204, 153));
                    } else {
                        c.setBackground(new Color(255, 160, 160));
                    }
                }
                return c;
            }
        };

        scriptTable.setModel(model);
        scriptTable.setSelectionModel(new SingleRowSelectionModel());
        scriptTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                ScriptData data = (ScriptData) model.getValueAt(scriptTable.getSelectedRow(), 3);
                OpenEditor.at(data.getMarker());
                filterPanel.applyFilter(new ScriptFilter(data));
                CodeForestUIPlugin.ui(project, parent, "[Script Filter] @ [xml " + data.getScriptPosition() + ", real " + scriptTable.getSelectedRow() + "] " + data.getClassName() + "." + data.getMethodName());
            }
        });

        scriptScroll.setViewportView(scriptTable);

        GroupLayout scriptPanelLayout = new GroupLayout(this);
        this.setLayout(scriptPanelLayout);
        scriptPanelLayout.setHorizontalGroup(
                scriptPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(scriptPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scriptScroll)
                        .addContainerGap())
                );
        scriptPanelLayout.setVerticalGroup(
                scriptPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, scriptPanelLayout.createSequentialGroup()
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scriptScroll, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

    }
}

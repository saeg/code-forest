package br.usp.each.saeg.code.forest.ui.views;

import java.awt.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.swing.event.*;
import org.apache.commons.lang3.*;
import org.eclipse.core.resources.*;
import br.usp.each.saeg.code.forest.domain.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.metaphor.integration.*;
import br.usp.each.saeg.code.forest.ui.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class FilterPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel toolsPanel = new JPanel();
    private JTabbedPane parentPane = new JTabbedPane();

    private JPanel rankingPane = new JPanel();
    private RangeSlider slider = new RangeSlider(0, 100);
    private JLabel minValue = new JLabel();
    private JLabel minLabel = new JLabel();
    private JLabel maxValue = new JLabel();
    private JLabel maxLabel = new JLabel();

    private JPanel searchPane = new JPanel();
    private JTextField searchTerm = new JTextField();

    private JPanel filterPanel = new JPanel();
    private final Forest forest;
    private volatile boolean updateChanges = true;
    private IProject project;
    private Object parent;

    public FilterPanel(final IProject project, final Forest arg, final Canvas3D canvas, final Object parent) {
        this.forest = arg;
        this.project = project;
        this.parent = parent;
        setUp();

        slider.setOrientation(RangeSlider.HORIZONTAL);
        slider.setMajorTickSpacing(10);
        slider.setValue(0);
        slider.setUpperValue(100);
        slider.setPaintTicks(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!updateChanges) {
                    return;
                }
                forest.getRestrictions().setMinScore(slider.getValue() / 100f);
                forest.getRestrictions().setMaxScore(slider.getUpperValue() / 100f);
                updateSliderValues();
                forest.applyForestRestrictions();
                canvas.requestFocus();
                CodeForestUIPlugin.ui(project, parent, "Slider Restrictions [ " + forest.getRestrictions() + " ]");
            }
        });

        searchTerm.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent arg) {
                if (!updateChanges) {
                    return;
                }
                forest.getRestrictions().setTerm(searchTerm.getText());
                forest.applyForestRestrictions();
                CodeForestUIPlugin.ui(project, parent, "Term Restrictions [ " + forest.getRestrictions() + " ]");
            }

            @Override
            public void insertUpdate(DocumentEvent arg) {
                if (!updateChanges) {
                    return;
                }
                forest.getRestrictions().setTerm(searchTerm.getText());
                forest.applyForestRestrictions();
                CodeForestUIPlugin.ui(project, parent, "Term Restrictions [ " + forest.getRestrictions() + " ]");
            }

            @Override
            public void changedUpdate(DocumentEvent arg) {
            }
        });
    }

    public void applyFilter(ScriptFilter scriptFilter) {
        if (scriptFilter == null) {
            return;
        }
        updateChanges = false;
        forest.getRestrictions().setMinScore(slider.getValue() / 100f);
        forest.getRestrictions().setMaxScore(slider.getUpperValue() / 100f);
        forest.getRestrictions().setTerm(scriptFilter.getSearchString());
        forest.applyForestRestrictions();

        slider.setUpperValue(Math.round(100 * scriptFilter.getMaximumScore()));
        slider.setValue(Math.round(100 * scriptFilter.getMinimumScore()));
        updateSliderValues();
        searchTerm.setText(scriptFilter.getSearchString());
        updateChanges = true;
        CodeForestUIPlugin.ui(project, parent, "Filter Restrictions [ " + forest.getRestrictions() + " ]");
    }

    public void resetFilter() {
        updateChanges = false;
        forest.getRestrictions().reset();
        forest.applyForestRestrictions();

        slider.setUpperValue(Math.round(100 * forest.getRestrictions().getMaxScore()));
        slider.setValue(Math.round(100 * forest.getRestrictions().getMinScore()));
        updateSliderValues();
        searchTerm.setText(forest.getRestrictions().getTerm());
        updateChanges = true;
        CodeForestUIPlugin.ui(project, parent, "Reset Filter Restrictions [ " + forest.getRestrictions() + " ]");
    }

    private void updateSliderValues() {
        minValue.setText(formatScore(slider.getValue()));
        maxValue.setText(formatScore(slider.getUpperValue()));
    }

    private static String formatScore(int score) {
        if (score < 100) {
            return "0." + StringUtils.leftPad(String.valueOf(score), 2, '0');
        }
        return "1.00";
    }

    private void setUp() {
        setLayout(new BorderLayout());

        rankingPane.setBorder(BorderFactory.createTitledBorder("Ranking"));
        minLabel.setText("Min");
        maxLabel.setText("Max");
        minValue.setText("0.00");
        maxValue.setText("1.00");

        GroupLayout rankingPaneLayout = new GroupLayout(rankingPane);
        rankingPane.setLayout(rankingPaneLayout);
        rankingPaneLayout.setHorizontalGroup(rankingPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(slider, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGroup(rankingPaneLayout.createSequentialGroup().addComponent(minLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(minValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(51, 51, 51).addComponent(maxLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(maxValue, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        rankingPaneLayout.setVerticalGroup(rankingPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(rankingPaneLayout.createSequentialGroup().addGroup(rankingPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(minLabel).addComponent(maxLabel).addComponent(minValue).addComponent(maxValue)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(slider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

        searchPane.setBorder(BorderFactory.createTitledBorder("Text"));

        GroupLayout searchPaneLayout = new GroupLayout(searchPane);
        searchPane.setLayout(searchPaneLayout);
        searchPaneLayout.setHorizontalGroup(searchPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(searchTerm));
        searchPaneLayout.setVerticalGroup(searchPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(searchTerm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE));

        GroupLayout toolsPanelLayout = new GroupLayout(toolsPanel);
        toolsPanel.setLayout(toolsPanelLayout);
        toolsPanelLayout.setHorizontalGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                toolsPanelLayout.createSequentialGroup().addGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(rankingPane, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(searchPane, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(0, 0, Short.MAX_VALUE)));
        toolsPanelLayout.setVerticalGroup(toolsPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
                toolsPanelLayout.createSequentialGroup().addComponent(rankingPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(searchPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGap(0, 0, Short.MAX_VALUE)));

        parentPane.addTab("Filters", toolsPanel);

        GroupLayout filterPanelLayout = new GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(filterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, filterPanelLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(parentPane, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)));
        filterPanelLayout.setVerticalGroup(filterPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(filterPanelLayout.createSequentialGroup().addComponent(parentPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addGap(0, 160, Short.MAX_VALUE)));

        add(filterPanel, BorderLayout.LINE_END);
    }
}

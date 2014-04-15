package br.usp.each.saeg.code.forest.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import br.usp.each.saeg.code.forest.metaphor.*;

public class FilterPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public FilterPanel(final Forest forest) {
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel rightPanel = new JPanel(new GridLayout());

        JPanel sliderPanel = new JPanel(new GridLayout(4,1));
        sliderPanel.setBorder(new TitledBorder("Ranking"));
        final RangeSlider slider = new RangeSlider(0, 100);
        slider.setOrientation(RangeSlider.HORIZONTAL);
        slider.setMajorTickSpacing(10);
        slider.setValue(0);
        slider.setUpperValue(100);
        slider.setPaintTicks(true);
        sliderPanel.add(slider);

        JPanel middleSliderPanel = new JPanel(new GridLayout(2,2));
        JLabel minText = new JLabel("Mín");
        minText.setHorizontalAlignment(JLabel.LEFT);
        minText.setVerticalAlignment(JLabel.BOTTOM);
        middleSliderPanel.add(minText);

        JLabel maxText = new JLabel("Máx");
        maxText.setHorizontalAlignment(JLabel.RIGHT);
        maxText.setVerticalAlignment(JLabel.BOTTOM);
        middleSliderPanel.add(maxText);

        final JLabel minValue = new JLabel("0.0");
        minValue.setHorizontalAlignment(JLabel.LEFT);
        minValue.setVerticalAlignment(JLabel.TOP);
        middleSliderPanel.add(minValue);

        final JLabel maxValue = new JLabel("1.0");
        maxValue.setHorizontalAlignment(JLabel.RIGHT);
        maxValue.setVerticalAlignment(JLabel.TOP);
        middleSliderPanel.add(maxValue);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                forest.getRestrictions().setMinScore(slider.getValue()/100f);
                minValue.setText(String.valueOf(slider.getValue()/100f));
                forest.getRestrictions().setMaxScore(slider.getUpperValue()/100f);
                maxValue.setText(String.valueOf(slider.getUpperValue()/100f));
                forest.applyForestRestrictions();
            }
        });

        JPanel optionsPanel = new JPanel(new GridLayout(3,1));
        optionsPanel.setBorder(new TitledBorder("Filtrar por"));
        final JCheckBox tree = new JCheckBox("Árvores");
        tree.setSelected(true);
        tree.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                forest.getRestrictions().setTrees(tree.isSelected());
                forest.applyForestRestrictions();
            }
        });

        final JCheckBox branch = new JCheckBox("Galhos");
        branch.setSelected(true);
        branch.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                forest.getRestrictions().setBranches(branch.isSelected());
                forest.applyForestRestrictions();
            }
        });

        final JCheckBox leaf = new JCheckBox("Folhas");
        leaf.setSelected(true);
        leaf.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                forest.getRestrictions().setLeaves(leaf.isSelected());
                forest.applyForestRestrictions();
            }
        });

        optionsPanel.add(tree);
        optionsPanel.add(branch);
        optionsPanel.add(leaf);

        JPanel searchPanel = new JPanel(new GridLayout(3,1));
        searchPanel.setBorder(new TitledBorder("Contendo o termo"));
        searchPanel.add(new JPanel());
        final JTextField searchTerm = new JTextField();
        searchPanel.add(searchTerm);
        searchTerm.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent arg0) {
                forest.getRestrictions().setTerm(searchTerm.getText());
                forest.applyForestRestrictions();
            }

            @Override
            public void insertUpdate(DocumentEvent arg) {
                forest.getRestrictions().setTerm(searchTerm.getText());
                forest.applyForestRestrictions();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
            }
        });

        sliderPanel.add(middleSliderPanel);
        sliderPanel.add(searchPanel);
        sliderPanel.add(optionsPanel);
        rightPanel.add(sliderPanel);

        tabbedPane.addTab("Filtros", rightPanel);
        add(tabbedPane, BorderLayout.NORTH);
    }
}

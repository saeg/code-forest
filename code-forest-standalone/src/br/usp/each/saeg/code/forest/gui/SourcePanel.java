package br.usp.each.saeg.code.forest.gui;

import java.awt.*;

import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;

import br.usp.each.saeg.code.forest.image.*;
import br.usp.each.saeg.code.forest.metaphor.*;
import br.usp.each.saeg.code.forest.util.*;

public class SourcePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private RSyntaxTextArea textArea;
    private RTextScrollPane scrollPane;
    private Gutter gutter;
    private static final ImageIcon arrow = ImageUtils.getImageIcon();

    private GutterIconInfo lastLocation;

    public SourcePanel(final Forest forest) {
        setSize(new Dimension(0, (int) DisplayUtils.getProportionalDimension().getHeight()/4));
        setLayout(new BorderLayout());
        createTextArea();
        add(scrollPane, BorderLayout.WEST);
    }

    private void createTextArea() {
        textArea = new RSyntaxTextArea(10,120);
        textArea.setAutoIndentEnabled(true);
        textArea.setEditable(false);
        textArea.requestFocusInWindow();
        textArea.setTextMode(RSyntaxTextArea.OVERWRITE_MODE);
        textArea.setCaretPosition(0);
        textArea.setCodeFoldingEnabled(false);
        textArea.setClearWhitespaceLinesEnabled(false);
        textArea.setSyntaxEditingStyle(RSyntaxTextArea.SYNTAX_STYLE_JAVA);
        textArea.setHighlightCurrentLine(false);
        scrollPane = new RTextScrollPane(textArea, true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setIconRowHeaderEnabled(true);
        gutter = scrollPane.getGutter();
    }

    public RSyntaxTextArea getTextArea() {
        return textArea;
    }

    public void setLinePosition(int lineNumber) {
        textArea.setCaretPosition(lastLocation.getMarkedOffset());
    }

    public void setArrow(int lineNumber) {
        try {
            if (lastLocation != null) {
                gutter.removeTrackingIcon(lastLocation);
            }
            lastLocation = gutter.addLineTrackingIcon(lineNumber, arrow);

        } catch (Exception e) { }
    }
}
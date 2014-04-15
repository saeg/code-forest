package gfx;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import representation.*;
import util.*;
import domain.*;

/**
 * @author dmutti@gmail.com
 */
public class Grid {

    private static Random rand = new Random();

    //http://stackoverflow.com/questions/6222442/overlay-images-on-a-gridlayout-in-java
    public static void main(String[] args) {
        JFrame fr = new JFrame();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        fr.setSize((int) dim.getWidth()/2, (int) dim.getHeight()/2);
        Display.on(fr);

        Container pane = fr.getContentPane();
        pane.setLayout(new GridBagLayout());
        pane.setBackground(Color.DARK_GRAY);

//        GridBagConstraints bgc = new GridBagConstraints();
//        bgc.fill = GridBagConstraints.BOTH;
//        bgc.weighty = 1;
//        bgc.weightx = 1;

//        JPanel bg = new JPanel(new GridBagLayout());
//        JLabel bgImage = new JLabel(new ImageIcon(ImageUtils.loadFromClassPath("01_background.jpg")));
//        bg.add(bgImage);
//
//        pane.add(bg);
//
//        JPanel fg = new JPanel(new GridBagLayout());
//        fg.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.weightx = 1;


        pane.add(get(pane, 0), c);
        pane.add(get(pane, 1), c);
        pane.add(get(pane, 2), c);
        c.gridy = 1;
        pane.add(get(pane, 4), c);
        pane.add(get(pane, 5), c);
        pane.add(get(pane, 6), c);
        pane.add(get(pane, 7), c);

        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }

    private static ClassRepresentation get(Component comp, int id) {
        ClassDescription desc = new ClassDescription();
        desc.setName("class" + id);

        for (int m = 0; m < 10 + rand.nextInt(39); m++) {
            MethodDescription meth = new MethodDescription();
            meth.setClassName(desc.getName());
            meth.setName("method"+m);
            for (int j = 0; j < 1 + rand.nextInt(20) + rand.nextInt(20); j++) {
                meth.getLocScore().put("l"+j, rand.nextFloat());
            }
            desc.getMethods().add(meth);
        }
        ClassRepresentation result = new ClassRepresentation(desc);
        comp.addMouseListener(result.getMouseListener());
        return result;
    }
}

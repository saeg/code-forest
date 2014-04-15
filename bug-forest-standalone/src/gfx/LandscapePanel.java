/* Copyright (c) - UOL Inc,
 * Todos os direitos reservados
 *
 * Este arquivo e uma propriedade confidencial do Universo Online Inc.
 * Nenhuma parte do mesmo pode ser copiada, reproduzida, impressa ou
 * transmitida por qualquer meio sem autorizacao expressa e por escrito
 * de um representante legal do Universo Online Inc.
 *
 * All rights reserved
 *
 * This file is a confidential property of Universo Online Inc.
 * No part of this file may be reproduced or copied in any form or by
 * any means without written permission from an authorized person from
 * Universo Online Inc.
 */
package gfx;

import java.awt.*;
import javax.swing.*;


/**
 * @author dmutti@uolinc.com
 */
public class LandscapePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    public static final int CENTERED = 0;
    public static final int SCALED   = 1;

    private Image backgroundImage = null;
    private int   backgroundType  = CENTERED;

    public LandscapePanel() {
        super();
    }

    public LandscapePanel( boolean isDoubleBuffered ) {
        super( isDoubleBuffered );
    }

    public LandscapePanel( LayoutManager layout ) {
        super( layout );
    }

    public LandscapePanel( LayoutManager layout, boolean isDoubleBuffered ) {
        super( layout, isDoubleBuffered );
    }

    public void setBackgroundImage( Image image ) {
        backgroundImage = image;
        repaint();
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundType( int type ) {
        if ( type == CENTERED || type == SCALED ) {
            backgroundType = type;
            repaint();
        }
        else {
            throw new IllegalArgumentException( "background type should be SCALED or CENTERED." );
        }
    }

    public int getBackgroundType() {
        return backgroundType;
    }

    public void paintComponent( Graphics g ) {
        g.drawImage( backgroundImage, 0, 0, getWidth(), getHeight(), this );
    }
}

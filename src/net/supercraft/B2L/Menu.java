/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L;

import javax.swing.JPanel;

/**
 *
 * @author jojolepro
 */
public class Menu{
    protected JPanel currentPanel = new JPanel();
    /**
     * If you can either add stuff to the jpanel in the constructor, or create a new one with a custom paintComponent
     */
    public Menu(){
        
    }
    public JPanel getPanel(){
        return currentPanel;
    }
    public void onScreenSizeChange(){
        
    }
}

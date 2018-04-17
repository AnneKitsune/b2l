/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun;

import java.awt.Color;

/**
 *
 * @author jojolepro
 */
public enum GunTier {
    PRIMITIVE(new Color(165,165,165)),
    MIDDLEAGES(new Color(140,255,255)),
    GW1(new Color(210,115,25)),
    GW2(new Color(120,25,215)),
    MODERN(new Color(255,105,0)),
    EXPERIMENTAL(new Color(255,0,0)),
    ALIEN(new Color(210,5,160));
    private final Color color;
    private GunTier(Color color){
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
}

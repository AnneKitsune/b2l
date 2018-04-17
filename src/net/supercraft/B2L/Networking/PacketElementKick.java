/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;

/**
 *
 * @author jojolepro
 */
public class PacketElementKick extends PacketElement{

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        System.out.println("Kicking player! (WIP)");
    }
    
}

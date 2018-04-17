/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.Entity;
import net.supercraft.B2L.Entities.EntityPlayer;

/**
 *
 * @author jojolepro
 */
public class PacketElementHealth extends PacketElement{

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        Entity e = gameInstance.getModuleEntityManager().getEntity(Integer.valueOf(parent.getFirstPacketElement(PacketType.ENTITY_ID).getData()));

        if (e != null && e instanceof EntityPlayer) {
            ((EntityPlayer)e).setHealth(Float.valueOf(this.getData()));
        }
    }
    
}

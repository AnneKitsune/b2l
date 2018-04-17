/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.Entity;
import net.supercraft.B2L.Entities.EntityPlayer;
import net.supercraft.B2L.Entities.PlayerState;

/**
 *
 * @author jojolepro
 */
public class PacketElementPlayerState extends PacketElement{

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        System.out.println("RECEIVED PLAYER STATE "+this.getData());
        Entity e = gameInstance.getModuleEntityManager().getEntity(Integer.valueOf(parent.getFirstPacketElement(PacketType.ENTITY_ID).getData()));

        if (e != null && e instanceof EntityPlayer) {
            ((EntityPlayer)e).setPlayerState(PlayerState.valueOf(this.getData()));
        }
    }
    
}

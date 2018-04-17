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
public class PacketElementDisconnect extends PacketElement{

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        ConnectionData conn = ((NetworkedServer)gameInstance.getModuleNetwork()).getConnectionData(parent.getSenderIP(), parent.getSenderPort());
        if(conn!=null){
            int id = conn.getID();
            Entity e = gameInstance.getModuleEntityManager().getEntity(id);
            if(e!=null && e instanceof EntityPlayer){
                gameInstance.getModuleEntityManager().removeObject(e, true);
                ((NetworkedServer)gameInstance.getModuleNetwork()).removeConnectionData(conn.getID());
            }
        }
    }
    
}

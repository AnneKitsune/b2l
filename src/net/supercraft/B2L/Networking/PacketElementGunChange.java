/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.Entity;
import net.supercraft.B2L.Entities.EntityPlayer;
import net.supercraft.B2L.Entities.Gun.EntityGun;

/**
 *
 * @author jojolepro
 */
public class PacketElementGunChange extends PacketElement{

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        //Should only be called cl->sv (command call)
        //there is a different packet for sv->cl (state call)
        
        ConnectionData conn = ((NetworkedServer)gameInstance.getModuleNetwork()).getConnectionData(parent.getSenderIP(), parent.getSenderPort());
        if(conn!=null){
            int id = conn.getID();
            Entity e = gameInstance.getModuleEntityManager().getEntity(id);
            if(e!=null && e instanceof EntityPlayer){
                Entity g = gameInstance.getModuleEntityManager().getEntity(String.valueOf(this.getData()));
                if(g!=null && g instanceof EntityGun){
                    ((EntityPlayer)e).changeGun(((EntityGun)g));
                }
            }
        }
    }
    
}

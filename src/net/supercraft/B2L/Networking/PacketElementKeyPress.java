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
public class PacketElementKeyPress extends PacketElement{
    public PacketElementKeyPress(){
        
    }
    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        ConnectionData conn = ((NetworkedServer)gameInstance.getModuleNetwork()).getConnectionData(parent.getSenderIP(), parent.getSenderPort());
        if(conn!=null){
            int id = conn.getID();
            Entity e = ((EntityPlayer)gameInstance.getModuleEntityManager().getEntity(id));
            if(e!=null && e instanceof EntityPlayer){
                String[] actions = this.getData().split(String.valueOf(PacketType.DATA_SEPARATOR));
                for(int i=0;i<actions.length;i++){
                    boolean press = false;
                    if(actions[i].charAt(0)=='+'){
                        press = true;
                    }
                    ((EntityPlayer)e).updatedKeyState(actions[i].substring(1), press, 0);
                }
            }
        }
    }
    
}

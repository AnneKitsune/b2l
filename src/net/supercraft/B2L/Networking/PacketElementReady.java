/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import java.util.ArrayList;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.Entity;

/**
 *
 * @author jojolepro
 */
public class PacketElementReady extends PacketElement{

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        //send all states to cl
        System.out.println("Received ready!");
        ArrayList<Entity> e = gameInstance.getModuleEntityManager().getList();
        for(int i=0;i<e.size();i++){
            System.out.println("Sending entity positions...");
            PacketElementPosition pos = (PacketElementPosition)PacketType.POSITION.newInstance("");
            pos.setData(e.get(i).getNode().getLocalTranslation());
            ((NetworkedServer)gameInstance.getModuleNetwork()).addPacketElement(e.get(i).getID(), pos);
        }
    }
    
}

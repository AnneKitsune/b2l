/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.EntityPlayer;

/**
 *
 * @author jojolepro
 */
public class PacketElementConnect extends PacketElement {
    public PacketElementConnect(){
        
    }
    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        if (((NetworkedServer) gameInstance.getModuleNetwork()).acceptConnection(parent)) {

            //We received the "CONNECT" packet, not the "CONNECTED", so the rule in ModuleEntityManager to create the player don't apply
            EntityPlayer newPlayer = new EntityPlayer("MPClient");
            B2L.getGameInstance().getModuleEntityManager().addObject(newPlayer, true, true);
            newPlayer.initPlayer();

            ConnectionData c = new ConnectionData(parent.getSenderIP(), parent.getSenderPort());
            c.setID(B2L.getGameInstance().getModuleEntityManager().getHighestID());
            ((NetworkedServer) gameInstance.getModuleNetwork()).addConnectionData(c);

            //sendBuffer.add(new SendBuffer(c.getID()));
            
            
            //we are sending his connection id instead of his entity id, and cl sets his id based on the result of this
            ((NetworkedServer) gameInstance.getModuleNetwork()).addPacketElement(c.getID(), PacketType.CONNECTED.newInstance(String.valueOf(c.getID())));

            System.out.println("Player connected! " + c.getID());
        }
    }
}

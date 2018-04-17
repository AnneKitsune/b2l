/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.EntityPlayer;
import net.supercraft.B2L.MenuInGame;

/**
 *
 * @author jojolepro
 */
public class PacketElementConnected extends PacketElement {

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        if (((NetworkedClient) gameInstance.getModuleNetwork()).getID() == -1) {//We just connected and we were not connected
            System.out.println("Successfully connected to " + ((NetworkedClient) gameInstance.getModuleNetwork()).getSvIP().toString() + ":" + ((NetworkedClient) gameInstance.getModuleNetwork()).getSvPort());

            ((NetworkedClient) gameInstance.getModuleNetwork()).setID(Integer.valueOf(this.getData()));
            System.out.println("MyId:" + ((NetworkedClient) gameInstance.getModuleNetwork()).getID());

            gameInstance.getModuleEntityManager().clearAll();
            
            gameInstance.getModuleEntityManager().setMainPlayerEntity(new EntityPlayer("MainPlayer"));
            gameInstance.getModuleEntityManager().getMainPlayer().setID(((NetworkedClient) gameInstance.getModuleNetwork()).getID());
            gameInstance.getModuleEntityManager().getMainPlayer().initPlayer();
            gameInstance.getModuleEntityManager().getMainPlayer().setControllable(true);

            gameInstance.getModuleCustomWindow().changeMenu(new MenuInGame());
            
            ((NetworkedClient)gameInstance.getModuleNetwork()).addPacketElement(PacketType.READY.newInstance(""));
        }
    }

}

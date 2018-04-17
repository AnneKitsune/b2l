/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import com.jme3.math.Vector3f;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.Entity;

/**
 *
 * @author jojolepro
 */
public class PacketElementPosition extends PacketElement {

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        //Change player pos
        //Assume that the packet have ENTITY_ID, because the network module needs to check it
        //This entity needs to be created before we run this packet, by adding the entity if we see that the ENTITY_ID doesn't exists
        //State can only be received by the client, state sent to the server will be discarted

        Entity e = gameInstance.getModuleEntityManager().getEntity(Integer.valueOf(parent.getFirstPacketElement(PacketType.ENTITY_ID).getData()));

        if (e != null) {
            Vector3f newPos = new Vector3f();
            String[] vars = this.getData().split(String.valueOf(PacketType.DATA_SEPARATOR));
            if (vars.length == 3) {//Correct formed packet
                newPos.setX(Float.valueOf(vars[0]));
                newPos.setY(Float.valueOf(vars[1]));
                newPos.setZ(Float.valueOf(vars[2]));
                
                e.setPosition(newPos);
            }
        }
    }
    public void setData(Vector3f pos){
        this.data = String.valueOf(pos.getX())+PacketType.DATA_SEPARATOR+String.valueOf(pos.getY())+PacketType.DATA_SEPARATOR+String.valueOf(pos.getZ());
    }

}

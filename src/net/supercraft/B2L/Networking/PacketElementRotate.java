/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import com.jme3.math.Quaternion;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.Entity;
import net.supercraft.B2L.Entities.EntityPlayer;

/**
 *
 * @author jojolepro
 */
public class PacketElementRotate extends PacketElement {

    public PacketElementRotate() {

    }

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        ConnectionData conn = ((NetworkedServer) gameInstance.getModuleNetwork()).getConnectionData(parent.getSenderIP(), parent.getSenderPort());
        if (conn != null) {
            int id = conn.getID();
            Entity e = ((EntityPlayer) gameInstance.getModuleEntityManager().getEntity(id));
            if (e != null && e instanceof EntityPlayer) {
                String[] vars = this.getData().split(String.valueOf(PacketType.DATA_SEPARATOR));
                if (vars.length == 3) {//Correct formed packet
                    ((EntityPlayer)e).setRotation(new Quaternion().fromAngles(Float.valueOf(vars[0]), Float.valueOf(vars[1]), Float.valueOf(vars[2])));
                }
            }
        }
    }

    public void setData(Quaternion q) {
        float[] angles = new float[3];
        q.toAngles(angles);
        this.setData(String.valueOf(angles[0]) + PacketType.DATA_SEPARATOR + String.valueOf(angles[1]) + PacketType.DATA_SEPARATOR + String.valueOf(angles[2]));
    }
}

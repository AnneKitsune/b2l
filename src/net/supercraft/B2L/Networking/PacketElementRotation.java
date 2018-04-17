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
public class PacketElementRotation extends PacketElement {

    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        Entity e = gameInstance.getModuleEntityManager().getEntity(Integer.valueOf(parent.getFirstPacketElement(PacketType.ENTITY_ID).getData()));
        if (e != null) {
            String[] vars = this.getData().split(String.valueOf(PacketType.DATA_SEPARATOR));
            if (vars.length == 3) {//Correct formed packet
                e.setRotation(new Quaternion().fromAngles(Float.valueOf(vars[0]), Float.valueOf(vars[1]), Float.valueOf(vars[2])));
                /*if(e instanceof EntityPlayer){
                    ((EntityPlayer)e).getCamCopyNode().getLocalRotation().fromAngles(Float.valueOf(vars[0]), Float.valueOf(vars[1]), Float.valueOf(vars[2]));
                }else{
                    e.getNode().getLocalRotation().fromAngles(Float.valueOf(vars[0]), Float.valueOf(vars[1]), Float.valueOf(vars[2]));
                }*/
            }
        }
    }
    public void setData(Quaternion q){
        float[] angles = new float[3];
        q.toAngles(angles);
        this.setData(String.valueOf(angles[0]) + PacketType.DATA_SEPARATOR + String.valueOf(angles[1]) + PacketType.DATA_SEPARATOR + String.valueOf(angles[2]));
    }
    /*public Quaternion fromData(){
        
    }*/
}

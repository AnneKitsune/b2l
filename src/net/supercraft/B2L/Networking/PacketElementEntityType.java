/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;

/**
 *
 * @author jojolepro
 */
public class PacketElementEntityType extends PacketElement{
    public PacketElementEntityType(){
        
    }
    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        //TODO
        //if entity !exist, create
        /*Entity e = gameInstance.getModuleEntityManager().getEntity(Integer.valueOf(this.getData()));
        if(e==null){
            //Add entity
            //How to know entity player,gun,...?
        }*/
    }
}

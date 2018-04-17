/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Entities.Entity;
import net.supercraft.B2L.Entities.EntityType;

/**
 *
 * @author jojolepro
 */
public class PacketElementEntityID extends PacketElement{
    public PacketElementEntityID(){
        
    }
    @Override
    public void runAction(B2L gameInstance, Packet parent) {
        //TODO
        //if entity !exist, create
        Entity e = gameInstance.getModuleEntityManager().getEntity(Integer.valueOf(this.getData()));
        if(e==null){
            System.out.println("Entity "+Integer.valueOf(this.getData())+" does not exists.");
            //Add entity
            //How to know entity player,gun,...? -> EntityType registry! Works like PacketType.
            PacketElement pe = parent.getFirstPacketElement(PacketType.ENTITY_TYPE);
            if(pe!=null){
                EntityType type = EntityType.getFromTypeID(Integer.valueOf(pe.getData()));
                if(type!=null){
                    System.out.println("Creating entity "+Integer.valueOf(this.getData()));
                    Entity ne = type.newInstance();
                    ne.setID(Integer.valueOf(this.getData()));
                    gameInstance.getModuleEntityManager().addObject(ne, true);
                }
            }
        }
    }
}

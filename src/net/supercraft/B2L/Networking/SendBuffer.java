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
public class SendBuffer {

    private int id = 0;
    private Packet buf = new Packet();

    /**
     * Used by server to send state/commands of a specific entity(
     *
     * @param id
     */
    public SendBuffer(int id) {
        this.id = id;
    }

    public void emptyBuf() {
        buf.getAllPacketElement().clear();
    }

    public void addPacketElement(PacketElement pe) {
        //Cl can't send state packet, and we only send entity id if it is a state
        if (B2L.getGameInstance().isServer() && (buf.isStatePacket()||!pe.getPacketType().isCmd())) {
            if (!buf.contains(PacketType.ENTITY_ID)) {
                buf.addPacketElement(PacketType.ENTITY_ID.newInstance(String.valueOf(id)));
            }
            if (!buf.contains(PacketType.ENTITY_TYPE)) {
                Entity entity = B2L.getGameInstance().getModuleEntityManager().getEntity(id);
                if(entity!=null){
                    EntityType type = EntityType.getTypeFromEntity(entity);
                    if(type!=null){
                        buf.addPacketElement(PacketType.ENTITY_TYPE.newInstance(String.valueOf(type.getTypeID())));
                    }else{
                        System.err.println("Can't send data to entity, the entity type is not defined");
                    }
                }else{
                    System.err.println("Can't send data to null entity!(or not defined entity type)");
                }
            }
        }
        buf.addPacketElement(pe);
    }

    public Packet getBuf() {
        return buf;
    }

    public int getID() {
        return id;
    }
}

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
public class PacketElement {
    protected PacketType packetType;
    protected String data;

    public PacketElement() {
        
    }

    public PacketElement(PacketType packetType, String data) {
        this.packetType = packetType;
        this.data = data;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    /**
     * Will throw error if there is no packetType define
     * @return 
     */
    public String getFormattedContent() {
        StringBuilder out = new StringBuilder();
        out.append(PacketType.PREFIX_START);
        out.append(getPacketType().getID());
        out.append(PacketType.PREFIX_END);
        out.append(getData());
        return out.toString();
    }
    @Override
    public String toString(){
        return packetType.name()+":"+data;
    }
    
    public void runAction(B2L gameInstance, Packet parent){
        //Nothing by default
        System.out.println("Default null run action called!");
    }
}

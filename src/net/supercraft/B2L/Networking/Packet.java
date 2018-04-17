/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import java.net.InetAddress;
import java.util.ArrayList;
import net.supercraft.B2L.Entities.Entity;

/**
 *
 * @author jojolepro
 */
public class Packet {

    private ArrayList<PacketElement> content = new ArrayList<PacketElement>();
    protected InetAddress senderIP;
    protected int senderPort;

    public Packet() {

    }

    public Packet(PacketElement... elements) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] != null) {
                content.add(elements[i]);
            }
        }
    }

    public void addPacketElement(PacketElement packetElement) {
        content.add(packetElement);
    }

    public void fromFormattedData(String formattedData) {
        String buf = "";
        boolean read = false;
        for (int i = 0; i < formattedData.length(); i++) {
            char currentChar = formattedData.charAt(i);
            buf += currentChar;
            if (currentChar == PacketType.PREFIX_START || i == formattedData.length() - 1) {//We just hit a new prefix or we hit the end of the data
                if (buf.length() > 1) {
                    if (buf.endsWith(String.valueOf(PacketType.PREFIX_START))) {//Remove the trailing prefix that may be added by the reader
                        buf = buf.substring(0, buf.length() - 1);
                    }
                    PacketElement currentPacketElement = getPacketElement(buf);
                    if (currentPacketElement != null) {
                        content.add(currentPacketElement);
                    }
                }

                buf = String.valueOf(currentChar);//Add current data so we don't miss the new prefix
            }
        }
    }

    public String toFormattedData() {
        String out = "";
        for (int i = 0; i < content.size(); i++) {
            out += content.get(i).getFormattedContent();
        }
        return out;
    }

    private PacketElement getPacketElement(String formattedDataSection) {
        PacketType packetType = getPacketType(formattedDataSection);
        String data = getPacketData(formattedDataSection);
        return packetType.newInstance(data);
    }

    /**
     * Combine packets that can be. Example: KEY_PRESS if you have +jump and
     * +shoot, you can combine them in 1 packet element +jump_+shoot
     */
    public void pack() {
        ArrayList<PacketElement> kp = this.getPacketElement(PacketType.KEY_PRESS);
        for (int i = 1; i < kp.size(); i++) {
            kp.get(0).setData(kp.get(0).getData() + PacketType.DATA_SEPARATOR + kp.get(i).getData());
            this.getAllPacketElement().remove(kp.get(i));
        }
    }
    /**
     * Remove duplicates PacketElements, keeps last one
     * @param packetType
     */
    public void removeDuplicates(PacketType packetType){
        ArrayList<PacketElement> l = this.getPacketElement(packetType);
        for(int i=0;i<l.size()-1;i++){
            this.getAllPacketElement().remove(l.get(i));
        }
    }
    public boolean isStatePacket() {
        boolean state = false;
        for (int i = 0; i < this.getAllPacketElement().size(); i++) {
            if (!this.getAllPacketElement().get(i).getPacketType().isCmd()) {
                state = true;
            }
        }
        return state;
    }

    public boolean isValid() {
        boolean valid = true;
        if (isStatePacket()) {
            if (!this.contains(PacketType.ENTITY_ID)||!this.contains(PacketType.ENTITY_TYPE)) {
                //Drop packet
                valid = false;
            }
        }
        return valid;
    }
    /*@Deprecated
     public boolean containsPacketType(PacketType packetType) {
     return getPrefixedData(packetType).length() > 0;
     }

     @Deprecated
     public String getPrefixedData(PacketType packetType) {
     String out = "";
     String buf = "";
     boolean read = false;
     for (int i = 0; i < content.length(); i++) {
     char currentChar = content.charAt(i);
     if (!read) {//Want to find where data starts
     if (currentChar == PacketType.PREFIX_START) {//Found start of packet
     read = true;
     buf += currentChar;
     }
     } else {//Prefix was found, now we read until we hit another prefix or EOF
     if (currentChar == PacketType.PREFIX_START || i == content.length() - 1) {//We just hit a new prefix or we hit the end of the data
     read = false;
     PacketType currentPacketType = getPacketType(buf);
     if (packetType.equals(currentPacketType)) {//We found the good packet
     out = buf;
     break;
     } else {//We got a wrong packet, reset the buffer and continue searching
     buf = String.valueOf(currentChar);//Add current data so we don't miss the new prefix
     }
     } else {
     buf += currentChar;
     }
     }
     }
     return out;
     }*/

    private PacketType getPacketType(String prefixedData) {
        String packetID = "";
        for (int i = 0; i < prefixedData.length(); i++) {
            char currentChar = prefixedData.charAt(i);
            if (currentChar != PacketType.PREFIX_START) {//we are before the packet id
                if (currentChar == PacketType.PREFIX_END) {//we are after the packet id
                    break;
                } else {
                    packetID += currentChar;
                }
            }
        }
        int id = Integer.valueOf(packetID);
        return PacketType.getPacketTypeFromID(id);
    }

    private String getPacketData(String prefixedData) {
        String data = "";
        int prefixEndPos = prefixedData.indexOf(PacketType.PREFIX_END);
        if (prefixEndPos > 0 && prefixEndPos < prefixedData.length()) {
            data = prefixedData.substring(prefixEndPos + 1);
        }
        return data;
    }

    public boolean contains(PacketType packetType) {
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).getPacketType().equals(packetType)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<PacketElement> getAllPacketElement() {
        return content;
    }

    public ArrayList<PacketElement> getPacketElement(PacketType packetType) {
        ArrayList<PacketElement> out = new ArrayList<PacketElement>();
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).getPacketType().equals(packetType)) {
                out.add(content.get(i));
            }
        }
        return out;
    }

    public PacketElement getFirstPacketElement(PacketType packetType) {
        ArrayList<PacketElement> tmp = getPacketElement(packetType);
        if (tmp.size() > 0) {
            return tmp.get(0);
        }
        return null;
    }

    /**
     * Does the packet contains necessary elements to be used?(PlayerID)
     *
     * @return
     */
    public boolean containsNeededElements() {
        if(isStatePacket()){
            if(!contains(PacketType.ENTITY_ID) || !contains(PacketType.ENTITY_TYPE)){
                return false;
            }
        }
        if(getAllPacketElement().isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return toFormattedData();
    }

    public InetAddress getSenderIP() {
        return senderIP;
    }

    public void setSenderIP(InetAddress senderIP) {
        this.senderIP = senderIP;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }
    /*public static ArrayList<PacketElement> generateEntityState(Entity e){
        
     }*/
}

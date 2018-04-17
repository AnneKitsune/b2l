/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.supercraft.B2L.CustomModuleManager;
import net.supercraft.jojoleproUtils.module.control.Module;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;

/**
 *
 * @author jojolepro
 */
public abstract class ModuleNetworkUdp extends Module implements IUpdatable{

    public static final String LOCALHOST = "::1";
    protected DatagramSocket socket;
    
    protected LinkedList<Packet> readBuf = new LinkedList<Packet>();
    
    public InetAddress localInetAddress;
    public int localPort = 2950;
    
    //public ArrayList<ConnectionData> connections = new ArrayList<ConnectionData>();
    

    public ModuleNetworkUdp(CustomModuleManager mm) {
        super(mm);
   }
    
    public void createNetworkSocket(String localIp, int localPort) {
        if (socket != null) {
            System.out.println("Network client already created! please destroyNetworkSocket()");
            socket.close();
            socket = null;
        }
        try {
            socket = new DatagramSocket(localPort);
            localInetAddress = InetAddress.getByName(localIp);
            this.localPort = localPort;
        } catch (SocketException ex) {
            Logger.getLogger(NetworkedClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetworkedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Thread t = new Thread(new Runnable(){
            public void run(){
                while(socket!=null){
                    listen();
                }
            }
        }) ;
        t.start();
    }
    
    /*public void send(int id,Packet packet) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getID() == id) {
                send(connections.get(i),packet);
            }
        }
    }*/

    public void send(InetAddress destIP, int port, Packet packet) {
        try {
            if(packet.toFormattedData().equals("")){//Don't send empty packet, this check is performed on manually sent packet(connection packet mainly)
                System.err.println("Can't send empty packet!");
                return;
            }
            if(!packet.containsNeededElements()){
                System.err.println("Can't send packet without all needed elements");
                return;
            }
            packet.pack();
            packet.removeDuplicates(PacketType.POSITION);
            packet.removeDuplicates(PacketType.ROTATION);
            
            byte[] packetData = packet.toFormattedData().getBytes(Charset.forName("UTF-8"));
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, destIP, port);
            socket.send(sendPacket);
            //System.out.println("PacketSent:"+packet.toFormattedData());
        } catch (IOException ex) {
            Logger.getLogger(NetworkedClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Empty ONE connection data by sending buffer to EVERYONE
     * connected
     * @param id 
     */
    /*public void flush(int id) {
        for(int i=0;i<connections.size();i++){
            if(connections.get(i).getID()==id){
                flush(connections.get(i));
            }
        }
    }*/
    /**
     * Empty ONE connection data by sending buffer to EVERYONE
     * connected
     * @param dest 
     */
    /*public void flush(ConnectionData dest){
        if(dest.getSendBuffer().toFormattedData().isEmpty()){//Don't send if it is empty, avoid error messages when there is nothing to send
            return;
        }
        this.broadcast(dest.getSendBuffer());
        dest.emptyBuffer();
    }*/
    
    /**
     * Broadcast each connections data to each player
     * 
     * conndata->self data that we are sending to everyone
     */
    /*public void flushAll(){
        for(int i=0;i<connections.size();i++){
            flush(connections.get(i));
        }
    }*/
    /**
     * Sends packet to everyone connected
     * @param packet 
     */
    /*public void broadcast(Packet packet) {
        for (int i = 0; i < connections.size(); i++) {
            send(connections.get(i), packet);
        }
    }*/
    /**
     * Add the packet element to our current buffer,
     * this buffer will eventually be emptied by the main
     * loop and broadcasted to everyone
     * 
     * Prefer using this one when working in player classes,
     * because the code will be shared between cl and sv.
     * CL SELF ID: NETWORK ID
     * SV SELF ID: PLAYER ID(STATE) NETWORK ID(COMMAND REDIRECT)
     * 
     * SV SIDE ONLY CALL
     * @param selfID
     * @param packetElement 
     */
    /*public void addPacketElement(int selfID, PacketElement packetElement){
        for(int i=0;i<connections.size();i++){
            if(connections.get(i).getID()==selfID){
                addPacketElement(connections.get(i), packetElement);
            }
        }
    }*/
    /**
     * Add the packet element to our current buffer,
     * this buffer will eventually be emptied by the main
     * loop and broadcasted to everyone
     * 
     * SV SIDE ONLY CALL
     * @param self
     * @param packetElement 
     */
    /*public void addPacketElement(ConnectionData self, PacketElement packetElement) {
        self.addPacketElement(packetElement);
    }*/
    /**
     * Add the packet element to our current buffer,
     * this buffer will eventually be emptied by the main
     * loop and broadcasted to everyone
     * 
     * Automatically add packet to the correct buffer on cl side
     * CL SIDE ONLY CALL
     * @param packetElement 
     */
    /*public void addPacketElement(PacketElement packetElement){
        if(getConnectionData(myID)!=null){
            addPacketElement(getConnectionData(myID), packetElement);
        }else{
            addPacketElement(getConnectionData(myID), packetElement);
        }
    }*/
    /**
     * Player id will be automatically ignored client side
     * 
     * Add the packet element to our current buffer,
     * this buffer will eventually be emptied by the main
     * loop and broadcasted to everyone
     * 
     * CL SELF ID: NETWORK ID
     * SV SELF ID: PLAYER ID(STATE) NETWORK ID(COMMAND REDIRECT)
     * @param playerID the id of the player sending the information, automatically ignored cl side
     * @param packetElement 
     */
    //public abstract void addPacketElement(int playerID,PacketElement packetElement);
    
    public void destroyNetworkSocket() {
        if (socket != null) {
            socket.close();
        }
    }

    @Override
    public void update(long tpf) {
        /*for (int i = 0; i < connections.size(); i++) {
            connections.get(i).connectionTime += tpf;
        }*/
        sendAll();
        consumeReadBuffer();
    }
    public abstract void consumeReadBuffer();
    /*public void remove(int id){
        for(int i=0;i<connections.size();i++){
            if(connections.get(i).getID()==id){
                connections.remove(i);
                return;
            }
        }
    }*/
    /*public void remove(ConnectionData conn){
        connections.remove(conn);
    }*/
    public void listen(){
        try {
            DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
            socket.receive(receivedPacket);
            String receivedData = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

            Packet received = new Packet();
            received.fromFormattedData(receivedData);
            received.setSenderIP(receivedPacket.getAddress());
            received.setSenderPort(receivedPacket.getPort());
            System.out.println("RECEIVED PACKET!:" + received.toFormattedData());
            
            if(received.containsNeededElements()){
                synchronized(this){
                    readBuf.add(received);
                }
            }
            //((CustomModuleManager)mm).onPacketReceived(received);
            
        } catch (SocketException ex) {
            System.err.println("Can't create UDP server! Ex:" + ex.toString());
        } catch (IOException ex) {
            Logger.getLogger(NetworkedServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void shutdown() {
        destroyNetworkSocket();
        super.shutdown();
    }
    /*public ConnectionData getConnectionData(int id){
        for(int i=0;i<connections.size();i++){
            if(connections.get(i).getID()==id){
                return connections.get(i);
            }
        }
        return null;
    }*/
    public abstract void removeNotRunnablePackets(Packet packet);
    protected abstract void sendAll();
}

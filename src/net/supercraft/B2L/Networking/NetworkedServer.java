/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import java.net.InetAddress;
import java.util.ArrayList;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.CustomModuleManager;
import net.supercraft.B2L.Entities.Entity;

/**
 *
 * @author jojolepro
 */
public class NetworkedServer extends ModuleNetworkUdp {

    //Send buffer, 1/entity
    private ArrayList<SendBuffer> sendBuffer = new ArrayList<SendBuffer>();

    //List of connected clients, for ip/port auth via id
    private ArrayList<ConnectionData> connectedClients = new ArrayList<ConnectionData>();

    public NetworkedServer(CustomModuleManager mm) {
        super(mm);
        localPort = 2950;
        this.createNetworkSocket(ModuleNetworkUdp.LOCALHOST, localPort);

        System.out.println("Listening on " + localInetAddress.toString() + ":" + localPort);
    }

    public void kick(int id) {
        addPacketElement(id, PacketType.DISCONNECT.newInstance("Kicked by server!"));
        removeConnectionData(id);
    }

    public SendBuffer getSendBufferOfClient(int clID) {
        for (int i = 0; i < sendBuffer.size(); i++) {
            if (sendBuffer.get(i).getID() == clID) {
                return sendBuffer.get(i);
            }
        }
        return null;
    }

    /*public synchronized void onPacketReceived(Packet packet) {
     if (packet.contains(PacketType.CONNECT)) {
     if (acceptConnection(packet)) {

     //We received the "CONNECT" packet, not the "CONNECTED", so the rule in ModuleEntityManager to create the player don't apply
     EntityPlayer newPlayer = new EntityPlayer("MPClient");
     B2L.getGameInstance().getModuleEntityManager().addObject(newPlayer, true, true);
     newPlayer.initPlayer();

     ConnectionData c = new ConnectionData(packet.getSenderIP(), packet.getSenderPort());
     c.setID(B2L.getGameInstance().getModuleEntityManager().getHighestID());
     connectedClients.add(c);
                
     sendBuffer.add(new SendBuffer(c.getID()));
                
     addPacketElement(c.getID(), PacketType.CONNECTED.newInstance(String.valueOf(c.getID())));
                
     System.out.println("Player connected! " + c.getID());
     return;
     }
     }

     if (!checkAuth(packet)) {//After this, we know that there is a player id and that it is associated with the correct ip/port
     System.err.println("Received wrong auth packet!");
     return;
     }

     //if (packet.contains(PacketType.POSITION)) {
     //broadcast(packet);//Simple client->broadcast position redirect, change when implementing anti-cheat
     //}
     if (packet.contains(PacketType.DISCONNECT)) {
     ConnectionData conn = getConnectionData(Integer.valueOf(packet.getPacketElement(PacketType.ENTITY_ID).get(0).getData()));
     System.out.println("Player disconnected! " + conn.getIP().toString() + ":" + conn.getPort());
            
     //Tell everyone that someone disconnected
     addPacketElement(Integer.valueOf(packet.getFirstPacketElement(PacketType.ENTITY_ID).getData()), PacketType.ENTITY_REMOVE.newInstance(""));
     removeConnectionData(conn.getID());
     }
     }*/
    public boolean checkAuth(Packet packet) {
        if (packet.contains(PacketType.ENTITY_ID)) {
            PacketElement playerID = packet.getPacketElement(PacketType.ENTITY_ID).get(0);
            int id = Integer.valueOf(playerID.getData());
            if (!isRegistered(id)) {//Check if id is registered
                System.err.println("Data sent from disconnected user.");
                return false;
            } else {//Registered user, check if ip and port are OK
                if (checkIPAndPort(packet, id)) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean checkIPAndPort(Packet packet, int playerID) {
        ConnectionData conn;
        if ((conn = getConnectionData(playerID)) != null) {
            if (conn.getIP().toString().equals(packet.getSenderIP().toString()) && conn.getPort() == packet.getSenderPort()) {
                return true;
            }
        }
        return false;
    }

    public ConnectionData getConnectionData(int id) {
        for (int i = 0; i < connectedClients.size(); i++) {
            if (connectedClients.get(i).getID() == id) {
                return connectedClients.get(i);
            }
        }
        return null;
    }

    public ConnectionData getConnectionData(InetAddress ip, int port) {
        for (int i = 0; i < connectedClients.size(); i++) {
            if (connectedClients.get(i).getIP().toString().equals(ip.toString()) && connectedClients.get(i).getPort() == port) {
                return connectedClients.get(i);
            }
        }
        return null;
    }

    public void removeConnectionData(int id) {
        ConnectionData conn = getConnectionData(id);
        if (conn != null) {
            sendAll();//Empty buffers before we remove one, so we don't lose data
            sendBuffer.remove(getSendBufferOfClient(id));
            connectedClients.remove(conn);
        }
    }

    private boolean isRegistered(int playerID) {
        return getConnectionData(playerID) != null;
    }

    public boolean acceptConnection(Packet packet) {
        ConnectionData connectingPlayer = new ConnectionData(packet.getSenderIP(), packet.getSenderPort());

        //Also look if user is already registered
        for (int i = 0; i < connectedClients.size(); i++) {
            if (connectedClients.get(i).getIP().toString().equals(packet.getSenderIP().toString()) && connectedClients.get(i).getPort() == packet.getSenderPort()) {//Already connected
                System.err.println("Conn refused: Cl already connected on this IP/PORT!");
                return false;
            }
        }
        return true;
    }

    @Override
    @Deprecated
    public void removeNotRunnablePackets(Packet packet) {
        for (int i = 0; i < packet.getAllPacketElement().size(); i++) {
            /*if (packet.getAllPacketElement().get(i).getPacketType().isSvSentOnly()) {
             System.err.println("Received packet that should only be sent from server side!");
             packet.getAllPacketElement().remove(i);
             i--;
             } else if (!packet.getAllPacketElement().get(i).getPacketType().isSvRunnable()) {
             System.err.println("Received packet that is not runnable by the server!");
             packet.getAllPacketElement().remove(i);
             i--;
             } else if (!packet.getAllPacketElement().get(i).getPacketType().isOP()) {
             ArrayList<PacketElement> pid;
             if ((pid = packet.getPacketElement(PacketType.ENTITY_ID)).size() > 0) {
             ConnectionData conn;
             if ((conn = getConnectionData(Integer.valueOf(pid.get(0).getData()))) != null) {
             if (!conn.isOP()) {
             System.err.println("Received packet that requires OP from non-op player!");
             packet.getAllPacketElement().remove(i);
             i--;
             }
             }
             }
             }*/
        }
    }

    public void addPacketElement(int id, PacketElement packetElement) {
        for (int i = 0; i < sendBuffer.size(); i++) {
            if (sendBuffer.get(i).getID() == id) {
                sendBuffer.get(i).addPacketElement(packetElement);
                return;
            }
        }
        System.err.println("Trying to add data to non existant send buffer");
        System.out.println("id:" + id);
    }
    public void addEntitySendBuffer(Entity e){
        sendBuffer.add(new SendBuffer(e.getID()));
    }
    public void removeEntitySendBuffer(Entity e){
        for(int i=0;i<sendBuffer.size();i++){
            if(sendBuffer.get(i).getID()==e.getID()){
                sendBuffer.remove(i);
                return;
            }
        }
    }
    @Override
    protected void sendAll() {
        for (int i = 0; i < sendBuffer.size(); i++) {
            if (sendBuffer.get(i).getBuf().containsNeededElements()) {
                for (int j = 0; j < connectedClients.size(); j++) {
                    send(connectedClients.get(j).getIP(), connectedClients.get(j).getPort(), sendBuffer.get(i).getBuf());
                }
            }
            sendBuffer.get(i).emptyBuf();
        }
    }

    public void addConnectionData(ConnectionData conn) {
        this.connectedClients.add(conn);
        this.sendBuffer.add(new SendBuffer(conn.getID()));
    }
    
    @Override
    public void consumeReadBuffer() {
        synchronized (this) {
            while (!readBuf.isEmpty()) {
                Packet p = readBuf.pop();
                if (p.isValid()) {
                    for (int i = 0; i < p.getAllPacketElement().size(); i++) {
                        if (p.getAllPacketElement().get(i).getPacketType().isSvRunnable()) {
                            if (p.getAllPacketElement().get(i).getPacketType().isOP()) {
                                if (getConnectionData(p.getSenderIP(), p.getSenderPort()).isOP()) {
                                    p.getAllPacketElement().get(i).runAction(B2L.getGameInstance(), p);
                                }
                            } else {
                                p.getAllPacketElement().get(i).runAction(B2L.getGameInstance(), p);
                            }
                        }
                    }
                }
            }
        }
    }
}

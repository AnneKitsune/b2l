/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import java.net.InetAddress;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.CustomModuleManager;
import net.supercraft.B2L.ICustomKeyControllable;

/**
 *
 * @author jojolepro
 */
public class NetworkedClient extends ModuleNetworkUdp implements ICustomKeyControllable{

    private int clID = -1;
    private SendBuffer sendBuffer = new SendBuffer(-1);

    private InetAddress svIP;
    private int svPort = 2950;

    public NetworkedClient(CustomModuleManager mm) {
        super(mm);

        localPort = 2949;
        //this.createNetworkSocket(LOCALHOST, localPort);
        /*try {
         this.connect(InetAddress.getByName(LOCALHOST), 2950);
         } catch (UnknownHostException ex) {
         Logger.getLogger(NetworkedClient.class.getName()).log(Level.SEVERE, null, ex);
         }*/

    }

    public void connect(InetAddress svIP, int svPort) {
        if (this.svIP != null) {
            System.err.println("Already connected! Please disconnect()");
            return;
        }

        this.svIP = svIP;
        this.svPort = svPort;
        System.out.println("Sending connections request to " + this.svIP + ":" + svPort);

        addPacketElement(PacketType.CONNECT.newInstance(""));
    }

    public void disconnect() {
        if (svIP != null) {
            System.out.println("Disconnecting from:" + svIP.toString() + ":" + svPort);

            addPacketElement(PacketType.DISCONNECT.newInstance(""));

            this.clID = -1;
            svIP = null;
        }
    }

    /*public synchronized void onPacketReceived(Packet packet) {
     //The only way to end up with a wrong id, is if 2 players connect in the same server tick, or if a network latency makes a wrong connection packet arrive before the good one
     if (packet.contains(PacketType.CONNECTED)) {
     if (clID == -1) {//We just connected and we were not connected
     System.out.println("Successfully connected to " + svIP.toString() + ":" + svPort);
                
     clID = Integer.valueOf(packet.getFirstPacketElement(PacketType.ENTITY_ID).getData());
     System.out.println("MyId:" + clID);
                
                
     B2L.getGameInstance().getModuleCustomWindow().changeMenu(new MenuInGame());
     }
            
     B2L.getGameInstance().getModuleEntityManager().setMainPlayerEntity(new EntityPlayer("Client#"+connections.get(connections.size()-1).getID()));
     B2L.getGameInstance().getModuleEntityManager().getPlayer().id = getID();
     B2L.getGameInstance().getModuleEntityManager().getPlayer().setControllable(true);
     B2L.getGameInstance().getModuleCustomWindow().changeMenu(new MenuInGame());
     }
     }*/
    @Override
    public void shutdown() {
        this.disconnect();
        this.destroyNetworkSocket();
        super.shutdown();
    }

    /*@Override
     public void flushAll() {
     if (sendBuf.getAllPacketElement().size() > 0) {
     broadcast(sendBuf);
     sendBuf = new Packet();
     }
     }*/
    @Override
    @Deprecated
    public void removeNotRunnablePackets(Packet packet) {
        /*for (int i = 0; i < packet.getAllPacketElement().size(); i++) {
         if (packet.getAllPacketElement().get(i).getPacketType().isClRunnable()) {
         System.err.println("Received packet that should only be sent from server side!");
         packet.getAllPacketElement().remove(i);
         i--;
         } else if (!packet.getAllPacketElement().get(i).getPacketType().isSvRunnable()) {
         System.err.println("Received packet that is not runnable by the server!");
         packet.getAllPacketElement().remove(i);
         i--;
         }
         }*/
        //broke rn
    }

    public void addPacketElement(PacketElement packetElement) {
        sendBuffer.addPacketElement(packetElement);
    }

    @Override
    protected void sendAll() {
        if (sendBuffer.getBuf().containsNeededElements()) {
            send(svIP, svPort, sendBuffer.getBuf());
            sendBuffer.emptyBuf();
        }
    }

    public int getID() {
        return clID;
    }

    public void setID(int id) {
        this.clID = id;
    }

    public InetAddress getSvIP() {
        return svIP;
    }

    public int getSvPort() {
        return svPort;
    }

    @Override
    public void consumeReadBuffer() {
        synchronized (this) {
            while (!readBuf.isEmpty()) {
                Packet p = readBuf.pop();
                if (p.isValid()) {
                    for (int i = 0; i < p.getAllPacketElement().size(); i++) {
                        if (p.getAllPacketElement().get(i).getPacketType().isClRunnable()) {
                            //System.out.println("Run packet:"+p.getAllPacketElement().get(i).getPacketType());
                            p.getAllPacketElement().get(i).runAction(B2L.getGameInstance(), p);
                        }
                    }
                }else{
                    System.err.println("Packet not valid");
                }
            }
        }
    }

    public void updatedKeyState(String name, boolean pressed, float tpf) {
        if(pressed){
            addPacketElement(PacketType.KEY_PRESS.newInstance("+"+name));
        }else{
            addPacketElement(PacketType.KEY_PRESS.newInstance("-"+name));
        }
    }
}

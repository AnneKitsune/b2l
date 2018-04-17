/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jojolepro
 */
public class ConnectionData {
    public InetAddress ip;
    public int port;
    private long connectionTime = 0;
    
    //Only used to associate a ip/port to a sv received packet id
    private int id;//Supposed to be the same id as the player instance on sv side
    //Supposed to be 0 on the cl side(represents server id)
    private boolean isOP = false;
    
    //private Packet sendBuf = new Packet();
    
    /**
     * Keeps track of the cl / (sv) connections
     * Id is supposed to be the same as the one is module entity manager
     * @param ip
     * @param port 
     */
    public ConnectionData(String ip,int port){
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConnectionData.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.port = port;
    }
    public ConnectionData(InetAddress ip,int port){
        this.ip = ip;
        this.port = port;
    }
    public InetAddress getIP(){
        return ip;
    }
    public int getPort(){
        return port;
    }
    public int getID(){
        return id;
    }
    public void setID(int id){
        this.id = id;
    }
    public void setOP(boolean op){
        this.isOP = op;
    }
    public boolean isOP(){
        return isOP;
    }
    public long getConnectionTime(){
        return connectionTime;
    }
    public void addConnectionTime(long tpf){
        this.connectionTime = tpf;
    }
    public void setConnectionTime(long connTime){
        this.connectionTime = connTime;
    }
}

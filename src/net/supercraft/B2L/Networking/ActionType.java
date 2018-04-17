/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Networking;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *2 packet types->command and state
 * command->cl and sv can send
 * state->sv only can send(broadcast each entity state to all clients)
 * @author jojolepro
 */
public enum ActionType {
    //Sv->Cl only
    TIME_SINCE_START      (PacketElement.class, 0, true, false, false, false),
    ENTITY_ID             (PacketElementEntityID.class, 1, true, false, false, false),
    POSITION              (PacketElementPosition.class, 2, true, false, false, false),
    ROTATION              (PacketElementRotation.class, 3, true, false, false, false),
    PLAYER_STATE          (PacketElementPlayerState.class, 4, true, false, false, false),
    HEALTH                (PacketElementHealth.class, 5, true, false, true, false),
    GUN                   (PacketElementGun.class, 6, true, false, false, false),
    ENTITY_TYPE           (PacketElementEntityType.class, 7 ,true, false, false, false),
    
    
    KEY_PRESS             (PacketElementKeyPress.class, 51, false, true, false, true),//+FORWARD_+JUMP_-JUMP... || +1_+2_-2 Cl->Sv
    GUN_CHANGE            (PacketElementGunChange.class, 52, false, true, false, true),//Cl->Sv //Uses entity id of gun... need to find better way
    GUN_DROP              (PacketElementGunDrop.class, 53, false, true, false, true),//Cl->Sv
    //SHOOT                 (107, false, true),//Is included in KEY_PRESS
    //GUN_PICKUP            (9, false,  true, false,  true,  true,  true),//->attach gun to player, remove physics
    KILL                  (PacketElementKill.class, 54, true, true, true, true),//Cl<->Sv
    CONNECT               (PacketElementConnect.class, 55, false, true, false, true),//Cl->Sv
    CONNECTED             (PacketElementConnected.class, 56, true, false, true,  true),//Cl<-Sv
    //SET_ID                (52, true,  true, false,  true, false,  true),
    DISCONNECT            (PacketElementDisconnect.class, 57, false, true, false, true),//Cl->Sv
    //DISCONNECTED          (54,false,  true, false,  true,  true,  true),//->remove entity
    //VALIDATE_CONNECTION   (55,false,  true,  true, false, false, false),
    KICK                  (PacketElementKick.class, 68, true, true, true, true),//Cl<->Sv
    ENTITY_REMOVE         (PacketElementEntityRemove.class, 59, true, true, true, true),//EntityId_Message (no ADD_ENTITY, because cl will add entity as soon as he receives entity state)//Cl<->Sv
    //SET_LEVEL             (60, true,  true,  true, false, false, false),//->add entity map
    SET_SCORE             (PacketElementSetScore.class, 60, true, true, true, true),//Cl<->Sv
    READY                 (PacketElementReady.class, 61, false, true, false, true),//Cl->Sv
    ROTATE                (PacketElementRotate.class, 62, false, true, false, true);//Cl->Sv
    
    
    
    LEFT,RIGHT,FRONT,BACK,JUMP,CROUCH,SHOOT,DROP,SPAWN_ENTITY,DESPAWN_ENTITY,MOVE,ROTATE,SCALE,CONNECT,DISCONNECT,SET_SCORE
    
    public static final char PREFIX_START = '$';
    public static final char PREFIX_END = '=';
    public static final char DATA_SEPARATOR = '_';
    
    
    private Class packetElementClass;
    
    private int id;
    
    //Receive cmd sv side->isOP?->look sender's privileges
    private boolean isOP;
    
    private boolean clRunnable;
    private boolean svRunnable;
    //private boolean applySelf;
    //private boolean applyOthers;
    
    //!cmd->svsentonly
    private boolean cmd;
    private ActionType(Class packetElementClass, int id,boolean clRunnable, boolean svRunnable, boolean isOP, boolean cmd){
        this.packetElementClass = packetElementClass;
        this.id = id;
        this.clRunnable = clRunnable;
        this.svRunnable = svRunnable;
        this.isOP = isOP;
        this.cmd = cmd;
        
    }
    public int getID(){
        return id;
    }
    public boolean isClRunnable(){
        return clRunnable;
    }
    public boolean isSvRunnable(){
        return svRunnable;
    }
    public boolean isOP() {
        return isOP;
    }

    public boolean isCmd() {
        return cmd;
    }
    public static PacketType getPacketTypeFromID(int id){
        for(int i=0;i<PacketType.values().length;i++){
            if(PacketType.values()[i].getID()==id){
                return PacketType.values()[i];
            }
        }
        return null;
    }
    public PacketElement newInstance(String data){
        try {
            PacketElement pe = (PacketElement)packetElementClass.getDeclaredConstructor().newInstance();
            pe.setPacketType(this);
            pe.setData(data);
            return pe;
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(PacketType.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(PacketType.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(PacketType.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PacketType.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(PacketType.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(PacketType.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

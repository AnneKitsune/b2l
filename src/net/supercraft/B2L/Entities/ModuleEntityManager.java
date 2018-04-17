/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities;

import com.jme3.scene.Node;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.CustomModuleManager;
import net.supercraft.B2L.ICustomKeyControllable;
import net.supercraft.B2L.Networking.NetworkedServer;
import net.supercraft.jojoleproUtils.module.control.ModuleObjectContainer;

/**
 *
 * @author jojolepro
 */
public class ModuleEntityManager extends ModuleObjectContainer<Entity> implements ICustomKeyControllable{
    private EntityPlayer mainPlayer;
    private Node entityNode = new Node();
    private Node entityRigidNode = new Node();
    private Node entityStaticNode = new Node();
    private Node entityPlayersNode = new Node();
    
    private int highestID = 0;
    public ModuleEntityManager(CustomModuleManager moduleManager) {
        super(moduleManager);
        
        entityNode.setName("EntityNode");
        entityRigidNode.setName("EntityRigidNode");
        entityStaticNode.setName("EntityStaticNode");
        entityPlayersNode.setName("EntityPlayersNode");
        resetNodeRelations();
        B2L.getGameInstance().getRootNode().attachChild(entityNode);
    }
    /**
     * Add object to scene by default NOT WORKING DO NOT USEEEEEEEEEE
     * LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOK
     * THIIIIIIIIS   IIIISSSSSSSSSS BROOOOOKEEEEEEEEEN!!!!
     * WTFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
     * @param entity 
     */
    @Deprecated
    @Override
    public void addObject(Entity entity){
        this.addObject(entity, true);
    }
    public void addObject(Entity object,boolean addToScene){
        /*for(int i=0;i<list.size();i++){
            if(list.get(i).getID()==object.id){
                System.err.println("Id already registered! Not adding the entity.");
                return;
            }
        }*/
        
	super.addObject(object);
        
        for(int i=0;i<list.size();i++){
            System.out.println("ModuleEntityManager content: "+list.get(i).getID()+"="+list.get(i).getName());
        }
        
        if(!addToScene || object.getNode()==null){
            return;
        }
        if(object instanceof EntityPlayer){//ObjectOriented-> if player extends rigid extends entity, player extend entity... :D (we need to put top layer first)
            entityPlayersNode.attachChild(object.getNode());
        }else if(object instanceof EntityRigid){
            entityRigidNode.attachChild(object.getNode());
        }else if(object instanceof EntityStatic){
            entityStaticNode.attachChild(object.getNode());
        }else{
            entityNode.attachChild(object.getNode());
        }
    }
    public void addObject(Entity object, boolean addToScene, boolean autoID){
        addObject(object, addToScene);
        
        if(autoID){
            highestID++;
            object.id = highestID;
            System.out.println("entity id:"+object.id+" name:"+object.getName());
            ((NetworkedServer)B2L.getGameInstance().getModuleNetwork()).addEntitySendBuffer(object);
        }
    }
    /**
     * Remove object from scene by default
     * @param object 
     */
    @Override
    public void removeObject(Entity object){
        this.removeObject(object, true);
    }
    public void removeObject(Entity object,boolean removeFromScene){
        super.removeObject(object);
        if(!removeFromScene){
            return;
        }
        if(object instanceof EntityPlayer){//ObjectOriented-> if player extends rigid extends entity, player extend entity... :D
            entityPlayersNode.detachChild(object.getNode());
        }else if(object instanceof EntityRigid){
            entityRigidNode.detachChild(object.getNode());
        }else if(object instanceof EntityStatic){
            entityStaticNode.detachChild(object.getNode());
        }else{
            entityNode.detachChild(object.getNode());
        }
        
        if(B2L.getGameInstance().isServer()){
            ((NetworkedServer)B2L.getGameInstance().getModuleNetwork()).removeEntitySendBuffer(object);
        }
    }
    public void updatedKeyState(String name, boolean pressed, float tpf) {
        for(int i=0;i<list.size();i++){
            if(list.get(i) instanceof ICustomKeyControllable){
                ((ICustomKeyControllable)list.get(i)).updatedKeyState(name, pressed, tpf);
            }
        }
    }
    public Entity getEntity(String name){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getName().equals(name)){
                return list.get(i);
            }
        }
        return null;
    }
    public Entity getEntity(Node node){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getNode().equals(node)){
                return list.get(i);
            }
        }
        return null;
    }
    /**
     * Adds the main player entity to the entity list and keep a reference to it
     * You can use .getPlayer() after setting this
     * @param player
     */
    public void setMainPlayerEntity(EntityPlayer player){
        this.mainPlayer = player;
        addObject(this.mainPlayer,true);
    }
    /**
     * This may return null if addPlayerEntity was not used, or if the entity list was cleared
     * @return Player
     */
    public EntityPlayer getMainPlayer(){
        return this.mainPlayer;
    }
    public void clearAll(){
        this.mainPlayer = null;
        list.clear();
        entityNode.detachAllChildren();
        entityRigidNode.detachAllChildren();
        entityStaticNode.detachAllChildren();
        
        resetNodeRelations();
    }
    protected void resetNodeRelations(){
        entityRigidNode.attachChild(entityPlayersNode);
        entityNode.attachChild(entityRigidNode);
        entityNode.attachChild(entityStaticNode);
    }
    /**
     * Do not attach/detach directly!
     * Use addObject and removeObject instead.
     * @return 
     */
    public Node getEntityNode(){
        return entityNode;
    }
    /**
     * Do not attach/detach directly!
     * Use addObject and removeObject instead.
     * @return 
     */
    public Node getEntityRigidNode(){
        return entityRigidNode;
    }
    /**
     * Do not attach/detach directly!
     * Use addObject and removeObject instead.
     * @return 
     */
    public Node getEntityStaticNode(){
        return entityStaticNode;
    }
    /**
     * Do not attach/detach directly!
     * Use addObject and removeObject instead.
     * @return 
     */
    public Node getEntityPlayersNode(){
        return entityPlayersNode;
    }
    
    public Entity getEntity(int id){
        for(int i=0;i<list.size();i++){
            if(list.get(i).id == id){
                return list.get(i);
            }
        }
        return null;
    }
    public int getHighestID(){
        return highestID;
    }
}

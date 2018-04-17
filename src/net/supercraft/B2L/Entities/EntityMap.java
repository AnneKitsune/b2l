/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import net.supercraft.B2L.B2L;

/**
 *
 * @author jojolepro
 */
public class EntityMap extends EntityStatic{
    public String mapFilePath = "DEFAULT_MAP_FILE_PATH";
    private RigidBodyControl rbc;
    public EntityMap(){}
    public EntityMap(String name){
        super(name);
    }
    public EntityMap(String name, String filePath){
        super(name);
        this.mapFilePath = filePath;
        this.loadMap(filePath);
        this.getNode().setName("MapNode");
    }
    public void loadMap(String filePath){
        this.setNode((Node) B2L.getGameInstance().getAssetManager().loadModel(filePath));
        B2L.getGameInstance().getRootNode().attachChild(this.getNode());
        
        Spatial scene = this.getNode();
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) scene);
        rbc = new RigidBodyControl(sceneShape, 0);
        scene.addControl(rbc);
        B2L.getGameInstance().getBulletAppState().getPhysicsSpace().add(rbc);
    }
    public void setMaterial(String material){
        Material mapMat = new Material(B2L.getGameInstance().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        setMaterial(mapMat);
    }
    public void setMaterial(Material material){
        this.getNode().setMaterial(material);
    }
    public void unloadMap(){
        B2L.getGameInstance().getRootNode().detachChild(this.getNode());
        B2L.getGameInstance().getBulletAppState().getPhysicsSpace().remove(rbc);
    }
    public RigidBodyControl getRigidBodyControl(){
        return rbc;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import net.supercraft.B2L.B2L;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;

/**
 * Physics apply
 * @author jojolepro
 */
public class EntityRigid extends Entity implements IUpdatable{
    public RigidBodyControl rigidBodyControl;
    public BoxCollisionShape collisionShape = new BoxCollisionShape(new Vector3f(0.3f,0.3f,0.3f));
    public EntityRigid(){
        
    }
    public EntityRigid(String name){
        super(name);
    }
    public void createPhysicState(){
        BoxCollisionShape collShape = collisionShape;
        rigidBodyControl = new RigidBodyControl( collShape , 6f ); // dynamic
        rigidBodyControl.setFriction(1f);
        rigidBodyControl.setCcdMotionThreshold(0.01f);
        this.getNode().addControl(rigidBodyControl);
        B2L.getGameInstance().getBulletAppState().getPhysicsSpace().add(rigidBodyControl);
    }
    public void removePhysicState(){
        this.getNode().removeControl(rigidBodyControl);
        B2L.getGameInstance().getBulletAppState().getPhysicsSpace().remove(rigidBodyControl);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.objects.PhysicsRigidBody;

/**
 *
 * @author jojolepro
 */
public class CustomCharacterControl extends BetterCharacterControl{
    public CustomCharacterControl(float radius, float height, float mass){
        super(radius, height, mass);
    }
    public PhysicsRigidBody getPhysicsRigidBody(){
        return this.rigidBody;
    }
    public boolean wantToUnduck(){
        return wantToUnDuck;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun.Ranged;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.math.Vector3f;
import net.supercraft.B2L.Entities.Gun.EntityGun;

/**
 *
 * @author jojolepro
 */
public abstract class EntityGunRanged extends EntityGun{
    public EntityGunRanged(){
        super();
        this.collisionShape = new BoxCollisionShape(new Vector3f(0.09f,0.15f,0.5f));
    }
}

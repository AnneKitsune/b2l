/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun.Throwable;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import java.util.ArrayList;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Engine.ModelGenerator;
import net.supercraft.B2L.Entities.Entity;
import net.supercraft.B2L.Entities.EntityPlayer;
import net.supercraft.B2L.Entities.EntityRigid;
import net.supercraft.B2L.Entities.Gun.EntityGun;
import net.supercraft.B2L.Entities.Gun.WeaponMode;
import net.supercraft.B2L.NodeUtils;

/**
 *
 * @author jojolepro
 */
public class EntityGunThrowable extends EntityGun{
    protected AudioNode explosionSound;
    protected boolean willExplode = false;
    protected long delayBeforeExplosion = 10000;
    public EntityGunThrowable(){
        super();
        setVars();
    }
    public EntityGunThrowable(EntityGunThrowable toCopy){
        super();
        setVars();
        this.setPickable(false);
        this.willExplode = true;
        this.setNode(toCopy.getNode().clone(true));
    }
    private void setVars(){
        this.maxSprayAngle = 0;
        this.clipSize = 1;
        this.clipContent = clipSize;
        this.totalBulletCount = 5;
        this.reloadTime = 1;
        this.weaponMode = WeaponMode.SEMI;
        this.autoReload = true;
        this.collisionShape = new BoxCollisionShape(new Vector3f(0.4f,0.4f,0.4f));
        this.penetrationPowerLossPercent = 100f;
    }
    @Override
    public void createProjectile() {
        if (!willExplode) {//Throw grenade
            if (B2L.getGameInstance().getModuleEntityManager().getMainPlayer() == null) {
                return;
            }
            
            if (gunSound != null) {
                gunSound.playInstance();
            }
            
            EntityGunThrowable threw = new EntityGunThrowable(this);
            threw.createPhysicState();
            B2L.getGameInstance().getModuleEntityManager().addObject(threw, true);
            //impulse
            EntityPlayer.applyDropImpulse(threw, B2L.getGameInstance().getModuleEntityManager().getMainPlayer(), 100);

            consumeAmmo();

            if (totalBulletCount <= 0 && clipContent <= 0) {
                B2L.getGameInstance().getModuleEntityManager().getMainPlayer().detachGun(false);
            }
        } else {//After explosion
            ArrayList<Entity> ls = B2L.getGameInstance().getModuleEntityManager().getList();
            for (int i = 0; i < ls.size(); i++) {
                Ray ray = new Ray(this.getNode().getWorldTranslation(), ls.get(i).getNode().getWorldTranslation().subtract(this.getNode().getWorldTranslation()).normalize());
                CollisionResults cr = new CollisionResults();
                B2L.getGameInstance().getRootNode().collideWith(ray, cr);
                
                if (showLines) {
                    Vector3f endSpray = this.getNode().getWorldTranslation().add(ray.getDirection().mult(5000f));

                    Cylinder cylSpray = new Cylinder(4, 8, 0.005f, this.getNode().getWorldTranslation().distance(endSpray));

                    Geometry geomSpray = new Geometry("VisibleTestRay", cylSpray);
                    geomSpray.setLocalTranslation(FastMath.interpolateLinear(0.5f, this.getNode().getWorldTranslation(), endSpray));
                    geomSpray.lookAt(endSpray, Vector3f.UNIT_Y);

                    Material matSpray = new Material(B2L.getGameInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                    matSpray.setColor("Color", ColorRGBA.randomColor());
                    geomSpray.setMaterial(matSpray);

                    B2L.getGameInstance().getRootNode().attachChild(geomSpray);
                }
                
                /*
                
                *
                *
                *
                *
                *
                *
                *TODO change notifyhit so it damage player and push rigid entities
                *
                *
                *
                *
                *
                
                */
                if (REMOVE_DUPLICATE_COLLISIONS) {
                    notifyShotHits(removeDuplicateCollisions(cr));
                } else {
                    notifyShotHits(cr);
                }
            }
        }
    }
    
    //We notify the shot hit for EACH bullet fires (shotgun = multiple notify())
    @Override
    public void notifyShotHits(CollisionResults cr) {
        int penetrationCount = 0;

        //Parse all collisions
        for (int i = 0; i < cr.size(); i++) {
            //Check if the gun could actually hit at this distance
            if (cr.getCollision(i).getDistance() <= maxDistance) {
                //Ignore test cubes and test rays
                if (!cr.getCollision(i).getGeometry().getName().equals("HitResultCube") && !cr.getCollision(i).getGeometry().getName().equals("VisibleTestRay")) {
                    //Ignore the case when a player shoots itselfs
                    penetrationCount++;
                    //Says hit object, and if this object is a player (or child of player node)
                    System.out.println("Hit on #" + penetrationCount + ":" + cr.getCollision(i).getGeometry().getName());
                    if(showLines){
                        Geometry hitCube = ModelGenerator.createCube("HitResultCube", new Vector3f(-0.1f, -0.1f, -0.1f), new Vector3f(0.1f, 0.1f, 0.1f));
                        hitCube.setLocalTranslation(cr.getCollision(i).getContactPoint());
                        B2L.getGameInstance().getRootNode().attachChild(hitCube);
                    }

                    //We just hit a player
                    if (NodeUtils.isNodeChildOf(B2L.getGameInstance().getModuleEntityManager().getEntityPlayersNode(), cr.getCollision(i).getGeometry().getParent())) {
                        Node playerNode = NodeUtils.getDeepChildOfParent(cr.getCollision(i).getGeometry().getParent(), B2L.getGameInstance().getModuleEntityManager().getEntityPlayersNode(), 1);
                        EntityPlayer damagedPlayer = (EntityPlayer) B2L.getGameInstance().getModuleEntityManager().getEntity(playerNode);
                        calculateAndDamagePlayer(damagedPlayer, penetrationCount, cr.getCollision(i).getDistance());
                    }else if(NodeUtils.isNodeChildOf(B2L.getGameInstance().getModuleEntityManager().getEntityRigidNode(), cr.getCollision(i).getGeometry().getParent())){//We hit a rigidentity
                        Node rigidBodyNode = NodeUtils.getDeepChildOfParent(cr.getCollision(i).getGeometry().getParent(), B2L.getGameInstance().getModuleEntityManager().getEntityRigidNode(), 1);
                        EntityRigid entityRigid = (EntityRigid) B2L.getGameInstance().getModuleEntityManager().getEntity(rigidBodyNode);
                        //push entity in the opposite direction from the grenade
                    }
                }
            }
        }
    }
    @Override
    public void update(long tpf){
        super.update(tpf);
        if(willExplode){
            delayBeforeExplosion-=tpf;
            if(delayBeforeExplosion<=0){
                explode();
            }
        }
    }
    public void explode(){
        willExplode = true;
        createProjectile();
        B2L.getGameInstance().getModuleEntityManager().removeObject(this,true);
        this.removePhysicState();
    }
}

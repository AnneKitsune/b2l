/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun;

import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import java.util.Random;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Engine.ModelGenerator;
import net.supercraft.B2L.Entities.EntityPlayer;
import net.supercraft.B2L.Entities.EntityRigid;
import net.supercraft.B2L.NodeUtils;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;

/**
 *
 * @author jojolepro
 */
//to store in packet: clipContent,totalBulletCount
public abstract class EntityGun extends EntityRigid implements IUpdatable, Cloneable {
    public float baseDamage = 20;
    public WeaponMode weaponMode = WeaponMode.SEMI;
    public int burstShootCount = 3;
    public float fireRate = 1;
    public int clipSize = 5;
    public int clipContent = clipSize;
    public int totalBulletCount = 20;
    public int projectileCount = 1;
    public int ammoConsumptionPerShot = 1;

    public float reloadTime = 5;
    public float recoil = 3;//in degree, force camera knockback and comeback afterwards

    public float maxDistance = 200;
    public float effectiveDistance = 50;//After the distance, the bullet will lose its power(linear)
    public float penetrationPowerLossPercent = 25;

    private long timeSinceLastShot;
    private boolean reloading = false;

    private boolean burstFiring = false;
    private int burstShotsLeft = burstShootCount;

    public AudioNode gunSound;
    
    
    public float defaultSprayAngle = 0;//0 = first shot ultra-precise (when adding walk trigger, add a +incrementMaxSprayAngle)
    public float sprayAngleIncrementPerVelocity = 0.8f;//SEE CHANGELOG FOR IDEA, maybe incrementPerVelocity = 0.0xx?
    public float sprayAngleIncrementPerShot = 1;
    public float sprayAngleShotOffsetDecreasePerSecond = 2f;
    public float maxSprayAngle = 5;//in-air -> max
    public float currentSprayAngleShotOffset = 0;//Used to calculate how much the an
    
    public float currentSprayAngle = maxSprayAngle;//CurrentSprayAngle-> bullet can go from straight up to currentSprayAngleValue
    private Random rand = new Random();
    protected boolean showLines = true;
    
    public static boolean REMOVE_DUPLICATE_COLLISIONS = true;
    public boolean crosshairAllowed = true;
    
    public boolean autoReload = false;
    
    public EntityPlayer attachedPlayer;
    
    public GunTier gunTier = GunTier.PRIMITIVE;
    //public float damageVariation = 5f;//Make the damage of the bullet add a random number between -damageVariation and damageVariation MAYBE?
    
    public EntityGun() {
        super();
        this.pickable = true;
        this.dropable = true;
    }

    public EntityGun(String name) {
        super(name);
        this.pickable = true;
        this.dropable = true;
    }

    /**
     * Register in ModuleEntityManager after, so the update loop works
     *
     * @param name
     * @param filePath
     */
    public EntityGun(String name, String filePath) {
        super(name);
        loadGun(filePath);
    }

    protected void loadGun(String filePath) {
        this.setNode((Node) B2L.getGameInstance().getAssetManager().loadModel(filePath));
        this.loadAudio("Sounds/Sniper.wav");
    }

    protected void loadAudio(String filePath) {
        if (gunSound != null) {
            B2L.getGameInstance().getRootNode().detachChild(gunSound);
        }
        gunSound = new AudioNode(B2L.getGameInstance().getAssetManager(), filePath, false);
        gunSound.setPositional(false);
        gunSound.setLooping(false);
        gunSound.setVolume(1.0f);
        B2L.getGameInstance().getRootNode().attachChild(gunSound);
    }

    public void unloadGun() {
        B2L.getGameInstance().getRootNode().detachChild(gunSound);
    }
    @Override
    public void update(long tpf) {
        timeSinceLastShot += tpf;
        if (reloading) {
            if (timeSinceLastShot / 1000f >= reloadTime) {//reloading done
                if (totalBulletCount > 0) {
                    if (totalBulletCount + clipContent < clipSize) {//not enough ammo to fill all the clip
                        clipContent = totalBulletCount + clipContent;
                        totalBulletCount = 0;
                    } else {//we have enough bullets to fill the clip
                        totalBulletCount -= clipSize - clipContent;
                        clipContent = clipSize;
                    }
                }
                reloading = false;
                timeSinceLastShot = 999999999;//We set a big time since last shot so we are able to shoot right after reloading(because reloading use timeSinceLastShot as timer).
            }
        }
        if (burstFiring) {
            if (burstShotsLeft > 0) {
                if (timeSinceLastShot / 1000f >= 1 / fireRate / burstShootCount && clipContent >= ammoConsumptionPerShot) {
                    //animator.Play("Shoot",0);
                    this.createProjectile();
                    burstShotsLeft--;
                    timeSinceLastShot = 0;
                } else if (clipContent <= 0) {
                    burstFiring = false;
                }
            } else {
                burstFiring = false;
            }
        }
        if(currentSprayAngleShotOffset-sprayAngleShotOffsetDecreasePerSecond*(tpf/1000f)>0){
            currentSprayAngleShotOffset-=sprayAngleShotOffsetDecreasePerSecond*(tpf/1000f);
        }else{
            currentSprayAngleShotOffset = 0;
        }
    }

    public void shoot(boolean keptPressed) {
        if (timeSinceLastShot / 1000f >= (1 / fireRate)) {
            if (reloading) {
                return;
            }
            if (clipContent <= 0) {//No bullets to shoot
                //animator.Play("EmptyShoot",0);
                return;
            }
            if (weaponMode == WeaponMode.SEMI) {
                if (keptPressed == false) {
                    //animator.Play("Shoot",0);
                    this.createProjectile();
                }
            } else if (weaponMode == WeaponMode.MANUAL) {
                if (keptPressed == false) {
                    //animator.Play("Shoot",0);
                    this.createProjectile();
                }
            } else if (weaponMode == WeaponMode.BURST) {
                if (keptPressed == false) {
                    burstFiring = true;
                    burstShotsLeft = burstShootCount;

                    //animator.Play("Shoot",0);//Shot first bullet here and the n-1 others in the update loop
                    this.createProjectile();
                }
            } else if (weaponMode == WeaponMode.AUTO) {
                //animator.Play("Shoot",0);
                this.createProjectile();
            }
            timeSinceLastShot = 0;
        }
    }
    public void consumeAmmo(){
        clipContent-=ammoConsumptionPerShot;
        if(clipContent<=0){
            clipContent = 0;
            if(autoReload){
                reload();
            }
        }
    }
    public void reload() {
        if (clipContent < clipSize && totalBulletCount > 0) {//Clip not full and ammo left
            if (reloading == false) {//If we are not reloading, we start to do it
                reloading = true;
                timeSinceLastShot = 0;
                //animator.Play("Reload",0);
                //animator.SetFloat("ReloadMultiplier",(reloadAnimation.length/reloadTime));//We set the animation to be done at the same time that reloadTime is done
            }
        }
    }

    public void createProjectile() {
        if(attachedPlayer==null){
            return;
        }
        if (gunSound != null && !B2L.getGameInstance().isServer()) {
            gunSound.playInstance();
        }
        Random rand = new Random();
        for (int i = 0; i < projectileCount; i++) {
            //Vector3f shotDirectionSpray = B2L.getGameInstance().getCamera().getDirection().add(new Vector3f(generateRandomSprayNumber(),generateRandomSprayNumber(),generateRandomSprayNumber()));
            Vector3f shotDirectionSpray = attachedPlayer.camCopyNode.getWorldRotation().getRotationColumn(2).add(new Vector3f(generateRandomSprayNumber(),generateRandomSprayNumber(),generateRandomSprayNumber()));
            //attachedPlayer.camCopyNode.getWorldRotation().getRotationColumn(2);
            /*Vector3f forward = spatial.getLocalRotation().getRotationColumn(0);
            il faut changer camera par la camCopyNode du joueur
                    */
            Ray ray = new Ray(attachedPlayer.camCopyNode.getWorldTranslation(), shotDirectionSpray);
            CollisionResults cr = new CollisionResults();
            B2L.getGameInstance().getRootNode().collideWith(ray, cr);
            
            if(REMOVE_DUPLICATE_COLLISIONS){
                notifyShotHits(removeDuplicateCollisions(cr));
            }else{
                notifyShotHits(cr);
            }
            
            
            if(showLines){
                Vector3f endSpray = attachedPlayer.camCopyNode.getWorldTranslation().add(shotDirectionSpray.mult(5000));

                Cylinder cylSpray = new Cylinder(4, 8, 0.005f, attachedPlayer.camCopyNode.getWorldTranslation().distance(endSpray));

                Geometry geomSpray = new Geometry("VisibleTestRay", cylSpray);
                geomSpray.setLocalTranslation(FastMath.interpolateLinear(0.5f, attachedPlayer.camCopyNode.getWorldTranslation(), endSpray));
                geomSpray.lookAt(endSpray, Vector3f.UNIT_Y);

                Material matSpray = new Material(B2L.getGameInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                matSpray.setColor("Color", ColorRGBA.randomColor());
                geomSpray.setMaterial(matSpray);

                B2L.getGameInstance().getRootNode().attachChild(geomSpray);
            }
            
            if(currentSprayAngleShotOffset+sprayAngleIncrementPerShot<maxSprayAngle){
                currentSprayAngleShotOffset+=sprayAngleIncrementPerShot;
            }else{
                currentSprayAngleShotOffset = maxSprayAngle;
            }
        }
        
        consumeAmmo();
    }
    protected float generateRandomSprayNumber(){
        return FastMath.DEG_TO_RAD*((rand.nextFloat()*currentSprayAngle)-currentSprayAngle/2f);
    }
    public int getClipContent() {
        return clipContent;
    }

    public int getAmmoLeft() {
        return totalBulletCount;
    }
    /**
     * Resets the precision of the gun
     * Useful when you pickup a gun, because a dropped gun's precision won't be updated by the NOT attached player
     */
    public void resetGunPrecision(){
        currentSprayAngleShotOffset = 0;
        currentSprayAngle = maxSprayAngle;
    }
    /**
     * Need to be called by the player's update method
     * This will update the gun precision based on the velocity, on ground and on the gun's current firing state(just fired->more sprayAngle)
     * @param velocity
     * @param inAir 
     */
    public void updateGunPrecision(float velocity, boolean inAir){
        if(inAir){
            currentSprayAngle = maxSprayAngle;
        }else{
            currentSprayAngle = defaultSprayAngle + sprayAngleIncrementPerVelocity*velocity + currentSprayAngleShotOffset;
        }
        
        if(currentSprayAngle>maxSprayAngle){
            currentSprayAngle = maxSprayAngle;
        }
    }
    //We notify the shot hit for EACH bullet fires (shotgun = multiple notify())
    public void notifyShotHits(CollisionResults cr) {
        int penetrationCount = 0;
        
        //Parse all collisions
        for (int i = 0; i < cr.size(); i++) {
            //Check if the gun could actually hit at this distance
            if (cr.getCollision(i).getDistance() <= maxDistance) {
                //Ignore test cubes and test rays
                if (!cr.getCollision(i).getGeometry().getName().equals("HitResultCube") && !cr.getCollision(i).getGeometry().getName().equals("VisibleTestRay")) {
                    //Ignore the case when a player shoots itselfs
                    if (!NodeUtils.isNodeChildOf(attachedPlayer.getNode(), cr.getCollision(i).getGeometry().getParent())) {
                        penetrationCount++;
                        //Says hit object, and if this object is a player (or child of player node)
                        System.out.println("Hit on #" + penetrationCount + ":" + cr.getCollision(i).getGeometry().getName());
                        if(showLines){
                            Geometry hitCube = ModelGenerator.createCube("HitResultCube", new Vector3f(-0.1f, -0.1f, -0.1f), new Vector3f(0.1f, 0.1f, 0.1f));
                            hitCube.setLocalTranslation(cr.getCollision(i).getContactPoint());
                            B2L.getGameInstance().getRootNode().attachChild(hitCube);
                        }
                        
                        //We just hit a player that is not ourselves
                        if(NodeUtils.isNodeChildOf(B2L.getGameInstance().getModuleEntityManager().getEntityPlayersNode(), cr.getCollision(i).getGeometry().getParent())){
                            Node playerNode = NodeUtils.getDeepChildOfParent(cr.getCollision(i).getGeometry().getParent(), B2L.getGameInstance().getModuleEntityManager().getEntityPlayersNode(), 1);
                            EntityPlayer damagedPlayer = (EntityPlayer)B2L.getGameInstance().getModuleEntityManager().getEntity(playerNode);
                            if(B2L.getGameInstance().isServer()){
                                calculateAndDamagePlayer(damagedPlayer, penetrationCount, cr.getCollision(i).getDistance());
                            }
                        }
                    }
                }
            }
        }
    }
    protected void calculateAndDamagePlayer(EntityPlayer damagedPlayer, int penetrationCount, float distance){
        float damage;
        if(distance<=effectiveDistance){
            damage = baseDamage;
        }else{
            damage = baseDamage - (baseDamage*(distance-effectiveDistance)/(maxDistance-effectiveDistance));
        }
        
        if(penetrationCount>1){
            if(penetrationPowerLossPercent!=100){
                damage = damage*(penetrationCount-1)*(penetrationPowerLossPercent/100f);
            }else{
                damage = 0;
            }
        }
        
        if(damage<0){
            damage = 0;
        }
        damagedPlayer.damage(damage);
        System.out.println("Damaged player health="+damagedPlayer.health+" damage="+damage+" name="+damagedPlayer.getName());
    }
    protected CollisionResults removeDuplicateCollisions(CollisionResults cr){
        CollisionResults results = new CollisionResults();
        
        boolean duplicate = false;
        for(int i=0;i<cr.size();i++){
            for(int j=0;j<results.size();j++){
                if(cr.getCollision(i).getGeometry().equals(results.getCollision(j).getGeometry())){
                    duplicate = true;
                    break;
                }
            }
            if(!duplicate){
                results.addCollision(cr.getCollision(i));
            }
            duplicate = false;
        }
        //System.out.println("cr:"+cr.size());
        //System.out.println("res:"+results.size());
        return results;
    }
    public void setAttachedPlayer(EntityPlayer attached){
        this.attachedPlayer = attached;
    }
}

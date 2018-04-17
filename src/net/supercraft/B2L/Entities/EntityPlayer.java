package net.supercraft.B2L.Entities;

import net.supercraft.B2L.Entities.Gun.Ranged.EntityGunRifle308;
import net.supercraft.B2L.Entities.Gun.EntityGun;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import net.supercraft.B2L.B2L;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import net.supercraft.B2L.Engine.ModelGenerator;
import net.supercraft.B2L.ICustomKeyControllable;
import net.supercraft.B2L.Networking.NetworkedClient;
import net.supercraft.B2L.Networking.NetworkedServer;
import net.supercraft.B2L.Networking.PacketElement;
import net.supercraft.B2L.Networking.PacketElementHealth;
import net.supercraft.B2L.Networking.PacketElementPlayerState;
import net.supercraft.B2L.Networking.PacketElementPosition;
import net.supercraft.B2L.Networking.PacketElementRotate;
import net.supercraft.B2L.Networking.PacketElementRotation;
import net.supercraft.B2L.Networking.PacketType;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;

public class EntityPlayer extends EntityRigid implements ICustomKeyControllable, IUpdatable {

    public static final float PLAYER_HEIGHT = 2f;
    public static final float DEFAULT_CAMERA_HEIGHT = PLAYER_HEIGHT / 1.1f;
    public static final float DUCK_HEIGHT_MULTIPLIER = 0.77f;
    public static final float MINIMUM_FALL_DAMAGE_HEIGHT = 2.5f;

    private PlayerState playerState = PlayerState.STANDING;
    private CustomCharacterControl playerController;
    private Vector3f velocity = new Vector3f();

    public Node playerRoot = new Node();
    public Node playerBody = new Node();
    public Node playerHead = new Node();
    public Node camCopyNode = new Node();
    protected Node verticalLock = new Node();
    private EntityGun currentGun;

    public boolean left = false, right = false, up = false, down = false;
    public boolean handleCamera = false;
    public boolean lockCameraVertical = true;
    protected boolean waitingForUnCrouch = false;
    protected boolean mouseButtonHold = false;
    protected boolean wasFalling = false;
    protected float fallDistance = 0;
    public boolean isControllable = false;

    public boolean applyFriction = true;
    public boolean limitSpeed = true;

    public float normalSpeedCap = 5f;
    public float friction = 1.1f;//0<x<1 acceleration x=1 no friction x>1 real friction (controls both acceleration and deceleration, written description is when no key is pressed)
    public float airControl = 0.3f;//The amount of the movement that we can control when mid-air
    public float acceleration = 1f;//player movement force multiplier (used to make acceleration faster or slower)
    public float currentCameraHeight = DEFAULT_CAMERA_HEIGHT;
    public float fallDamagePerMeter = 30f;

    public float health = 100f;
    public float pickupDistance = 1.0f;

    //AudioNode gunSound;
    public EntityPlayer() {
        initPlayer();
    }

    public EntityPlayer(String name) {
        super(name);
        initPlayer();
    }

    public void initPlayer() {
        playerController = new CustomCharacterControl(0.4f, PLAYER_HEIGHT, 77f);//0.4,2,77
        playerController.setJumpForce(new Vector3f(0f, 300f, 0f));
        playerController.setDuckedFactor(DUCK_HEIGHT_MULTIPLIER);
        playerController.getPhysicsRigidBody().setFriction(0f);
        //Attach a default gun
        
        //this.attachGun(new EntityGunRifle308());
        
        //////////////////////Anims  TODO: anim + UV maps
        /*gun = (Node) B2L.getGameInstance().getAssetManager().loadModel("Models/TestModelBonesUV.j3o");
         Material mat = B2L.getGameInstance().getAssetManager().loadMaterial("Materials/TestModelBonesUV.j3m");
         gun.setMaterial(mat);
         gun.move(0, 0, 2f);
         AnimControl con = gun.getChild("Cylinder").getControl(AnimControl.class);
         AnimChannel cha = con.createChannel();
         cha.setSpeed(1f);*/
        ////////////////////////////

        playerRoot.addControl(playerController);
        playerRoot.setName("PlayerRootNode");
        /*
         *
         *
         *playerRoot manually added in entitymanager
         *
         */
        B2L.getGameInstance().getModuleEntityManager().getEntityPlayersNode().attachChild(playerRoot);

        playerHead.attachChild(ModelGenerator.createCube("head", new Vector3f(-0.1f, -0.1f, -0.1f), new Vector3f(0.1f, 0.1f, 0.1f)));

        playerBody.setLocalTranslation(verticalLock.getWorldTranslation().getX(), verticalLock.getWorldTranslation().getY() - currentCameraHeight, verticalLock.getWorldTranslation().getZ());
        //playerBody = (Node) B2L.getGameInstance().getAssetManager().loadModel("Models/bonhemmeavecarmature.j3o");
        playerBody.attachChild(ModelGenerator.createCube("foot", new Vector3f(-0.1f, -0.1f, -0.1f), new Vector3f(0.1f, 0.1f, 0.1f)));

        verticalLock.attachChild(playerBody);

        camCopyNode.attachChild(verticalLock);
        camCopyNode.attachChild(playerHead);
        this.setNode(camCopyNode);

        this.registerBulletPhysics();

        //this.teleport(new Vector3f(0, 10, 0));
    }

    public void attachGun(EntityGun newGun) {
        detachGun(false);
        currentGun = newGun;
        newGun.setAttachedPlayer(this);
        currentGun.setPosition(Vector3f.ZERO);
        currentGun.setRotation(Quaternion.DIRECTION_Z);
        B2L.getGameInstance().getModuleEntityManager().addObject(currentGun, false);
        playerHead.attachChild(newGun.getNode());

        currentGun.attachedPlayer = this;
    }

    public void detachGun(boolean removeAudio) {
        if (currentGun != null) {
            B2L.getGameInstance().getModuleEntityManager().removeObject(currentGun, false);
            currentGun.unloadGun();
            playerHead.detachChild(currentGun.getNode());
            currentGun.attachedPlayer = null;
            currentGun = null;
        }
    }

    public void changeGun(EntityGun newGun) {
        //Animator
        //wait for animator done
        this.attachGun(newGun);
    }

    private void registerBulletPhysics() {
        B2L.getGameInstance().getBulletAppState().getPhysicsSpace().add(playerController);
        B2L.getGameInstance().getBulletAppState().getPhysicsSpace().addAll(playerRoot);
    }

    public void move(long tpf) {
        Vector3f deltaVelocity = new Vector3f(0, 0, 0);
        
        //lastPos = playerRoot.getLocalTranslation().clone();
        //lastRot = camCopyNode.getWorldRotation().getRotationColumn(2).clone().normalizeLocal();
        
        //Getting camera direction
        Vector3f camDir = camCopyNode.getWorldRotation().getRotationColumn(2).clone().normalizeLocal();
        Vector3f camLeft = camCopyNode.getWorldRotation().getRotationColumn(0).clone().normalizeLocal();

        //Doing the actual movement action
        //walkDirection.set(0, 0, 0);DISABLED FOR VELOCITY
        if (left) {
            deltaVelocity.addLocal(camLeft);
        }
        if (right) {
            deltaVelocity.addLocal(camLeft.negate());
        }
        if (up) {
            deltaVelocity.addLocal(camDir.x, 0, camDir.z);
        }
        if (down) {
            deltaVelocity.addLocal(-camDir.x, 0, -camDir.z);
        }
        deltaVelocity.normalizeLocal();
        deltaVelocity.multLocal(acceleration);

        //We apply the amount that we can actually control(if in mid-air)
        if (!playerController.isOnGround()) {//[0,1]-> biggest = more air control
            deltaVelocity.multLocal(airControl);
        }

        velocity.addLocal(deltaVelocity);

        if (applyFriction) {//Apply slowdown
            if (playerController.isOnGround()) {
                velocity.divideLocal(friction);//speed cap (friction too)
            }
        }

        if (limitSpeed) {//Limit the velocity to the current max speed(which itself depends on the state of the player)
            if (velocity.length() > getCurrentSpeedCap()) {
                velocity.normalizeLocal().multLocal(getCurrentSpeedCap());
            }
        }

        playerController.setWalkDirection(velocity);

        this.updateCameraPosition();

        if (playerState != PlayerState.RUNNING && playerState != PlayerState.CROUCHING) {
            if (isMoving()) {
                if (!playerState.equals(PlayerState.WALKING)) {
                    setPlayerState(PlayerState.WALKING);
                }
            } else {
                if (!playerState.equals(PlayerState.STANDING)) {
                    setPlayerState(PlayerState.STANDING);
                }
            }
        }
    }

    private float getCurrentSpeedCap() {
        return normalSpeedCap * playerState.getSpeedMultiplier();
    }

    public void updateCameraPosition() {
        //playerRoot.lookAt(Vector3f.ZERO, playerController.getViewDirection());
        if (isControllable) {
            B2L.getGameInstance().getCamera().setLocation(new Vector3f(playerRoot.getLocalTranslation().getX(), playerRoot.getLocalTranslation().getY() + currentCameraHeight, playerRoot.getLocalTranslation().getZ()));
            camCopyNode.setLocalRotation(B2L.getGameInstance().getCamera().getRotation());
        }

        camCopyNode.setLocalTranslation(new Vector3f(playerRoot.getLocalTranslation().getX(), playerRoot.getLocalTranslation().getY() + currentCameraHeight, playerRoot.getLocalTranslation().getZ()));

        //Need to ajust the player height if we are crouching
        playerBody.setLocalTranslation(0, -currentCameraHeight, 0);

        //Vertical body loc
        float[] angles = new float[3];
        camCopyNode.getLocalRotation().toAngles(angles);

        verticalLock.setLocalRotation(new Quaternion().fromAngles(-angles[0], 0, 0));
    }

    public void damage(float damage) {
        health -= damage;
        PacketElementHealth phealth = (PacketElementHealth) PacketType.HEALTH.newInstance(String.valueOf(this.health));
        ((NetworkedServer) B2L.getGameInstance().getModuleNetwork()).addPacketElement(id, phealth);
        if (health <= 0) {
            this.kill();
        }

    }

    //TODO
    public void kill() {

    }

    public BetterCharacterControl getController() {
        return playerController;
    }

    public Node getPlayerRootNode() {
        return playerRoot;
    }

    private boolean isMoving() {
        return velocity.length() > 0.00001f;
    }

    @Override
    public void updatedKeyState(String name, boolean pressed, float tpf) {
        if (!B2L.getGameInstance().isServer()) {//Cl
            if (!isControllable) {//Cl not controllable(not us)->don't run
                return;
            }
        }
        //Keys will be sent to sv via NetworkedClient

        if (name.equals("Left")) {
            this.left = pressed;
        } else if (name.equals("Right")) {
            this.right = pressed;
        } else if (name.equals("Up")) {
            this.up = pressed;
        } else if (name.equals("Down")) {
            this.down = pressed;
        } else if (name.equals("Jump")) {
            if (pressed) {
                playerController.jump();
            }
        } else if (name.equals("Shoot")) {
            if (pressed) {
                if (currentGun != null) {
                    currentGun.shoot(false);
                    //((NetworkedClient)B2L.getGameInstance().getModuleNetwork()).addPacketElement(PacketType.KEY_PRESS.newInstance("+Shoot"));
                }
                mouseButtonHold = true;
            } else {
                mouseButtonHold = false;
            }
        } else if (name.equals("Duck")) {
            //this.duck(pressed);
            if (pressed) {
                setPlayerState(PlayerState.CROUCHING);
            } else {
                setPlayerState(PlayerState.STANDING);
            }
        } else if (name.equals("Sprint")) {
            //this.sprint(pressed);
            if (pressed) {
                setPlayerState(PlayerState.RUNNING);
            } else {
                setPlayerState(PlayerState.STANDING);
            }
        } else if (name.equals("Escape")) {
            if (pressed) {
                if (B2L.getGameInstance().getFlyByCamera().isDragToRotate()) {
                    B2L.getGameInstance().getFlyByCamera().setDragToRotate(false);
                } else {
                    B2L.getGameInstance().getFlyByCamera().setDragToRotate(true);
                }
            }
        } else if (name.equals("Select1")) {
            if (pressed) {
                dropGun();
            }
        } else if (name.equals("Reload")) {
            if (pressed) {
                if (currentGun != null) {
                    currentGun.reload();
                }
            }
        }
    }

    @Override
    public void update(long tpf) {
        if (waitingForUnCrouch && !playerController.wantToUnduck() && playerState.equals(PlayerState.CROUCHING)) {//we were waiting to unchrouch, and now we can
            unduck();
        }
        if (currentGun != null) {
            currentGun.updateGunPrecision(this.velocity.length(), !this.playerController.isOnGround());
            if (mouseButtonHold) {
                currentGun.shoot(mouseButtonHold);
            }
        }
        if (!playerController.isOnGround()) {
            wasFalling = true;
            float deltaY = lastPos.y - playerRoot.getLocalTranslation().y;
            if (deltaY > 0) {
                fallDistance += deltaY;
            }
        } else if (wasFalling) {
            wasFalling = false;
            if (fallDistance >= MINIMUM_FALL_DAMAGE_HEIGHT) {
                if (B2L.getGameInstance().isServer()) {
                    this.damage((fallDistance - 3) * fallDamagePerMeter + 10);//10 base damage + x damage/meter
                }
            }
            fallDistance = 0;
        }

        if (lockCameraVertical) {
            lockCameraVertical();
        }

        if (currentGun == null) {
            tryToPickup();
        }

        //tell cl the player pos on sv
        if (B2L.getGameInstance().isServer()) {
            //broadcastData(tpf);
        } else {
            this.sendRotate(tpf);
        }
        super.update(tpf);
        this.move(tpf);//Always move last, because we need last pos to be correct (it only updates at the end of the main loop)
        lastRot = B2L.getGameInstance().getCamera().getRotation().clone();
    }
    //sv only
    /*@Override
    public void broadcastData(long tpf) {
        lastSend += tpf;
        if (lastSend >= 15) {//60tick
            lastSend = 0;
            if (lastPos.equals(playerRoot.getWorldTranslation())) {
                Vector3f pos = playerRoot.getWorldTranslation();
                PacketElementPosition ppos = (PacketElementPosition) PacketType.POSITION.newInstance("");
                ppos.setData(pos);
                ((NetworkedServer) B2L.getGameInstance().getModuleNetwork()).addPacketElement(id, ppos);
            }
            if (!lastRot.equals(camCopyNode.getWorldRotation().getRotationColumn(2).clone().normalizeLocal())) {
                PacketElementRotation prot = (PacketElementRotation) PacketType.ROTATION.newInstance("");
                prot.setData(camCopyNode.getLocalRotation());
                ((NetworkedServer) B2L.getGameInstance().getModuleNetwork()).addPacketElement(id, prot);
            }
        }
    }*/
    //cl sent
    public void sendRotate(long tpf) {
        lastSend += tpf;
        if (lastSend >= 15) {
            lastSend = 0;
            if (!lastRot.equals(B2L.getGameInstance().getCamera().getRotation())) {
                PacketElementRotate pe = (PacketElementRotate) PacketType.ROTATE.newInstance("");
                pe.setData(B2L.getGameInstance().getCamera().getRotation());
                ((NetworkedClient) B2L.getGameInstance().getModuleNetwork()).addPacketElement(pe);
            }
        }
    }

    private void lockCameraVertical() {
        float[] angles = new float[3];
        B2L.getGameInstance().getCamera().getRotation().toAngles(angles);
        if (angles[0] > FastMath.HALF_PI) {
            angles[0] = FastMath.HALF_PI;
            B2L.getGameInstance().getCamera().setRotation(new Quaternion().fromAngles(angles));
        } else if (angles[0] < -FastMath.HALF_PI) {
            angles[0] = -FastMath.HALF_PI;
            B2L.getGameInstance().getCamera().setRotation(new Quaternion().fromAngles(angles));
        }
    }

    public float getHealth() {
        return health;
    }

    public EntityGun getCurrentGun() {
        return currentGun;
    }

    public void setControllable(boolean controllable) {
        isControllable = controllable;
        if (isControllable) {
            handleCamera = true;
        }
    }

    //cl only
    public void dropGun() {
        if (currentGun != null && currentGun.isDropable()) {
            B2L.getGameInstance().getModuleEntityManager().addObject(currentGun, true);

            currentGun.createPhysicState();

            applyDropImpulse(currentGun, this, 60);

            detachGun(false);

            if ((!B2L.getGameInstance().isServer() && this.id == ((NetworkedClient) B2L.getGameInstance().getModuleNetwork()).getID())/* || B2L.getGameInstance().isServer()*/) {
                ((NetworkedClient) B2L.getGameInstance().getModuleNetwork()).addPacketElement(PacketType.GUN_DROP.newInstance(""));
            }
        }
    }

    public void pickupGun(EntityGun pickedUp) {
        pickedUp.removePhysicState();
        B2L.getGameInstance().getModuleEntityManager().removeObject(pickedUp, true);
        attachGun(pickedUp);
    }
    
    //sv only
    public void tryToPickup() {
        if (!B2L.getGameInstance().isServer()) {
            return;
        }
        //Sv only can control pickup

        Node rigidNode = B2L.getGameInstance().getModuleEntityManager().getEntityRigidNode();
        for (int i = 0; i < rigidNode.getQuantity(); i++) {
            Entity e = B2L.getGameInstance().getModuleEntityManager().getEntity((Node) rigidNode.getChild(i));
            if (e != null) {
                if (e.isPickable()) {
                    if (e.getNode().getWorldTranslation().distance(playerRoot.getWorldTranslation()) < pickupDistance) {
                        if (e instanceof EntityGun) {
                            PacketElement pe = PacketType.GUN.newInstance(String.valueOf(e.id));
                            ((NetworkedServer) B2L.getGameInstance().getModuleNetwork()).addPacketElement(id, pe);
                            pickupGun((EntityGun) e);
                            System.out.println("sv current gun is null? "+(currentGun==null));
                        }
                    }
                }
            }
        }
    }

    public static void applyDropImpulse(EntityRigid dropped, EntityPlayer playerDropping, float power) {
        dropped.rigidBodyControl.setPhysicsLocation(playerDropping.playerHead.getWorldTranslation().add(playerDropping.camCopyNode.getWorldRotation().getRotationColumn(2).mult(1f)));
        dropped.rigidBodyControl.setPhysicsRotation(playerDropping.playerHead.getWorldRotation());
        dropped.rigidBodyControl.applyImpulse(dropped.rigidBodyControl.getPhysicsLocation().subtract(playerDropping.playerHead.getWorldTranslation()).normalize().mult(power), Vector3f.ZERO);
    }

    public Node getCamCopyNode() {
        return camCopyNode;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState state) {
        if (state.equals(playerState)) {
            return;
        }
        switch (state) {
            case CROUCHING:
                playerController.setDucked(true);
                playerState = PlayerState.CROUCHING;
                currentCameraHeight = DEFAULT_CAMERA_HEIGHT * playerController.getDuckedFactor();
                break;
            case STANDING:
                if (playerController.isDucked()) {
                    unduck();
                } else {
                    currentCameraHeight = DEFAULT_CAMERA_HEIGHT;
                    playerState = PlayerState.STANDING;
                }
                break;
            case WALKING:
                if (playerController.isDucked()) {
                    unduck();
                } else {
                    playerState = PlayerState.WALKING;
                }
                break;
            case RUNNING:
                if (playerController.isDucked()) {
                    unduck();
                } else {
                    playerState = PlayerState.RUNNING;
                }
                break;
            case AIMING://broke
                break;
            default:
                System.err.println("Wrong player state!");
                break;
        }
        if (B2L.getGameInstance().isServer()) {
            PacketElementPlayerState pstate = (PacketElementPlayerState) PacketType.PLAYER_STATE.newInstance(playerState.toString());
            ((NetworkedServer) B2L.getGameInstance().getModuleNetwork()).addPacketElement(id, pstate);
        }
    }

    private void unduck() {
        playerController.setDucked(false);
        if (playerController.wantToUnduck()) {
            waitingForUnCrouch = true;
        } else {//Successful uncrouch
            waitingForUnCrouch = false;
            setPlayerState(PlayerState.STANDING);
        }
    }
    
    public void setHealth(float health) {
        this.health = health;
    }
    @Override
    public void setRotation(Quaternion quat){
        camCopyNode.setLocalRotation(quat);
    }
    @Override
    public Quaternion getRotation(){
        return camCopyNode.getLocalRotation();
    }
    @Override
    public Vector3f getDirection(){
        return camCopyNode.getWorldRotation().getRotationColumn(2);
    }
    @Override
    public void setPosition(Vector3f pos){
        playerController.warp(pos);
    }
    @Override
    public Vector3f getPosition(){
        return playerRoot.getLocalTranslation();
    }
    @Override
    public void setDirection(Vector3f dir){
        camCopyNode.setLocalRotation(new Quaternion().fromAngles(dir.x, dir.y, dir.z));
    }
}
//make entities sv side with properties
//try to attach the player root node to the camCopyNode, so we don't manually attach it to the scene
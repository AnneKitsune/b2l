package net.supercraft.B2L.Entities;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.Networking.NetworkedServer;
import net.supercraft.B2L.Networking.PacketElementPosition;
import net.supercraft.B2L.Networking.PacketElementRotation;
import net.supercraft.B2L.Networking.PacketType;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;

public class Entity implements IUpdatable {

    //TODO: Networking
    //Maybe client side anti-cheat won't be needed, but server side anti cheat will.

    protected Node model;
    private String name = "ENTITY";
    public boolean pickable = false;
    public boolean dropable = false;
    public int id = -1;
    protected long lastSend = 0;

    protected Vector3f lastPos = new Vector3f(0, -10000000, 0);
    protected Quaternion lastRot = Quaternion.ZERO;

    public Entity() {

    }

    public Entity(String name) {
        this.name = name;
    }

    public Node getNode() {
        return model;
    }

    public void setNode(Node model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPickable(boolean pickable) {
        this.pickable = pickable;
    }

    public boolean isPickable() {
        return pickable;
    }

    public boolean isDropable() {
        return dropable;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    @Override
    public void update(long tpf) {
        if (B2L.getGameInstance().isServer()) {
            broadcastData(tpf);
        }
        setLastData();
    }

    public void setLastData() {
        lastPos = getPosition().clone();
        lastRot = getRotation().clone();
    }

    //sv only

    public void broadcastData(long tpf) {
        if (B2L.getGameInstance().isServer()) {
            lastSend += tpf;
            if (lastSend >= 15) {//60tick
                lastSend = 0;
                if (!lastPos.equals(getPosition())) {
                    Vector3f pos = getPosition();
                    PacketElementPosition ppos = (PacketElementPosition) PacketType.POSITION.newInstance("");
                    ppos.setData(pos);
                    ((NetworkedServer) B2L.getGameInstance().getModuleNetwork()).addPacketElement(id, ppos);

                }
                if (!lastRot.equals(getRotation())) {
                    PacketElementRotation prot = (PacketElementRotation) PacketType.ROTATION.newInstance("");
                    prot.setData(getRotation());
                    ((NetworkedServer) B2L.getGameInstance().getModuleNetwork()).addPacketElement(id, prot);

                }
            }
        }
    }
    
    public void setPosition(Vector3f pos) {
        model.setLocalTranslation(pos);
    }

    public void setRotation(Quaternion quat) {
        model.setLocalRotation(quat);
    }

    public Vector3f getPosition() {
        return model.getLocalTranslation();
    }

    public Quaternion getRotation() {
        return model.getLocalRotation();
    }

    public Vector3f getDirection() {
        return model.getLocalRotation().getRotationColumn(2).normalize();
    }

    public void setDirection(Vector3f dir) {
        model.setLocalRotation(new Quaternion().fromAngles(dir.x, dir.y, dir.z));
    }
}

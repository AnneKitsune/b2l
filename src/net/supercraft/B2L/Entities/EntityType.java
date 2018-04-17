/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.supercraft.B2L.Entities.Gun.Ranged.EntityGunRifle308;
import net.supercraft.B2L.Networking.PacketType;

/**
 *
 * @author jojolepro
 */
public enum EntityType {

    PLAYER(1, EntityPlayer.class),
    GUN_RIFLE308(2, EntityGunRifle308.class),
    MAP_TEST(3, EntityMapTest.class);

    private int typeID;
    private Class entityClass;

    private EntityType(int typeID, Class entityClass) {
        this.typeID = typeID;
        this.entityClass = entityClass;
    }

    public int getTypeID() {
        return typeID;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public Entity newInstance() {
        try {
            Entity e = (Entity) entityClass.getDeclaredConstructor().newInstance();
            return e;
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

    public static EntityType getFromTypeID(int id) {
        for (int i = 0; i < EntityType.values().length; i++) {
            if (EntityType.values()[i].getTypeID() == id) {
                return EntityType.values()[i];
            }
        }
        return null;
    }

    public static EntityType getTypeFromEntity(Entity entity) {
        if (entity != null) {
            for (int i = 0; i < EntityType.values().length; i++) {
                //System.out.println(entity.getClass()+":"+EntityType.values()[i].getEntityClass());
                if (EntityType.values()[i].getEntityClass().equals(entity.getClass())) {
                    return EntityType.values()[i];
                }
            }
        }
        return null;
    }
}

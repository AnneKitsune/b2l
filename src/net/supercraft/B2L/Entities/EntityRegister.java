/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities;

import java.util.HashMap;
import net.supercraft.B2L.B2L;

/**
 *
 * @author jojolepro
 */
public class EntityRegister {
    private final HashMap<Integer,String> regEntity = new HashMap<Integer,String>();
    /*
    server will push which classes should have which entity type id
    */
    public void registerEntity(String entityClasspath){
        if(!regEntity.containsValue(entityClasspath)){
            regEntity.put(regEntity.values().size(), entityClasspath);
        }else{
            B2L.getGameInstance().get
        }
    }
}

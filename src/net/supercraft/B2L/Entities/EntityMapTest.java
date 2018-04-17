/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities;

/**
 *
 * @author jojolepro
 */
public class EntityMapTest extends EntityMap{
    public EntityMapTest(){
        this.loadMap("Models/chateau.j3o");
        this.setMaterial("Common/MatDefs/Light/Lighting.j3md");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun.Throwable;

/**
 *
 * @author jojolepro
 */
public class EntityGunGrenade extends EntityGunThrowable{
    public EntityGunGrenade(){
        super();
        this.loadGun("Models/KnifeTest.j3o");
        this.maxDistance = 6;
        this.effectiveDistance = 2;
    }
}

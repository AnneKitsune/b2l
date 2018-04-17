/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun.Melee;

/**
 *
 * @author jojolepro
 */
public class EntityGunKnife extends EntityGunMelee{
    public EntityGunKnife(){
        super();
        this.setName("EntityGunKnife");
        this.loadGun("Models/KnifeTest.j3o");
        this.getNode().setName("EntityGunKnifeNode");
    }
}

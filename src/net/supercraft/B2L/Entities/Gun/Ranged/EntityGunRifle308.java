/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun.Ranged;

import net.supercraft.B2L.Entities.Gun.WeaponMode;

/**
 *
 * @author jojolepro
 */
public class EntityGunRifle308 extends EntityGunRanged{
    public EntityGunRifle308(){
        super();
        this.setName("Rifle308");
        this.loadGun("Models/Rifle308.j3o");
        this.getNode().setName("Rifle308Node");
        this.loadAudio("Sounds/Sniper.wav");
        this.weaponMode = WeaponMode.SEMI;
        this.clipSize = 5;
        this.clipContent = clipSize;
        this.totalBulletCount = 100;
        this.fireRate = 0.75f;
        
        
        
        this.clipSize = 20;
        this.clipContent = 9;
        this.fireRate = 10f;
        this.weaponMode = WeaponMode.AUTO;
        this.effectiveDistance=10;
        this.maxDistance = 20;
        this.maxSprayAngle = 20;
    }
}

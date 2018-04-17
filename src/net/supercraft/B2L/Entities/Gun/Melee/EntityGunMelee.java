/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Entities.Gun.Melee;

import net.supercraft.B2L.Entities.Gun.EntityGun;
import net.supercraft.B2L.Entities.Gun.WeaponMode;

/**
 *
 * @author jojolepro
 */
public abstract class EntityGunMelee extends EntityGun{
    public EntityGunMelee(){
        this.setName("EntityGunMelee");
        this.weaponMode = WeaponMode.SEMI;
        this.clipSize = 1;
        this.clipContent = clipSize;
        this.totalBulletCount = 1;
        this.fireRate = 1.0f;
        this.ammoConsumptionPerShot = 0;
        this.sprayAngleIncrementPerShot = 0;
        this.sprayAngleIncrementPerVelocity = 0;
        this.maxSprayAngle = 0;
        
        this.effectiveDistance = 1.5f;
        this.maxDistance = 1.5f;
        this.recoil = 0;
        
        this.dropable = true;
    }
}

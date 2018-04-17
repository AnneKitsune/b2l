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
public enum PlayerState {
    STANDING(1,""),
    WALKING(1,""),
    RUNNING(1.5f,""),
    CROUCHING(0.5f,""),
    AIMING(0.5f,"")//Should depends on gun weight, but this should not be calculated here
    ;
    private float speedMultiplier = 1f;
    private String animationName = "";
    private PlayerState(float speedMultiplier, String animationName){//Replace animationName by a Animation enum
        this.speedMultiplier = speedMultiplier;
        this.animationName = animationName;
    }
    public float getSpeedMultiplier(){
        return speedMultiplier;
    }
    public String getAnimation(){
        return animationName;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L.Listener;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import org.lwjgl.input.Keyboard;

public enum EnumDefaultKeyMaps {
    LEFT("Left", new KeyTrigger(KeyInput.KEY_A)),
    RIGHT("Right", new KeyTrigger(KeyInput.KEY_D)),
    UP("Up", new KeyTrigger(KeyInput.KEY_W)),
    DOWN("Down", new KeyTrigger(KeyInput.KEY_S)),
    JUMP("Jump", new KeyTrigger(KeyInput.KEY_SPACE)),
    SPRINT("Sprint", new KeyTrigger(KeyInput.KEY_LSHIFT)),
    DUCK("Duck", new KeyTrigger(KeyInput.KEY_LCONTROL)),
    SHOOT("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT)),
    SELECT1("Select1", new KeyTrigger(Keyboard.KEY_1)),
    SELECT2("Select2", new KeyTrigger(Keyboard.KEY_2)),
    SELECT3("Select3", new KeyTrigger(Keyboard.KEY_3)),
    SELECT4("Select4", new KeyTrigger(Keyboard.KEY_4)),
    SELECT5("Select5", new KeyTrigger(Keyboard.KEY_5)),
    SELECT6("Select6", new KeyTrigger(Keyboard.KEY_6)),
    SELECT7("Select7", new KeyTrigger(Keyboard.KEY_7)),
    SELECT8("Select8", new KeyTrigger(Keyboard.KEY_8)),
    SELECT9("Select9", new KeyTrigger(Keyboard.KEY_9)),
    ESCAPE("Escape", new KeyTrigger(Keyboard.KEY_ESCAPE)),
    RELOAD("Reload", new KeyTrigger(Keyboard.KEY_R))
    ;
    private String name = "DEFAULT_KEY_NAME";
    private Trigger[] triggers;
    private EnumDefaultKeyMaps(String name, Trigger... triggers){
        this.name = name;
        this.triggers = triggers;
    }
    public String getName(){
        return this.name;
    }
    public Trigger[] getTriggers(){
        return triggers;
    }
}

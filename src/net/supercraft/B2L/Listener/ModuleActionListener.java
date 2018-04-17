package net.supercraft.B2L.Listener;


import com.jme3.input.InputManager;
import org.lwjgl.input.Keyboard;

import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import net.supercraft.B2L.B2L;
import net.supercraft.B2L.CustomModuleManager;
import net.supercraft.jojoleproUtils.module.control.Module;

public class ModuleActionListener extends Module implements ActionListener{
    private boolean left,right,up,down;
    public ModuleActionListener(CustomModuleManager mm){
        super(mm);
    }
    
    @Override
    public void onAction(String name, boolean bool, float tpf) {
        ((CustomModuleManager)mm).updateKeyState(name, bool, tpf);
    }
    
    public void initDefaultKeys(){
        this.registerMappings();
        this.registerListener();
    }
    public void initDefaultKeys(boolean clearOldMappings){
        if(clearOldMappings){
            B2L.getGameInstance().getInputManager().clearMappings();
            B2L.getGameInstance().getInputManager().removeListener(this);
        }
        this.initDefaultKeys();
    }
    /**
     * This sets the mapping name and triggers inside the input manager.
     */
    private void registerMappings(){
        InputManager im = B2L.getGameInstance().getInputManager();
        for(int i=0;i<EnumDefaultKeyMaps.values().length;i++){
            im.addMapping(EnumDefaultKeyMaps.values()[i].getName(),EnumDefaultKeyMaps.values()[i].getTriggers());
        }
    }
    /**
     * This register this listener 
     */
    private void registerListener(){
        String[] mapNames = new String[EnumDefaultKeyMaps.values().length];
        for(int i=0;i<EnumDefaultKeyMaps.values().length;i++){
            mapNames[i] = EnumDefaultKeyMaps.values()[i].getName();
        }
        B2L.getGameInstance().getInputManager().addListener(this, mapNames);
    }
}

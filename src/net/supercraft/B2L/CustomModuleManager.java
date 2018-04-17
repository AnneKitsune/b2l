package net.supercraft.B2L;

import net.supercraft.jojoleproUtils.module.control.ModuleManager;

public class CustomModuleManager extends ModuleManager {

    public CustomModuleManager() {
        super();
    }

    public void updateKeyState(String name, boolean pressed, float tpf) {
        for (int i = 0; i < loadedModules.size(); i++) {
            if (loadedModules.get(i) instanceof ICustomKeyControllable && loadedModules.get(i).isEnabled()) {
                ((ICustomKeyControllable) loadedModules.get(i)).updatedKeyState(name, pressed, tpf);
            }
        }
    }
}

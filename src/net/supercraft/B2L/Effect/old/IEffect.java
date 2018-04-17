package net.supercraft.B2L.Effect.old;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleMesh.Type;

public interface IEffect {
    public final boolean POINT_SPRITE = true;
    public final Type EMITTER_TYPE = POINT_SPRITE ? Type.Point : Type.Triangle;
    
    public void createEffect(AssetManager assetManager);
    
	/**
	 * Do all render of the effect in this method.
	 * This will use the tpf to increase the timer to define the state of the effect using this speed variable.
	 * @param pos Define where to render
	 * @param tpf For timing purposes
	 */
	public void render(float tpf);
	public void resetEffect();
	public boolean getEnabled();
	
}

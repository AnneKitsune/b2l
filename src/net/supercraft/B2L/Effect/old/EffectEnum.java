package net.supercraft.B2L.Effect.old;

import com.jme3.asset.AssetManager;

public enum EffectEnum {
	EXPLOSION(new EffectExplosion());
	
	private EffectEnum(IEffect effect){
		
	}
	public void createEffect(AssetManager assetManager){}
	
}

package net.supercraft.B2L.Effect.old;

import java.util.ArrayList;

public class EffectList {
	ArrayList<? extends IEffect> effects = new ArrayList<IEffect>();
	
	public ArrayList getList(){
		return effects;
	}
}

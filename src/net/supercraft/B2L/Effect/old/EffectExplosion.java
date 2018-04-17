package net.supercraft.B2L.Effect.old;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class EffectExplosion implements IEffect{
	ParticleEmitter flame,flash,roundspark,spark,smoketrail,debris,shockwave;
	Node effectExplosionNode = new Node();
	float time = 0;
	float speed = 1f;
	int state = 0;
	boolean enabled = false;
	boolean loop = false;
	
	
	@Override
	public void createEffect(AssetManager assetManager){
		effectExplosionNode = new Node();
		flame = Effect.createFlame(flame, EMITTER_TYPE, 32, 1f, effectExplosionNode, assetManager);
		flash = Effect.createFlash(flash, EMITTER_TYPE,24, 1f, effectExplosionNode, assetManager);
		roundspark = Effect.createRoundSpark(roundspark, EMITTER_TYPE, 20, 1f, effectExplosionNode, assetManager);
		spark = Effect.createSpark(roundspark, EMITTER_TYPE, 30, 1f, effectExplosionNode, assetManager);
		smoketrail = Effect.createSmokeTrail(smoketrail, EMITTER_TYPE, 22, 1f, effectExplosionNode, assetManager);
		debris = Effect.createDebris(debris, EMITTER_TYPE, 15, 1f, effectExplosionNode, assetManager);
		shockwave = Effect.createShockwave(shockwave, EMITTER_TYPE, 1, 0.8f, effectExplosionNode, assetManager);
		effectExplosionNode.attachChild(flame);
		effectExplosionNode.attachChild(flash);
		effectExplosionNode.attachChild(roundspark);
		effectExplosionNode.attachChild(spark);
		effectExplosionNode.attachChild(smoketrail);
		effectExplosionNode.attachChild(debris);
		effectExplosionNode.attachChild(shockwave);
	}
	//rgba(84, 161, 255, 1);
	@Override
	public void render(float tpf) {
		time += tpf / speed;
		//time++;
        if (time > 0f && state == 0){
            flash.emitAllParticles();
            spark.emitAllParticles();
            smoketrail.emitAllParticles();
            debris.emitAllParticles();
            shockwave.emitAllParticles();
            state++;
        }
        if (time > 0f + .05f / speed && state == 1){
            flame.emitAllParticles();
            roundspark.emitAllParticles();
            state++;
        }
        
        // rewind the effect
        if (time > 4 / speed && state == 2){
        	resetEffect();
        	if(!loop){
        		enabled = false;
        	}
        }
	}
	
	@Override
	public void resetEffect() {
		state = 0;
        time = 0;

        flash.killAllParticles();
        spark.killAllParticles();
        smoketrail.killAllParticles();
        debris.killAllParticles();
        flame.killAllParticles();
        roundspark.killAllParticles();
        shockwave.killAllParticles();
        
        
	}
	
	public boolean getEnabled(){
		return enabled;
	}
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	public void setPos(Vector3f pos){
		effectExplosionNode.setLocalTranslation(pos);
	}
	public void setLoopingMode(boolean looping){
		loop = looping;
	}
	public Node getNode(){
		return effectExplosionNode;
	}
	
	
}

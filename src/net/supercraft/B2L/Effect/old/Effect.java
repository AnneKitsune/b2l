package net.supercraft.B2L.Effect.old;

import java.util.ArrayList;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Effect {
	static ArrayList<EffectExplosion> aee = new ArrayList<EffectExplosion>();
	
	public static EffectExplosion getBestEffectExplosion(){
		for(int i=0;i<aee.size();i++){
			if(!aee.get(i).enabled){
				return aee.get(i);
			}
		}
		aee.add(new EffectExplosion());
		return aee.get(aee.size()-1);
	}
	public static void freeEffectExplosion(int index){
		aee.get(index).setEnabled(false);
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param pe The instance of ParticleEmitter
	 * @param emitterType
	 * @param NbParticles number of particles default 32
	 * @param alphaStartColor alpha transparency at the start of the effect default 1f
	 * @param effectNode
	 * @param assetManager
	 * @return pe The instance of ParticleEmitter after modifications
	 */
	public static ParticleEmitter createFlame(ParticleEmitter flame, Type EMITTER_TYflame,int NbParticles, float alphaStartColor,Node effectNode, AssetManager assetManager){
        flame = new ParticleEmitter("flame", EMITTER_TYflame, NbParticles);
        flame.setSelectRandomImage(true);
        flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, alphaStartColor));
        flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
        flame.setStartSize(1.3f);
        flame.setEndSize(2f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0, -5, 0);
        flame.setLowLife(.4f);
        flame.setHighLife(.5f);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 7, 0));
        flame.getParticleInfluencer().setVelocityVariation(1f);
        flame.setImagesX(2);
        flame.setImagesY(2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        //mat.setBoolean("PointSprite", POINT_SPRITE);
        mat.setBoolean("PointSprite", true);
        flame.setMaterial(mat);
        effectNode.attachChild(flame);
        return flame;
    }
	/**
	 * 
	 * 
	 * 
	 * @param pe The instance of ParticleEmitter
	 * @param emitterType
	 * @param NbParticles number of particles default 24
	 * @param alphaStartColor alpha transparency at the start of the effect default 1f
	 * @param effectNode
	 * @param assetManager
	 * @return pe The instance of ParticleEmitter after modifications
	 */
	public static ParticleEmitter createFlash(ParticleEmitter flash, Type EMITTER_TYPE,int NbParticles, float alphaStartColor,Node effectNode, AssetManager assetManager){
        flash = new ParticleEmitter("Flash", EMITTER_TYPE, NbParticles);
        flash.setSelectRandomImage(true);
        flash.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, alphaStartColor));
        flash.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        flash.setStartSize(.1f);
        flash.setEndSize(3.0f);
        flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
        flash.setParticlesPerSec(0);
        flash.setGravity(0, 0, 0);
        flash.setLowLife(.2f);
        flash.setHighLife(.2f);
        flash.setInitialVelocity(new Vector3f(0, 5f, 0));
        flash.setVelocityVariation(1);
        flash.setImagesX(2);
        flash.setImagesY(2);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
        mat.setBoolean("PointSprite", true);
        flash.setMaterial(mat);
        effectNode.attachChild(flash);
        return flash;
    }
    /**
	 * 
	 * 
	 * 
	 * @param pe The instance of ParticleEmitter
	 * @param emitterType
	 * @param NbParticles number of particles default 20
	 * @param alphaStartColor alpha transparency at the start of the effect default 1f
	 * @param effectNode
	 * @param assetManager
	 * @return pe The instance of ParticleEmitter after modifications
	 */
	public static ParticleEmitter createRoundSpark(ParticleEmitter roundspark, Type EMITTER_TYPE,int NbParticles, float alphaStartColor,Node effectNode, AssetManager assetManager){
        roundspark = new ParticleEmitter("RoundSpark", EMITTER_TYPE, NbParticles);
        roundspark.setStartColor(new ColorRGBA(1f, 0.29f, 0.34f, alphaStartColor));
        roundspark.setEndColor(new ColorRGBA(0, 0, 0, 0.5f));
        roundspark.setStartSize(1.2f);
        roundspark.setEndSize(1.8f);
        roundspark.setShape(new EmitterSphereShape(Vector3f.ZERO, 2f));
        roundspark.setParticlesPerSec(0);
        roundspark.setGravity(0, -.5f, 0);
        roundspark.setLowLife(1.8f);
        roundspark.setHighLife(2f);
        roundspark.setInitialVelocity(new Vector3f(0, 3, 0));
        roundspark.setVelocityVariation(.5f);
        roundspark.setImagesX(1);
        roundspark.setImagesY(1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/roundspark.png"));
        mat.setBoolean("PointSprite", true);
        roundspark.setMaterial(mat);
        effectNode.attachChild(roundspark);
        return roundspark;
    }
    /**
	 * 
	 * 
	 * 
	 * @param pe The instance of ParticleEmitter
	 * @param emitterType
	 * @param NbParticles number of particles default 30
	 * @param alphaStartColor alpha transparency at the start of the effect default 1f
	 * @param effectNode
	 * @param assetManager
	 * @return pe The instance of ParticleEmitter after modifications
	 */
	public static ParticleEmitter createSpark(ParticleEmitter spark, Type EMITTER_TYPE,int NbParticles, float alphaStartColor,Node effectNode, AssetManager assetManager){
        spark = new ParticleEmitter("Spark", Type.Triangle, NbParticles);
        spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, alphaStartColor));
        spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        spark.setStartSize(.5f);
        spark.setEndSize(.5f);
        spark.setFacingVelocity(true);
        spark.setParticlesPerSec(0);
        spark.setGravity(0, 5, 0);
        spark.setLowLife(1.1f);
        spark.setHighLife(1.5f);
        spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
        spark.getParticleInfluencer().setVelocityVariation(1);
        spark.setImagesX(1);
        spark.setImagesY(1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
        spark.setMaterial(mat);
        effectNode.attachChild(spark);
        return spark;
    }
    /**
	 * 
	 * 
	 * 
	 * @param pe The instance of ParticleEmitter
	 * @param emitterType
	 * @param NbParticles number of particles default 22
	 * @param alphaStartColor alpha transparency at the start of the effect default 1f
	 * @param effectNode
	 * @param assetManager
	 * @return pe The instance of ParticleEmitter after modifications
	 */
	public static ParticleEmitter createSmokeTrail(ParticleEmitter smoketrail, Type EMITTER_TYPE,int NbParticles, float alphaStartColor,Node effectNode, AssetManager assetManager){
        smoketrail = new ParticleEmitter("SmokeTrail", Type.Triangle, NbParticles);
        smoketrail.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, alphaStartColor));
        smoketrail.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
        smoketrail.setStartSize(.2f);
        smoketrail.setEndSize(1f);

//        smoketrail.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        smoketrail.setFacingVelocity(true);
        smoketrail.setParticlesPerSec(0);
        smoketrail.setGravity(0, 1, 0);
        smoketrail.setLowLife(.4f);
        smoketrail.setHighLife(.5f);
        smoketrail.setInitialVelocity(new Vector3f(0, 12, 0));
        smoketrail.setVelocityVariation(1);
        smoketrail.setImagesX(1);
        smoketrail.setImagesY(3);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
        smoketrail.setMaterial(mat);
        effectNode.attachChild(smoketrail);
        return smoketrail;
    }
    /**
	 * 
	 * 
	 * 
	 * @param pe The instance of ParticleEmitter
	 * @param emitterType
	 * @param NbParticles number of particles default 15
	 * @param alphaStartColor alpha transparency at the start of the effect default 1f
	 * @param effectNode
	 * @param assetManager
	 * @return pe The instance of ParticleEmitter after modifications
	 */
	public static ParticleEmitter createDebris(ParticleEmitter debris, Type EMITTER_TYPE,int NbParticles, float alphaStartColor,Node effectNode, AssetManager assetManager){
        debris = new ParticleEmitter("Debris", Type.Triangle, NbParticles);
        debris.setSelectRandomImage(true);
        debris.setRandomAngle(true);
        debris.setRotateSpeed(FastMath.TWO_PI * 4);
        debris.setStartColor(new ColorRGBA(1f, 0.59f, 0.28f, alphaStartColor));
        debris.setEndColor(new ColorRGBA(.5f, 0.5f, 0.5f, 0f));
        debris.setStartSize(.2f);
        debris.setEndSize(.2f);

//        debris.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
        debris.setParticlesPerSec(0);
        debris.setGravity(0, 12f, 0);
        debris.setLowLife(1.4f);
        debris.setHighLife(1.5f);
        debris.setInitialVelocity(new Vector3f(0, 15, 0));
        debris.setVelocityVariation(.60f);
        debris.setImagesX(3);
        debris.setImagesY(3);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/Debris.png"));
        debris.setMaterial(mat);
        effectNode.attachChild(debris);
        return debris;
    }
    /**
	 * 
	 * 
	 * 
	 * @param pe The instance of ParticleEmitter
	 * @param emitterType
	 * @param NbParticles number of particles default 1
	 * @param alphaStartColor alpha transparency at the start of the effect default 0.8f
	 * @param effectNode
	 * @param assetManager
	 * @return pe The instance of ParticleEmitter after modifications
	 */
	public static ParticleEmitter createShockwave(ParticleEmitter shockwave, Type EMITTER_TYPE,int NbParticles, float alphaStartColor,Node effectNode, AssetManager assetManager){
        shockwave = new ParticleEmitter("Shockwave", Type.Triangle, NbParticles);
//        shockwave.setRandomAngle(true);
        shockwave.setFaceNormal(Vector3f.UNIT_Y);
        shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, alphaStartColor));
        shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

        shockwave.setStartSize(0f);
        shockwave.setEndSize(7f);

        shockwave.setParticlesPerSec(0);
        shockwave.setGravity(0, 0, 0);
        shockwave.setLowLife(0.5f);
        shockwave.setHighLife(0.5f);
        shockwave.setInitialVelocity(new Vector3f(0, 0, 0));
        shockwave.setVelocityVariation(0f);
        shockwave.setImagesX(1);
        shockwave.setImagesY(1);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
        shockwave.setMaterial(mat);
        effectNode.attachChild(shockwave);
        return shockwave;
    }
}

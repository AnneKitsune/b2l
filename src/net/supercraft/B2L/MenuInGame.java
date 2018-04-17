/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.supercraft.B2L;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import java.awt.BorderLayout;
import net.supercraft.B2L.Entities.EntityPlayer;
import net.supercraft.B2L.Networking.NetworkedClient;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;

/**
 *
 * @author jojolepro
 */
public class MenuInGame extends Menu implements IUpdatable{
    protected BitmapFont guiFont;
    protected ColorRGBA guiColor = ColorRGBA.Green;
    
    protected BitmapText healthText;
    protected float healthTextSize = 3;
    
    protected BitmapText ammoText;
    protected float ammoTextSize = 3;
    
    protected Node crosshairNode = new Node();
    protected Node crosshairLeftNode = new Node();
    protected Node crosshairRightNode = new Node();
    protected Node crosshairUpNode = new Node();
    protected Node crosshairDownNode = new Node();
    
    protected float crosshairLenght = 10f;
    protected float crosshairThickness = 1f;
    protected float crosshairGap = 9f;
    protected float crosshairGapAngleMultiplier = 10f;
    private float crosshairRotation = 0f;
    protected Quad crosshairBit;
    protected Geometry crosshairBitGeom;
    protected ColorRGBA crosshairColor = ColorRGBA.Green;
    private boolean showCrosshair = true;
    private boolean showAmmo = true;
    public boolean dynamicCrosshair = true;
    
    public MenuInGame(){
        B2L.getGameInstance().setDisplayFps(true);
        B2L.getGameInstance().setDisplayStatView(true);
        
        guiFont = B2L.getGameInstance().getAssetManager().loadFont("Interface/Fonts/Impact.fnt");
        
        healthText = new BitmapText(guiFont, false);
        healthText.setColor(guiColor);
        B2L.getGameInstance().getGuiNode().attachChild(healthText);
        
        ammoText = new BitmapText(guiFont, false);
        ammoText.setColor(guiColor);
        B2L.getGameInstance().getGuiNode().attachChild(ammoText);
        
        
        crosshairBit = new Quad(crosshairThickness,crosshairLenght);
        crosshairBitGeom = new Geometry("CrosshairBit", crosshairBit);
        crosshairBitGeom.setLocalTranslation(-crosshairThickness/2f, -crosshairLenght/2f, 0);
        Material crosshairMaterial = new Material(B2L.getGameInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        crosshairMaterial.setColor("Color", crosshairColor);
        crosshairBitGeom.setMaterial(crosshairMaterial);
        
        
        crosshairLeftNode.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_Z));
        crosshairRightNode.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Z));
        
        crosshairLeftNode.attachChild(crosshairBitGeom.clone());
        crosshairRightNode.attachChild(crosshairBitGeom.clone());
        crosshairUpNode.attachChild(crosshairBitGeom.clone());
        crosshairDownNode.attachChild(crosshairBitGeom.clone());
        
        crosshairNode.attachChild(crosshairLeftNode);
        crosshairNode.attachChild(crosshairRightNode);
        crosshairNode.attachChild(crosshairUpNode);
        crosshairNode.attachChild(crosshairDownNode);
        
        B2L.getGameInstance().getGuiNode().attachChild(crosshairNode);
        
        
        this.currentPanel.setLayout(new BorderLayout());
        this.currentPanel.add(B2L.getGameInstance().getModuleCustomWindow().getJmeCanvasContext().getCanvas(),BorderLayout.CENTER);
    }

    public void update(long l) {
        setShowCrosshair(showCrosshair);
        EntityPlayer player;
        if ((player = B2L.getGameInstance().getModuleEntityManager().getMainPlayer()) != null) {
            healthText.setText("Health: " + (int) FastMath.ceil(player.getHealth()));
            if (player.getCurrentGun() != null) {
                setShowAmmo(true);
                if(player.getCurrentGun().crosshairAllowed){
                    setShowCrosshair(true);
                }else{
                    setShowCrosshair(false);
                }
                ammoText.setText("Ammo: " + player.getCurrentGun().getClipContent() + "/" + player.getCurrentGun().getAmmoLeft());
                
                crosshairNode.setLocalTranslation(currentPanel.getWidth() / 2f, currentPanel.getHeight() / 2f, 0);
                if(dynamicCrosshair){
                    crosshairLeftNode.setLocalTranslation(-crosshairGap - player.getCurrentGun().currentSprayAngle * crosshairGapAngleMultiplier, 0, 0);
                    crosshairRightNode.setLocalTranslation(crosshairGap + player.getCurrentGun().currentSprayAngle * crosshairGapAngleMultiplier, 0, 0);
                    
                    crosshairUpNode.setLocalTranslation(0, crosshairGap + player.getCurrentGun().currentSprayAngle * crosshairGapAngleMultiplier, 0);
                    crosshairDownNode.setLocalTranslation(0, -crosshairGap - player.getCurrentGun().currentSprayAngle * crosshairGapAngleMultiplier, 0);
                }else{
                    crosshairLeftNode.setLocalTranslation(-crosshairGap, 0, 0);
                    crosshairRightNode.setLocalTranslation(crosshairGap, 0, 0);
                    
                    crosshairUpNode.setLocalTranslation(0, crosshairGap, 0);
                    crosshairDownNode.setLocalTranslation(0, -crosshairGap, 0);
                }
            }else{
                setShowCrosshair(false);
                setShowAmmo(false);
            }
        }
        healthText.setSize(guiFont.getCharSet().getRenderedSize() * healthTextSize);
        healthText.setLocalTranslation(this.currentPanel.getWidth() - healthText.getLineWidth() - 10, healthText.getLineHeight(), 0);

        ammoText.setSize(guiFont.getCharSet().getRenderedSize() * ammoTextSize);
        ammoText.setLocalTranslation((this.currentPanel.getWidth() / 2) - (ammoText.getLineWidth() / 2), ammoText.getLineHeight(), 0);
    }
    @Override
    public void onScreenSizeChange(){
        
    }
    public void setCrosshairRotation(float rot){
        this.crosshairRotation = rot;
        crosshairNode.setLocalRotation(new Quaternion().fromAngles(0, 0, FastMath.DEG_TO_RAD*rot));
    }
    public float getCrosshairRotation(){
        return crosshairRotation;
    }
    public void setShowCrosshair(boolean show){
        if(showCrosshair!=show){
            if(showCrosshair){//true->false
                B2L.getGameInstance().getGuiNode().detachChild(crosshairNode);
            }else{//false->true
                B2L.getGameInstance().getGuiNode().attachChild(crosshairNode);
            }
            showCrosshair = show;
        }
    }
    public void setShowAmmo(boolean show){
        if(showAmmo!=show){
            if(showAmmo){//true->false
                B2L.getGameInstance().getGuiNode().detachChild(ammoText);
            }else{//false->true
                B2L.getGameInstance().getGuiNode().attachChild(ammoText);
            }
            showAmmo = show;
        }
    }
}

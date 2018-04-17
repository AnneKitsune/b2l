package net.supercraft.B2L;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import net.supercraft.jojoleproUtils.math.PointXY;
import net.supercraft.jojoleproUtils.module.model.IUpdatable;
import net.supercraft.jojoleproUtils.module.view.ModuleWindow;

/**
 * 
 * @author jojolepro
 */
public class ModuleCustomWindow extends ModuleWindow implements IUpdatable, ICustomKeyControllable{
    protected Menu currentMenu;
    protected JmeCanvasContext ctx;
    public ModuleCustomWindow(CustomModuleManager moduleManager, String title, PointXY dimension) {
        super(moduleManager, title, dimension);
    }
    public void initWindow(){
        this.createScreenSettings();
        
        
        B2L.getGameInstance().createCanvas(); // create canvas!
        ctx = (JmeCanvasContext) B2L.getGameInstance().getContext();
        ctx.setSystemListener(B2L.getGameInstance());
        ctx.setSettings(B2L.getGameInstance().getSettings());
        Dimension dim = new Dimension(B2L.getGameInstance().getSettings().getWidth(),B2L.getGameInstance().getSettings().getHeight());
        ctx.getCanvas().setPreferredSize(dim);
        
        //Screen auto resize
        frame.addComponentListener(new ComponentAdapter() {
            private int oldWidth = 0;
            private int oldHeight = 0;

            @Override
            public void componentResized(ComponentEvent e) {
                oldWidth = getFrame().getWidth();
                oldHeight = getFrame().getHeight();
                ajustPanel();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if (getFrame().getWidth() != oldWidth || getFrame().getHeight() != oldHeight) {
                    ajustPanel();
                }
                oldWidth = getFrame().getWidth();
                oldHeight = getFrame().getHeight();
            }
        });
        
        
        /*setPanel(new JPanel() {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                ctx.getCanvas().setLocation(0, 0);
                ctx.getCanvas().setSize(this.getSize());
                if(currentMenu.getPanel()!=null){
                    currentMenu.getPanel().setLocation(0, 0);
                    currentMenu.getPanel().setSize(this.getSize());
                }
                if (cleanScreen) {
                    super.paintComponent(g);
                }
                Graphics2D g2d = (Graphics2D) g;
                //drawAllOtherComponents(g2d);
            }
        });*/
        
        //getPanel().add(ctx.getCanvas());//Old manual ctx adding to the main panel, do not use unless debugging
        //getFrame().invalidate();
        //getFrame().validate();
        ajustPanel();
        
        B2L.getGameInstance().startCanvas(false);
    }
    public void ajustPanel(){
        if(currentMenu!=null){
            currentMenu.onScreenSizeChange();
        }
    }
    public void createScreenSettings(){
        B2L.getGameInstance().setShowSettings(false);
        AppSettings cfg = new AppSettings(true);
        cfg.setFrameRate(60); // set to less than or equal screen refresh rate
        cfg.setVSync(true);   // prevents page tearing
        cfg.setFrequency(60); // set to screen refresh rate
        //cfg.setResolution(4, 3);//If we change this to a higher value, the screen won't resize properly
        cfg.setResolution(800, 600);
        cfg.setFullscreen(false);
        cfg.setBitsPerPixel(24);
        cfg.setSamples(16);    // anti-aliasing
        cfg.setTitle(B2L.GAMETITLE); // branding: window name
        /*try {
            // Branding: window icon
            cfg.setIcons(new BufferedImage[]{ImageIO.read(new File("assets/Interface/icon.png"))});
        } catch (IOException ex) {
            Logger.getLogger(B2L.class.getName()).log(Level.SEVERE, "Icon missing.", ex);
        }
        // branding: load splashscreen from assets
        cfg.setSettingsDialogImage("Interface/icon.png");*/
        //app.setShowSettings(false); // or don't display splashscreen
        B2L.getGameInstance().setSettings(cfg);
        B2L.getGameInstance().setPauseOnLostFocus(false);
    }
    public void changeMenu(Menu newMenu){
        this.currentMenu = newMenu;
        setPanel(currentMenu.getPanel());
        this.ajustPanel();
    }
    public void update(long l) {
        if(currentMenu instanceof IUpdatable){
            ((IUpdatable)currentMenu).update(l);
        }
    }
    public Menu getCurrentMenu(){
        return currentMenu;
    }
    public void updatedKeyState(String name, boolean pressed, float tpf) {
        if(currentMenu instanceof ICustomKeyControllable){
            ((ICustomKeyControllable)currentMenu).updatedKeyState(name, pressed, tpf);
        }
    }
    public JmeCanvasContext getJmeCanvasContext(){
        return ctx;
    }
}

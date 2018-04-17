package net.supercraft.B2L;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.supercraft.B2L.Entities.EntityMapTest;
import net.supercraft.B2L.Entities.Gun.Ranged.EntityGunRifle308;
import net.supercraft.B2L.Entities.ModuleEntityManager;
import net.supercraft.B2L.Listener.ModuleActionListener;
import net.supercraft.B2L.Networking.ModuleNetworkUdp;
import net.supercraft.B2L.Networking.NetworkedClient;
import net.supercraft.B2L.Networking.NetworkedServer;
import net.supercraft.jojoleproUtils.math.PointXY;

public class B2L extends SimpleApplication {

    private static String[] args;
    private static B2L gameInstance;
    public static final String GAMETITLE = "B2L 0.0.1A";
    private CustomModuleManager mm;
    private ModuleCustomWindow mw;
    private Logger logger;
    private ModuleActionListener keyListener;
    private ModuleEntityManager em;
    private ModuleNetworkUdp net;
    private BulletAppState bulletAppState;
    private boolean isServer = false;

    public B2L(String args[]) {
        B2L.gameInstance = this;

        mm = new CustomModuleManager();

        boolean started = false;
        for (int i = 0; i < B2L.args.length; i++) {
            if (B2L.args[i].toLowerCase().contains("-server")) {
                started = true;
                isServer = true;
                net = new NetworkedServer(mm);
            }
        }
        if (!started) {
            net = new NetworkedClient(mm);
        }
        
        mm.addModule(net);
        
        Logger.getLogger("").setLevel(Level.WARNING);
        
        
        //CL/SV shared
        em = new ModuleEntityManager(mm);
        mm.addModule(em);
        
        if(!isServer){//CL
            mw = new ModuleCustomWindow(mm, GAMETITLE, new PointXY(800, 600));
            mm.addModule(mw);

            keyListener = new ModuleActionListener(mm);
            mm.addModule(keyListener);
            
            mw.initWindow();
        }else{//SV
            B2L.getGameInstance().start(JmeContext.Type.Headless);
        }
        
        
        /*mw = new ModuleCustomWindow(mm, GAMETITLE, new PointXY(800, 600));

         mm.addModule(mw);

         keyListener = new ModuleActionListener(mm);

         mm.addModule(keyListener);

         em = new ModuleEntityManager(mm);

         mm.addModule(em);

         mw.initWindow();

         this.startCanvas(false);//now in mw.initWindow() */
    }

    public static void main(String args[]) {
        //B2L.gameInstance = new B2L(args);
        B2L.args = args;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new B2L(B2L.args);
            }
        });
    }

    public void simpleInitApp() {
        //CL/SV shared
        this.createBulletPhysics();
        
        if (!isServer) {//CL
            this.initCameraSettings();

            //this.initWorld();

            getModuleActionListener().initDefaultKeys();

            this.createLightAndShadows();

            getModuleCustomWindow().changeMenu(new MenuServerConnect());
        }else{//SV
            //Tmp move init world so sv send map data to cl
            this.initWorld();
        }
        
        
        //TO REMOVE
        
        
        
        //this.initWorld();
        
        
        //TO REMOVE
        
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        mm.updateModules((long) (tpf * 1000));
    }

    @Override
    public void simpleRender(RenderManager rm) {

    }

    public void createBulletPhysics() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
    }

    public void initWorld() {
        //net.getModuleEntityManager().setMainPlayerEntity(new EntityPlayer("MainPlayer"));
        //net.getModuleEntityManager().getPlayer().setControllable(true);

        //EntityPlayer bot = new EntityPlayer("Bot1");
        //getModuleEntityManager().addObject(bot, true);
        
        /*EntityGunKnife test = new EntityGunKnife();
         em.addObject(test,true);
         test.createPhysicState();
         test.rigidBodyControl.setPhysicsLocation(new Vector3f(1,3,0));*/
        /*EntityGunGrenade test = new EntityGunGrenade();
        getModuleEntityManager().addObject(test, true);
        test.createPhysicState();
        test.rigidBodyControl.setPhysicsLocation(new Vector3f(1, 3, 0));*/

        EntityMapTest map = new EntityMapTest();
        //map.setMaterial("Common/MatDefs/Light/Lighting.j3md");
        getModuleEntityManager().addObject(map, true, true);
        
        EntityGunRifle308 g = new EntityGunRifle308();
        g.getNode().setLocalTranslation(0, 1, 5);
        
        g.createPhysicState();
        getModuleEntityManager().addObject(g, true, true);
    }

    public void initCameraSettings() {
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        this.flyCam.setDragToRotate(false);
        getCamera().setFrustumPerspective(45f, (float) getCamera().getWidth() / (float) getCamera().getHeight(), 0.1f, 1000f);
    }

    public void createLightAndShadows() {
        //Create an ambient gray (so we can see object without an actual light, they'll be in gray color)
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));
        rootNode.addLight(ambient);

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-0.1f, -1f, 0f));
        rootNode.addLight(sun);

        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        dlsr.setEdgesThickness(0);
        viewPort.addProcessor(dlsr);

        //float aspect = (float) cam.getWidth() / cam.getHeight();
        //cam.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize, -frustumSize);//Zoom in/out
    }

    @Override
    public void stop() {
        super.stop();
        System.exit(0);
    }

    public static B2L getGameInstance() {
        return gameInstance;
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public CustomModuleManager getModuleManager() {
        return mm;
    }

    public AppSettings getSettings() {
        return settings;
    }
    public boolean isServer() {
        return isServer;
    }
    public ModuleNetworkUdp getModuleNetwork(){
        return net;
    }
    public ModuleCustomWindow getModuleCustomWindow() {
        return mw;
    }

    public ModuleActionListener getModuleActionListener() {
        return keyListener;
    }
    public ModuleEntityManager getModuleEntityManager() {
        return em;
    }
}

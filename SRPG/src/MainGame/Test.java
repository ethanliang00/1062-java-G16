package MainGame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;

import com.jme3.math.Vector3f;


public class Test extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private SceneAppState sceneApp;
    private CharactorAppState playerApp;
    private InteractionAppState inputHandleApp;



    public static void main(String[] args) {
        Test app = new Test();
        app.start();
    }

//    public Test() {
//
//        super(new SceneAppState(), new CharactorAppState());
//    }
    @Override
    public void simpleInitApp() {
        System.out.println("Test simpleInitialize...");
        sceneApp = new SceneAppState();
        playerApp = new CharactorAppState();
        bulletAppState = new BulletAppState();
        inputHandleApp = new InteractionAppState();
        
        stateManager.attachAll(bulletAppState, sceneApp, playerApp, inputHandleApp);

//        stateManager.attach(sceneApp);
        flyCam.setMoveSpeed(100);
//        flyCam.setEnabled(false);
        setupLights();
        setupKeys();
        
//        inputManager.setCursorVisible(true);
        System.out.println("Test simpleInitialize Done...");

    }

 

    public void setupKeys() {

    }

    public void setupLights() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.5F, -0.5F, -0.5F).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {


    }
}

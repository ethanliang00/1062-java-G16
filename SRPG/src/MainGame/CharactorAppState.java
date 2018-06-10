package MainGame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CharactorAppState extends BaseAppState implements AnalogListener, ActionListener {
    
    private Node charactorNode, sceneNode;
    private SimpleApplication app;
    private BetterCharacterControl charactorControl;
    private Geometry marker;
    private static final Logger LOGGER = Logger.getLogger(CharactorAppState.class.getName());
    
    @Override
    protected void initialize(Application app) {
        System.out.println("CharactorAppState Initialize...");
        
        this.app = ((SimpleApplication) app);

        //  charactor init
        charactorNode = new Node("Charator");
        Box boxMesh = new Box(1.0F, 1.0F, 1.0F);
        Geometry boxGeo = new Geometry("Colored Box", boxMesh);
        Material boxMat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        boxMat.setBoolean("UseMaterialColors", true);
        boxMat.setColor("Ambient", ColorRGBA.Green);
        boxMat.setColor("Diffuse", ColorRGBA.Green);
        boxGeo.setMaterial(boxMat);
        
        Spatial charator = boxGeo;
        charator.move(new Vector3f(0.0F, 1.0F, 0.0F));
        charactorNode.attachChild(charator);
        
        charactorNode.setUserData("aspect", true);
        charactorNode.setUserData("TargetDestination", null);
        charactorNode.setUserData("MovementSpeed", 10f);
        
        charactorControl = new BetterCharacterControl(1.414F, 3.0F, 1.0F);
        charactorNode.addControl(charactorControl);
        
        charactorControl.setGravity(new Vector3f(0, -10f, 0));
        
        
        initMark();
        setupKeys();

        //  physics setting
        ((BulletAppState) getStateManager().getState(BulletAppState.class)).getPhysicsSpace().add(charactorControl);
        ((BulletAppState) getStateManager().getState(BulletAppState.class)).getPhysicsSpace().addAll(charactorNode);
        
        sceneNode = getStateManager().getState(SceneAppState.class).getSceneNode();
        app.getInputManager().setCursorVisible(true);
        System.out.println("CharactorAppState Initialize... DONE");
        
    }
    
    @Override
    protected void cleanup(Application app) {
        charactorNode.removeFromParent();
        ((BulletAppState) getStateManager().getState(BulletAppState.class)).getPhysicsSpace().remove(charactorControl);
        ((BulletAppState) getStateManager().getState(BulletAppState.class)).getPhysicsSpace().removeAll(charactorNode);
    }
    
    @Override
    protected void onEnable() {
        app.getRootNode().attachChild(charactorNode);
    }
    
    @Override
    protected void onDisable() {
        app.getRootNode().detachChild(charactorNode);
    }
    
    @Override
    public void update(float tpf) {
        if (charactorNode.getUserData("aspect")) {
            app.getCamera().lookAt(charactorNode.getLocalTranslation(), new Vector3f(0.0F, 1.0F, 0.0F));
        }

        if (charactorNode.getUserData("TargetDestination") != null) {
            LOGGER.log(Level.INFO, "TargetDestination: {0}", charactorNode.getUserData("TargetDestination"));
            Vector3f direction = ((Vector3f) charactorNode.getUserData("TargetDestination")).subtract(charactorNode.getWorldTranslation()).normalizeLocal();
            
            LOGGER.log(Level.INFO, "direction: {0}", direction);
            charactorControl.setWalkDirection(direction.mult((float) charactorNode.getUserData("MovementSpeed")));
        } else {
            charactorControl.setWalkDirection(new Vector3f(0.0F, 0.0F, 0.0F));
        }
//        charactorControl.setWalkDirection(new Vector3f(100f, 0, 0));
    }
    
    @Override
    public void onAnalog(String name, float intensity, float tpf) {
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case "ToggleAspect":
                if (isPressed) {
                    charactorNode.setUserData("aspect", !((Boolean) charactorNode.getUserData("aspect")));
                }
                
                break;
            case "Movement":
                CollisionResults results = new CollisionResults();
                
                Vector2f click2d = app.getInputManager().getCursorPosition();
                Vector3f click3d = app.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = app.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
                
                sceneNode.collideWith(new Ray(click3d, dir), results);
                
                if (results.size() > 0) {
                    Vector3f clickPt = results.getClosestCollision().getContactPoint();
                    charactorNode.setUserData("TargetDestination", clickPt);
                    
                    marker.setLocalTranslation(clickPt);
                    app.getRootNode().attachChild(marker);
                } else {
                    app.getRootNode().detachChild(marker);
                }
                break;
        }
    }
    
    public void initMark() {
        
        Sphere sphereMesh = new Sphere(10, 10, 2f);
        marker = new Geometry("Mark", sphereMesh);
        
        Material Mat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        Mat.setBoolean("UseMaterialColors", true);
        Mat.setColor("Ambient", ColorRGBA.Red);
        Mat.setColor("Diffuse", ColorRGBA.Red);
        marker.setMaterial(Mat);
    }
    
    public void setupKeys() {
        app.getInputManager().addMapping("ToggleAspect", new KeyTrigger(KeyInput.KEY_SPACE));
        app.getInputManager().addMapping("Movement", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        
        app.getInputManager().addListener(this, "ToggleAspect", "Movement");
    }
}

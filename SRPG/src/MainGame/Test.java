package MainGame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

public class Test extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    private SceneAppState sceneApp;
    private Node scene;
    private Geometry marker;

    public static void main(String[] args) {
        Test app = new Test();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        sceneApp = new SceneAppState();

        stateManager.attach(sceneApp);

//        flyCam.setMoveSpeed(100);
//        flyCam.setEnabled(false);
        setupLights();
        setupKeys();
        initMark();
        inputManager.setCursorVisible(true);

    }

    public void initMark() {

        Sphere sphereMesh = new Sphere(10, 10, 2f);
        marker = new Geometry("Mark", sphereMesh);

        Material Mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Mat.setBoolean("UseMaterialColors", true);
        Mat.setColor("Ambient", ColorRGBA.Red);
        Mat.setColor("Diffuse", ColorRGBA.Red);
        marker.setMaterial(Mat);
    }

    public void setupKeys() {
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addListener(this, "Click");

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
        scene = sceneApp.getSceneNode();  // current coding situation
        System.out.println(scene);
        if (name.equals("Click")) {

            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);

            scene.collideWith(ray, results);

            if (results.size() > 0) {
                marker.setLocalTranslation(results.getClosestCollision().getContactPoint());
                rootNode.attachChild(marker);

            }

        } else {
            rootNode.detachChild(marker);
        }
    }
}

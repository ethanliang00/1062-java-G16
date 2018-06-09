package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;










public class HelloCollision
  extends SimpleApplication
  implements ActionListener
{
  private Spatial sceneModel;
  private BulletAppState bulletAppState;
  private RigidBodyControl landscape;
  private BetterCharacterControl player;
  private Vector3f walkDirection = new Vector3f();
  private boolean left = false; private boolean right = false; private boolean up = false; private boolean down = false;
  Geometry box;
  
  public HelloCollision() {}
  
  private Vector3f camDir = new Vector3f();
  private Vector3f camLeft = new Vector3f();
  
  public static void main(String[] args) {
    HelloCollision app = new HelloCollision();
    app.start();
  }
  


  public void simpleInitApp()
  {
    bulletAppState = new BulletAppState();
    stateManager.attach(bulletAppState);
    


    viewPort.setBackgroundColor(new ColorRGBA(0.7F, 0.8F, 1.0F, 1.0F));
    flyCam.setMoveSpeed(100.0F);
    setUpKeys();
    setUpLight();
    

    assetManager.registerLocator("town.zip", ZipLocator.class);
    sceneModel = assetManager.loadModel("main.scene");
    sceneModel.setLocalScale(2.0F);
    



    CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
    landscape = new RigidBodyControl(sceneShape, 0.0F);
    sceneModel.addControl(landscape);
    









    Box boxMesh = new Box(1.0F, 1.0F, 1.0F);
    Geometry boxGeo = new Geometry("Colored Box", boxMesh);
    Material boxMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    boxMat.setBoolean("UseMaterialColors", true);
    boxMat.setColor("Ambient", ColorRGBA.Green);
    boxMat.setColor("Diffuse", ColorRGBA.Green);
    boxGeo.setMaterial(boxMat);
    rootNode.attachChild(boxGeo);
    box = boxGeo;
    box.setLocalTranslation(new Vector3f(0.0F, 10.0F, 0.0F));
    
    player = new BetterCharacterControl(1.0F, 2.0F, 1.0F);
    box.addControl(player);
    player.setJumpForce(new Vector3f(0.0F, 20.0F, 0.0F));
    



    player.setGravity(new Vector3f(0.0F, -30.0F, 0.0F));
    


    player.warp(new Vector3f(0.0F, 10.0F, 0.0F));
    


    rootNode.attachChild(sceneModel);
    bulletAppState.getPhysicsSpace().add(landscape);
    bulletAppState.getPhysicsSpace().add(player);
  }
  
  private void setUpLight()
  {
    AmbientLight al = new AmbientLight();
    al.setColor(ColorRGBA.White.mult(1.3F));
    rootNode.addLight(al);
    
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(2.8F, -2.8F, -2.8F).normalizeLocal());
    rootNode.addLight(dl);
  }
  



  private void setUpKeys()
  {
    inputManager.addMapping("Left", new Trigger[] { new KeyTrigger(30) });
    inputManager.addMapping("Right", new Trigger[] { new KeyTrigger(32) });
    inputManager.addMapping("Up", new Trigger[] { new KeyTrigger(17) });
    inputManager.addMapping("Down", new Trigger[] { new KeyTrigger(31) });
    inputManager.addMapping("Jump", new Trigger[] { new KeyTrigger(57) });
    inputManager.addListener(this, new String[] { "Left" });
    inputManager.addListener(this, new String[] { "Right" });
    inputManager.addListener(this, new String[] { "Up" });
    inputManager.addListener(this, new String[] { "Down" });
    inputManager.addListener(this, new String[] { "Jump" });
  }
  



  public void onAction(String binding, boolean isPressed, float tpf)
  {
    if (binding.equals("Left")) {
      left = isPressed;
    } else if (binding.equals("Right")) {
      right = isPressed;
    } else if (binding.equals("Up")) {
      up = isPressed;
    } else if (binding.equals("Down")) {
      down = isPressed;
    } else if (binding.equals("Jump"))
    {




      player.jump();
    }
  }
  







  public void simpleUpdate(float tpf)
  {
    camDir.set(cam.getDirection()).multLocal(0.6F);
    camLeft.set(cam.getLeft()).multLocal(0.4F);
    walkDirection.set(0.0F, 0.0F, 0.0F);
    if (left) {
      walkDirection.addLocal(camLeft);
    }
    if (right) {
      walkDirection.addLocal(camLeft.negate());
    }
    if (up) {
      walkDirection.addLocal(camDir);
    }
    if (down) {
      walkDirection.addLocal(camDir.negate());
    }
    player.setWalkDirection(walkDirection);
    cam.setLocation(box.getLocalTranslation());
  }
}

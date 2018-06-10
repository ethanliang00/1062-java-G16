package MainGame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;





public class CharactorAppState
  extends BaseAppState
  implements AnalogListener, ActionListener
{
  private Node charactorNode;
  private SimpleApplication app;
  private BetterCharacterControl charactorControl;
  
  public CharactorAppState() {}
  
  protected void initialize(Application app)
  {
    charactorNode = new Node("Charator");
    this.app = ((SimpleApplication)app);
    
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
    

    charactorNode.setUserData("aspect", Boolean.valueOf(true));
    charactorNode.setUserData("TargetDestination", null);
    charactorNode.setUserData("MovementSpeed", null);
    

    charactorControl = new BetterCharacterControl(1.414F, 3.0F, 1.0F);
    charactorNode.addControl(charactorControl);
    


    ((BulletAppState)getStateManager().getState(BulletAppState.class)).getPhysicsSpace().add(charactorControl);
    ((BulletAppState)getStateManager().getState(BulletAppState.class)).getPhysicsSpace().addAll(charactorNode);
    

    app.getInputManager().addMapping("ToggleAspect", new Trigger[] { new KeyTrigger(57) });
    app.getInputManager().addMapping("Movement", new Trigger[] { new MouseButtonTrigger(1) });
  }
  

  protected void cleanup(Application app)
  {
    charactorNode.removeFromParent();
    ((BulletAppState)getStateManager().getState(BulletAppState.class)).getPhysicsSpace().remove(charactorControl);
    ((BulletAppState)getStateManager().getState(BulletAppState.class)).getPhysicsSpace().removeAll(charactorNode);
  }
  
  protected void onEnable()
  {
    app.getRootNode().attachChild(charactorNode);
  }
  
  protected void onDisable()
  {
    app.getRootNode().detachChild(charactorNode);
  }
  






  public void update(float tpf)
  {
    if (((Boolean)charactorNode.getUserData("aspect")).booleanValue()) {
      app.getCamera().lookAt(charactorNode.getLocalTranslation(), new Vector3f(0.0F, 1.0F, 0.0F));
    }
    

    if (charactorNode.getUserData("TargetDestination") != null) {
      Vector3f direction = ((Vector3f)charactorNode.getUserData("TargetDestination")).subtract(charactorNode.getWorldTranslation()).normalize();
      charactorControl.setWalkDirection(direction.mult(tpf * ((Float)charactorNode.getUserData("MovementSpeed")).floatValue()));
    } else {
      charactorControl.setWalkDirection(new Vector3f(0.0F, 0.0F, 0.0F));
    }
  }
  


  public void onAnalog(String name, float intensity, float tpf) {}
  


  public void onAction(String name, boolean isPressed, float tpf)
  {
    switch (name) {
    case "ToggleAspect": 
      if (isPressed) {
        charactorNode.setUserData("aspect", Boolean.valueOf(!((Boolean)charactorNode.getUserData("aspect")).booleanValue()));
      }
      

      break;
    case "Movement": 
      CollisionResults results = new CollisionResults();
      
      Vector2f click2d = app.getInputManager().getCursorPosition();
      Vector3f click3d = app.getCamera().getWorldCoordinates(new Vector2f(x, y), 0.0F).clone();
      Vector3f dir = app.getCamera().getWorldCoordinates(new Vector2f(x, y), 1.0F).subtractLocal(click3d);
      
      ((SceneAppState)getStateManager().getState(SceneAppState.class)).getSceneNode().collideWith(new Ray(click3d, dir), results);
      
      if (results.size() > 0) {
        charactorNode.setUserData("TargetDestination", results.getClosestCollision().getContactPoint());
      }
      break;
    }
  }
}

package MainGame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;





public class SceneAppState
  extends BaseAppState
{
  private Node sceneNode;
  private RigidBodyControl floorControl;
  
  public SceneAppState() {}
  
  protected void initialize(Application app)
  {
    app = (SimpleApplication)app;
    sceneNode = new Node("Scene");
    Spatial landscape = app.getAssetManager().loadModel("Scenes/landscape.j3o");
    sceneNode.attachChild(landscape);
    

    CollisionShape floorColliShape = CollisionShapeFactory.createMeshShape(landscape);
    floorControl = new RigidBodyControl(floorColliShape, 0.0F);
    sceneNode.addControl(floorControl);
    
    floorControl.setPhysicsLocation(new Vector3f(0.0F, -10.0F, 0.0F));
    
    ((BulletAppState)getStateManager().getState(BulletAppState.class)).getPhysicsSpace().add(floorControl);
    ((BulletAppState)getStateManager().getState(BulletAppState.class)).getPhysicsSpace().addAll(sceneNode);
  }
  



  protected void cleanup(Application app)
  {
    sceneNode.removeFromParent();
    ((BulletAppState)getStateManager().getState(BulletAppState.class)).getPhysicsSpace().remove(sceneNode);
  }
  

  protected void onEnable()
  {
    ((SimpleApplication)getApplication()).getRootNode().attachChild(sceneNode);
  }
  
  protected void onDisable()
  {
    ((SimpleApplication)getApplication()).getRootNode().detachChild(sceneNode);
  }
  

  public void update(float tpf) {}
  

  public Node getSceneNode()
  {
    return sceneNode;
  }
}

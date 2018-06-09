package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class Main
  extends SimpleApplication
{
  public Main() {}
  
  public static void main(String[] args)
  {
    Main app = new Main();
    app.start();
  }
  
  public void simpleInitApp()
  {
    Box b = new Box(1.0F, 1.0F, 1.0F);
    Geometry geom = new Geometry("Box", b);
    
    Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat.setColor("Color", ColorRGBA.Blue);
    geom.setMaterial(mat);
    
    Spatial landscape = assetManager.loadModel("Scenes/landscape.j3o");
    rootNode.attachChild(landscape);
    


    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-0.5F, -0.5F, -0.5F).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);
    rootNode.attachChild(geom);
  }
  
  public void simpleUpdate(float tpf) {}
  
  public void simpleRender(RenderManager rm) {}
}

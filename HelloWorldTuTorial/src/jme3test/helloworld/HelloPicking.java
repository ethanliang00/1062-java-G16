package jme3test.helloworld;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
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

/** Sample 8 - how to let the user pick (select) objects in the scene
 * using the mouse or key presses. Can be used for shooting, opening doors, etc. */
public class HelloPicking extends SimpleApplication {

  public static void main(String[] args) {
    HelloPicking app = new HelloPicking();
    app.start();
  }
  private Node shootables, inventory;
  private Geometry mark, lastGeo;
  private ColorRGBA lastColor;
  private Material hitMat, lastMat;

  @Override
  public void simpleInitApp() {
    initCrossHairs(); // a "+" in the middle of the screen to help aiming
    initKeys();       // load custom key mappings
    initMark();       // a red sphere to mark the hit
    viewPort.setBackgroundColor(ColorRGBA.White);
    /** create four colored boxes and a floor to shoot at: */
    shootables = new Node("Shootables");
    rootNode.attachChild(shootables);
    shootables.attachChild(makeOto("a Dragon", -2f, 0f, 1f));
    shootables.attachChild(makeOto("a tin can", 1f, -2f, 0f));
    shootables.attachChild(makeOto("the Sheriff", 0f, 1f, -2f));
    shootables.attachChild(makeOto("the Deputy", 1f, 0f, -4f));
    shootables.attachChild(makeFloor());
    shootables.attachChild(makeCharacter());
        /** A white, directional light source */ 
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun); 
    guiNode.addLight(sun);
    
    inventory = new Node("Inventory");
    guiNode.attachChild(inventory);
    guiNode.setLocalScale(100);
    
    
//     flyCam.setEnabled(false);
    
    inputManager.setCursorVisible(true);
    
    hitMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    hitMat.setColor("Color", ColorRGBA.Green);
  }

  /** Declaring the "Shoot" action and mapping to its triggers. */
  private void initKeys() {
    inputManager.addMapping("Shoot",
      new KeyTrigger(KeyInput.KEY_SPACE), // trigger 1: spacebar
      new MouseButtonTrigger(MouseInput.BUTTON_LEFT)); // trigger 2: left-button click
    inputManager.addListener(actionListener, "Shoot");
  }
  /** Defining the "Shoot" action: Determine what was hit and how to respond. */
  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("Shoot") && !keyPressed) {
        // 1. Reset results list.
        CollisionResults results = new CollisionResults();
//        Vector2f click2d = inputManager.getCursorPosition().clone();
//        
//        Vector3f click3d = cam.getWorldCoordinates(
//        click2d, 0f).clone();
//        
//        Vector3f dir = cam.getWorldCoordinates(
//        click2d, 1f).subtractLocal(click3d).normalizeLocal();
        
//        Ray ray = new Ray(click3d, dir);
        // 2. Aim the ray from cam loc to cam direction.
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        // DO NOT check collision with the root node, or else ALL collisions will hit the
        // skybox! Always make a separate node for objects you want to collide with.
//        shootables.collideWith(ray, results);
        inventory.collideWith(ray, results);

//        // 4. Print the results
//        System.out.println("----- Collisions? " + results.size() + "-----");
//        System.out.println(results);
//        System.out.println("click2d: " + click2d);
//        System.out.println("click3d: " + click3d);
//        System.out.println("dir: " + dir);
//        System.out.println("cam Location: " + cam.getLocation());
//        System.out.println("cam Direction: " + cam.getDirection());
//        System.out.println("cam Height: " + cam.getHeight() + "Width: " +  cam.getWidth());
//        for (int i = 0; i < results.size(); i++) {
//          // For each hit, we know distance, impact point, name of geometry.
//          float dist = results.getCollision(i).getDistance();
//          Vector3f pt = results.getCollision(i).getContactPoint();
//          String hit = results.getCollision(i).getGeometry().getName();
//          System.out.println("* Collision #" + i);
//          System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
//        }
        // 5. Use the results (we mark the hit object)
        if (results.size() > 0) {
          // The closest collision point is what was truly hit:
          CollisionResult closest = results.getClosestCollision();
          Geometry g = closest.getGeometry();
          inventory.detachChild(g);
          shootables.attachChild(g);
          
//          System.out.println(g);
//          System.out.println(g.getMaterial());
//          System.out.println(g.getMaterial().getParam("Color").getValue());
//          if(lastGeo != null && lastColor != null && lastGeo.getMaterial().getParam("Color") != null) {
//              lastGeo.getMaterial().setColor("Color", lastColor);
//          }
//          lastGeo = g;
//          if(g.getMaterial().getParam("Color")  != null) {
//              lastColor = (ColorRGBA)(g.getMaterial().getParam("Color").getValue());
//              g.getMaterial().setColor("Color", ColorRGBA.Green);
//          }
//          if(lastMat != null && lastGeo != null) {
//                lastGeo.setMaterial(lastMat);
//          }
//          lastGeo = g;
//          lastMat = g.getMaterial();
//          g.setMaterial(hitMat);
          
          // Let's interact - we mark the hit with a red dot.
          mark.setLocalTranslation(closest.getContactPoint());
          rootNode.attachChild(mark);
        } else {
            shootables.collideWith(ray, results);
            if(results.size() > 0) {
                Geometry g = results.getClosestCollision().getGeometry();
                shootables.detachChild(g);
                inventory.attachChild(g);
            }else {
            
          // No hits? Then remove the red mark.
                rootNode.detachChild(mark);
            }
      }
    }
  }
  };
 

  /** A cube object for target practice */
  protected Geometry makeCube(String name, float x, float y, float z) {
    Box box = new Box(1, 1, 1);
    Geometry cube = new Geometry(name, box);
    cube.setLocalTranslation(x, y, z);
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.randomColor());
    cube.setMaterial(mat1);
    return cube;
  }
  
  protected Spatial makeOto(String name, float x, float y, float z) {
      Spatial golem = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
      golem.setLocalTranslation(x, y, z);
      golem.setName(name);
      
      return golem;
  }

  /** A floor to show that the "shot" can go through several objects. */
  protected Geometry makeFloor() {
    Box box = new Box(15, .2f, 15);
    Geometry floor = new Geometry("the Floor", box);
    floor.setLocalTranslation(0, -4, -5);
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Gray);
    floor.setMaterial(mat1);
    return floor;
  }

  /** A red ball that marks the last spot that was "hit" by the "shot". */
  protected void initMark() {
    Sphere sphere = new Sphere(30, 30, 0.2f);
    mark = new Geometry("BOOM!", sphere);
    Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mark_mat.setColor("Color", ColorRGBA.Red);
    mark.setMaterial(mark_mat);
  }

  /** A centred plus sign to help the player aim. */
  protected void initCrossHairs() {
    setDisplayStatView(false);
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
      settings.getWidth() / 2 - ch.getLineWidth()/2,
      settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
    guiNode.attachChild(ch);
  }

  protected Spatial makeCharacter() {
    // load a character from jme3test-test-data
    Spatial golem = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
    golem.scale(0.5f);
    golem.setLocalTranslation(-1.0f, -1.5f, -0.6f);

    // We must add a light to make the model visible
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
    golem.addLight(sun);
    return golem;
  }
}
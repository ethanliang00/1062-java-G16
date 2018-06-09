package MainGame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapText;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;




public class Test
  extends SimpleApplication
{
  private BulletAppState bulletAppState;
  
  public Test() {}
  
  public static void main(String[] args)
  {
    Test app = new Test();
    app.start();
  }
  




















  public void simpleInitApp()
  {
    initCrossHairs();
  }
  
  protected void initCrossHairs() {
    setDisplayStatView(false);
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    
    ch.setText(System.getProperty("java.library.path"));
    ch.setLocalTranslation(settings
      .getWidth() / 2 - ch.getLineWidth() / 2.0F, settings
      .getHeight() / 2 + ch.getLineHeight() / 2.0F, 0.0F);
    guiNode.attachChild(ch);
  }
  


  public void setupCam() {}
  


  public void setupLights()
  {
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection(new Vector3f(-0.5F, -0.5F, -0.5F).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);
  }
}

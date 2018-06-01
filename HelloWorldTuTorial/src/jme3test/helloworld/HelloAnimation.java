package jme3test.helloworld;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.debug.SkeletonDebugger;
import com.jme3.material.Material;

/** Sample 7 - how to load an OgreXML model and play an animation,
 * using channels, a controller, and an AnimEventListener. */
public class HelloAnimation extends SimpleApplication
  implements AnimEventListener {
  private AnimChannel channel;
  private AnimChannel upperBodyChannel ;
  
  private AnimControl control, control2;
  Node player, player2;
  public static void main(String[] args) {
    HelloAnimation app = new HelloAnimation();
    app.start();
  }

  @Override
  public void simpleInitApp() {
    viewPort.setBackgroundColor(ColorRGBA.LightGray);
    initKeys();
    DirectionalLight dl = new DirectionalLight();
    dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
    rootNode.addLight(dl);
    player = (Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
//  Dodge
//  Walk
//  stand
//  pull
//  push
    
//    player2 = (Node)assetManager.loadModel("Models/IronMan/IronMan.obj");
    player.setLocalScale(0.5f);
    
//    player2.setLocalScale(0.1f);
    rootNode.attachChild(player);
//    rootNode.attachChild(player2);
    control = player.getControl(AnimControl.class);
//    control2 = player2.getControl(AnimControl.class);
    System.out.println(control2);
    control.addListener(this);
//    control2.addListener(this);
    
    channel = control.createChannel();
    upperBodyChannel = control.createChannel();
    upperBodyChannel.setAnim("stand");
    channel.setAnim("stand");
//    for (String anim : control2.getAnimationNames()) {
////        channel2.setAnim(anim, 0.5f);
//        System.out.println(anim);
//    }
    SkeletonDebugger skeletonDebug =
         new SkeletonDebugger("skeleton", control.getSkeleton());
     Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
     mat.setColor("Color", ColorRGBA.Green);
     mat.getAdditionalRenderState().setDepthTest(false);
     skeletonDebug.setMaterial(mat);
     player.attachChild(skeletonDebug);
  }

  public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
//    if (animName.equals("Walk")) {
//      channel.setAnim("stand", 0.50f);
//      channel.setLoopMode(LoopMode.DontLoop);
//      channel.setSpeed(1f);
//    }
    if (animName.equals("Dodge") || animName.equals("pull") || animName.equals("push")) {
//      upperBodyChannel.setAnim("stand", 0.50f);
//      upperBodyChannel.setLoopMode(LoopMode.DontLoop);
//      upperBodyChannel.setSpeed(1f);
    }
  }

  public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    // unused
  }

  /** Custom Keybinding: Map named actions to inputs. */
  private void initKeys() {
    inputManager.addMapping("Walk", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("Dodge", new KeyTrigger(KeyInput.KEY_F));
    inputManager.addMapping("pull", new KeyTrigger(KeyInput.KEY_I));
    inputManager.addMapping("push", new KeyTrigger(KeyInput.KEY_O));

    inputManager.addListener(actionListener, "Walk", "Dodge", "pull", "push");
  }
  
  private ActionListener actionListener = new ActionListener() {
    public void onAction(String name, boolean keyPressed, float tpf) {
      if (name.equals("Walk") ) {
          if(keyPressed) {
            if (!channel.getAnimationName().equals("Walk")) {
                channel.setAnim("Walk", 0.50f);
                channel.setLoopMode(LoopMode.Loop);
            }
          }else {
              channel.setAnim("stand", 0.50f);
              channel.setLoopMode(LoopMode.DontLoop);
              channel.setSpeed(1f);
          }
        
      }else if (name.equals("Dodge") && keyPressed) {
//        if (!channel.getAnimationName().equals("Dodge")) {
          upperBodyChannel.setAnim("Dodge");
          upperBodyChannel.setLoopMode(LoopMode.Loop);
//        }
       }else if (name.equals("pull") && keyPressed) {
//        if (!channel.getAnimationName().equals("Dodge")) {
          upperBodyChannel.setAnim("pull");
          upperBodyChannel.setLoopMode(LoopMode.Loop);
//        }
      }else if (name.equals("push") && keyPressed) {
//        if (!channel.getAnimationName().equals("Dodge")) {
          upperBodyChannel.setAnim("push");
          upperBodyChannel.setLoopMode(LoopMode.Loop);
//        }
      }
      
    }
  };
}

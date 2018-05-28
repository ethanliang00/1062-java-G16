package jme3test.helloworld;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/** Sample 4 - how to trigger repeating actions from the main event loop.
 * In this example, you use the loop to make the player character
 * rotate continuously. */
public class HelloLoop extends SimpleApplication {

    public static void main(String[] args){
        HelloLoop app = new HelloLoop();
        app.start();
    }

    protected Geometry player, player2;
    private float d = 1.1f, timeVar = 0;

    @Override
    public void simpleInitApp() {
        /** this blue box is our player character */
        Box b = new Box(1, 1, 1);
        player = new Geometry("blue cube", b);
        player2 = new Geometry("red cube", b);
        Material mat = new Material(assetManager,
          "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        player.setMaterial(mat);

        mat = mat.clone();
        mat.setColor("Color", ColorRGBA.Red);
        player2.setMaterial(mat);
        player2.setLocalTranslation(2f, 0, 0);
        rootNode.attachChild(player);
        rootNode.attachChild(player2);
        
    }

    /* Use the main event loop to trigger repeating actions. */
    @Override
    public void simpleUpdate(float tpf) {
        // make the player rotate:
        Vector3f sc = player.getLocalScale();
        player.rotate(0, -2*tpf, 0);
        if(sc.getX()>1.2f)
            d = 1 - 3*tpf;
        else if(sc.getX()<0.8f)
            d = 1 + 3*tpf;
        
        player.scale(d);
        timeVar += tpf;
        if (timeVar > 1) {
            player.getMaterial().setColor("Color", ColorRGBA.randomColor());
            timeVar= 0;
        }
        float timeInSec = timer.getTimeInSeconds();
        float initScale = 1;
        float amplitude = 0.5f;
        float angularFrequency = 1;
        float scale = initScale + amplitude * FastMath.sin(timeInSec * angularFrequency);
        player2.setLocalScale(scale);
        player2.rotate(0, -4*tpf, 0);
    }
}
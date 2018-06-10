package MainGame;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext.Type;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;





public class ServerMain
  extends SimpleApplication
{
  private Server server;
  
  public ServerMain() {}
  
  public static void main(String[] args)
  {
    UtNetworking.InitSerializer();
    ServerMain app = new ServerMain();
    app.start(JmeContext.Type.Headless);
  }
  
  public void simpleInitApp()
  {
    try
    {
      server = Network.createServer(3000);
      server.start();
    } catch (IOException ex) {
      Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "Server open port faill", ex);
    }
  }
  
  public void simpleUpdate(float tpf)
  {
    server.broadcast(new UtNetworking.TextMessage("Fuck You" + tpf));
    Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, "tpf: " + tpf, Float.valueOf(tpf));
  }
  
  public void destroy()
  {
    server.close();
    super.destroy();
  }
}

package MainGame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;






public class ClientMain
  extends SimpleApplication
{
  private Client client;
  private ConcurrentLinkedQueue<String> messageQueue;
  
  public ClientMain() {}
  
  public static void main(String[] args)
  {
    UtNetworking.InitSerializer();
    ClientMain app = new ClientMain();
    app.start();
  }
  
  public void simpleInitApp()
  {
    try
    {
      client = Network.connectToServer("localhost", 3000);
      client.start();
    } catch (IOException ex) {
      Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, "can not connect to server", ex);
    }
    
    messageQueue = new ConcurrentLinkedQueue();
    client.addMessageListener(new TextMessageListener(null));
  }
  

  public void simpleUpdate(float tpf)
  {
    String message = (String)messageQueue.poll();
    
    if (message != null) {
      fpsText.setText(message);
    } else {
      fpsText.setText("No Message!!!");
    }
  }
  
  private class TextMessageListener implements MessageListener<Client> {
    private TextMessageListener() {}
    
    public void messageReceived(Client source, Message m) {
      if ((m instanceof UtNetworking.TextMessage)) {
        UtNetworking.TextMessage message = (UtNetworking.TextMessage)m;
        
        messageQueue.add(message.getMessage());
      }
    }
  }
}

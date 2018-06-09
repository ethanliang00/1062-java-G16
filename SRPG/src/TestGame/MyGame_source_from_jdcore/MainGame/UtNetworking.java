package MainGame;

import com.jme3.network.serializing.Serializer;

public class UtNetworking {
  public static final int PORT = 3000;
  
  public UtNetworking() {}
  
  public static void InitSerializer() {
    Serializer.registerClass(TextMessage.class);
  }
  
  @com.jme3.network.serializing.Serializable
  public static class TextMessage extends com.jme3.network.AbstractMessage
  {
    private String message;
    
    public TextMessage() {}
    
    public TextMessage(String message) {
      this.message = message;
    }
    
    public String getMessage() {
      return message;
    }
  }
}

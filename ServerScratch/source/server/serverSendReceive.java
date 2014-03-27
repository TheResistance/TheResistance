enum serverSendReceive{
    NORESPONSE,REQUEST, SET, RESPONSE
}

class serverSend implements java.io.Serializable  {
    public serverSendReceive tag; 
    public int leader; 
    public int missionLength; 
    public GameState gamestate; 
    public String message; 
    public boolean gameover; 
    public boolean spy; 
    public int id; 
}
class serverReceive implements java.io.Serializable {
    public serverSendReceive tag; 
    public int id; 
    public SetState s; 
    public int [] mission; 
    public String message;
}
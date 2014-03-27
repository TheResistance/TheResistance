enum serverSendReceive{
    REQUEST, SET, RESPONSE, NORESPONSE
}
class newPlayer implements java.io.Serializable {  
       public int id; 
       public boolean spy; 
}  
class serverSend implements java.io.Serializable  {
    public serverSendReceive tag; 
    public int leader; 
    public int missionLength; 
    public GameState gamestate; 
    public String message; 
    public boolean gameover; 
}
class serverReceive implements java.io.Serializable {
    public serverSendReceive tag; 
    public int id; 
    public SetState s; 
    public int [] mission; 
}
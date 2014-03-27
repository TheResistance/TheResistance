import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.net.ServerSocket;
import java.net.Socket;
 
/**
 * This class implements java Socket server
 * @author pankaj
 *
 */

public class Server extends Thread {
     
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9907;
    Game g;
    Player[] players; 
    int player_num = 0; 
    public Server (int players) throws IOException {
        server = new ServerSocket(port);
        g = new Game(players); 
        //players = new Player[g.getPlayers().length];
    }
    //public static void main(String args[]) throws IOException, ClassNotFoundException{
        //create the socket server object
        
        //keep listens indefinitely until receives 'exit' call or program terminates
   public static void main(String [] args)
   {
      try
      {
         System.out.println("Starting server"); 
         Thread t = new Server(5);
         t.start();
      }catch(IOException e)
      {
         e.printStackTrace();
      }
   }
    public void run(){   
         int player = -1;  
         boolean gameover = false; 
         Socket socket;
         while (!gameover) {
             try {
             System.out.println("Waiting for client on port " + server.getLocalPort() + "...");
             socket = server.accept();
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             serverReceive req = (serverReceive) ois.readObject();
             System.out.println(req.message); 
             serverSend send = new serverSend(); 
             if (player_num < g.getPlayers().length ) {
                 if (req.message.equals("connect")) {
                     System.out.println("Creating player: " + player_num);  
                     player = player_num; 
                     send.id = player_num; 
                     send.spy = g.amISpy(player_num);
                     player_num++; 
                 }
             } else {
                 g.startGame();
             }
             send.leader = g.getLeader();
             send.message = "EMPTY"; 
             send.gameover = g.gameOver(); 
             gameover = send.gameover; 
             send.gamestate = g.getGameState(req.id); 
             send.missionLength = g.getMissionLength(); 
             if (req.tag == serverSendReceive.SET) {
                    System.out.println(req.tag + " " + req.id + " " + req.s + " " + req.mission); 
                    g.setGameState(req.id , req.s, req.mission);
             }
            oos.writeObject(send);
            ois.close();
            oos.close();
            socket.close();
            
            } catch(IOException e)
            {
                e.printStackTrace(); 
                break;
            }catch(ClassNotFoundException c)
            {
                c.printStackTrace();
                break;
            } 
           }
           try {server.close();}catch(IOException e){}
            
        }
}
    

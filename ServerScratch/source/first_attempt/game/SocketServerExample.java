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

public class SocketServerExample {
     
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9875;
     
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        Game g = new Game(5); 
        int player_num = 0; 
        Player[] players = new Player[g.getPlayers().length];
        while(player_num < players.length){
            System.out.println("Waiting for player connections");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String message = (String) ois.readObject();
            System.out.println("Connected " + (player_num+1) + " / " + players.length);
            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
             if(message.equalsIgnoreCase("connect")) 
             {
                newPlayer newplayer = new newPlayer();
                newplayer.id = player_num; 
                newplayer.spy = g.amISpy(player_num);
                oos.writeObject(newplayer);
                player_num++; 
            } else {
                oos.writeObject("WAIT"); 
            }
            //close resources
            ois.close();
            oos.close();
            socket.close();
        }
        System.out.println("Lets the games begin!");
        g.startGame(); 
        
        while (!g.gameOver()) {
            int leader = g.getLeader(); 
            int missionLength = g.getMissionLength(); 
            System.out.println("Server reading"); 
            Socket socket = server.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            serverReceive playerRequest = (serverReceive) ois.readObject();
            if (playerRequest.id < 0 || playerRequest.id > g.getPlayers().length)
                continue;
            
            serverSend send = new serverSend(); 
            send.tag = serverSendReceive.NORESPONSE; 
 
            if (playerRequest.tag == serverSendReceive.SET) {
                g.setGameState(playerRequest.id , playerRequest.s, playerRequest.mission);
            }
            send.tag = serverSendReceive.RESPONSE;
            send.leader = leader; 
            send.missionLength = missionLength; 
            send.gamestate = g.getGameState(playerRequest.id); 
            send.message = "EMPTY"; 
            send.gameover = g.gameOver();
            System.out.println("Server sending"); 
            oos.writeObject(send);
            ois.close();
            oos.close();
            socket.close();
            
        }
        
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    }
     
}
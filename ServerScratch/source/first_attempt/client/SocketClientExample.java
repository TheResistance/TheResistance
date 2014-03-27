import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
 
/**
 * This class implements java socket client
 * @author pankaj
 *
 */

public class SocketClientExample {
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Player [] players = new Player[5]; 
        int port = 9875; 
        for(int i=0; i<5;i++){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), port);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            oos.writeObject("connect");
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            newPlayer p = (newPlayer) ois.readObject(); 
            players[i] = new Player(p.id, p.spy); 
            //System.out.println(players[i]);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
        while (true) { 
            serverReceive message = new serverReceive(); 
            serverSend response = null;
            message.tag = serverSendReceive.REQUEST;
            SetState s = null; 
            int [] mission = null;
            for (int j = 0; j < players.length; j+=1) {
                System.out.println("Playing as " + players[j]);  
                for (int i = 0; i < 2; i++) {
                    socket = new Socket(host.getHostName(),port);
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    if(message.tag == serverSendReceive.REQUEST) {
                        message.id = j; 
                        oos.writeObject(message); 
                        message.tag = serverSendReceive.SET; 
                    } else {
                        message.id = j; 
                        message.s = s; 
                        message.mission = mission;
                        oos.writeObject(message);
                        message.tag = serverSendReceive.REQUEST;
                    }
                    ois = new ObjectInputStream(socket.getInputStream());
                    response = (serverSend) ois.readObject(); 
                    mission = players[i].createEmptyMission(response.missionLength); 
                    System.out.println(response.gamestate); 
                    s = players[i].play(response.gamestate, response.leader, mission);
                    
                    message.tag = serverSendReceive.SET; 
                    ois.close();
                    oos.close();
                }
            }
                
            if (response.gameover) {
                break; 
            }
        }
        
                
        
    }
}
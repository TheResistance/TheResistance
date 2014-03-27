// File Name GreetingClient.java

import java.net.*;
import java.io.*;

public class Client
{
   int port = 9907;
   Player p = null; 
   int id = 0; 
   boolean init = false; 
   serverReceive req;  
   
   serverSend in = null; 
   int counter = 0; 
   boolean stalled = false; 
   public static void main(String [] args)  throws ClassNotFoundException
   {
       Client c = new Client(); 
       c.run(); 
   }
   public void run() throws ClassNotFoundException {
     req = new serverReceive();
     req.tag = serverSendReceive.REQUEST; 
      while (true) {
          try
          {
             Socket client = new Socket("localhost", port);
            
             ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
             if (!init) {
                 req.message = "connect";  
             } else {
                 req.id = id; 
                 System.out.println(req.tag); 
                 if (req.tag == serverSendReceive.SET) {
                     req.mission = p.createEmptyMission(in.missionLength); 
                     req.s = p.play(in.gamestate, in.leader, req.mission); 
                 }
                 
             }
             out.writeObject(req);
   
             if (req.tag == serverSendReceive.SET) { 
                 req.tag = serverSendReceive.REQUEST;
             } else if (req.tag == serverSendReceive.REQUEST) {
                 req.tag = serverSendReceive.SET;
             }
             ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
             in = (serverSend) ois.readObject();
             if (in.gamestate == GameState.STALL || 
                 in.gamestate == GameState.WAIT_ON_MISSION || 
                 in.gamestate == GameState.WAIT_ON_LEADER) {
                 if (!stalled) {
                     stalled = true; 
                 }

                 Thread.sleep(500);
             } else {
                 System.out.println("Just connected to "+ client.getRemoteSocketAddress());
                 System.out.println("Player ID: " + id); 
                 System.out.println("Server says " + in.gamestate);
                 System.out.println(in.leader+ " " +in.missionLength+" "+in.gamestate);
                 stalled = false; 
             }

             if (!init) {
                 req.message = ""; 
                 init = true; 
                 req.id = in.id; 
                 id = req.id; 
                 p = new Player(id, in.spy);
             }
             
             client.close();
          }catch(IOException e)
          {
             e.printStackTrace();
          }catch(InterruptedException  e)
          {
             System.out.println("Interupt"); 
             e.printStackTrace();
          }
     }
   }
}

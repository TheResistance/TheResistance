 
/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;




public class Server
{
    private ServerSocket server = null;
    public List<ClientServerSide> client_list = new ArrayList<ClientServerSide>();
    
    public LinkedBlockingQueue<Object> messages = new LinkedBlockingQueue<Object>();
    
    Game game;
    
    public void startServer(Game g)
    {
        this.game = g;
        
        int player_counter = 0;

        final int PORT = 7765;
        System.out.println("Waiting for clients...");
        try 
        {
            server = new ServerSocket(PORT);
            
            while (player_counter < g.getPlayers().length)
            {                                               
                Socket s = server.accept();
                System.out.println("Client connected from " + s.getLocalAddress().getHostName());
                
                ClientServerSide client = new ClientServerSide(this, s, "Player" + player_counter++);
                client_list.add(client);
                Thread t = new Thread(client);
                t.start();
            }
            System.out.println("All players have joined"); 
        } 
        catch (Exception e) 
        {
            System.out.println("An error occured.");
                        e.printStackTrace();
        }
    }
    
    public void sendAll(String message)
    {
        
    }
    
    public void notifyServer(String message) throws InterruptedException
    {
        //messages.put(message);
        //game.message(messages.take());
    }

    @Override
    protected void finalize() throws Throwable
    {
        
        server.close();
        
        for(ClientServerSide s : client_list)
        {
            try
            {
                if (!s.socket.isClosed())
                    s.socket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
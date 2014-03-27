package networking;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.ClientSendMessage;
import core.ResistanceGame;
import core.ServerSendMessage;



public class Server
{
    private ServerSocket server = null;
    public List<ClientServerSide> client_list = new ArrayList<ClientServerSide>();
    
    public LinkedBlockingQueue<ClientSendMessage> messages = new LinkedBlockingQueue<ClientSendMessage>();
    
    ResistanceGame game;
    
    public void startServer(ResistanceGame g)
    {
        this.game = g;
        
        int player_counter = 1;

        final int PORT = 7766;
        System.out.println("Waiting for clients...");
        try 
        {
            server = new ServerSocket(PORT);
            
            while (player_counter < 6)
            {                                               
                Socket s = server.accept();
                System.out.println("Client connected from " + s.getLocalAddress().getHostName());
                
                ClientServerSide client = new ClientServerSide(this, s, "Player" + player_counter++);
                client_list.add(client);
                Thread t = new Thread(client);
                t.start();
            }
        } 
        catch (Exception e) 
        {
            System.out.println("An error occured.");
                        e.printStackTrace();
        }
    }
    
    public void sendAll(ServerSendMessage message)
    {
    	System.out.println("sending message");
    	ObjectOutputStream out;
        for (ClientServerSide c : client_list)
        {
        	try 
        	{
				out = new ObjectOutputStream(c.socket.getOutputStream());
	        	out.writeObject(message);
	        	out.flush();
			}
        	catch (IOException e) 
			{
				e.printStackTrace();
			}
        }
    }
    
    public void notifyServer(ClientSendMessage message) throws InterruptedException
    {
        messages.put(message);
        game.fromClientMessage(messages.take());
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

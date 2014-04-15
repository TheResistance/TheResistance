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
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import core.ClientSendMessage;
import core.ResistanceGame;
import core.ServerSendMessage;



public class Server
{
    private ServerSocket server = null;
    public Hashtable<Integer, ClientServerSide> client_list = new Hashtable<Integer, ClientServerSide>();
    
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
                
                ClientServerSide client = new ClientServerSide(this, s, player_counter);

//                client.out = new ObjectOutputStream(s.getOutputStream());
                Thread t = new Thread(client);
                t.start();
                client_list.put(player_counter, client);
                player_counter++;
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
    	for (Integer key : client_list.keySet())
    	{
    		ClientServerSide c = client_list.get(key);
    		if (message != null && "communication".equals(message.phase) && c.playerNumber == message.playerNumber)
    		{
    			System.out.println("skipping comm for " + c.playerNumber);
    		}
    		else
    		{
	        	try 
	        	{
	//        		if (c == null)
	//        			System.out.println("1111C IS NULL!");
	//        		if (c.out == null)
	//        			System.out.println("1111C's output is null!");
	        		if (message == null)
	        			System.out.println("1111Message is null");
	        		c.out.reset();
					c.out.writeObject(message);
		        	c.out.flush();
				}
	        	catch (IOException e) 
				{
					e.printStackTrace();
				}
    		}
        }
    }
    
    public void sendToClient(int playerNumber, ServerSendMessage message)
    {
    	System.out.println("getting connection for player " + playerNumber);
    	System.out.println("client list length: " + client_list.size());
    	ClientServerSide c = client_list.get(playerNumber);
    	
    	try 
    	{
    		if (c == null)
    			System.out.println("C IS NULL!");
    		if (c.out == null)
    			System.out.println("C's output is null!");
    		c.out.reset();
			c.out.writeObject(message);
        	c.out.flush();
		}
    	catch (IOException e) 
		{
			e.printStackTrace();
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
        
        for(Integer key : client_list.keySet())
        {
        	ClientServerSide s = client_list.get(key);
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

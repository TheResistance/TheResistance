/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/
package networking;

import gui.GameGui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import core.ServerSendMessage;

public class Client implements Runnable
{

    private Socket socket;
    // client input. Will be passed along to the server using *in*
    Scanner playerInput = new Scanner(System.in);
    ObjectInputStream in;
    ObjectOutputStream out;
    
    private GameGui gui = new GameGui(this, 0, "", "");
	
	public Client(Socket s)
	{
		socket = s;
	}
	
	@Override
	public void run()
	{
		try
		{
			
			in = new ObjectInputStream(socket.getInputStream()); // messages FROM the server
			out = new ObjectOutputStream(socket.getOutputStream()); // messages TO the server
			
			
			Thread write = new Thread()
			{
                public void run()
                {
                    while(true)
                    {
                        try
                        {
                            String input = playerInput.nextLine();
                            //out.println(input);
                            out.flush();
                        }
                        catch(Exception e)
                        { 
                            e.printStackTrace(); 
                        }
                    }
                }
            };

//            read.setDaemon(true);
            write.start();
            
			Thread read = new Thread()
			{
                public void run()
                {
                    while(true)
                    {
                    	try
                    	{
	                        Object objectMessage = in.readObject();
	                        ServerSendMessage serverMessage = null;
	                        if (objectMessage instanceof ServerSendMessage)
	                        {
	                        	serverMessage = (ServerSendMessage) objectMessage;
	                            handleServerMessage(serverMessage);
	                        }
	                        else
	                        {
	                        	System.out.println("UH OH. WRONG MESSAGE TYPE");
	                        }
                    	}
	                    catch(Exception e)
	                    { 
	                        e.printStackTrace(); 
	                    }
                    }
                }
            };

//            read.setDaemon(true);
            read.start();
            
            /*
			while (true)
			{						
				String input = playerInput.nextLine();
				out.println(input);
				out.flush();
				
				if(in.hasNext())
					System.out.println(in.nextLine());
			}
			*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}

	private void handleServerMessage(ServerSendMessage message)
	{
		System.out.println("message received: " + message);
	}
}

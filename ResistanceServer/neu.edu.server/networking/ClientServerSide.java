/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/
package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import core.ClientSendMessage;


public class ClientServerSide implements Runnable
{

	public Socket socket;
	public String client_name;
	public Server server;
	
	public ClientServerSide(Server server, Socket s, String name)
	{
		this.server = server;
		this.socket = s;
		this.client_name = name;
	}
	
	@Override
	public void run() 
	{
		try 
		{
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			
			while (true)
			{		
				Object input = in.readObject();
				ClientSendMessage clientMessage = null;
				if (input instanceof ClientSendMessage)
				{
					clientMessage = (ClientSendMessage)input;
				}
				else
				{
					System.out.println("WRONG MESSAGE TYPE");
				}
				System.out.println(client_name + " said: " + input);
				server.notifyServer(clientMessage);
				//out.println(client_name + " said: " + input);
				//out.flush();
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

    @Override
    protected void finalize() throws Throwable
    {
        if(!socket.isClosed())
        {
            socket.getOutputStream().close();
            socket.getInputStream().close();
            socket.close();
        }
    }

	
}
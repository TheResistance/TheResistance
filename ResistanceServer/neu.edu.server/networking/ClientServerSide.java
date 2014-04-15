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
import java.net.SocketException;
import java.util.Scanner;

import core.ClientSendMessage;


public class ClientServerSide implements Runnable
{

	public Socket socket;
	public int playerNumber;
	public Server server;
	public ObjectInputStream in;
	public ObjectOutputStream out;
	
	public ClientServerSide(Server server, Socket s, int number)
	{
		this.server = server;
		this.socket = s;
		this.playerNumber = number;
		try
		{
			
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		try 
		{
			
			
			while (true)
			{		
				Object input = in.readObject();
				ClientSendMessage clientMessage = null;
				if (input instanceof ClientSendMessage)
				{
					clientMessage = (ClientSendMessage)input;
					System.out.println("Player " + playerNumber + " said: ");
					((ClientSendMessage) input).printMessage();
					server.notifyServer(clientMessage);
				}
				else
				{
					System.out.println("WRONG MESSAGE TYPE");
				}
				//out.println(client_name + " said: " + input);
				//out.flush();
			}
		} 
    	catch(SocketException se)
    	{
    		System.out.println("Caught SE. Ending app.");
    		System.exit(0);
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
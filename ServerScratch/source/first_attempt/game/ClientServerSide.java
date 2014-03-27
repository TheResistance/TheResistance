/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/
 

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class ClientServerSide implements Runnable
{

	public Socket socket;
	public String client_name;
	private Server server;
	
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
			Scanner in = new Scanner(socket.getInputStream());
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			
			while (true)
			{		
				if (in.hasNext())
				{
					String input = in.nextLine();
					System.out.println(client_name + " said: " + input);
					server.notifyServer(input);
					//out.println(client_name + " said: " + input);
					//out.flush();
				}
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
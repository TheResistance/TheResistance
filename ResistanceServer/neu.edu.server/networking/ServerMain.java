package networking;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class ServerMain
{

	public static void main(String[] args) throws IOException
	{
	    int player_counter = 1;
		try 
		{
			final int PORT = 6677;
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Waiting for clients...");
		
			while (player_counter < 6)
			{												
				Socket s = server.accept();
				System.out.println("Client connected from " + s.getLocalAddress().getHostName());			
				ClientServerSide chat = new ClientServerSide(s, "Player" + player_counter);
				Thread t = new Thread(chat);
				t.start();
			}
		} 
		catch (Exception e) 
		{
			System.out.println("An error occured.");
                        e.printStackTrace();
		}
	}
}

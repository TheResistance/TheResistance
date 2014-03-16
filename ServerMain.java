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

public class ServerMain {

	public static void main(String[] args) throws IOException {
		try 
		{
			final int PORT = 6677;
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Waiting for clients...");
		
			while (true)
			{												
				Socket s = server.accept();
				System.out.println("Client connected from " + s.getLocalAddress().getHostName());			
				ClientServerSide chat = new ClientServerSide(s);
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

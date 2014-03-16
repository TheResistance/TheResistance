/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

package networking;
import java.io.IOException;
import java.net.Socket;

public class ClientMain
{

	private final static int PORT = 6677;
	private final static String HOST = "localhost";
	
	public static void main(String[] args) throws IOException
	{
		try 
		{
			
			Socket s = new Socket(HOST, PORT);
			
			System.out.println("You connected to " + HOST);
			
			Client client = new Client(s);
			
			Thread t = new Thread(client);
			t.start();
			
		} 
		catch (Exception noServer)
		{
			System.out.println("The server might not be up at this time.");
			System.out.println("Please try again later.");
		}
	}
}


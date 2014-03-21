package networking;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/
import java.net.Socket;

public class InitializeClientNetwork
{
    private final static int PORT = 7766;
    private final static String HOST = "10.102.34.215";
    
    public void initialize()
    {
    
        try 
        {
            
            Socket s = new Socket(HOST, PORT);
            
            System.out.println("You connected to " + HOST);
            
            Client client = new Client(s);
            Thread c = new Thread(client);
            c.start();
            
        } 
        catch (Exception noServer)
        {
            System.out.println("The server might not be up at this time.");
            System.out.println("Please try again later.");
        }
    }
}

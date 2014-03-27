 
/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/
import java.net.Socket;

public class InitializeClientNetwork
{
    private final static int PORT = 7765;
    private final static String HOST = "localhost";
    Client client; 
    public void initialize()
    {
    
        try 
        {
            
            Socket s = new Socket(HOST, PORT);
            
            System.out.println("You connected to " + HOST);
            
            client = new Client(s);
            Thread c = new Thread(client);
            c.start();
            
        } 
        catch (Exception noServer)
        {
            System.out.println("The server might not be up at this time.");
            System.out.println("Please try again later.");
        }
    }
    public void run() {
        client.run();
    }
    
}

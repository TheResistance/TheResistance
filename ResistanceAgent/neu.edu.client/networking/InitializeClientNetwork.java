package networking;


/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/
import java.net.Socket;

import javax.swing.SwingUtilities;

import agent.AgentLogic;

public class InitializeClientNetwork
{
    private final static int PORT = 7766;
    private final static String HOST = "localhost";
    
    private Client client;
    
    public void initialize()
    {
        try 
        {
            
            Socket s = new Socket(HOST, PORT);
            
            System.out.println("You connected to " + HOST);
            
            client = new Client(s);
            System.out.println("creating gui");
            client.gui = new AgentLogic(client, 0, "", "");
    	    
            client.createStreams();
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

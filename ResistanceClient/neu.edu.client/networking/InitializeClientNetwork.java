package networking;
import gui.GameGui;

/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/
import java.net.Socket;

import javax.swing.SwingUtilities;

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
    	    SwingUtilities.invokeLater(new Runnable() 
    	    {
                @Override
                public void run() 
                {
                	System.out.println("creating gui");
                    client.gui = new GameGui(client, 0, "", "");
                    client.gui.setVisible(true);
                }
            });
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

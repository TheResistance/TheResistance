/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/
package networking;

import gui.GameGui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Client implements Runnable
{

    private Socket socket;
    // client input. Will be passed along to the server using *in*
    Scanner playerInput = new Scanner(System.in);
    Scanner in;
    PrintWriter out;
    
    private GameGui gui = new GameGui(0, "", "");
	
	public Client(Socket s)
	{
		socket = s;
		Scanner in;
		PrintWriter out;
	}
	
	@Override
	public void run()
	{
		try
		{
			
			in = new Scanner(socket.getInputStream()); // messages FROM the server
			out = new PrintWriter(socket.getOutputStream()); // messages TO the server
			
			
			Thread write = new Thread()
			{
                public void run()
                {
                    while(true)
                    {
                        try
                        {
                            String input = playerInput.nextLine();
                            out.println(input);
                            out.flush();
                        }
                        catch(Exception e)
                        { 
                            e.printStackTrace(); 
                        }
                    }
                }
            };

//            read.setDaemon(true);
            write.start();
            
			Thread read = new Thread()
			{
                public void run()
                {
                    while(true)
                    {
                        try
                        {
                            String serverMessage = in.nextLine();
                            handleServerMessage(serverMessage);
                        }
                        catch(Exception e)
                        { 
                            e.printStackTrace(); 
                        }
                    }
                }
            };

//            read.setDaemon(true);
            read.start();
            
            /*
			while (true)
			{						
				String input = playerInput.nextLine();
				out.println(input);
				out.flush();
				
				if(in.hasNext())
					System.out.println(in.nextLine());
			}
			*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}

	private void handleServerMessage(String message)
	{
		
	}
}

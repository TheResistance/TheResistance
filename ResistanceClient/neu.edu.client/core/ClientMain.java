/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

package core;
import gui.GameGui;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import networking.Client;
import networking.InitializeClientNetwork;

public class ClientMain
{

	
	public static void main(String[] args) throws IOException
	{
	    
		InitializeClientNetwork init = new InitializeClientNetwork();
		init.initialize();

//	    SwingUtilities.invokeLater(new Runnable() 
//	    {
//			Client client = new Client(null);
//            @Override
//            public void run() 
//            {
//            	System.out.println("creating gui");
//                client.gui = new GameGui(client, 0, "", "");
//                client.gui.setVisible(true);
//            }
//        });
	}
}


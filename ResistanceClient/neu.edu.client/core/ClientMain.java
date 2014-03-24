/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

package core;
import gui.GameGui;

import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import networking.InitializeClientNetwork;

public class ClientMain
{

	
	public static void main(String[] args) throws IOException
	{
//		InitializeClientNetwork init = new InitializeClientNetwork();
//		init.initialize();
		

	    SwingUtilities.invokeLater(new Runnable() 
	    {
            @Override
            public void run() 
            {
                GameGui ex = new GameGui(4, "Resistance", "Player 2");
                ex.setVisible(true);
            }
        });
	}
}


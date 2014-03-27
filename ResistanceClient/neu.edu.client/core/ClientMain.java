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

import networking.InitializeClientNetwork;

public class ClientMain
{

	
	public static void main(String[] args) throws IOException
	{
		InitializeClientNetwork init = new InitializeClientNetwork();
		init.initialize();
		

	    SwingUtilities.invokeLater(new Runnable() 
	    {
            @Override
            public void run() 
            {
                GameGui gui = new GameGui(0, "", "");
                gui.setVisible(true);
                
//                List<String> team = new ArrayList<String>();
//                team.add("Player 1");
//                team.add("Player 4");
//                gui.setCurrentlySelectedTeam(team);
            }
        });
	}
}


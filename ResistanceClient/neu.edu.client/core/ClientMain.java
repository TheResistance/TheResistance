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
	}
}


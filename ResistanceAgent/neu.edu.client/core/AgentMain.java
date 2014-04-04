/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

package core;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import agent.AgentLogic;
import networking.InitializeClientNetwork;

public class AgentMain
{

	
	public static void main(String[] args) throws IOException
	{
	    
		InitializeClientNetwork init = new InitializeClientNetwork();
		init.initialize();
	}
}


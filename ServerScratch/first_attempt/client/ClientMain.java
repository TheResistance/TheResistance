/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

 
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;





public class ClientMain
{

	
	public static void main(String[] args) throws IOException
	{
		InitializeClientNetwork init = new InitializeClientNetwork();
		init.initialize();
        init.run(); 
	}
}


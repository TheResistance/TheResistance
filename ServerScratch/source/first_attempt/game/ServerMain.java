 
/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

public class ServerMain
{
    

    public static void main(String[] args)
    {

        Server server = new Server();
        server.startServer(new Game(5));

    }
}

package core;

import networking.ClientServerSide;
import networking.Server;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

public class ServerMain
{
    

    public static void main(String[] args) throws InterruptedException
    {
        ResistanceGame game = new ResistanceGame();
        Server server = new Server();
        server.startServer(game);
        System.out.println("Server started");
        game.setServer(server);
        Thread.sleep(5000);

//        System.out.println("client list size: " + server.client_list.size());
//        System.out.println("client list keys: " + server.client_list.keys());
//        ClientServerSide client = server.client_list.get(1);
//        if (client == null)
//        	System.out.println("client is null");
//        server.sendToClient(1, null);
        game.beginGame();
    }
}

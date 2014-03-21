package core;

import networking.Server;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/

public class ServerMain
{
    

    public static void main(String[] args)
    {
        ResistanceGame game = new ResistanceGame();
        game.beginGame();
        Server server = new Server();
        server.startServer(game);

    }
}
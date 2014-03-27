package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import networking.Server;

/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/

public class ResistanceGame
{
    public int playerTurn = -1;
    public List<Integer> order = new ArrayList<Integer>();

    public int currentLeader = -1;
    public int [] spies = new int [2];
    
    public boolean gameOver = false;
    
    public Server server;
    
    /*
     * Possible phases:
     * groupSelection, groupApproval, missionVoting
     */
    public String missionPhase = new String();
    
    public void setServer(Server s)
    {
    	this.server = s;
    }
    
    public ResistanceGame()
    {
    	/*
    	 * Shuffle list, first two are the spies.
    	 */
    	order = new ArrayList<Integer>();
        order.add(1);
        order.add(2);
        order.add(3);
        order.add(4);
        order.add(5);
        Collections.shuffle(order);
        spies[0] = order.get(0);
        spies[1] = order.get(1);
        
        /*
         * Shuffle list again to find final order.
         */
        Collections.shuffle(order);
        
        /*
         * Assign turn and initial state
         */
        playerTurn = order.get(0);
        currentLeader = playerTurn;
        missionPhase = "groupSelection";
    }
    
    public void beginGame()
    {
    	ServerSendMessage msg = new ServerSendMessage();
    	msg.playerTurn = playerTurn;
    	msg.phase = missionPhase;
    	sendServerMessage(msg);
    }
    
    public boolean isSpy(int player)
    {
    	if (player == spies[0] || player == spies[1])
    		return true;
    	return false;
    }
    
    public void fromClientMessage(ClientSendMessage message)
    {
        System.out.println("Game received message: " + message);
    }
    
    public void groupSelection(int player, List<Integer> proposedGroup)
    {
    	if (player != playerTurn || player != currentLeader)
    		return;
    }
    
    public void submitGroupApprovalVote(int player, String vote)
    {
    	if (player != playerTurn)
    		return;
    	
    }
    
    public void submitPlayerMissionVote(int player, String vote)
    {
    	if (player != playerTurn)
    		return;
    }
    
    public void sendServerMessage(ServerSendMessage message)
    {
    	server.sendAll(message);
    }
}

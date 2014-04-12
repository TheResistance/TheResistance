package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
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
    public Hashtable<Integer, String> groupApprovalVotes = new Hashtable<Integer, String>();
    public Hashtable<Integer, String> missionApprovalVotes = new Hashtable<Integer, String>();
    
    public int missionSuccesses = 0;
    public int missionFailures = 0;
    
    public int currentLeader = -1;
    public int [] spies = new int [2];
    public int [] missionGroup;
    public int rejectCount;
    
    public boolean gameOver = false;
    
    public Server server;
    
    /*
     * Possible phases:
     * groupSelection, groupApproval, missionVote
     */
    public String missionPhase = new String();
    public int missionNumber = -1;
    
    private int [] missionSize = {0,2,3,2,3,3};
    
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
        order.add(2);
        order.add(3);
        order.add(4);
        order.add(5);
        Collections.shuffle(order);
        spies[0] = order.get(0);
        spies[1] = order.get(1);
        order.add(1);
        /*
         * Shuffle list again to find final order.
         */
        Collections.shuffle(order);
        String text = "Order: ";
        for (Integer i : order)
        {
        	text += i + " ";
        }
        System.out.println(text);
        /*
         * Assign turn and initial state
         */
        playerTurn = order.get(0);
        currentLeader = playerTurn;
        missionPhase = "groupSelection";
        missionNumber = 1;
    }
    
    public void beginGame()
    {
        // First need to send out assignments
        for (int i = 1; i <= 5; i++)
        {
	        ServerSendMessage message = new ServerSendMessage();
	        message.phase = "assignment";
	        message.playerTurn = order.get(0);
	        message.playerNumber = i;
	        if (i == spies[0])
	        {
	        	message.faction = "spy";
	        	message.otherSpy = spies[1];
	        }
	        else if (i == spies[1])
	        {
	        	message.faction = "spy";
	        	message.otherSpy = spies[0];
	        }
	        else
	        	message.faction = "resistance";
	        if (i <= server.client_list.size())
	        	server.sendToClient(i, message);
        }
    	ServerSendMessage msg = new ServerSendMessage();
    	msg.playerTurn = playerTurn;
    	msg.phase = missionPhase;
    	msg.currentLeader = currentLeader;
    	msg.groupSize = missionSize[missionNumber];
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
    	System.out.println("current turn: " + playerTurn);
    	if ("communication".equals(message.messageType))
    	{
    		ServerSendMessage msg = new ServerSendMessage();
        	msg.playerNumber = message.playerId;
        	msg.phase = message.messageType;
        	msg.message = message.message;
        	msg.groupSelection = message.groupSelection;
        	sendServerMessage(msg);
    	}
    	else if (message.playerId == playerTurn)
        {
        	switch(message.messageType)
        	{
        		case ("groupSelection"):
        			groupSelection(message.playerId, message.groupSelection);
        			break;
        		case ("groupApproval"):
        			submitGroupApprovalVote(message.playerId, message.message);
        			break;
        		case ("missionVote"):
        			submitPlayerMissionVote(message.playerId, message.message);
        			break;
        	}		
        }
        else
        	System.out.println("Ignored message from player " + message.playerId + " b/c it is not their turn.");
    }
    
    public void groupSelection(int player, List<Integer> groupSelection)
    {
    	if (player != currentLeader)
    	{
    		System.out.println("Player " + player + " is not the current leader.");
    		return;
    	}
    	//String[] participants = undecipheredString.split("&");
    	if (groupSelection.size() != missionSize[missionNumber])
    	{
    		System.out.println("mission size is " + missionSize[missionNumber] + ". Player " + player + " selected " + groupSelection.size());
    		return;
    	}
    	missionGroup = new int[missionSize[missionNumber]];
    	ServerSendMessage message = new ServerSendMessage();
    	message.groupSelection = new ArrayList<Integer>();
    	for (int i=0; i<missionGroup.length; i++)
    	{
    		missionGroup[i] = groupSelection.get(i);
    		message.groupSelection.add(missionGroup[i]);
    	}
    	if (rejectCount != 4)
    	{
	    	message.phase = "groupApproval";
	    	message.currentLeader = currentLeader;
	    	int next = nextInOrder(player);
	    	message.playerTurn = next;
	    	playerTurn = next;
	    	server.sendAll(message);
    	}
    	else
    	{
    		rejectCount = 0;
    		message.phase = "missionVote";
    		message.currentLeader = currentLeader;
    		message.groupSelectionResult = "success";
    		int next = nextMissionVoter(-1);
    		message.playerTurn = next;
			playerTurn = next;
    	}
    	groupApprovalVotes = new Hashtable<Integer, String>();
    }
    
    public void submitGroupApprovalVote(int player, String vote)
    {
    	if (player != playerTurn)
    	{
    		System.out.println("Group Approval submitted for player " + player + ". Not their turn.");
    		return;
    	}
    	if ("accept".equals(vote) || "reject".equals(vote))
    	{
	    	if (!groupApprovalVotes.containsKey(player))
	    	{
	    		System.out.println("valid vote by " + player + ": " + vote);
	    		groupApprovalVotes.put(player, vote);
	    	}
    	}
    	else
    		System.out.println("Player " + player + " submitted an invalid group approval vote: " + vote);
    	// voting has completed and we need to move on to the mission or new leader
    	if (groupApprovalVotes.keySet().size() == 5)
    	{
    		System.out.println("obtained all group approvals votes. Tally the score.");
    		int passCount = 0;
    		for(Integer playerKey : groupApprovalVotes.keySet())
    		{
    			if ("accept".equals(groupApprovalVotes.get(playerKey)))
    				passCount++;
    		}
    		ServerSendMessage message = new ServerSendMessage();
    		message.playerVotes = groupApprovalVotes;
    		// If there are enough votes to pass, change state to mission voting
    		if(passCount >= 3)
    		{
        		message.phase = "missionVote";
	    		message.groupSelectionResult = "success";
	    		message.currentLeader = currentLeader;
	        	for (int i=0; i<missionGroup.length; i++)
	        	{
	        		message.groupSelection.add(missionGroup[i]);
	        	}
	    		int next = nextMissionVoter(-1);
	    		message.playerTurn = next;
    			playerTurn = next;
    			rejectCount = 0;
    		}
    		// If there are not enough to pass, revert to group selection phase
    		else
    		{
        		message.phase = "groupSelection";
    			message.groupSelectionResult = "fail";
	        	for (int i=0; i<missionGroup.length; i++)
	        	{
	        		message.groupSelection.add(missionGroup[i]);
	        	}
    			int next = nextInOrder(player);
    			message.playerTurn = next;
    			message.groupSize = missionSize[missionNumber];
    			currentLeader = next;
    			message.currentLeader = currentLeader;
    			playerTurn = next;
    			rejectCount++;
    		}
    		server.sendAll(message);
    	}
    	// Still waiting on votes, pass to the next player
    	else
    	{
    		ServerSendMessage message = new ServerSendMessage();
    		message.phase = "groupApproval";
    		message.currentLeader = currentLeader;
    		int next = nextInOrder(player);
    		message.playerTurn = next;
			playerTurn = next;
    		server.sendAll(message);
    	}
    }
    
    public void submitPlayerMissionVote(int player, String vote)
    {
    	if (player != playerTurn)
    	{
    		System.out.println("Mission vote submitted for player " + player + ". Not their turn.");
    		return;
    	}
    	if (!isInMission(player))
    	{
    		System.out.println("Mission vote submitted for player " + player + ". They are not in the mission.");
    		return;
    	}
    	if ("accept".equals(vote) || "reject".equals(vote))
    	{
	    	if (!missionApprovalVotes.containsKey(player))
	    	{
	    		missionApprovalVotes.put(player, vote);
	    	}
    	}
    	else
    		System.out.println("Player " + player + " submitted an invalid mission vote: " + vote);
    	if (missionApprovalVotes.keySet().size() == missionSize[missionNumber])
    	{
    		int failCount = 0;
    		for(Integer playerKey : missionApprovalVotes.keySet())
    		{
    			if ("reject".equals(missionApprovalVotes.get(playerKey)))
    				failCount++;
    		}
    		ServerSendMessage message = new ServerSendMessage();
    		
    		// If there is one fail vote, mission fails. Pass control to next leader.
    		if(failCount >= 1)
    		{
    			System.out.println("$$$$$$$$$$FAILED MISSION$4444444444444");
    			System.out.println("new failure count: " + (missionFailures + 1));
        		message.phase = "groupSelection";
        		
    			if(missionFailures+1 == 3)
    			{
    				message.phase = "gameOver";
    				message.winners = "Spies";
    				gameOver = true;
    			}

	    		message.missionResult = "fail";
	    		message.missionFailVotes = failCount;
	    		message.groupSize = missionSize[++missionNumber];
	    		message.gameOver = gameOver;
	    		// set the next player to the person after the leader. New round starting.
	    		int next = nextInOrder(currentLeader);
	    		message.playerTurn = next;
	    		currentLeader = next;
	    		message.currentLeader = currentLeader;
    			playerTurn = next;
    			missionApprovalVotes = new Hashtable<Integer,String>();
    			missionFailures++;
    		}
    		// If no one fails, succeed mission, pass control to person after the current leader
    		else
    		{
    			System.out.println("mission success. prior Number: " + missionSuccesses);
    			if(missionSuccesses+1 == 3)
    			{
    				message.phase = "gameOver";
    				message.winners = "Resistance";
    				gameOver = true;
    			}
    			else
    				message.phase = "groupSelection";
    			message.missionResult = "success";
    	    	for (int i=0; i<missionGroup.length; i++)
    	    	{
    	    		message.groupSelection.add(missionGroup[i]);
    	    	}
    			int next = nextInOrder(currentLeader);
    			message.playerTurn = next;
    			message.groupSize = missionSize[++missionNumber];
    			message.gameOver = gameOver;
    			playerTurn = next;
    			currentLeader = next;
    			message.currentLeader = currentLeader;
    			missionSuccesses++;
    			missionApprovalVotes = new Hashtable<Integer,String>();
    		}
    		if(gameOver)
    		{
    			if (missionSuccesses == 3)
    				System.out.println("Game over. Resistance won. Spies were player " + spies[0] + " and player " + spies[1]);
    			
    			else
    				System.out.println("Game over. Spies won. Spies were player " + spies[0] + " and player " + spies[1]);
    		}
    		server.sendAll(message);
    	}
    	// Still waiting on votes, pass to the next mission participant
    	else
    	{
    		ServerSendMessage message = new ServerSendMessage();
    		message.phase = "missionVote";
    		int next = nextMissionVoter(player);
    		message.playerTurn = next;
    		message.currentLeader = currentLeader;
			playerTurn = next;
    		server.sendAll(message);
    	}
    }
    
    public void sendServerMessage(ServerSendMessage message)
    {
    	server.sendAll(message);
    }
    
    private int nextInOrder(int currentPlayer)
    {
    	int index = order.indexOf(currentPlayer);
    	if (index == 4)
    		return order.get(0);
    	return order.get(index+1);
    }
    
    private int nextMissionVoter(int currentPlayer)
    {
    	if (currentPlayer == -1)
    		return missionGroup[0];
    	if (missionGroup[0] == currentPlayer)
    		return missionGroup[1];
    	if (missionGroup[1] == currentPlayer)
    		return missionGroup[2];
    	else
    	{
    		System.out.println("error with mission voter order");
    		return -1;
    	}
    }
    
    private boolean isInMission(int player)
    {
    	if (player == missionGroup[0] || player == missionGroup[1])
    	{
    		return true;
    	}
    	else if(missionGroup.length == 3 && player == missionGroup[2])
    	{
    		return true;
    	}
    	else
    		return false;
    }
}

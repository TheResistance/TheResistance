package agent;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 22, 2014
*/


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import core.ClientSendMessage;
import networking.Client;

public class AgentLogic {
	public Client client;
	
	
	public String turn = "";
	public String currentRole = "";
	public String playerFaction;
	public int playerNumber;
	public boolean isLeader = false;
	public boolean isMissionParticipant = false;
	public List<Integer> selectedTeam = new ArrayList<Integer>();
	public String phase;
	
	
	public AgentLogic(Client c, int player, String faction, String otherSpy) 
    {
		this(player, faction, otherSpy);
		this.client = c;
    }

    public AgentLogic(int player, String faction, String otherSpy) 
    {
    	playerFaction = faction;
    	playerNumber = player;
    	
		
		/*
		 * Player number
		 */
		System.out.println("Player " + player);
		System.out.println("Player " + faction);
		/*
		 * Faction or the player.
		 * Faction text shows actual faction.
		 */

		/*
		 * Says who the other spy is if the player is a spy
		 */
		System.out.println(otherSpy);
		
		
		/*
		 * States what the player is doing this turn.
		 */

		System.out.println("Role: " + currentRole);
		
		
		/*
		 * States who's turn it currently is.
		 */
		System.out.println("Turn: " + turn);
		
		/*
		 * Currently selected team for a mission.
		 */

		
//		selectedTeam.add("Player 1");
//		selectedTeam.add("Player 4");
//		selectedTeam.add("Player 5");
		
		String tempTeam = "   ";
		for (Integer member : selectedTeam)
		{
			tempTeam += ", Player " + member;
		}
		tempTeam = tempTeam.substring(1);
		System.out.println("Team: " +  tempTeam);
		
		/*
		 * This field has all the game communication.
		 */
		
		
		/*
		 * Accept and Reject buttons.
		 * Run validation on current information before sending to server.
		 */
		boolean accept = false; 
		boolean reject = false;
		
		if (accept) {
			client.sendServerVote(phase, "accept");
		}
		if (reject) {
			client.sendServerVote(phase, "reject");
		}
		/*
		 * Add everything to the panel.
		 */
		
		
		if (playerFaction != "Spy") hideSpyInformation();
    }
    
    public void createTeamSelectionGui(int choiceCount)
    {
    	System.out.println("choice count: " + choiceCount);
    }
    
    public void hideSpyInformation()
    {
    }
    
    public void showSpyInformation()
    {
    
    }
    
    public void setTurnRole(String role)
    {
    	currentRole = role;
    	System.out.println("Changed role to: " + currentRole);
    }
    
    public void setCurrentPlayer(String curPlayer)
    {
    	turn = curPlayer;
    	System.out.println("Changed turn to: " + turn);
    }
    
    public void setCurrentlySelectedTeam(List<Integer> team)
    {
    	if (team.size() > 0)
    	{
	    	selectedTeam = new ArrayList<Integer>();
	    	String tempTeam = new String();
	    	for (Integer member : team)
	    	{
	    		selectedTeam.add(member);
				tempTeam += ", Player " + member;
			}
			tempTeam = tempTeam.substring(1);
			System.out.println("Changed team to: " + tempTeam);
    	}
    }
    
    public void sendClientMessage(String message)
    {
    	//gameMessages.setText(gameMessages.getText() + "\n" + message);

    }
    
    public void updatePlayerNumber(int num)
    {
    	playerNumber = num;
    	System.out.println("PLayer" + playerNumber);
    }
    
    public void updateFactionInformation()
    {
    	
    }
    public String getGroupApproval() {
    	return Math.random() < 0.5 ? "accept" : "reject";
    }
    public String getMissionVote() {
    	if ("spy".equals(playerFaction)) {
    		System.out.println("I am a spy!");
    		return Math.random() < 0.5 ? "accept" : "reject";
    	}
    	return "accept";
    }
    public void sendGroupSelection(int groupsize)
    {
    	List<Integer> groupMembers = new ArrayList<Integer>(); 
    	for (int i=0; i < groupsize; i++) {
    		groupMembers.add(i+1);
    	}
    	ClientSendMessage message = new ClientSendMessage();
    	message.playerId = playerNumber;
    	message.messageType = "groupSelection";
    	for (Integer choice : groupMembers)
    	{
    		System.out.println("group choice: " + choice);
    	}
    	message.groupSelection = groupMembers;
    	
    	try
    	{
			client.out.writeObject(message);
		}
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

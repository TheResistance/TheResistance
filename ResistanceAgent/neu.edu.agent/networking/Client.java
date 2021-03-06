/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/
package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import rules.Bot;
import rules.ExpertStatsAgent;
import rules.RandomResistance;
import agent.AgentLogic;
import core.ClientSendMessage;
import core.ServerSendMessage;

public class Client implements Runnable
{

    public Socket socket;
    // client input. Will be passed along to the server using *in*
    public Scanner playerInput = new Scanner(System.in);
    public ObjectInputStream in;
    public ObjectOutputStream out;
    public Bot bot = null;
    public AgentLogic agent_logic;
    private Client self = this;
	
	public Client(Socket s)
	{
		socket = s;
	}
	
	public void createStreams()
	{
		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{	    
		try
		{			
			Thread write = new Thread()
			{
                public void run()
                {
                    while(true)
                    {
                        try
                        {
                            String input = playerInput.nextLine();
                            //out.println(input);
                            out.flush();
                        }
                        catch(Exception e)
                        { 
                            e.printStackTrace(); 
                        }
                    }
                }
            };

//            read.setDaemon(true);
            write.start();
            
			Thread read = new Thread()
			{
                public void run()
                {
                    while(true)
                    {
                    	try
                    	{
                    		if (in != null)
                    		{
                    			Object objectMessage = in.readObject();
		                        ServerSendMessage serverMessage = null;
		                        if (objectMessage instanceof ServerSendMessage)
		                        {
		                        	serverMessage = (ServerSendMessage) objectMessage;
		                            handleServerMessage(serverMessage);
		                        }
		                        else
		                        {
		                        	System.out.println("1111UH OH. WRONG MESSAGE TYPE");
		                        }
                    		}
                    	}
                    	catch(SocketException se)
                    	{
                    		System.out.println("Caught SE. Ending app.");
                    		System.exit(0);
                    	}
	                    catch(Exception e)
	                    { 
	                        e.printStackTrace(); 
	                    }
                    }
                }
            };

//            read.setDaemon(true);
            read.start();
            
            /*
			while (true)
			{						
				String input = playerInput.nextLine();
				out.println(input);
				out.flush();
				
				if(in.hasNext())
					System.out.println(in.nextLine());
			}
			*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	}

	private void handleServerMessage(ServerSendMessage message)
	{
		if (agent_logic == null)
			System.out.println("gui is null");
		if (message == null)
			System.out.println("message is null");
		message.printMessage();
		//agent_logic.setCurrentLeader(message.currentLeader);
		agent_logic.isLeader = (message.currentLeader == agent_logic.playerNumber) ? true : false;
		if (message.missionResult != null)
		{
			System.out.println("$$$$$$$$ Calling onMissionComplete.");
			bot.onMissionComplete(agent_logic.selectedTeam, message.missionFailVotes);
	          
	        ClientSendMessage accusalResponse = bot.sendMessage(true);

	        ClientSendMessage suggestionResponse = bot.sendMessage(false);
	        
	        if(accusalResponse.groupSelection.size() > 0)
	        {
	        	agent_logic.sendCommunication(accusalResponse);
	        }
	        if(suggestionResponse.groupSelection.size() > 0)
	        {
	        	agent_logic.sendCommunication(suggestionResponse);
	        }
	        
			//gui.gameMessages.setText(newText);
		}
		if("groupSelection".equals(message.phase) && message.playerTurn == agent_logic.playerNumber)
		{
			agent_logic.isMissionParticipant = false;
			agent_logic.setTurnRole("You are the mission leader. Select a group.");
			agent_logic.phase = "groupSelection";
			System.out.println("group size: " + message.groupSize);
			agent_logic.sendGroupSelection(message.groupSize);
			if (message.groupSelection != null && message.groupSelection.size() > 0)
				agent_logic.setCurrentlySelectedTeam(message.groupSelection);
		}
		else if("communication".equals(message.phase))
		{
			bot.getMessage(message);
		}
		else if("groupApproval".equals(message.phase))
		{
			if (message.playerTurn == agent_logic.playerNumber)
			{
				agent_logic.isMissionParticipant = false;
				agent_logic.phase = "groupApproval";
				agent_logic.setTurnRole("Vote to approve or reject the group proposal by Player " + message.currentLeader);
				String vote = agent_logic.getGroupApproval(); 
				sendServerVote(agent_logic.phase,vote);
			}
			else
			{
				agent_logic.isMissionParticipant = false;
				agent_logic.setTurnRole("Waiting...");
				agent_logic.updateFactionInformation();
			}
			if (message.groupSelection != null && message.groupSelection.size() > 0)
				agent_logic.setCurrentlySelectedTeam(message.groupSelection);
		}
		else if("missionVote".equals(message.phase) && message.playerTurn == agent_logic.playerNumber)
		{
			agent_logic.isMissionParticipant = true;
			agent_logic.phase = "missionVote";
			agent_logic.setTurnRole("You are in the mission. Vote for the mission to succeed or fail.");
			if (message.groupSelection != null && message.groupSelection.size() > 0)
				agent_logic.setCurrentlySelectedTeam(message.groupSelection);
			String vote = agent_logic.getMissionVote(); 
			sendServerVote(agent_logic.phase,vote);
		}
		else if("assignment".equals(message.phase))
		{
			System.out.println("assignment phase");
			agent_logic.phase = "assignment";
			agent_logic.playerFaction = message.faction;
			agent_logic.playerNumber = message.playerNumber;
			bot = new ExpertStatsAgent(agent_logic.playerNumber);
			agent_logic.setBot(bot);
			System.out.println("my faction is: " + agent_logic.playerFaction);
			if ("spy".equals(agent_logic.playerFaction))
			{
				System.out.println("I am a spy");
				//gui.otherSpyText.setText("Player " + message.otherSpy);
				agent_logic.showSpyInformation();
			}
			agent_logic.updatePlayerNumber(agent_logic.playerNumber);
			agent_logic.updateFactionInformation();
		}
		else if("gameOver".equals(message.phase))
		{
			//JFrame frame = new JFrame();
			//JOptionPane.showMessageDialog(frame, "Game over. " + message.winners + " win!");
			System.exit(1);
		}
		else
		{
			if (message.groupSelection != null && message.groupSelection.size() > 0)
				agent_logic.setCurrentlySelectedTeam(message.groupSelection);
			agent_logic.isMissionParticipant = false;
			agent_logic.setTurnRole("Waiting...");
			agent_logic.updateFactionInformation();
		}
		List<Integer> cur_selection = null;
		if (message.groupSelectionResult != null)
		{
			//String newText = gui.gameMessages.getText() + "\nGroup Selection Result: " + message.groupSelectionResult;
			if ("success".equals(message.groupSelectionResult))
			{
				String newText = ". Selected Members: ";
				for (Integer selected : message.groupSelection)
				{
					newText += "Player " + selected + "  ";
				}
				cur_selection = message.groupSelection; 
			}
			//gui.gameMessages.setText(newText + "\n");
		}

		if (message.currentLeader != 0)
		{
			agent_logic.setCurrentLeader(message.currentLeader);
		}
	}
	
	public void sendServerVote(String phase, String vote)
	{
		ClientSendMessage message = new ClientSendMessage();
		message.playerId = agent_logic.playerNumber;
		message.messageType = phase;
		message.message = vote;
		try
		{
			out.reset();
			out.writeObject(message);
			out.flush();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

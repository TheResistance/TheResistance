/*
Created by: Britton Horn
Group: The Resistance

Date: March 16, 2014
*/
package networking;

import gui.GameGui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.SwingUtilities;

import core.ClientSendMessage;
import core.ServerSendMessage;

public class Client implements Runnable
{

    public Socket socket;
    // client input. Will be passed along to the server using *in*
    public Scanner playerInput = new Scanner(System.in);
    public ObjectInputStream in;
    public ObjectOutputStream out;
    
    public GameGui gui;
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
		                        	System.out.println("UH OH. WRONG MESSAGE TYPE");
		                        }
                    		}
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
		System.out.println("message received: " + message.phase);
		if (gui == null)
			System.out.println("gui is null");
		if (message == null)
			System.out.println("message is null");
		gui.isLeader = (message.currentLeader == gui.playerNumber) ? true : false;
		if("groupSelection".equals(message.phase) && message.playerTurn == gui.playerNumber)
		{
			gui.isMissionParticipant = false;
			gui.setTurnRole("You are the mission leader. Select a group.");
			gui.phase = "groupSelection";
			System.out.println("group size: " + message.groupSize);
			gui.createTeamSelectionGui(message.groupSize);
		}
		else if("groupApproval".equals(message.phase) && message.playerTurn == gui.playerNumber)
		{
			gui.isMissionParticipant = false;
			gui.phase = "groupApproval";
			gui.setTurnRole("Vote to approve or reject the group proposal by Player " + message.currentLeader);
		}
		else if("missionVote".equals(message.phase) && message.playerTurn == gui.playerNumber)
		{
			gui.isMissionParticipant = true;
			gui.phase = "missionVote";
			gui.setTurnRole("You are in the mission. Vote for the mission to succeed or fail.");
		}
		else if("assignment".equals(message.phase))
		{
			System.out.println("assignment phase");
			gui.phase = "assignment";
			gui.playerFaction = message.faction;
			gui.playerNumber = message.playerTurn;
			System.out.println("my faction is: " + gui.playerFaction);
			if ("spy".equals(gui.playerFaction))
			{
				System.out.println("I am a spy");
				gui.otherSpyText.setText("Player " + message.otherSpy);
				gui.showSpyInformation();
			}
			gui.updatePlayerNumber(gui.playerNumber);
			gui.updateFactionInformation();
		}
		else
		{
			gui.isMissionParticipant = false;
			gui.setTurnRole("Waiting...");
			gui.updateFactionInformation();
		}
		if (message.groupSelectionResult != null)
		{
			String newText = gui.gameMessages.getText() + "\nGroup Selection Result: " + message.groupSelectionResult;
			if ("success".equals(message.groupSelectionResult))
			{
				newText += ". Selected Members: ";
				for (Integer selected : message.groupSelection)
				{
					newText += "Player " + selected + "  ";
				}
			}
			gui.gameMessages.setText(newText);
		}
		if (message.missionResult != null)
		{
			String newText = gui.gameMessages.getText() + "\nMission Vote Result: " + message.missionResult;
			if ("fail".equals(message.groupSelectionResult))
			{
				newText += ". Number of fail votes: " + message.missionFailVotes;
			}
			gui.gameMessages.setText(newText);
		}
	}
	
	public void sendServerVote(String phase, String vote)
	{
		ClientSendMessage message = new ClientSendMessage();
		message.playerId = gui.playerNumber;
		message.messageType = phase;
		message.message = vote;
		try
		{
			out.writeObject(message);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

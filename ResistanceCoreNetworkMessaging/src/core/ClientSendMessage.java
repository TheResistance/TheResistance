package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 27, 2014
*/

public class ClientSendMessage implements Serializable
{
	public Integer playerId = new Integer(0);
	// groupSelection, groupApproval, missionVote
	public String messageType;
	public String message;
	public List<Integer> groupSelection = new ArrayList<Integer>();;
	
	public void printMessage()
	{
		System.out.println("================================");
		System.out.println("playerId: " + playerId);
		System.out.println("messageType: " + messageType);
		System.out.println("message: " + message);
		for (Integer selection : groupSelection)
		{
			System.out.println("selection: " + selection);
		}
		System.out.println("++++++++++++++++++++++++++++++++");
	}
}

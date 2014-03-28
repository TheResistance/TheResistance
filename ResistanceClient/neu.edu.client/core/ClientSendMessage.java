package core;

import java.io.Serializable;
import java.util.List;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 27, 2014
*/

public class ClientSendMessage implements Serializable
{
	public int playerId;
	// groupSelection, groupApproval, missionVote
	public String messageType;
	public String message;
	public List<Integer> groupSelection;
}

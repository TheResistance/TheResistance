package core;

import java.io.Serializable;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 27, 2014
*/
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ServerSendMessage implements Serializable
{
	public int playerTurn;
	public List<Integer> groupSelection = new ArrayList<Integer>();
	public String groupSelectionResult;
	public String missionResult;
	public int missionFailVotes;
	public Hashtable<Integer,String> playerVotes;
	public boolean gameOver;
	public String phase;
	public String message;
}

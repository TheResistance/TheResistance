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
	public Integer playerTurn = new Integer(0);
	public List<Integer> groupSelection = new ArrayList<Integer>();
	public String groupSelectionResult;
	public String missionResult;
	public Integer missionFailVotes = new Integer(0);
	public Hashtable<Integer,String> playerVotes = new Hashtable<Integer,String>();
	public Boolean gameOver = new Boolean(false);
	public String phase;
	public String message;
	public Integer missionNumber = new Integer(0);
	public Integer groupSize = new Integer(0);
	public Integer currentLeader = new Integer(0);
	public String faction;
	public Integer otherSpy = new Integer(0);
	public Integer playerNumber = new Integer(0);
	public String winners;
	
	public void printMessage()
	{
		System.out.println("==================================");
		System.out.println("playerTurn: " + playerTurn);
		System.out.println("groupSelectionResult: " + groupSelectionResult);
		System.out.println("missionResult: " + missionResult);
		System.out.println("missionFailVotes: " + missionFailVotes);
		System.out.println("gameOver: " + gameOver);
		System.out.println("phase: " + phase);
		System.out.println("message: " + message);
		System.out.println("missionNumber: " + missionNumber);
		System.out.println("groupSize: " + groupSize);
		System.out.println("currentLeader: " + currentLeader);
		System.out.println("faction: " + faction);
		System.out.println("otherSpy: " + otherSpy);
		System.out.println("playerNumber: " + playerNumber);
		System.out.println("Winners: " + winners);
		
		for (Integer selection : groupSelection)
		{
			System.out.println("selection: " + selection);
		}
		for (Integer key : playerVotes.keySet())
		{
			System.out.println("vote " + key + ": " + playerVotes.get(key));
		}
		System.out.println("++++++++++++++++++++++++++++++++++");
	}
}

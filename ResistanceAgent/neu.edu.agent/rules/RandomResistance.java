package rules; 

import java.util.Collections;
import java.util.Hashtable;
import java.util.List; 
import java.util.ArrayList; 

import core.ClientSendMessage;
import core.ServerSendMessage;
/**
 * Write a description of class RandomSpy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RandomResistance implements Bot
{
    int number; 
    boolean spy; 
    public RandomResistance(int number) {
        this.number = number; 
        spy = true; 
    }
    public List<Integer> select(int missionSize) {
        List<Integer> mission = new ArrayList<Integer>(); 
        while (missionSize > 0) {
            int player = (int)(Math.random()*5+1); 
            if (!mission.contains(player)) {
                mission.add(player); 
                missionSize--; 
            }
        }
        return mission;
    }
    public void setLeader(int leader) {}
    public int getId() {
        return number; 
    }
    public boolean vote(List<Integer> team) {
        return Math.random() > .5 ? true : false; 
    }
    public boolean sabotage() {
        return false; 
    }
    public void onMissionComplete(List<Integer> team, int failVotes) {
        return; 
    }
 public void getMessage(ServerSendMessage msg) {
        
    }
    public ClientSendMessage sendMessage(boolean isAccusal) {
    	ClientSendMessage message = new ClientSendMessage();
    	message.playerId = number;
    	message.messageType = "communication";
    	message.message = isAccusal ? "accuses" : "suggests";
    	
    	List<Integer> otherPlayers = new ArrayList<Integer>();
    	for (int i = 1; i <= 5; i++)
    	{
    		if (i != number)
    			otherPlayers.add(i);
    	}
    	Collections.shuffle(otherPlayers);
    	List<Integer> suggestedPlayers = new ArrayList<Integer>();
    	suggestedPlayers.add(otherPlayers.get(0));
    	message.groupSelection = suggestedPlayers;
    	System.out.println("random res " + number + " " + message.message + " " + suggestedPlayers.toString());
        return new ClientSendMessage();
    }
    
}
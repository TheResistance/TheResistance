package rules;
import java.util.Hashtable;
import java.util.List; 

import core.ClientSendMessage;
import core.ServerSendMessage;
 

/**
 * Write a description of interface Bot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface Bot
{
    public List<Integer> select(int missionSize); 
    public void setLeader(int leader);
    public int getId();
    public boolean vote(List<Integer> team); 
    public boolean sabotage(); 
    public void onMissionComplete(List<Integer> team, int missionFailVotes); 
    public void getMessage(ServerSendMessage message);
    public ClientSendMessage sendMessage(boolean isAccusal); 
    
}

package rules;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import core.ClientSendMessage;
import core.ServerSendMessage;
public class ExpertStatsAgent implements Bot{
    private HashMap<Integer, PlayerInfo> playerInfos = new HashMap<Integer, PlayerInfo>(); 
    int self; 
    int voteNo = 0; 
    private int leader; 
    private double threshold = .6;
    
    
    public ExpertStatsAgent(int self) {
        System.out.println(self); 
        this.self = self;  
        playerInfos.put(0,null); 
        for (int i = 1; i <= 5; i++) {
            playerInfos.put(i,new PlayerInfo(i,i==self));
        }
    }
    public int getId() {
        return self; 
    }
    public void setLeader(int leader) {
        this.leader = leader;
    }
    /* 
     * Rule based system:
     * I am the safest person. Pick me first; 
     * Next safest is members who have never been on a failed mission before
     * Next is members who have been on failed missions (they can't be trusted!)
     */
    public List<Integer> select(int missionSize) {
        List<Integer> mission = new ArrayList<Integer>(); 
        System.out.println("adding " + self); 
        mission.add(self); 
        missionSize--; 
        int counter = 1; 
        // sort the hashtable
        ArrayList<Map.Entry<?, PlayerInfo>> sortedInfos = new ArrayList(playerInfos.entrySet());
        Collections.sort(sortedInfos, new Comparator<Map.Entry<?, PlayerInfo>>() {
        	public int compare(Map.Entry<?, PlayerInfo> o1, Map.Entry<?, PlayerInfo> o2) {
        		return o1.getValue().compareTo(o2.getValue());
        	}
        });
        
        System.out.println("sorted probs for player " + self + ": " + sortedInfos.toString());
        
        while (missionSize > 0) {
            int id = sortedInfos.get(counter).getValue().getId(); 
            mission.add(id);
            missionSize--;
            counter++ ;
            if (counter > 5) {
                counter = 1; 
            }
        }
        return mission;         
    }
    public boolean vote(List<Integer> team) {
        for (Integer player : team) {
        	if (playerInfos.get(player).resistanceChance() < threshold)
        		return false;
        }
        return true; 
    }
    public boolean sabotage() {
        //Resistance cannot sabotage a mission
        return false; 
    }
    public void onMissionComplete(List<Integer> team, int failVotes) {
        System.out.println(team); 
        if (failVotes == 0)
        {
        	return;
        }

        boolean self_c = false; 
        if (team.contains(self)) {
            self_c = true;
          //  team.remove((Integer) self); 
        }
        //playerInfos.get(self).setResistance(); 

        //System.out.println(playerInfos); 
        if (failVotes == 2 && team.size() == 2) {
            playerInfos.get(team.get(0)).setSpy(); 
            playerInfos.get(team.get(1)).setSpy(); 
            System.out.println("Agent: " + self + " is ABSOLUTELY sure " + team + " is a spy"); 
        }

        for ( Integer player : team) {
        	if (playerInfos.get(player).getId() != self)
        	{
        		playerInfos.get(player).factualProbability *= .8;
        	}
        }
        if (!team.contains(leader)) {
            System.out.println("Suspicious activity by " + leader); 
            playerInfos.get(leader).factualProbability *= .9;
        }
        if (team.contains(self) && failVotes == team.size() - 1) {
            for ( Integer player : team) {
            	if (playerInfos.get(player).getId() != self)
            	{
                    playerInfos.get(player).setSpy();
                    System.out.println("Agent: " + self + " is absolutely sure " + playerInfos.get(player).getId() + " is a spy"); 
            	}
            }
        }
       
        if (self_c) {
          //  team.add((Integer) self); 
        }
                    
      
    }
    public void getMessage(ServerSendMessage msgs) {
        try {
            int player = msgs.playerNumber; 
            String msg = msgs.message; 
            List<Integer> other_players = msgs.groupSelection;
            
            if (player != self)
            {
            	for (Integer other_player : other_players)
            	{
            		if (other_player != self)
            		{			           
			            //System.out.println(X + " " +Y); 
 
			            if (msg.equals("accusal")) 
			            {
			            	if (playerInfos.get(player).getProbability() > threshold)
			            	{
			            		playerInfos.get(other_player).updateResistanceProbabilityFromCommunication(.8);
			            	}
			            	else if (playerInfos.get(player).getProbability() < 1-threshold)
			            	{
			            		playerInfos.get(other_player).updateResistanceProbabilityFromCommunication(1.2);
			            	}
			            	else
			            	{
			            		
			            	}
			            }
			            if (msg.equals("suggestions")) 
			            {			                
			            	if (playerInfos.get(player).getProbability() > threshold)
			            	{
			            		playerInfos.get(other_player).updateResistanceProbabilityFromCommunication(1.2);
			            	}
			            	else if (playerInfos.get(player).getProbability() < 1-threshold)
			            	{
			            		playerInfos.get(other_player).updateResistanceProbabilityFromCommunication(.8);
			            	}
			            	else
			            	{
			            		
			            	}
			            }
			            //X = neurosis.getX();
			            //Y = neurosis.getY(); 
			          
			            //System.out.println(X + " " +Y); 
            		}
            	}
	        }
        } 
        catch (NumberFormatException e)
        {
        	e.printStackTrace();
        }

    }
    public ClientSendMessage sendMessage(boolean isAccusal) 
    {
    	ClientSendMessage message = new ClientSendMessage();
    	List<Integer> otherPlayers = new ArrayList<Integer>();
    	
        for (Map.Entry<Integer, PlayerInfo> pEntry: playerInfos.entrySet()) 
        {
        	PlayerInfo p = pEntry.getValue();
            if (p != null) 
            {
            	if (p.isSpy() && isAccusal)
            	{
            		otherPlayers.add(p.getId());
            	}
            	if (!isAccusal && p.isResistance())
            	{
            		otherPlayers.add(p.getId());
            	}
            }
            
        }
        if (otherPlayers.size() > 0)
        {
        	message.playerId = self;
        	message.messageType = "communication";
        	message.message = isAccusal ? "accuses" : "suggests";
        	message.groupSelection = otherPlayers;
        }
        return message; 
    }
    

        
}


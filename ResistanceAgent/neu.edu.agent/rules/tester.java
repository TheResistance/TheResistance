package rules; 

import java.util.*;

import core.ClientSendMessage;
import core.ServerSendMessage;
/**
 * Write a description of class tester here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class tester
{
    public static void main(String args[]) {

        int rounds = 0; 
        int lost = 0; 
        int leader = 1; 
        int votes = 0; 
        boolean result; 
        int lostTally = 0; 
        
        int [] missionSize = {2,3,2,3,3};
        List<Integer> position = new ArrayList<Integer>(); 
        for (int i = 1; i <= 5; i++) {
            position.add(i); 
        }
        int lostGames = 0;
        for (int count = 0; count < 5000; count++) {
            Collections.shuffle(position); 
            List<Bot> bots = new ArrayList<Bot>();
            bots.add(null); 
            boolean random = false; 
            if (!random) {
                bots.add(new ExpertStatsAgent(position.get(0)));
                bots.add(new ExpertStatsAgent(position.get(1)));
                bots.add(new ExpertStatsAgent(position.get(2)));
            } else {
                bots.add(new RandomResistance(position.get(0))); 
                bots.add(new RandomResistance(position.get(1))); 
                bots.add(new RandomResistance(position.get(2))); 
            }
            bots.add(new RandomSpy(position.get(3))); 
            bots.add(new RandomSpy(position.get(4))); 
              
            int i = 0; 

            lostTally = 0;
            int leaderChanges = 0;
            while (i < 5) {
                System.out.println("Spies: " + position.get(3) + " " + position.get(4)); 
                result = true; 
                List<Integer> team = bots.get(leader).select(missionSize[i]); 
                votes = 0; 
                for (int j = 1; j <= 5; j++) {
                    if (j!=leader && bots.get(j).vote(team)) {
                        votes++; 
                    }
                    bots.get(j).setLeader(bots.get(leader).getId());
                }
                
                leader++; 
                if (leader > 5) {
                    leader = 1; 
                }
                if (votes <= 2) {
                	leaderChanges++;
                	if (leaderChanges < 5)
                	{
                		continue;
                	}
                }
                lost = 0;
                for (int k = 1; k <= 5; k++ ) {
                    int id = bots.get(k).getId(); 
                    if(team.contains(id)) {
                        boolean vote = bots.get(k).sabotage(); 
                        if (vote) {
                        	lost++;
                            if (result) {  
                                
                                System.out.println("lost a round"); 
                            }
                            result = false;
                        }
                    }
                }
                if (result) {
                    System.out.println("won a round"); 
                }
                else
                	lostTally++;
                
                for (int k = 1; k <= 5; k++ ) {
                    bots.get(k).onMissionComplete(team,lost); 
                    //message += bots.get(k).sendMessage();
                }
                for (int k = 1; k <= 5; k++)
                {
                    ClientSendMessage response = bots.get(k).sendMessage(true);
                    ServerSendMessage firstMessage = new ServerSendMessage();
                    firstMessage.playerNumber = response.playerId;
                    firstMessage.phase = response.messageType;
                    firstMessage.message = response.message;
                    firstMessage.groupSelection = response.groupSelection;

                    response = bots.get(k).sendMessage(false);
                    ServerSendMessage secondMessage = new ServerSendMessage();
                    secondMessage.playerNumber = response.playerId;
                    secondMessage.phase = response.messageType;
                    secondMessage.message = response.message;
                    secondMessage.groupSelection = response.groupSelection;
                    
                	for (int j = 1; j <= 5; j++)
                	{
                		if (firstMessage.groupSelection.size() > 0)
                		{
                			bots.get(j).getMessage(firstMessage);
                		}
                		if (secondMessage.groupSelection.size() > 0)
                		{
                			bots.get(j).getMessage(secondMessage);
                		}
                	}
                }
                i++; 
            }
            rounds++;
            if (lostTally >= 3)
            	lostGames++;
            
        }
        System.out.println("Players " + rounds + " won: % " + (100*(rounds-lostGames))/((double) rounds)); 
    }
}

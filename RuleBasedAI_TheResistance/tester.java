import java.util.*;
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
            
        for (int count = 0; count < 10000; count++) {
            Collections.shuffle(position); 
            List<Bot> bots = new ArrayList<Bot>();
            bots.add(null); 
            boolean random = false; 
            if (!random) {
                bots.add(new ResistanceAgent(position.get(0)));
                bots.add(new ResistanceAgent(position.get(1)));
                bots.add(new ResistanceAgent(position.get(2)));
            } else {
                bots.add(new RandomResistance(position.get(0))); 
                bots.add(new RandomResistance(position.get(1))); 
                bots.add(new RandomResistance(position.get(2))); 
            }
            bots.add(new RandomSpy(position.get(3))); 
            bots.add(new RandomSpy(position.get(4))); 
                
            int i = 0; 
            lost = 0; 
            while (i < 5) {
                result = true; 
                List<Integer> team = bots.get(leader).select(missionSize[i%5]); 
                votes = 0; 
                for (int j = 1; j <= 5; j++) {
                    if (j!=leader && bots.get(j).vote(team)) {
                        votes++; 
                    }
                }
                leader++; 
                if (leader >= 5) {
                    leader = 1; 
                }
                if (votes < 2) {
                    continue; 
                }
                List<Boolean> voters = new ArrayList<Boolean>(); 
                for (Integer player : team) {
                    boolean vote = bots.get(player).sabotage(); 
                    voters.add(vote); 
                    if (vote) {
                        if (result) {
                            lost++;
                        }
                        result = false; 
                    }
                }    
                Collections.shuffle(voters); 
                for (int k = 1; k <= 5; k++ ) {
                    bots.get(k).onMissionComplete(team,voters,result); 
                }
                if (lost >= 3) {
                    lostTally++;
                    break;
                } 
                i++; 
                 
            }
            rounds++;
        }
        System.out.println("Players " + rounds + " won: % " + (100*(rounds-lostTally))/((double) rounds)); 
    }
}

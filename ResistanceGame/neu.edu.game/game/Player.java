package game; 

import java.util.Scanner;
import java.lang.RuntimeException;
/**
 *  Human Player Class
 * 
 * @Kevin
 * @1
 */


public class Player
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class PLayer
     */
    boolean spy; 
    int player_id; 
    Scanner scan;
    
    public Player(int player_id,boolean spy )
    {
        this.spy = spy;
        this.player_id = player_id; 
        scan = new Scanner(System.in);
    }
    private boolean getKeyboard() {
        char input; 
        while (scan.hasNext()) {
            input = scan.next().toLowerCase().charAt(0); 
            if (input == 'n')
                return false; 
            if(input == 'y')
                return true;
            System.out.println("(y/n)"); 
        }
        throw new RuntimeException("Keyboard scanner depleted"); 
    }
    private SetState acceptMissionVote() {
        System.out.print("Do you allow the mission to proceed (y/n)"); 
        boolean pick = getKeyboard(); 
        if (!spy && !pick)
           throw new RuntimeException("Resistance must not sabotage the mission");     
        return pick ? SetState.VOTE_YES_ON_MISSION : SetState.VOTE_NO_ON_MISSION; 
    }
    private SetState acceptLeaderVote(int leader) {
        System.out.print("Do you accept player " + leader + " as your leader (y/n)" );
        return getKeyboard() ? SetState.VOTE_YES_ON_LEADER : SetState.VOTE_NO_ON_LEADER; 
    }
    private SetState pickMission(int[] mission, int turn) {
        int missionSize = 0; 
        int input; 
        System.out.print("Pick " + (mission.length ) + " players for the mission: ");
        while (missionSize < mission.length) {
            try {
                 mission[missionSize++] = Integer.parseInt(scan.next()); 
            } catch (NumberFormatException e) {
                missionSize--; 
                System.out.println("Player's are entered by integer id"); 
            }
        }
        return SetState.SET_MISSION; 
    }
    SetState play(GameState g, int leader, int[] mission, int turn) {
        switch(g) {
            case WAIT_ON_MISSION:
                System.out.println("Waiting on mission vote"); 
                return SetState.NO_CHANGE;
            case WAIT_ON_LEADER:
                System.out.println("Waiting on mission vote");
                 return SetState.NO_CHANGE;
            case WAIT_ON_PICK:
                System.out.println("Waiting on mission to be picked"); 
                return SetState.NO_CHANGE; 
            case VOTE_ON_LEADER:
                return acceptLeaderVote(leader); 
            case VOTE_ON_MISSION:
                return acceptMissionVote();
            case PICK_MISSION:
                return pickMission(mission,turn); 
            }
            throw new RuntimeException("Invalid GameState reached");
    }
    public String toString() {
        return spy ? "Player #: " + player_id + " is a spy" : "Player #: " + player_id + " is resistance";
    }
}

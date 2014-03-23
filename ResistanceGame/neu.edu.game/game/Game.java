package game;
/**
 * Simple game engine for the resistance
 * 
 * @Kevin 
 * @1
 */
public class Game
{
    private int players;
    private int turn; 

    private int leader; 
    
    private int spiesWon; 
    private int resWon; 
    
    private int[] votes; 
    
    private int[] mission; 
    
    private int voteYes = 0;
    private int voteNo = 0; 
    
    private boolean leaderVote; 
    private boolean missionVote; 

    public Game(int players)
    {
        this.players = players;
        votes = new int[players];
        
        mission = new int[players/2+1]; 
        leaderVote = true; 
        leader = 0; 
    }
    private void endLeaderVote(boolean accept) {
        for (int i = 0; i < players; i++) {
            votes[i] = 0;
        }
        voteNo = 0;
        voteYes = 0; 
        if (accept) {
            leaderVote = false; 
            missionVote = false; 
        }  else {
            leader = (leader + 1) % players;
        }
    }
    void proposeMission(int leader, int [] mission) {
        if (missionVote == false && leaderVote == false && leader == this.leader) {
            missionVote = true; 
            this.mission = mission; 
            this.leader= (leader+1)%players;
            /*mission = new int[this.mission.length];
            for (int i = 0; i < mission.length; i ++ ) {
                System.out.println(mission[i]); 
                this.mission[i] = mission[i];
            }*/
        }
    }
    int acceptLeader(int player, boolean vote) {
        if (leaderVote && votes[player] == 0) {
            votes[player] = 1;
            if (vote)
                voteYes++;
            else 
                voteNo++;
        }
        if (voteYes > players/2)
        {
            endLeaderVote(true);
            return 1;
        }
        if (voteNo > players/2) 
        {
            endLeaderVote(false);
            return -1; 
        }
        return 0;
    }
    int playMission(int player, boolean sucess) {
        if (!missionVote) 
            return 0; 
        for (int i = 0; i <= mission.length-turn%2; i++) {
            if (i == mission.length-turn%2)
                return 0;
            if (mission[i] == player) {
                mission[i] = -1*player; 
                break;
            }
         }
         if (!sucess) {
             voteNo++;
         } else {
             voteYes++;
         }
         if (mission.length-turn%2 == voteYes) {
            endMission(true);
            return 1;
         }
         if (voteNo > 0) {
            endMission(false);
            return -1;
         }
         return 0;
    }
    private void endMission(boolean success) {
        voteNo = 0; 
        voteYes = 0; 
        if (success) {
            resWon++;
        } else {
            spiesWon++;
        }
        turn++;
        leaderVote = true; 
        missionVote = false; 
    }
    void broadcastMisison() {
        if(missionVote) {
            for (int i = 0; i < mission.length-turn%2; i++) {
                System.out.print(mission[i] + " " ); 
            }
            System.out.println();
        }
    }
    public String toString() {
        String str = "Round #" + turn + "\nLeader #" + leader + "\nVotes: " + (voteYes + voteNo) + "\n";
        if (leaderVote) {
            str += ("Voting on a leader!\n");
        }
        else if (missionVote) {
            str += ("Voting on a mission\n");
             for (int i = 0; i < mission.length-turn%2; i++) {
                str += Math.abs(mission[i]) + " " ; 
            }
            str += "\n";
        } else {
            str += "Waiting for a mission\n";
        }
        return str;
    }
    public int getTurn() 
    {
        return turn; 
    }
    
}

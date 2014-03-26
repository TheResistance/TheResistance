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
    private int mission_size; 
    private int voteYes = 0;
    private int voteNo = 0; 
    
    private boolean leaderVote = false; 
    private boolean missionVote = false; 
    private boolean leaderPicked = false; 

    private int[] spies; 
    
    public Game(int players)
    {
        this.players = players;
        votes = new int[players];
        mission = new int[players/2+1];
        mission_size = players/2 + 1; 
        leaderVote = true;
        leader = 0; 
        spies = new int[players]; 
        int num_spies = spies.length/3+1; 
        int rnd; 
        while (num_spies > 0) {
            rnd= (int)(Math.random() * spies.length); 
            if (spies[rnd] != 1) {
                spies[rnd] =1; 
                num_spies--;
            }
        }
    }
    private void endLeaderVote(boolean accept) {
        for (int i = 0; i < players; i++) {
            votes[i] = 0;
        }
        voteNo = 0;
        voteYes = 0; 
        if (accept) {
            leaderPicked = true;
            leaderVote = false; 
            missionVote = false; 
        }  else {
            leader = (leader + 1) % players;
        }
    }
    private void proposeMission(int leader, int [] mission) {
        if (missionVote == false && leaderVote == false && leader == this.leader) {
            for (int i = 0; i < mission.length; i++) {
                if (mission[i] < 0 || mission[i] > players) {
                    System.out.println("Invalid Mission");
                    return;
                }
                if (mission.length > players/2 + (turn+1)%2) {
                    System.out.println("Only need " + (players/2 + (turn+1)%2) + "  players");
                    return;
                }
                //Check for duplicates
                for (int j = 0; j < mission.length; j++) {
                    if (i!=j && mission[i] == mission[j]) {
                        System.out.println("No duplicate players allowed on the mission"); 
                        return;
                    }
                }
            }
            missionVote = true; 
            this.mission = mission; 
            this.leader= (leader+1)%players;
            for (int i = 0; i < mission.length; i++) {
                System.out.println(mission[i]); 
            }
            /*mission = new int[this.mission.length];
            for (int i = 0; i < mission.length; i ++ ) {
                System.out.println(mission[i]); 
                this.mission[i] = mission[i];
            }*/
        }
    }
    private int acceptLeader(int player, boolean vote) {
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
    private int playMission(int player, boolean sucess) {
        if (!missionVote) 
            return 0; 
        for (int i = 0; i <= mission.length-(turn+1)%2; i++) {
            //if (i == mission.length-turn%2)
            //    return 0;
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
         if (mission.length-(turn+1)%2 == voteYes) {
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
            System.out.println("Resistance takes the round (" + resWon + "," + spiesWon + ")");
        } else {
            spiesWon++;
            System.out.println("Spies take the round (" + spiesWon + "," + resWon + ")");
        }
        turn++;
        leaderVote = true; 
        leaderPicked = false; 
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
    public int getLeader() 
    {
        return leader; 
    }
    public int getTurn() {
        return turn;
    }
    private boolean inMission(int player) {
        if (missionVote) {
            for (int i = 0; i < mission.length; i++) {
                if (mission[i] == player)
                    return true;
            }
        }
        return false; 
    }
    public GameState getGameState(int player) {
        if (leaderVote) {
            if ( votes[player] == 0 )
                return GameState.VOTE_ON_LEADER; 
            return GameState.WAIT_ON_LEADER; 
        }
        if (missionVote) {
            if(inMission(player)) 
                return GameState.VOTE_ON_MISSION; 
            return GameState.WAIT_ON_MISSION; 
        }
        if (!missionVote && !leaderVote && leaderPicked) {
            if (player == leader)
                return GameState.PICK_MISSION;
            return GameState.WAIT_ON_PICK;
        }
        return GameState.STALL; 
    }
    public int[] getPlayers() {
        return spies;
    }
    public boolean amISpy(int player) {
        return spies[player] == 1 ? true : false; 
    }
    public int getMissionLength() {
        return mission_size-(turn%2); 
    }
    public void setGameState(int player, SetState s, int[] mission) {
        switch (s) {
            case VOTE_NO_ON_LEADER:
                acceptLeader(player,false);
                break;
            case VOTE_YES_ON_LEADER:
                acceptLeader(player,true); 
                break;
            case VOTE_YES_ON_MISSION:
                playMission(player,true); 
                break;
            case VOTE_NO_ON_MISSION:
                playMission(player,false); 
                break;
            case SET_MISSION:
                proposeMission(player,mission); 
            default:
                break;
        }
    }
    public boolean gameOver() {
        return (resWon + spiesWon) >= players; 
    }
    public void Winner() {
        String str = "The winners are "; 
        if (gameOver()) {
            str += (resWon > spiesWon) ? "the resistance" : "the spies"; 
        }
        System.out.println(str); 
    }
}

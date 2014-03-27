  
public class tester
{
   public static void main(String args[]) {
       Game g = new Game(5);

       Player[] players = new Player[g.getPlayers().length];
       for (int i = 0; i < players.length; i++) {
            boolean spy = g.amISpy(i); 
            players[i] = new Player(i, spy); 
        }
        g.startGame(); 
        while (true) {
            for (int i = 0; i < players.length; i++) {
                int leader = g.getLeader(); 
                int missionLength = g.getMissionLength(); 
                GameState game_state = g.getGameState(i); 

                System.out.println(game_state); 
                System.out.println("Playing as " + players[i]); 
                int [] mission = players[i].createEmptyMission(missionLength); 
                SetState s = players[i].play(game_state,leader,mission); 
                g.setGameState(i,s,mission);     
            }
            if (g.gameOver()) {
                    break; 
            }
        }
        g.Winner();
    }
}

package game;


public class tester
{
   public static void main(String args[]) {
       Game g = new Game(5);
       int[] players_i = g.getPlayers();
       Player[] players = new Player[players_i.length];
       for (int i = 0; i < players_i.length; i++) {
            players[i] = new Player(i, players_i[i] == 1 ? true : false); 
        }
        while (!g.gameOver()) {
            for (int i = 0; i < players.length; i++) {
                int leader = g.getLeader(); 
                int[] mission = g.getEmptyMission(); 
                GameState game_state = g.getGameState(i); 
                System.out.println(game_state); 
                System.out.println("Playing as " + players[i]); 
                SetState s = players[i].play(game_state,leader,mission); 
                g.setGameState(i,s,mission); 
            }
        }
                
    }
}

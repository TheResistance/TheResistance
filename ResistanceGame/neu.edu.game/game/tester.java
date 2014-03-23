package game;

public class tester
{
   public static void main(String args[]) {
       Game g = new Game(5);
       int [] mission = {1,2,3}; 
       System.out.println(g);
       g.acceptLeader(1,true);
       g.acceptLeader(2,true);
       g.acceptLeader(3,true);
       System.out.println(g);
       g.proposeMission(0, mission); 
       System.out.println(g);
       g.playMission(1,true);
       g.playMission(2,true);
       System.out.println(g);
       g.playMission(3,true);
       System.out.println(g);
    }
    
}

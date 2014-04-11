package rules;
import java.util.List; 
import java.util.ArrayList; 
import java.util.Collections;
import java.util.Scanner; 
public class ExpertStatsAgent implements Bot{
    private List<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>(6);
    int optimistic; 
    int pessimistic; 
    int self; 
    int voteNo = 0; 
    int spyCount = 0; 
    private String denial = ""; 
    private MentalModel neurosis;
    private int leader; 
    ExpertStatsAgent(int self) {
        neurosis = new MentalModel(4,.4);
        System.out.println(self); 
        this.self = self; 
        optimistic = 5; 
        pessimistic = 0; 
        playerInfos.add(null); 
        for (int i = 1; i <= 5; i++) {
            playerInfos.add(new PlayerInfo(i,i==self));
        }
    }
    public int getId() {
        return self; 
    }
    public void setLeader(int leader) {
        this.leader = leader;
    }
    private void pessimistic(List<PlayerInfo> playerInfos) {
        pessimistic = 0; 
        optimistic = 0; 
        for (PlayerInfo p : playerInfos) {
            if (p == null) {continue;}
            optimistic = p.noProofOfSpy() ? optimistic + 1 : optimistic; 
            pessimistic = p.maybeSpy() ? pessimistic +1 : pessimistic; 
        }
    }
    private void optimistic() {
        pessimistic--; 
        optimistic++; 
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
        if(optimistic > 1) {
            for (PlayerInfo p : playerInfos) {
                if (p == null){ continue; }
                if (p.noProofOfSpy() && !mission.contains(p.number) && missionSize > 0) {
                    mission.add(p.number); 
                    missionSize--; 
                }
            }
        }
        int counter = 1; 
        Collections.sort(playerInfos); 
        
        while (missionSize > 0) {
            int id = playerInfos.get(counter).getId(); 
            if ( mission.contains(id) ) {}
            else if (!playerInfos.get(id).maybeSpy()) {
                missionSize--; 
                mission.add(id); 
            } else if (mission.size() + pessimistic >= 5 ) {
                missionSize--; 
                mission.add(id);
            }
            counter++ ;
            if (counter > 5) {
                counter = 1; 
            }
        }
        return mission;         
    }
    public boolean vote(List<Integer> team) {
        for (Integer player : team) {
            if (voteNo < 4 && playerInfos.get(player).getProbability() > Math.random()) {
                return true; 
            }
            if (playerInfos.get(player).maybeSpy() && voteNo < 4) {
                voteNo++;
                return false; 
            }
           
        }
        voteNo = 0; 
        return true; 
    }
    public boolean sabotage() {
        //Resistance cannot sabotage a mission
        return false; 
    }
    public void onMissionComplete(List<Integer> team, List<Boolean> vote, boolean result) {
        System.out.println(team); 
        if (result) { 
            return; }
        int numSpys = 0;
        int spyNum = 0; 
        int votecount = 0; 
        boolean self_c = false; 
        if (team.contains(self)) {
            self_c = true;
          //  team.remove((Integer) self); 
        }
        //playerInfos.get(self).setResistance(); 

        for (Boolean v : vote) {
            if (v) { votecount++; }
        }
        //System.out.println(playerInfos); 
        if (votecount == 2 && team.size() == 2) {
            playerInfos.get(team.get(0)).setSpy(); 
            playerInfos.get(team.get(1)).setSpy(); 
            System.out.println("Agent: " + self + " is absolutely sure " + team + " is a spy"); 
            spyCount = 2; 
            pessimistic(playerInfos); 
        }

        for ( Integer player : team) {
            PlayerInfo p = playerInfos.get(player); 
            if (!p.isResistance() && !p.maybeSpy()) {
                spyNum = player; 
                numSpys++; 
                System.out.println("Agent: " + self + " infered " + player + " as probable spy");
                p.setPessimistic(); 
                pessimistic(playerInfos); 
            }
        }
        if (!team.contains(leader)) {
            System.out.println("Suspicious activity by " + leader); 
            PlayerInfo p = playerInfos.get(leader);
            p.setPessimistic(); 
            pessimistic(playerInfos); 
        }
        if (numSpys == 1) {
            System.out.println("Agent: " + self + " is absolutely sure " + spyNum + " is a spy"); 
            playerInfos.get(spyNum).setSpy(); 
            spyCount++; 
        }
       
        if (self_c) {
          //  team.add((Integer) self); 
        }
                    
      
    }
    public void getMessage(String msgs) {
        Scanner scanner = new Scanner(msgs);
        Scanner keyboard = new Scanner(System.in);
        while (scanner.hasNextLine()) {
        String[] tokens = scanner.nextLine().split(" ");  
        try {
            int player = Integer.parseInt(tokens[0]); 
            String msg = tokens[1]; 
            int other_player = Integer.parseInt(tokens[2]);
            double X = playerInfos.get(player).getProbability(); 
            double Y = playerInfos.get(other_player).getProbability(); 
           
            //System.out.println(X + " " +Y); 
            neurosis.setX(X);
            neurosis.setY(Y); 
            if (msg.equals("accusal")) {
                if (other_player  == self) 
                    denial += self + " denial " + player + "\n"; 
                if ( player == self || other_player == self) continue;
                  neurosis.accuses(); 
                  X = .5; 
                  Y = .5; 
                  playerInfos.get(player).reduce_suspect(X);
                  playerInfos.get(other_player).suspect(Y);
            }
            if (msg.equals("denial")) {
                neurosis.denials();
            }
            if (msg.equals("suggestions")) {
                neurosis.suggestions(); 
                X = 1; 
                Y = 1; 
                
                playerInfos.get(player).reduce_suspect(X);
                playerInfos.get(other_player).reduce_suspect(Y);
            }
            //X = neurosis.getX();
            //Y = neurosis.getY(); 
          
            //System.out.println(X + " " +Y); 
        } catch (NumberFormatException E){ }
        }

    }
    public String sendMessage() {
        String rt = ""; 
        for (PlayerInfo p : playerInfos) {
            if (p != null && p.isSpy()) {
                rt += self + " accusal " + p.getId() + "\n";
            }
        }
        rt += self + " suggestions " + self + "\n";
        //rt += denial; 
        denial = ""; 
        return rt; 
    }
    

        
}


import java.util.List; 
import java.util.ArrayList; 
class PlayerInfo { 
    int number; 
    boolean self; 
    private boolean resistance; 
    private boolean spy; 
    private boolean optimistic; 
    private boolean pessimistic; 
    PlayerInfo(int number,boolean self) {
        this.self = self; 
        this.number = number; 
        optimistic = true; 
        pessimistic = false; 
        resistance = false; 
        spy = false; 
        if(self) {
            resistance = true;
        }
    }
    
    public void setSpy() {
        pessimistic = true; 
        spy = true; 
    }
    public void setPessimistic() { 
        pessimistic = true; 
        optimistic = false; 
    }
    public boolean maybeSpy() {
        return pessimistic; 
    }
    public boolean noProofOfSpy() {
        return optimistic; 
    }
    public boolean isSpy() {
        return spy; 
    }
    public boolean  isResistance() {
        return resistance; 
    }
    
}
public class ResistanceAgent implements Bot{
    private List<PlayerInfo> playerInfos = new ArrayList<PlayerInfo>(6);
    int optimistic; 
    int pessimistic; 
    int self; 
    int voteNo = 0; 
    ResistanceAgent(int self) {
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
    private void pessimistic() {
        pessimistic++; 
        optimistic--; 
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
        while (missionSize > 0) {
            if ( mission.contains(counter) ) {}
            else if (!playerInfos.get(counter).maybeSpy()) {
                missionSize--; 
                mission.add(counter); 
            } else if (mission.size() + pessimistic >= 5 ) {
                missionSize--; 
                mission.add(counter);
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
    public void onMissionComplete(List<Integer> team, boolean result) {
        if (result) { return; }
        int numSpys = 0;
        int spyNum = 0; 
        for ( Integer player : team) {
            PlayerInfo p = playerInfos.get(player); 
            if (!p.isResistance() && !p.isSpy()) {
                spyNum = player; 
                numSpys++; 
                p.setPessimistic(); 
                pessimistic(); 
            }
        }
        if (numSpys == 1) {
            playerInfos.get(spyNum).setSpy(); 
        }
      
    }
        
}


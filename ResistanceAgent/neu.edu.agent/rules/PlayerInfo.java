package rules;
class PlayerInfo implements Comparable<PlayerInfo>{ 
    int number; 
    boolean self; 
    double probablity;
    private boolean resistance; 
    private boolean spy; 
    private boolean optimistic; 
    private boolean pessimistic; 
    
    public PlayerInfo(int number,boolean self) {
        
        probablity = .5; 
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
    public Integer getId() {
        return (Integer) number;
    }
    public void setSpy() {
        //probablity = 0.0; 
        pessimistic = true; 
        spy = true; 
    }
    public void setPessimistic() { 
        //suspect();
        pessimistic = true; 
        optimistic = false; 
    }
    public void setResistance() { 
        //probablity = 1.0f; 
        optimistic = true; 
        resistance = true; 
    }
    public void updateResistanceProbability(double value) {
        if (probablity == 1) return;
        probablity *= value; 
        if (probablity > 1)   probablity = .99; 
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
    public double getProbability() {
        return probablity;
    }
    public int compareTo(PlayerInfo p) {
        if (p == null) return 1;
        if (Math.abs(getProbability()-p.getProbability()) < 0.0001) return 0; 
        if (getProbability() < p.getProbability()) return 1;
        return -1;
    }
    public String toString() {
        return("Player: " + number + " " + getProbability()); 
    }
    
}

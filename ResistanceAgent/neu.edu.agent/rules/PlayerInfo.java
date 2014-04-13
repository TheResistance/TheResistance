package rules;
class PlayerInfo implements Comparable<PlayerInfo>{ 
    int number; 
    boolean self; 
    double probability;
    double factualProbability = 1.0;
    double successProbability = 0; 
    private boolean resistance; 
    private boolean spy;  
    
    public PlayerInfo(int number,boolean self) {
        
        probability = 1.0; 
        this.self = self; 
        this.number = number;  
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
        factualProbability = 0.0; 
        spy = true; 
    }
    public void setResistance() { 
        factualProbability = 1.0f;  
        resistance = true; 
    }
    public void updateSuccessProbability(double value) {
    	successProbability += value; 
    	if (successProbability > 1.0)
    		successProbability = 1; 
    }
    public void updateResistanceProbabilityFromCommunication(double value) {
        if (probability == 1.0) return;
        probability *= value; 
        if (probability > 1.0)   probability = .99; 
    }
    public double resistanceChance() {
    	if (isSpy())
    		return 0.0;
        return (probability + factualProbability)/2.0; 
    }
    public boolean isSpy() {
        return spy; 
    }
    public boolean  isResistance() {
        return resistance; 
    }
    public double getProbability() {
        return probability;
    }
    public int compareTo(PlayerInfo p) {
        if (p == null) return 1;
        if (Math.abs(resistanceChance()-p.resistanceChance()) < 0.0001) return 0; 
        if (resistanceChance() < p.resistanceChance()) return 1;
        return -1;
    }
    public String toString() {
        return("Player: " + number + " " + resistanceChance()); 
    }
    
}

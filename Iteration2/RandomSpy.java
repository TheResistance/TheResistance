import java.util.List; 
import java.util.ArrayList; 
/**
 * Write a description of class RandomSpy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RandomSpy implements Bot
{
    int number; 
    boolean spy; 
    public RandomSpy(int number) {
        this.number = number; 
        spy = true; 
    }
    public List<Integer> select(int missionSize) {
        List<Integer> mission = new ArrayList<Integer>(); 
        while (missionSize > 0) {
            int player = (int)(Math.random()*5+1); 
            if (!mission.contains(player)) {
                mission.add(player); 
                missionSize--; 
            }
        }
        return mission;
    }
    public int getId() {
        return number; 
    }
    public boolean vote(List<Integer> team) {
        return Math.random() > .5 ? true : false; 
    }
    public boolean sabotage() {
        return true; 
    }
    public void onMissionComplete(List<Integer> team, List<Boolean> vote, boolean result) {
        return; 
    }
    
}
import java.util.List; 
import java.util.ArrayList; 
/**
 * Write a description of interface Bot here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public interface Bot
{
    public List<Integer> select(int missionSize); 
    public int getId();
    public boolean vote(List<Integer> team); 
    public boolean sabotage(); 
    public void onMissionComplete(List<Integer> team, List<Boolean> vote, boolean result); 
    
}

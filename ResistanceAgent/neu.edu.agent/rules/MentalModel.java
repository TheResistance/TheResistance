package rules;
/**
 * A simple mental model for our neurotic agent
 * neuroticism from 1-10
 * @author kevin
 * @version 1
 */
public class MentalModel
{
	  double threshold = .7;
	  double factualReduction = .7;
	  double leaderReduction = .2;
	  double communicationReduction = .6;
	  double suggestionThreshold = .95;
	  double successThreshold = 1-threshold;
	  boolean useSuccess = true; 
	  
    public MentalModel(int neuroticism) {
    	double offset = (5-neuroticism)/10.0;
    	factualReduction += factualReduction*offset; 
    	leaderReduction += leaderReduction*offset; 
    	suggestionThreshold += suggestionThreshold*offset;
    	successThreshold = 1-threshold; 
    	if (neuroticism < 5)
    		useSuccess = false; 
    	
    	factualReduction = factualReduction > 1 ? 1 : factualReduction; 
    	leaderReduction = leaderReduction > 1 ? 1 : leaderReduction; 
    	suggestionThreshold = suggestionThreshold > 1 ? 1 : suggestionThreshold; 
    	successThreshold = successThreshold > 1 ? 1 : successThreshold; 
    	
    	factualReduction = factualReduction < 0 ? 0 : factualReduction; 
    	leaderReduction = leaderReduction < 0 ? 0 : leaderReduction; 
    	suggestionThreshold = suggestionThreshold < 0 ? 0 : suggestionThreshold; 
    	successThreshold = successThreshold < 0 ? 0 : successThreshold; 
    	
    }
    public double getThreshold() {
		return threshold;
	}
	public double getFactualReduction() {
		return factualReduction;
	}
	public double getLeaderReduction() {
		return leaderReduction;
	}
	public double getCommunicationReduction() {
		return communicationReduction;
	}
	public double getSuggestionThreshold() {
		return suggestionThreshold;
	}
	public double getSuccessThreshold() {
		return successThreshold;
	}
	public boolean isUseSuccess() {
		return useSuccess;
	}
}
    
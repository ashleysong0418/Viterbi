package cs10;


//By Jiwhan In and Ashley Song, HMM/Viterbi, PS5, CS10 19S

//this file creates the data class for the tags and words, keeping track of the appearances of each word for each tag and the following transitions
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class dataClass {
	public int appearance;
	public HashMap<String, Integer> tagged;
	public HashMap<String, Integer> nextTag;
	public Integer unseen = -100;
	
	
	public dataClass() {
		appearance = 0;
		tagged = new HashMap<String, Integer>();
		nextTag = new HashMap<String, Integer>();
		
	}
	
	public HashMap<String, Integer> getWords(){
		return tagged;
	}
	
	public HashMap<String, Integer> getStates(){
		return nextTag;
	}

	public double obsScore(String word) {
		if (tagged.containsKey(word))
			return Math.log((double)tagged.get(word)/(double)appearance);
		else 
			return unseen; // the unseen world penalty of -100
	}
	
	public double transScore(String next) {
		return Math.log((double) nextTag.get(next)/appearance);
	}
	public String toString() {
		String res = "\n This state has following words: " + tagged.toString();
		res += "\n This state points to the following states: " + nextTag.keySet();
		res += "\n The probabilities of each states are: ";
		for(String i: nextTag.keySet()) {
			res += (i + "'s normalized probability is " + transScore(i) + ", " );
		}
		res += "\n This state has " + appearance + " number of hits";
		res += "\n\n\n";
		return res;
	}

}

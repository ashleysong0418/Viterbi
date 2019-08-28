package cs10;

//By Jiwhan In and Ashley Song, HMM/Viterbi, PS5, CS10 19S
//Hard Coded Recitation Graph Test

import java.util.HashMap;

public class test {
	public static void main(String[] args) {
		HashMap<String, dataClass> viterbi = new HashMap<String, dataClass>();
		viterbi.put("#", new dataClass());
		viterbi.get("#").getStates().put("NP", 3);
		viterbi.get("#").getStates().put("N", 7);
		viterbi.get("#").appearance = 10;
		viterbi.put("NP", new dataClass());
		viterbi.get("NP").getWords().put("chase", 10);
		viterbi.get("NP").getStates().put("CNJ", 2);
		viterbi.get("NP").getStates().put("V", 8);
		viterbi.get("NP").appearance = 10;
		viterbi.put("N", new dataClass());
		viterbi.get("N").getWords().put("cat", 4);
		viterbi.get("N").getWords().put("dog", 4);
		viterbi.get("N").getWords().put("watch", 2);
		viterbi.get("N").getStates().put("V", 8);
		viterbi.get("N").getStates().put("CNJ", 2);
		viterbi.get("N").appearance = 10;
		viterbi.put("CNJ", new dataClass());
		viterbi.get("CNJ").getWords().put("and", 10);
		viterbi.get("CNJ").getStates().put("N", 4);
		viterbi.get("CNJ").getStates().put("V", 4);
		viterbi.get("CNJ").getStates().put("NP", 2);
		viterbi.get("CNJ").appearance = 10;
		viterbi.put("V", new dataClass());
		viterbi.get("V").getWords().put("get", 1);
		viterbi.get("V").getWords().put("chase", 3);
		viterbi.get("V").getWords().put("watch", 6);
		viterbi.get("V").getStates().put("CNJ", 2);
		viterbi.get("V").getStates().put("NP", 4);
		viterbi.get("V").getStates().put("N", 4);
		System.out.println(viterbi);
	}
	 
}

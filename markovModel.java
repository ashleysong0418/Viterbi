package cs10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class markovModel {
	/*
     * Authored By: Jiwhan In and Ashley Song, CS10, PS5
	 * 1. create viterbi method to split words and tags -> return a file that contains probabilities of each tag
	 * 2. create a best selection method that guesses the tags of input strings based on the result of viterbi
	 * 3. create a method to check the correctness of viterbi-based selection
	 */

	public static HashMap<String, dataClass> viterbi(String words, String tag) throws IOException{
		//Map containing dataClass objects for every tag used in given text
		HashMap<String, dataClass> viterbi = new HashMap<String, dataClass>();
		//read the texts and tags from the input
		BufferedReader text = new BufferedReader(new FileReader(words));
		BufferedReader tags = new BufferedReader(new FileReader(tag));
		String textLined;
		String tagLined;
		//creating the "Start" dataClass object
		viterbi.put("#", new dataClass());
		while((textLined = text.readLine())!= null && (tagLined = tags.readLine())!=null) {
			String[] textAdd = textLined.toLowerCase().split(" ");
			String[] tagAdd = tagLined.toLowerCase().split(" ");
			ArrayList<String> wordList = new ArrayList<String>();
			ArrayList<String> tagList = new ArrayList<String>();
			for (String i : textAdd) {
				wordList.add(i);
			}
			for (String i : tagAdd) {
				tagList.add(i);
			}
			//connect dataClass object for "Start" with following tags
			if (viterbi.get("#").getStates().containsKey(tagList.get(0))) {
				viterbi.get("#").getStates().put(tagList.get(0),viterbi.get("#").getStates().get(tagList.get(0))+1);
			}
			else viterbi.get("#").getStates().put(tagList.get(0),1);
			viterbi.get("#").appearance+=1;		//keeps track of how many sentences are read for a given file
			
			for(int i = 0; i < wordList.size(); i++) {
				String currT = tagList.get(i);
				String currW = wordList.get(i);
				
				//if the current tag has a dataClass object
				if (viterbi.containsKey(currT)) {
					//if the current word is in the dataClass object increase the number of appearance by 1
					if (viterbi.get(currT).getWords().containsKey(currW)) {
						viterbi.get(currT).getWords().put(currW, viterbi.get(currT).getWords().get(currW)+1);
					}
					//if not, put the word in the map of words under the tag
					else viterbi.get(currT).getWords().put(currW, 1);
					//if current tag is not the last one, add the next tag to the map of tags and increase the freq by 1
					if (i < wordList.size()-1) {
						String nextT = tagList.get(i+1);
						if (viterbi.get(currT).getStates().containsKey(nextT)) {
							viterbi.get(currT).getStates().put(nextT, viterbi.get(currT).getStates().get(nextT)+1);
						}
						else viterbi.get(currT).getStates().put(nextT, 1);
						
					}
				}
				//if the current tag has no dataClass object, make a new dataClass object
				else {
					viterbi.put(currT, new dataClass());
					viterbi.get(currT).getWords().put(currW, 1);
					if (i < wordList.size()-1) {
						String nextT = tagList.get(i+1);
						viterbi.get(currT).getStates().put(nextT, 1);
					}
				}
				viterbi.get(currT).appearance +=1;
			}
			
		}
		text.close();
		tags.close();
		
		return viterbi;
	}
	//Helper method for sequence seeking. Given a line, provided the best set of tags.
	public static String bestSeqHelper(String sentence, HashMap<String, dataClass> viterbi) throws IOException{
		
		String res = "";
		//words inside a given sentence
		String[] inputAdd = sentence.toLowerCase().split(" ");
		//ArrayList of HashMaps used as a back pointer so that we can track the sequence later.
		ArrayList<HashMap<String,String>> backPointer = new ArrayList<HashMap<String,String>>();
		//Set of possible current states
		HashSet<String> currentStates = new HashSet<String>();
		//initiate with the "Start"
		currentStates.add("#");
		//Map keeping track of state/score combination
		HashMap<String, Double> currentScores = new HashMap<String, Double>();
		//initiate with the "Start"
		currentScores.put("#", 0.0); 
		
		for(int i = 0; i < inputAdd.length ; i++) {
			backPointer.add(new HashMap<String, String>());
			HashSet<String> nextStates = new HashSet<String>();
			HashMap<String, Double> nextScores = new HashMap<String, Double>();
			for (String currentState : currentStates) {
				for (String nextState: viterbi.get(currentState).getStates().keySet()) {
					nextStates.add(nextState);
					double nextScore = 0.0;
					if (viterbi.get(nextState).appearance != 0) {
						nextScore = currentScores.get(currentState)
								+ viterbi.get(nextState).obsScore(inputAdd[i])
								+ viterbi.get(currentState).transScore(nextState);		
						}
				else if(nextState.equals(".")){
					nextScore = currentScores.get(currentState);
				}
				if (!nextScores.containsKey(nextState) || nextScore > nextScores.get(nextState)) {
					nextScores.put(nextState, nextScore);
					backPointer.get(i).put(nextState, currentState);
				}
			}
		}
			currentStates = nextStates;
			currentScores = nextScores;
		
	}
	String maxState = ".";
	for(String state: currentStates) {
			if(state.equals(".")) {
				if(currentScores.get(state) > currentScores.get(maxState)) 	maxState = state;
			}
		}
	String currentTag = maxState;
	for (int x = inputAdd.length-1; x>=0; x--){
		if (currentTag != "#") {
			res = currentTag + " " + res;
			currentTag = backPointer.get(x).get(currentTag);
		}
	}
	return res;
	}
	
	public static void bestSeq(String sentence, HashMap<String, dataClass> viterbi, String output) throws IOException{
		BufferedReader input = new BufferedReader(new FileReader(sentence));
		BufferedWriter resTags = new BufferedWriter(new FileWriter(output));
		String inputLined;
		while((inputLined = input.readLine())!= null) {
		resTags.write(bestSeqHelper(inputLined, viterbi));
		resTags.write("\n");
		}
		
		input.close();
		resTags.close();
		
	}
	
	public static double correctness(String tagInput, String tagRes) throws IOException {
		double res = 0.0;
		int total = 0;
		int correct = 0;
		BufferedReader given = new BufferedReader(new FileReader(tagInput));
		BufferedReader created = new BufferedReader(new FileReader(tagRes));
		String inputLined;
		String resLined;
		while((inputLined = given.readLine())!= null && (resLined = created.readLine())!=null) {
			String[] inputAdd = inputLined.toLowerCase().split(" ");
			String[] resAdd = resLined.toLowerCase().split(" ");
			for (int i = 0; i < inputAdd.length ; i++) {
				//add all tags
				total ++;
				if (inputAdd[i].equals(resAdd[i])) {
					correct ++;
				}
			}

		}
		System.out.println("Out of "+total+" tags, there are "+correct+" correct tags.");
		res = ((double)correct/total);
		return res;
	}
	
	public static void ConsoleViterbi(String sentence, String tags) throws Exception{
		
		
		Scanner in = new Scanner(System.in);

		HashMap<String, dataClass> consoleTest = viterbi(sentence, tags);

		System.out.println("Type a test sentence");
		
		String testSentence = in.nextLine();
		System.out.println(bestSeqHelper(testSentence, consoleTest)); 
		

	}
	
	public static void main(String[] args) throws Exception {
		String trainingSentence = "texts/simple-train-sentences.txt";
		String trainingTags = "texts/simple-train-tags.txt";
		String testSentence = "texts/simple-test-sentences.txt";
		String testTags = "texts/simple-test-tags.txt";
		String outputName = "texts/output.txt";
		ConsoleViterbi(trainingSentence, trainingTags);
		HashMap<String, dataClass> test = viterbi(trainingSentence, trainingTags);
		System.out.println(test); 
		System.out.println("\n"); 
		bestSeq(testSentence, test, outputName); 
		correctness(testTags, outputName);
		}
		
}

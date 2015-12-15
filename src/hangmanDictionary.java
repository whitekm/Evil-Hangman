import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

public class hangmanDictionary {
	private HashMap<Integer, HashSet<String>> map; //map with words organized by length
	private String cFamilyKey; //manages what the user has guessed so far
	
	/*
	 * Constructor that reads in the file of words for the game and puts them in a set based 
	 * on their length
	 * @param name of the file to be played with
	 */
	public hangmanDictionary(String file){
		File textfile = new File(file);
		try{
			Scanner input = new Scanner(textfile);
			map = new HashMap<Integer, HashSet<String>>();
			HashSet<String> tempSet = new HashSet<String>();
			int wordSize;
			String word;
			while(input.hasNext()){
				word = input.next();
				wordSize = word.length();
				if(map.containsKey(wordSize)){
					tempSet = map.get(wordSize);
				}
				else{
					tempSet = new HashSet<String>();
				}
				tempSet.add(word);
				map.put(wordSize, tempSet);
			}
			//System.out.println(map);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	/*
	 * checks to see whether the length of the word the user entered matches a set of words 
	 * that this dictionary has
	 * @param length of words they would like to play with
	 * @return whether this dictionary has words of that length
	 */
	public boolean validLength(int x){
		return map.containsKey(x);
	}
	
	/*
	 * gets all of the words of a specific length
	 * @param length of words the user is going to play with
	 * @return the words the user will play with
	 */
	public HashSet<String> getSet(int wordLength){
		return map.get(wordLength);
	}
	
	/*
	 * takes in the remaining words that the user has to play with and creates new sets
	 * based on the character that the user has guessed and the string of letters they already
	 * guessed, and then picks the biggest set of words based on their keys to still be a part of
	 * the game
	 * @param rWords the words still remaining in the game
	 * @param g the guess they just made, 
	 * @param currentFK the string of letters they have already guessed
	 * @return the biggest set of words possible
	 */
	public HashSet<String> game(HashSet<String> rWords, char g, String currentFK){
		HashMap<String, HashSet<String>> families = new HashMap<String, HashSet<String>>();
		HashSet<String> tempSet = new HashSet<String>();
		cFamilyKey = currentFK;
		
		//creates different sets based on the characters the user guesses/has already guessed
		for(String w: rWords){
			String temp = format(w,g,currentFK);
			if(families.containsKey(temp)){
				tempSet = families.get(temp);
			}
			else{
				tempSet = new HashSet<String>();
			}
			tempSet.add(w);
			families.put(temp, tempSet);
		}	
		
		//find the biggest HashSet and that is what you return
		Iterator<Entry<String, HashSet<String>>> iter = families.entrySet().iterator();
		String biggestKey = iter.next().getKey();
		while(iter.hasNext()){
			String temp = iter.next().getKey();
			if(families.get(temp).size() > families.get(biggestKey).size()){
				biggestKey = temp;
			}
		}
		cFamilyKey = biggestKey;
		return families.get(biggestKey);

	}
	
	/*
	 * returns the current family key 
	 * @return cFamilyKey the string of letters that the user has already guessed
	 */
	public String getCurrentFamilyKey(){
		return cFamilyKey;
	}
	
	/*
	 * formats a key based on the word in the dictionary being checked, the character the user
	 * has just guessed, and whatever they have already guessed
	 * @param word the word in the dictionary
	 * @param g the letter the user has guessed
	 * @param cfk the updated version of what the user has already guessed successfully
	 * @return String the key that will be used for the families map
	 * 
	 */
	public String format(String word, char g, String cfk){
		//format word based on the character (food = _oo_)
		String formattedWord = "";
		int position;
		for(int i = 0; i < word.length(); i++){
			if(g == word.charAt(i)){
				formattedWord += g;
			}
			else{
				formattedWord += cfk.charAt(i);
			}
		}
		return formattedWord;
	}
}

import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 */

/**
 * @author Kelly White
 * Lab10
 * CSCE 270
 * 12/5/2015
 * Sources Consulted: Dr. Blaha, Paul the TA
 * Known Bugs: none
 * Creativity: none
 *
 */
public class evilHangman {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		hangmanDictionary dict = new hangmanDictionary("smallDictionary.txt");
		int wordLength;
		String showWords;
		boolean vLength, showTotalWords = false;
		int guesses;
		
		System.out.println("Enter the length of the word you would like to guess: ");
		wordLength = keyboard.nextInt();
		vLength = dict.validLength(wordLength);
		while(vLength == false){
			System.out.println("No words of that length, please enter another length:");
			wordLength = keyboard.nextInt();
			vLength = dict.validLength(wordLength);
		}
		
		System.out.println("How many guesses would you like to make?");
		guesses = keyboard.nextInt();
		while(guesses < 1){
			System.out.println("Number of guesses is invalid, try again:");
			guesses = keyboard.nextInt();
		}
		
		System.out.println("Would you like to keep a running total of the number of words remaining "
				+ "in the word list? (Y/N)");
		keyboard.nextLine();
		showWords = keyboard.nextLine();
		showWords = showWords.toUpperCase();
		while(showWords.charAt(0) != 'Y' && showWords.charAt(0) != 'N'){
			System.out.println("Incorrect input, would you like to keep a running total of the number of words remaining "
					+ "in the word list? (Y/N)");
			showWords = keyboard.nextLine();
			showWords = showWords.toUpperCase();
		}
		if(showWords.charAt(0) == 'Y')
			showTotalWords = true;
		if(showWords.charAt(0) == 'N')
			showTotalWords = false;
		
		String currentGuessedWord = "";
		for(int j = 0; j < wordLength; j++){
			currentGuessedWord = currentGuessedWord + "_";
		}
		HashSet<String> remainingWords = dict.getSet(wordLength);
		
		//loop to play the game
		boolean userWon = false;
		TreeMap<Character, TreeSet<Character>> usedGuesses = new TreeMap<Character, 
				TreeSet<Character>>();
		int i = guesses;
		while(i > 0 && userWon == false){
			
			System.out.println("Number of guesses remaining: " + i);
			System.out.println(currentGuessedWord);
			if(showTotalWords == true)
				System.out.println(remainingWords.toString());
			System.out.println("Guess a letter: ");
			String userGuess = keyboard.nextLine().toLowerCase();
			char uG = userGuess.charAt(0);
			
			//check to see if uG is a letter
			while(!Character.isLetter(uG)){
				System.out.println("That was not a letter, please try again: ");
				userGuess = keyboard.nextLine().toLowerCase();
				uG = userGuess.charAt(0);
			}
			
			//sends remaining words, the letter guessed by the user, and the current
			//status of the word to the game to decide what words remain
			remainingWords = dict.game(remainingWords, uG, currentGuessedWord);
			currentGuessedWord = dict.getCurrentFamilyKey();
			
			//does not take away a guess if the letter is in the word
			for(int k = 0; k < currentGuessedWord.length(); k++){
				if(uG == currentGuessedWord.charAt(k))
					i++;
			}
			
			//does not take away a guess if the letter has already been entered
			TreeSet<Character> tempSet = new TreeSet<Character>();
			if(usedGuesses.containsKey(uG)){
				tempSet = usedGuesses.get(uG);
				i++;
				System.out.println("You've already guessed this letter, try again.");
			}
			else{
				tempSet = new TreeSet<Character>();
			}
			tempSet.add(uG);
			usedGuesses.put(uG, tempSet);
			
			
			//checks to see if the user has won the game yet
			for(int a = 0; a < currentGuessedWord.length(); a++){
				if(currentGuessedWord.indexOf('_') == -1)
					userWon = true;
			}
			
			
			
			i--;
		}
		
		//will exit the while loop if there are no more guesses
		if(i == 0){
			userWon = false;
		}
		
		if(userWon == true){
			System.out.println("You guessed: " + currentGuessedWord );
			System.out.println("Congratulations! You have won the game!");
		}
		else{
			System.out.println("Sorry, you lose!");
			String rWords  = remainingWords.toString();
			int space = rWords.indexOf(',');
			String word = rWords.substring(1, space);
			System.out.println("The word was: " + word);
		}
		
		
		
	}

}

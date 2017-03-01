package Scrimish;

import java.util.*;

public class Gameplay {
	
	private static Scanner input = new Scanner(System.in);
	private static boolean gameOver = false;				// Signifies when the game is over
	private static boolean EndTurn;							// Signifies when the turn is over	
	private static boolean setupComplete;
	private static int setupChoice;
	private static String winner;							// The name of the winner of the game
	private static String myName;
	private static String oppName;
	
	// The chance, expressed as a percentage, that the opponent will attack as opposed to discard their card.
	private static int percentage = 80;
	
	public static void main(String args[]) {
	
		int action; // The decision to attack or defend
		int choice; // Computer's choice to attack or defend
		int deckNum; // The deck number that is being discarded
		
		// Welcome message and initializing player names
		
		System.out.println("Welcome to Scrimish! The rules are simple: \n");
		System.out.println("\t1. Both players have five decks, each with five cards. \n\t     Therefore, each player has an army of 25 cards.\n");
		System.out.println("\t2. The goal is to find your opponent's crown card.\n\t     It is at the bottom of their decks.\n");
		System.out.println("\t3. Players take turns attacking the top card of one of the \n\t     other's decks with the top card of one of their own.\n");
		System.out.println("\t4. Archer Cards beat anything they attack, but\n\t     but lose if they are attacked by any card.\n");
		System.out.println("\t5. Shield Cards cannot attack but any card attacking it\n\t     will be discarded, as well as the shield card. The only\n\t     exception is the Archer Card: when an Archer attacks \n\t     a Shield Card, neither are discarded.\n");
		System.out.println("\t6. A higher number card will beat a lower number card\n\t     but equal numbers will both be discarded.\n");
		System.out.println("\t7. Crown Cards can attack, but they will only win if \n\t     attacking another Crown Card.\n");
		System.out.println("\t8. A final twist: instead of attacking, you can discard a card\n\t     from the top of one of your decks and end your turn.\n\t     Crown Cards cannot be discarded.");

		printTurnSeparator();
		
		System.out.print("Choose a name:  ");
		myName = input.nextLine();
		System.out.print("Choose a name for your opponent: ");
		oppName = input.nextLine();
		
		// Asking the user to decide whether to choose
		while (!setupComplete) {
			setupComplete = true;
			try {
				setupChoice = setup();
			} catch (Exception ex) {
				System.out.println("Please enter an integer (0 or 1)\n");
				input.next();
				setupComplete = false;
			}
		}
		
		Player player = new Player(myName, setupChoice);    // Controlled by user
		Player opponent = new Player(oppName, 0);  			// Automated by computer
		
		printTurnSeparator();
		
		if (setupChoice == 0)
			System.out.println("Your cards have been randomized. Enjoy the game.");
		else
			System.out.println("Your setup is complete. Enjoy the game.");
	
		printTurnSeparator();

		// Game Play
		do {
		
			// Player's turn
			do {
				EndTurn = true;
				
				// Print the Updated Map at the start of the player's turn
				printMap(player, opponent);
				
				// Print player's console
				printConsole();
				
				try {   // Catches any accidental string inputs
					
					action = input.nextInt();
					
					switch (action) {
				
					case 1: 
						System.out.print("Select a deck (1 - 5) to attack from:  ");
						int myDeck = input.nextInt() - 1;
						System.out.print("Select an opponent's deck (1 - 5) to attack:  ");
						int oppDeck = input.nextInt() - 1;
						printTurnSeparator();
						try {attack(player, opponent, myDeck, oppDeck);}  // Catches any deck numbers that are out of bounds
						catch (IndexOutOfBoundsException ex) {
							System.out.println("Please enter a valid deck number (1 - 5).");
							EndTurn = false;
						}
						break;
						
					case 2: 
						System.out.print("From which deck (1 - 5) would you like to discard?  ");
						deckNum = input.nextInt() - 1;
						printTurnSeparator();
						try {discard(player, deckNum);}
						catch (IndexOutOfBoundsException ex) {
							System.out.println("Please enter a valid deck number (1 - 5).");
							EndTurn = false;
						}
						break;	
						
					default:
						printTurnSeparator();
						System.out.println("Please enter a valid action choice: 1 or 2");
						EndTurn = false;
						break;
						
					}

				} catch (InputMismatchException ex) {
					System.out.println("Please enter a valid deck number (1 - 5).");
					EndTurn = false;
					input.next();
				}
				
				// Print out turn separator
				printTurnSeparator();
				
			} while (!EndTurn && !gameOver);
			
			// Opponent's turn
			EndTurn = false;
			
			while (!EndTurn && !gameOver) {
				EndTurn = true;
				
				// Randomize attacking and discarding cards for opponent
				choice = (int) (Math.random() * 100);
				if (choice < percentage)
					action = 1;
				else
					action = 2;
				
				switch (action) {
				
					case 1: 
						int x = (int) (Math.random() * 5);     // Random integer (1-5) for opponent's deck
						int y = (int) (Math.random() * 5);     // Random integer (1-5) for player's deck
						attack(opponent, player, x, y);
						break;
						
					case 2: 
						deckNum = (int) (Math.random() * 5);   // Random integer (1-5) for opponent's deck
						discard(opponent, deckNum);
						break;	
				}
			
			}
			
			// Print out turn separator after opponent's completed turn
			printTurnSeparator();

		
		} while (!gameOver);
		
		// Print the final map and the Game-Over Screen
		printMap(player, opponent);
		printGameOver();
		input.close();

	}
	
	
	// SUBMETHODS
	
	// Returns the choice that the user enters to decide whether they want to randomize or choose their deck
	public static int setup() throws Exception {
		System.out.print("\nWould you like to choose your decks or randomize them?  (1 for choosing, 0 for randomizing)  ");
		int setupChoice = input.nextInt();
		while (setupChoice != 1 && setupChoice != 0) {
			System.out.println("Please select either 1 or 0.");
			System.out.print("\nWould you like to choose your decks or randomize them?  (1 for choosing, 0 for randomizing)  ");
			setupChoice = input.nextInt();
		}
		return setupChoice;
	}
	
	public static void printMap(Player player1, Player player2) {
		// Print Opponent's Decks (Represented by X's)
		player2.printOpponentMap();
		
		// Allow some space between opposing sides
		for (int i = 0; i < 2; i++)
			System.out.println();
		
		//Print Player's decks (Card strengths shown to Player)
		player1.printPlayerMap();
	}
	
	public static void printConsole() {
		System.out.println("\nACTIONS:");
		System.out.println("1. Attack your Opponent");
		System.out.println("2. Discard a Card");
		System.out.print("What would you like to do?  ");
	}
	
	public static void printTurnSeparator() {
		System.out.println();
		System.out.println("*****************************************************************************");
		System.out.println();
	}
	
	public static void printGameOver() {
		System.out.println("\nTHE CROWN HAS BEEN CAPTURED. " + winner.toUpperCase() + " WINS!");
	}
	
	public static void discard(Player p, int deckNum) {
		String card = p.getDecks().get(deckNum).get(0).getName();
		int success = p.discard(deckNum);
		if (success == -1) {
			System.out.println("Invalid Turn : You cannot discard a crown card.");
			EndTurn = false;
		}
		else if (success == 0) {
			System.out.println("Invalid Turn : You cannot discard from an empty deck.");
			EndTurn = false;
		}
		else {
			System.out.println(p.getName() +" discarded a " + card + ".");
		}
	}
	
	// Method that player utilizes to attack opponent
	public static void attack (Player p, Player opp, int myDeck, int oppDeck) {
		
		Card myCard = p.getTopCard(myDeck);
		Card oppCard = opp.getTopCard(oppDeck);
		
		int result = p.attack(opp, myDeck, oppDeck);
		
		switch (result) {
		
			case 0: System.out.println(p.getName() + " attacked a " + oppCard.getName() + " with a " + myCard.getName() + ". "
					+ "Both cards are returned to their decks.");break;
			
			case -1: System.out.println(p.getName() + " attacked a " + oppCard.getName() + " with a " + myCard.getName() + ". "
					+ p.getName() + " loses the battle."); break; 
					
			case 1: System.out.println(p.getName() + " attacked a " + oppCard.getName() + " with a " + myCard.getName() + ". "
					+ p.getName() + " wins the battle."); break;
					
			case 5: System.out.println(p.getName() + " attacked a " + oppCard.getName() + " with a " + myCard.getName() + ". "
					+ "Both cards are discarded."); break;
					
			case -5: 
				if (p.getName().equals(myName))
					System.out.println("Invalid Turn : You cannot attack with a shield.");
				EndTurn = false; break;
			
			case -6: 
				if (p.getName().equals(myName))
					System.out.println("Invalid Turn : You cannot attack from an empty deck.");
				EndTurn = false; break;
			
			case -7: 
				if (p.getName().equals(myName))
					System.out.println("Invalid Turn : You cannot attack a deck that is empty.");
				EndTurn = false; break;
			
			case -8:
				if (p.getName().equals(myName))
					System.out.println("Invalid Turn : Deck number(s) were out of range.");
				EndTurn = false; break;
			
			case 10: System.out.println(p.getName() + " attacked a " + oppCard.getName() + " with a " + myCard.getName() + ". "
					+ p.getName() + " wins the battle."); winner = p.getName(); gameOver = true; break;
					
			case -10: System.out.println(p.getName() + " attacked a " + oppCard.getName() + " with a " + myCard.getName() + ". "
					+ p.getName() + " loses the battle."); winner = opp.getName(); gameOver = true; break;
					
			default: break;
			
		}
			
	}
	
}

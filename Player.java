package Scrimish;
import java.util.*;

public class Player {
	
	// The class constructor
	protected Player(String name, int choice) {
		
		// Initialize the name
		this.name = name;
		
		switch (choice) { 
			case 0: pile = randomizeCards(); break;		// Randomize the cards
			case 1: pile = pickCards(); break;			// The player chooses the cards themselves
		}
		
		for (int row = 0; row < 5; row++) {
			
			// Add a new deck
			decks.add(new ArrayList<Card>());
			
			// Fill the new deck
			for (int col = 0; col < 5; col++)
				decks.get(row).add(pile[5 * row + col]);
		}
	}
	
	private static Card[] pile = new Card[25];			// Pile that will be made into decks. Can be randomized or chosen by user.
	private static int crownDeck;						// Deck in which the crown card will be placed. Used in choosing method.
	private static int Deck;							// Deck in which a generic card will be placed. Used in choosing method.
	
	// Method that allows user to pick cards
	public static Card[] pickCards() {
		
		// List of the quantities of cards left to be distributed
		int[] quantities = {5, 5, 3, 3, 2, 2, 2, 2};
		
		Card card;			// Create a card that will transfer cards into the pile
		java.util.Scanner scan = new java.util.Scanner(System.in);
		
		
		// Choosing which deck to put the crown card in
		boolean done1 = false;
		
		while (!done1) {
			
			try {
				System.out.print("\nWhich deck would you like to put the crown in? (1 - 5)  ");
				crownDeck = scan.nextInt();
				card = new Card('C');
				pile[crownDeck * 5 - 1] = card;
				done1 = true;
			} catch(InputMismatchException ex) {
				System.out.println("Please enter a valid input (1 - 5)\n");
				done1 = false;
				scan.next();
			} catch (IndexOutOfBoundsException ex) {
				System.out.println("Please enter a valid input (1 - 5)\n");
				done1 = false;
			}
		}
		
		card = new Card(0);		// Initializing the card to an arbitrary value
		
		// Choosing the rest of the cards
		System.out.println("\nChoose based on the following cards:");
		System.out.println("1. Dagger Card (5x)");
		System.out.println("1. Sword Card (5x)");
		System.out.println("3. Morning Star Card (3x)");
		System.out.println("4. War Axe Card (3x)");
		System.out.println("5. Halberd Card (2x)");
		System.out.println("6. LongSword Card (2x)");
		System.out.println("7. Shield Card (2x)");
		System.out.println("8. Archer Card (2x)");
		System.out.println();
		
		// Iterate 24 times if the crown is in the last deck, otherwise iterate 25 times.
		int iterations = (crownDeck == 5) ? 24 : 25;	
		
		for (int i = 0; i < iterations; i++) {	
			if (pile[i] != null)		// If the card is a crown card, skip it
				i++;
			
			boolean done2 = false;
			
			while(!done2) {
				
				try {
					
					System.out.print("Choose the next card for Deck " + (i / 5 + 1) + ":  ");
					Deck = scan.nextInt();
					switch(Deck) {
						case 1:
						case 2:
						case 3:
						case 4:
						case 5:
						case 6: card = new Card(Deck); done2 = true; break;
						case 7: card = new Card('S'); done2 = true; break;
						case 8: card = new Card('A'); done2 = true; break;
						default: done2 = false;
					}
					
					while (quantities[Deck-1] == 0) {
						System.out.println("There are no more " + card.getName() + "s");
						System.out.print("\nChoose the next card for Deck " + (i / 5 + 1) + ":  ");
						Deck = scan.nextInt();
						switch(Deck) {
							case 1:
							case 2:
							case 3:
							case 4:
							case 5:
							case 6: card = new Card(Deck); done2 = true; break;
							case 7: card = new Card('S'); done2 = true; break;
							case 8: card = new Card('A'); done2 = true; break;
							default: done2 = false;
						}	
					}
				} catch(InputMismatchException ex) {
					System.out.println("Please enter a valid input (1 - 8)\n");
					done2 = false;
					scan.next();
				} catch (IndexOutOfBoundsException ex) {
					System.out.println("Please enter a valid input (1 - 8)\n");
					done2 = false;
				}
			}
			pile[i] = (card);
			quantities[Deck-1]--;
		}
		return pile;		
	}
	
	
	// Method that randomizes a player's decks
	public static Card[] randomizeCards() {
		
		// Array of Card Strengths that will be acquired (Used when randomizing cards)
		int[] cardStrength = {1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 'A', 'A', 'S', 'S', 'C'};
		
		Card temp;
		int index;
		
		// Generate Objects and fill the array
		Card[] pile = new Card[25];
		for (int i = 0; i < pile.length; i++)
			pile[i] = (new Card(cardStrength[i]));
		
		// Shuffle the pile
		for (int i = 0; i < pile.length - 1; i++)
			for (int j = i; j < pile.length - 1; j++) {
				
				// Random index that is greater than the one swapping.
				index = j + (int) (Math.random() * (pile.length - j - 1));
				
				// Switch two cards
				temp = pile[j];
				pile[j] = pile[index];
				pile[index] = temp;	
			}
		
		// Randomize position of Crown card so it would end up on the bottom of one of the decks
		int[] newIndex = {4, 9, 14, 19, 24};
		int bottom = newIndex[(int) (Math.random() * 5)];   // Index of a random bottom card
		temp = pile[24];                                    // The crown card
		pile[24] = pile[bottom];							// Switch the crown card
		pile[bottom] = temp;								// with the random bottom card
				
		return pile;
	}
	
	// Each player has their own set of decks
	private ArrayList<ArrayList<Card>> decks = new ArrayList<ArrayList<Card>>(5);
	
	//Each player has a name
	private String name;
	
	// Access the player's name
	public String getName() {
		return this.name;
	}
	
	// Access the decks
	public ArrayList<ArrayList<Card>> getDecks() {
		return decks;
	}
	
	// Player's version of the map
	public void printPlayerMap() {
		for (int deckNum = 0; deckNum < decks.size(); deckNum++) {
			System.out.print("\t\t");
			for (int cardNum = 0; cardNum < 5; cardNum++) {
				Card card = decks.get(cardNum).get(deckNum);
				if (card.getStrength() == 0)
					System.out.print(" " + "\t");
				else if (card.getStrength() > 6)
					System.out.print((char) (card.getStrength()) + "\t");
				else
					System.out.print(card.getStrength() + "\t");
			}
			System.out.println();
		}
	}
	
	// Opponent's version of the map
	public void printOpponentMap() {
		for (int deckNum = decks.size() - 1; deckNum >= 0; deckNum--) {
			System.out.print("\t\t");
			for (int cardNum = 0; cardNum < decks.get(deckNum).size(); cardNum++) {
				Card card = decks.get(cardNum).get(deckNum);
				if (card.getStrength() == 0)
					System.out.print(" " + "\t");
				
				// If card was used to attack, show its value to the user
				else if (card.isVisible())
					if (card.getStrength() > 6)
						System.out.print((char) (card.getStrength()) + "\t");
					else
						System.out.print(card.getStrength() + "\t");
				else
					System.out.print("X" + "\t");
			}
			System.out.println();
		}
	}
	
	// Accessing the top card of a deck
	public Card getTopCard(int deckNum) {
		return decks.get(deckNum).get(0);
	}
	
	// Discarding a card during the discard phase of one's turn
	public int discard (int deckNum) throws IndexOutOfBoundsException{
		int strength = decks.get(deckNum).get(0).getStrength();
		if (strength == 'C')
			return -1;
		else if (decks.get(deckNum).get(0).getStrength() == 0)
			return 0;
		// Add a place holder so the size of the deck remains 5.
		else {
			decks.get(deckNum).remove(0);
			decks.get(deckNum).add(new Card(0));
		}
		return 1;
	}
	
	// Discarding a card during an attack phase of one's turn
	public void discard2 (int deckNum) {
		decks.get(deckNum).remove(0);
		// Add a place holder card of strength 0 so the size of the deck remains 5.
		decks.get(deckNum).add(new Card(0));
	}
	
	public int attack(Player opponent, int myDeck, int oppDeck) throws IndexOutOfBoundsException{
		
		// ACTION LEGEND FOR ATTACKING:
		
		// -5 = Re-do turn because shield is selected
		// -6 = Re-do turn because of attacking from null deck
		// -7 = Re-do because of attacking a null deck
		// -8 = Re-do because selected deck number is out of range
		// 10 = Attacker wins the game 
		// -10 = Attacker loses the game
		// 5 = Cards are of equal strength
		// 1 = Attacker wins the battle
		// -1 = Attacker loses the battle
		// 0 = Nothing happens
		
		if (myDeck > 5 || myDeck < 0 || oppDeck < 0 || oppDeck > 5)
			return -8;
		
		Card myCard = this.getTopCard(myDeck);
		Card oppCard = opponent.getTopCard(oppDeck);
		
		// If myDeck is null (Top card has a strength of 0), then re-do turn
		if (myCard.getStrength() == 0)
			return -6;
		// If oppDeck is null (Top card has a strength of 0), then re-do turn
		if (oppCard.getStrength() == 0)
			return -7;
		// If the card selected is a shield, re-do the turn (-5)
		if (myCard.getStrength() == 'S')
			return -5;
		// If any card attacks a crown, they win
		if (oppCard.getStrength() == 'C') {
			myCard.setVisible();
			oppCard.setVisible();
			return 10;
		}
		//When a crown attacks another card. If the other card is not a crown, the attacking player loses
		if (myCard.getStrength() == 'C' && oppCard.getStrength() != 'C') {
			myCard.setVisible();
			oppCard.setVisible();
			return -10;
		}
		// If the two cards have equal strengths, discard them both
		if ( myCard.getStrength() == oppCard.getStrength() ) {
			this.discard2(myDeck);
			opponent.discard2(oppDeck);
			return 5;
		}
		// If any card attacks an archer, it wins
		if (oppCard.getStrength() == 'A') {
			opponent.discard2(oppDeck);
			myCard.setVisible();
			return 1;
		}
		// When an archer attacks any card, except for a shield, it wins. If the other card is a shield nothing happens.
		if (myCard.getStrength() == 'A') {
			if (oppCard.getStrength() == 'S') {
				myCard.setVisible();
				oppCard.setVisible();
				return 0;
			}
			else {
				opponent.discard2(oppDeck);
				myCard.setVisible();
				return 1;
			}
		}
		// When a card attacks a shield, both are discarded
		if (oppCard.getStrength() == 'S') {
			this.discard2(myDeck);
			opponent.discard2(oppDeck);
			return 5;
		}
		// When number cards attack other number cards. If the number is greater, it wins. Otherwise, it loses.
		if (myCard.getStrength() > oppCard.getStrength()) {
			opponent.discard2(oppDeck);
			myCard.setVisible();
			return 1;
		}
		else {
			this.discard2(myDeck);
			oppCard.setVisible();
			return -1;
		}

	}

}

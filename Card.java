package Scrimish;

class Card {
	
	private String name;
	private int strength;
	private boolean visible;	// A card's visibility. Used to reveal the top card of the opponent's deck if it was used.
	
	public Card (int number){
		switch (number) {
		case 0: name = "Null Card"; strength = 0; break;
		case 1: name = "Dagger"; strength = 1; break;
		case 2: name = "Sword Card"; strength = 2; break;
		case 3: name = "Morning Star Card"; strength = 3; break;
		case 4: name = "War Axe Card"; strength = 4; break;
		case 5: name = "Halberd Card"; strength = 5; break;
		case 6: name = "Longsword Card"; strength = 6; break;
		case 'A': name = "Archer Card"; strength = 'A'; break;
		case 'S': name = "Shield Card"; strength = 'S'; break;
		case 'C': name = "Crown Card"; strength = 'C'; break;
		default: break;
		}	
	}
	
	int getStrength() {
		return strength;
	}
	
	String getName() {
		return name;
	}
	
	void setVisible() {
		visible = true;
	}
	
	boolean isVisible() {
		return visible;
	}
	
	@Override
	public String toString() {
		return this.getStrength() + "";
	}

	public boolean equals(Card card) {
		return this.getStrength() == card.getStrength();
	}
}

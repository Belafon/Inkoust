package likeliness;

import java.util.Random;

public class Dice {
	private int pocetSten;
	private Random random; 
	
	public Dice(int pocetSten){
		this.pocetSten = pocetSten;
		random = new Random();
	}
	
	public int toss() {
		if(pocetSten == 0)return 0;
		return(random.nextInt(this.pocetSten) + 1);
	}

	public int[] toss(int pocet) {
		int[] pole = new int[pocet];
		int i = pocet;
		while(i > 0) {
			pole[i - 1] = random.nextInt(this.pocetSten) + 1;
			i--;
		}
		return pole;

	}
}

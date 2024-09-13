package map;

import Game.Game;
import Game.Player;
import likeliness.Dice;

/* This class creates map for the game. 
 * The map is created from Place objects.
 * I create the nature first of all, than I create start villages. */
public class NewMapCreator {
	public static Place[][] createNewMap(Game game, int size_of_map) {
		Place[][] map = new Place[size_of_map][size_of_map];
		for(int i = 0; i < size_of_map; i++) { // I will iterate all places of the map
			for(int j = 0; j < size_of_map; j++) {
				Dice dice = new Dice(10); // I use likeliness here, I throw with the dice with 10 sides
				int toss = dice.toss();
				if(toss < 5) map[i][j] = new Meadow();
				else if(toss < 9) map[i][j] = new Forest();
				else map[i][j] = new Mountains();
			}
		}
		
		if(game.players.size() == 0)return map;
		
		// lets set position of villages, It puts villages into the circle
		int pocitadlo = 0;
		int radius = (int)((size_of_map - 2) / 2);
		float alpha = (float)(360f / (float)(game.players.size()));
		alpha = (float)Math.toRadians(alpha);
		for(Player player : game.players) {
			int positionX = (int)(((float)size_of_map) / 2f - (Math.cos(alpha * (float)pocitadlo) * radius));
			int positionY = (int)(((float)size_of_map) / 2f - (Math.sin(alpha * (float)pocitadlo) * radius));
			Village village = new Village(player, positionX, positionY);
			player.addVillage(village);
			map[positionX][positionY] = village;
			pocitadlo++;
		}
		
		return map;
	}
	
	// It draws map to command line
	public static void drawMap(Game game) {
		for(int i = 0; i < game.sizeOfMap; i++) {
			for(int j = 0; j < game.sizeOfMap; j++) {
				if(game.map[j][i] instanceof Meadow) System.out.print(" L ");
				else if(game.map[j][i] instanceof Forest) System.out.print(" F ");
				else if(game.map[j][i] instanceof Mountains) System.out.print(" M ");
				else if(game.map[j][i] instanceof Village) System.out.print(" V ");
			}
			System.out.println();
		}
	}
}

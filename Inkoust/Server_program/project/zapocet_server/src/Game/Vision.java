package Game;

import Server.Client;
import Units.Unit;
import map.Village;

public class Vision {
	
	// this method send info about places around one place, about which the player did not have a vision before
	public static void addVisionToPoint(Player player, int positionX, int positionY, int radius) {
		Game game = player.client.actual_game;
		String message = "";
		for(int i = positionX - radius; i < positionX + radius + 1; i++) // all places in the radius
			for(int j = positionY - radius; j < positionY + radius + 1; j++) {
				if(i >= 0 && j >= 0 && i < game.sizeOfMap && j < game.sizeOfMap) {
					if(player.vision[i][j] == 0) { // if the player did not have vision here before -> send info about the place
						if(game.map[i][j].getClass().getName().equals("map.Village"))
							 message += " " + Integer.toString(i) + " " + Integer.toString(j) + " " + game.map[i][j].getClass().getName() + " " + ((Village)game.map[i][j]).player.id;
						else message += " " + Integer.toString(i) + " " + Integer.toString(j) + " " + game.map[i][j].getClass().getName();
						// send also info about the units
						for(Unit unit : game.map[i][j].lastUnits) message += " " + unit.id + " " + unit.player.id + " " + unit.getClass().getName();
						message += " ;";
					}
					player.vision[i][j]++;
				}
			}
		
		if(!message.equals(""))player.client.writer.addVision(player.client, message);
	}
	
	// the same like addVision, but it remove vision
	public static void removeVisionAtPoint(Player player, int positionX, int positionY, int radius) {
		Game game = player.client.actual_game;
		String message = "";
		for(int i = positionX - radius; i < positionX + radius + 1; i++)
			for(int j = positionY - radius; j < positionY + radius + 1; j++) {
				if(i >= 0 && j >= 0 && i < game.sizeOfMap && j < game.sizeOfMap) {
					if(player.vision[i][j] == 1) message += " " + i + " " + j;
					if(player.vision[i][j] != 0) player.vision[i][j]--;
				}
			}
		player.client.writer.removeVision(player.client, message);
	}
}

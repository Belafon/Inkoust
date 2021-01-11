package Game;

import java.util.ArrayList;

import Moves.Move;
import Server.GetMessage;
import Units.Civilian;
import Units.Soldier;
import Units.Unit;
import likeliness.Dice;
import map.Meadow;
import map.Place;
import map.Village;

/* This is timer. it is watching the time and evaluate new round */
public class New_Round_maker {
	public volatile int duration_of_round = 0;
	public volatile int numberOfRounds;
	public volatile int actualRound;
	public New_Round_maker(final Game game, int duration_of_round, int numberOfRounds) {
		this.duration_of_round = duration_of_round;
		this.numberOfRounds = numberOfRounds;
		this.actualRound = numberOfRounds;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// wait to other players
				int counter = 0;
				while(true) {
					
					// if the game has loaded in each screen of players, the game will start
					if(game.readyToStart)break;
					
					// if the players are waiting too long time, the game ends automatically before it will start
					if(counter > 250) {
						end_game(game);
						return;
					}
					try {
						Thread.sleep(40);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					counter++;
				}
				
				// wait few seconds for slower devices
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// send info about new round to each player, clients app start to count the seconds to the end of round
				for(Player player : game.players) player.client.writer.newRound(player.client, actualRound);
				
				// set that the game is running
				game.isRunning = true;
				
				// this will allow other players to send their moves
				game.receiveMovesFromPlayers = true;
				
				// game loop 
				while(game.isRunning) {
					
					// we need to add some milliseconds because of long duration of sending message to players that new round has started
					try {
						Thread.sleep(duration_of_round + 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					// this stops to receive moves from players
					game.receiveMovesFromPlayers = false;
					
					if(actualRound == 0) {
						// end of game
						end_game(game);
						break;
					}
					
					evaluate_new_round(game);
					
					for(Player player : game.players) player.client.writer.newRound(player.client, actualRound);
					
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					actualRound--;
					game.receiveMovesFromPlayers = true;
				}
			}
		}).start();
	}
	
	private void end_game(Game game) {
		// TODO Auto-generated method stub
		game.isRunning = false;
		String message = "";
		for(Player player : game.players)
			message += player.id + " " + player.client.name + " " + player.gold + " ";
		for(Player player : game.players)
			player.client.writer.setResultOfGame(player.client, message);
		for(Player player : game.players) {
			if(player.client.disconnected)GetMessage.disconnect(player.client, player.client.writer.clientSocket);
			player.client.actual_game = null;
			player.client = null;
		}
	}

	   
	protected void evaluate_new_round(Game game) {
		
		chack_players_moves(game);
		
		try { // wait a second
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		chack_attacks(game);
		
		chack_villages(game);
	}

	private void chack_players_moves(Game game) {
	// TODO Auto-generated method stub
		for(Player player : game.players) {
			// send informations about moves of other players to player
			for(Move move : player.moves) {
				move.changeState(game); // it will change states like last size of units in the place
				for(Player otherPlayer : game.players)
					if(player != otherPlayer) move.influenceOtherPlayer(otherPlayer);
			}
			
			
			
			while(!player.unitsIsFreeForIterate)
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			player.unitsIsFreeForIterate = false;
			
			// This adds the resources to players, which was made by civilian (wood, stone, food, gold)
			for(Unit unit : player.units) 
				if(unit instanceof Civilian && game.map[unit.positionX][unit.positionY].units.size() == 1)
					game.map[unit.positionX][unit.positionY].get_resources(player);
			
			// This sets action to true to all units, so the units can move again
			for(Unit unit : player.units) unit.setAction(true);
			
			player.unitsIsFreeForIterate = true;
			
			// means that you can create new civilian in village again
			for(Village village : player.villages)village.setAction(true);
			
			// clear moves of player
			player.moves = new ArrayList<>();
		}
	
	}
	
	private void chack_attacks(Game game) {
	// TODO Auto-generated method stub
		for(int i = 0; i < game.sizeOfMap; i++) { // lets go through all places of the map
			for(int j = 0; j < game.sizeOfMap; j++) {
				Place place = game.map[i][j];
				if(place.units.size() > 1) { // if there is more than one unit in the place, it leads to problematic situation
					ArrayList<Player> players = new ArrayList<>(); // save all influenced player, which has soldier here
					int sizeOfDice = 0; // lets sum all forces of all influenced players with soldiers in this place
					for(int k = 0; k < place.units.size(); k++) {
						if(place.units.get(k) instanceof Soldier) {
							players.add(place.units.get(k).player);
							sizeOfDice += place.units.get(k).player.force;
						}
					}
					
					if(players.size() > 0) { // if it is zero, it means, the civilians are only in the place.
						Dice dice = new Dice(sizeOfDice); 
						int toss = dice.toss(); // lets loss the winner
						Player winner = null;
						
						int forceActual = 0;
						for(int k = 0; k < players.size(); k++) { // lets find out who is the winner
							forceActual += players.get(k).force;
							if(toss <= forceActual) {
								// player2 is the winner
								winner = players.get(k);
								break;
							}
						}
						
						// player winner is the winner
						winner.setGold(winner.gold + (players.size() - 1) * 2);
						
						// remove all units in the place except winners
						int counter = 0;
						while(counter < place.units.size()) {
							if(winner != place.units.get(counter).player)
								removeUnit(place.units.get(counter), game);
							else counter++;
						}
					}
				}
			}
		}
	}

	// this chack if some soldier is attacking to enemy's village
	private void chack_villages(Game game) {
		// TODO Auto-generated method stub
		for(Player player : game.players) {
			while(!player.unitsIsFreeForIterate)
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			player.unitsIsFreeForIterate = false;
			
			for(Unit unit : player.units) // it iterates all units in the game
				
				// lets find out if the unit is soldier and if he is standing in the village
				if(unit instanceof Soldier && game.map[unit.positionX][unit.positionY] instanceof Village)
					
					//lets find out if the village is enemy's
					if(((Village)game.map[unit.positionX][unit.positionY]).player != unit.player) {
						
						// destroy a village
						unit.player.setGold(unit.player.gold + 5);
						Vision.removeVisionAtPoint(((Village)game.map[unit.positionX][unit.positionY]).player, unit.positionX, unit.positionY, Village.radius);
						game.map[unit.positionX][unit.positionY] = new Meadow(); // recreate the place to meadow
						game.map[unit.positionX][unit.positionY].lastUnits.add(unit); // add the soldier to this place
						game.map[unit.positionX][unit.positionY].units.add(unit);
						for(Player player2 : game.players) // send info to other players about that
							if(player2.vision[unit.positionX][unit.positionY] > 0)player2.client.writer.removeVillage(player2.client, unit.positionX, unit.positionY);
					}
		player.unitsIsFreeForIterate = true;
		}
	}
	
	// this method removes unit from the game
	private void removeUnit(Unit unit, Game game) {
		// TODO Auto-generated method stub
		
		// send info to players
		for(Player player : game.players) player.client.writer.removeUnit(unit, unit.positionX, unit.positionY);
		
		// remove it from all stores
		while(!unit.player.unitsIsFreeForIterate)
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		unit.player.unitsIsFreeForIterate = false;
		
		unit.player.units.remove(unit);
		
		unit.player.unitsIsFreeForIterate = true;
		unit.player.client.actual_game.map[unit.positionX][unit.positionY].units.remove(unit);
		unit.player.client.actual_game.map[unit.positionX][unit.positionY].lastUnits.remove(unit);
		
		// remove vision of the unit's owner
		Vision.removeVisionAtPoint(unit.player, unit.positionX, unit.positionY, unit.vision);
	}
	
}

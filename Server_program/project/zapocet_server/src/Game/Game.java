package Game;

import java.util.ArrayList;

import Server.Server;
import Units.Civilian;
import map.NewMapCreator;
import map.Place;
import map.Village;

/* It contains everything about the game */
public class Game {
	public volatile ArrayList<Player> players;
	public volatile boolean isRunning = false;
	
	// This is the timer which is counting to and of round
	// than it evaluate the round and conflict situations between the players
	public volatile New_Round_maker new_Round_maker;
	public volatile Place[][] map;
	public volatile int sizeOfMap;
	
	// value to get always unique id to units, when they are created 
	public volatile int new_Units_id = 0; 
	public Server server;
	
	// This value is false, if the server wants to ignore messages sent from client
	// with part "set ", like "set move...". This is important to set it to false,
	// when the round has ended and we are waiting to start of another round
	// in this short time, players can not move.
	public volatile boolean receiveMovesFromPlayers = false;
	
	/* this value is set to true, when all of the players of the game
	 * send to server info about that they have started new activity (the game has loaded to screen)
	 * than I know the game can start and nobody has advantage */
	public volatile boolean readyToStart = false;
	
	public Game(int number_of_players, Server server, int sizeOfMap) {
		this.server = server;
		players =  new ArrayList<>();
		this.sizeOfMap = sizeOfMap;
	}
	
	/* Is called, when the creatorOfNewGame is full of players */
	public void start() {
		// first of all I will send info about size of map to all of the players
		for(Player player : players)player.client.writer.getSizeOfMap(player.client, sizeOfMap);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// than I will create a new map with villages
		map = NewMapCreator.createNewMap(this, sizeOfMap);
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// start the game loop in clients
		for(Player player : players) player.client.writer.startGame(player.client);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// add units
		for(Player player : players) {
			Civilian civilian = new Civilian(player, player.villages.get(0).positionX, player.villages.get(0).positionY, new_Units_id++);
			civilian.setVision();
			map[civilian.positionX][civilian.positionY].lastUnits.add(civilian);
		}
		
		// set vision of villages, to see everything right around the villages
		for(Player player : players) {
			player.villages.get(0).setVision();
		}
		
		for(Player player : players)player.client.writer.startGameTimer(player.client, 10, 40);
		new_Round_maker = new New_Round_maker(this, 10000, 40); // in the beginning the duration of round is 15 seconds
		System.out.println("new game has started...");
	}
}

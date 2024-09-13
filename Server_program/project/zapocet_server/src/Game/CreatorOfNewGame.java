package Game;

import Server.Client;
import Server.Server;

/* my simple match making (just wait for enough players) */
public class CreatorOfNewGame {
	// value which says how much players play in one game
	// you can rewrite it how you want 
	public int number_of_players;
	
	// says how much clients are in the queue
	private int actual_number_of_players = 0;
	
	// the object of game is already created
	// it waits to all players
	private volatile Game new_game;
	public Server server;
	
	// this value is still increasing to set to player specific id
	// when the player is created
	public volatile int new_ids_of_player = 0;
	
	public volatile int sizeOfMap;
	public CreatorOfNewGame(Server server, int sizeOfMap, int numberOfPlayers) {
		this.number_of_players = numberOfPlayers;
		this.server = server;
		this.sizeOfMap = sizeOfMap;
		new_game = new Game(number_of_players, server, sizeOfMap);
	}
	public synchronized void setActual_number_of_players(int actual_number_of_players) {
		this.actual_number_of_players = actual_number_of_players;
		for(Player player : new_game.players)
				player.client.writer.setNumberOfPlayersInQueue(player.client, number_of_players - actual_number_of_players);
	}
	
	// this method is called in GetMessage file, when server gets new message from client "lets_play_a_game"
	public synchronized void add_player_to_match_makeing(Client client) {
		if(client.actual_game != null) return; 
		Player player = new Player(client, new_game, new_ids_of_player++);
		new_game.players.add(player);
		client.player = player;
		client.actual_game = new_game;
		if(number_of_players > actual_number_of_players + 1) setActual_number_of_players(actual_number_of_players + 1);
		else {
			// lets start the game...
			setActual_number_of_players(number_of_players);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new_game.start();
					new_game = new Game(number_of_players, server, sizeOfMap);
					setActual_number_of_players(0);
				}
			}).start();
		}
	}
	
	// called in GetMessage file, when message "client_left_the_queue" is receved
	public synchronized void remove_client_form_queue(Client client) {
		// TODO Auto-generated method stub
		if(new_game.players.remove(client.player)) {
			client.player = null;
			client.actual_game = null;
			setActual_number_of_players(actual_number_of_players - 1);
		}
	}
}

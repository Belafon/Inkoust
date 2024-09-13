package Server;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import Game.Game;
import Game.Player;
import Game.Vision;
import Units.Unit;
import map.Village;

public class Client {
	// object which is used to send new messages to the client
	public volatile SendMessage writer;
	public Game actual_game = null;
	public volatile boolean disconnected = false;
	public Server server;
	public String name;
	public volatile Player player;
	public Client(Socket clientSocket, Server server) {
		writer = new SendMessage(clientSocket); 
		this.server = server;
	}
	
	// lets reconnect the player
	public void reconnect() {
		if(actual_game == null) { // if the game has ended, there is no reason to contine
			writer.setResultOfGame(this, "");
			return;
		}
		if(actual_game.new_Round_maker.actualRound <= 1)return; // if the game will end in one round, it is unnecessary to reconnect
		while(true) {
			if(actual_game.receiveMovesFromPlayers)break; // to avoid conflict situations
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writer.startGame(this); // we need to wait until the activity will be ready
	}
	
	public void reconnectSendInfoAboutAll() {
		writer.setFood(this, player.food);
		writer.setWood(this, player.wood);
		writer.setStone(this, player.stone);
		writer.setGold(this, player.gold);
		writer.setForce(this, player.force);
		player.vision = new int[actual_game.sizeOfMap][actual_game.sizeOfMap];
		for(Village village : player.villages)Vision.addVisionToPoint(player, village.positionX, village.positionY, Village.radius);
		for(Unit unit : player.units) {
			Vision.addVisionToPoint(player, unit.positionX, unit.positionY, unit.vision);
			writer.addUnit(unit);
		}
		disconnected = false;
	}
}

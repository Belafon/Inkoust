package Game;

import java.util.ArrayList;

import Moves.Move;
import Server.Client;
import Units.Unit;
import map.Village;

public class Player {
	public volatile int id;
	public volatile int wood; // for new villages
	public volatile int stone; // for attac upgrade (new barracks)
	public volatile int food; // for new villagers
	public volatile int gold; // to win
	public volatile ArrayList<Village> villages = new ArrayList<>();
	
	public volatile boolean unitsIsFreeForIterate = true;
	public volatile ArrayList<Unit> units = new ArrayList<>();
	
	/* it has the same size like map, and where is zero
	 * the player doesnt know what is there */
	public volatile int[][] vision;
	public volatile Client client;
	public volatile int force = 1; // to win the fights (better chance to win in likeliness)
	
	// moves what the player has done in this round
	// forexample move with unit, create new village, create new soldier...
	public volatile ArrayList<Move> moves = new ArrayList<>();
	
	// if the activity has loaded (the game to his screen)
	public volatile boolean ready = false;
	public Player(Client client, Game game, int id){
		this.id = id;
		this.client = client;
		vision = new int[game.sizeOfMap][game.sizeOfMap];	
		setNewId();
	}
	public synchronized void addVillage(Village village) {
		this.villages.add(village);
		client.writer.addVillage(client, village);
	}

	public synchronized void addUnits(Unit unit) {
		while(!unitsIsFreeForIterate)
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		unitsIsFreeForIterate = false;
		this.units.add(unit);
		client.writer.addUnit(unit);
		unitsIsFreeForIterate = true;
	}
	

	public synchronized void setNewId() {
		client.writer.setId(client, id);
	}
	public synchronized void setWood(int wood) {
		this.wood = wood;
		client.writer.setWood(client, wood);
	}
	public synchronized void setStone(int stone) {
		this.stone = stone;
		client.writer.setStone(client, stone);
	}
	public synchronized void setFood(int food) {
		this.food = food;
		client.writer.setFood(client, food);
	}
	public synchronized void setGold(int gold) {
		this.gold = gold;
		client.writer.setGold(client, gold);
	}
	public void setForce(int force) {
		// TODO Auto-generated method stub
		this.force = force;
		client.writer.setForce(client, force);
	}
}

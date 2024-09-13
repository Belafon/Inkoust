package map;

import Game.Game;
import Game.Player;
import Game.Vision;

public class Village extends Place{
	public static volatile double new_ids = 0;
	
	// it means the radius of vision
	// i am not sure if it will work if I rewrite to bigger size
	public static int radius = 1;
	public volatile double id;
	public volatile int positionX;
	public volatile int positionY;
	
	// player, who is the owner
	public volatile Player player;
	public Village(Player player, int positionX, int positionY) {
		this.player = player;
		this.positionX = positionX;
		this.positionY = positionY;
		id = new_ids++;
	}

	// is called, when the civilian is here, in this village
	@Override
	public void get_resources(Player player) {
		if(super.getAction())player.setGold(player.gold + 1);
	}
	
	public void setVision() {
		Vision.addVisionToPoint(player, positionX, positionY, 1);
	}
}

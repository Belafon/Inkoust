package Units;

import Game.Player;
import Game.Vision;

public abstract class Unit {
	public volatile int id;
	public volatile Player player;
	public volatile int positionX;
	public volatile int positionY;
	public volatile int lastPositionX;
	public volatile int lastPositionY;
	public int vision;
	private volatile boolean hasAction = true;
	public Unit(Player player, int positionX, int positionY, int vision, int id) {
		this.id = id;
		this.vision = vision;
		this.player = player;
		this.positionX = positionX;
		this.positionY = positionY;
		this.lastPositionX = positionX;
		this.lastPositionY = positionY;
		player.client.actual_game.map[positionX][positionY].units.add(this);
		player.addUnits(this);
	}
	
	public void setVision() {
		Vision.addVisionToPoint(player, positionX, positionY, vision);
	}
	public void setAction(boolean hasAction) {
		this.hasAction = hasAction;
	}
	public boolean getAction() {
		return hasAction;
	}
}

package map;

import java.util.ArrayList;

import Game.Player;
import Units.Civilian;
import Units.Soldier;
import Units.Unit;

/* The map is created from this places*/
public abstract class Place {
	public volatile ArrayList<Unit> units = new ArrayList<>();
	
	// it contains units which were there last round
	public volatile ArrayList<Unit> lastUnits = new ArrayList<>();
	
	// the place can also have its own action, for example, when 
	// the village is built, player can not to build here
	// new Civilian in the same round
	private volatile boolean hasAction = true;
	
	// this abstract class is called in the end of the round, when 
	// the evaluation of new round is running, when some civilian
	// is in this place, he cans to get resources from this place
	// this method is rewritten, so in Meadow he can get food,
	// gold in village, and wood in the woods.
	public abstract void get_resources(Player player);
	public void removeUnit(Unit unit) {
		units.remove(unit);
	}
	public void setAction(boolean hasAction) {
		this.hasAction = hasAction;
	}
	public boolean getAction() {
		return hasAction;
	}
}

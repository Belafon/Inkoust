package Units;

import Game.Player;

public class Civilian extends Unit{
	public static final int vision = 1;
	public Civilian(Player player, int positionX, int positionY, int id) {
		super(player, positionX, positionY, vision, id);
	}

}

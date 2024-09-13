package map;

import Game.Player;

public class Meadow extends Place{
	@Override
	public void get_resources(Player player) {
		player.setFood(player.food + 1);
	}
}

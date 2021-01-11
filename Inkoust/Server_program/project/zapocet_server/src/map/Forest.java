package map;

import Game.Player;

public class Forest extends Place {

	@Override
	public void get_resources(Player player) {
		player.setWood(player.wood + 1);
	}

}

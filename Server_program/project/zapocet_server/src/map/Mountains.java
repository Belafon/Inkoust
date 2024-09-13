package map;

import Game.Player;

public class Mountains extends Place{

	@Override
	public void get_resources(Player player) {
		player.setStone(player.stone + 1);
	}

}

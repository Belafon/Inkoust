package Moves;

import Game.Game;
import Game.Player;
/* The player can do these moves in the game. 
 * It is important for record of players moves, 
 * because, when the player is moving in the round
 * other players can get new vision and see units of other 
 * player in the place of last round still.*/
public abstract class Move {
	public abstract void influenceOtherPlayer(Player otherPlayer);
	public void changeState(Game game) {
	}
}

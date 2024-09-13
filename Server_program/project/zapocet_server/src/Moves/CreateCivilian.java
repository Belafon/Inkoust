package Moves;

import java.util.ArrayList;

import Game.Game;
import Game.Player;
import Units.Civilian;
import Units.Unit;

public class CreateCivilian extends Move{
	public CreateCivilian(Civilian civilian) {
		this.civilian = civilian;
	}
	Civilian civilian;
	@Override
	public void influenceOtherPlayer(Player otherPlayer) {
		// TODO Auto-generated method stub
		if(otherPlayer.vision[civilian.positionX][civilian.positionY] != 0)
			otherPlayer.client.writer.addUnit(civilian);
	}
	@Override
	public void changeState(Game game) {
		// TODO Auto-generated method stub
		game.map[civilian.positionX][civilian.positionY].lastUnits = (ArrayList<Unit>) game.map[civilian.positionX][civilian.positionY].units.clone();
	}
}

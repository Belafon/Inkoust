package Moves;

import java.util.ArrayList;

import Game.Game;
import Game.Player;
import Units.Unit;
import map.Village;

public class CreateVillage extends Move{
	public CreateVillage(Village village, Unit lastUnit) {
		this.village = village;
		this.lastUnit = lastUnit;
	}
	Unit lastUnit;
	Village village;
	@Override
	public void influenceOtherPlayer(Player otherPlayer) {
		// TODO Auto-generated method stub
		if(otherPlayer.vision[village.positionX][village.positionY] != 0) {
			otherPlayer.client.writer.addVillage(otherPlayer.client, village);
			otherPlayer.client.writer.removeUnit(lastUnit, lastUnit.positionX, lastUnit.positionY);
		}
	}
	
	@Override
	public void changeState(Game game) {
		// TODO Auto-generated method stub
		village.lastUnits = (ArrayList<Unit>) village.units.clone();
	}
}

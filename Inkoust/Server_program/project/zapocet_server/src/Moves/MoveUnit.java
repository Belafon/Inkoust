package Moves;

import java.util.ArrayList;

import Game.Game;
import Game.Player;
import Units.Unit;

public class MoveUnit extends Move{
	public MoveUnit(Unit unit, int lastPositionX, int lastPositionY) {
		this.unit = unit;
		this.lastPositionX = lastPositionX;
		this.lastPositionY = lastPositionY;
	}
	Unit unit;
	int lastPositionX;
	int lastPositionY;
	
	@Override
	public void influenceOtherPlayer(Player otherPlayer) {
		// TODO Auto-generated method stub
		if(otherPlayer.vision[lastPositionX][lastPositionY] != 0)
			otherPlayer.client.writer.removeUnit(unit, lastPositionX, lastPositionY);
		if(otherPlayer.vision[unit.positionX][unit.positionY] != 0)
			otherPlayer.client.writer.addUnit(unit);
	}
	
	@Override
	public void changeState(Game game) {
		// TODO Auto-generated method stub
		unit.lastPositionX = unit.positionX;
		unit.lastPositionY = unit.positionY;
		game.map[lastPositionX][lastPositionY].lastUnits = (ArrayList<Unit>) game.map[lastPositionX][lastPositionY].units.clone();
		game.map[unit.positionX][unit.positionY].lastUnits = (ArrayList<Unit>) game.map[unit.positionX][unit.positionY].units.clone();
	}
}

package Moves;

import java.util.ArrayList;

import Game.Game;
import Game.Player;
import Units.Civilian;
import Units.Soldier;
import Units.Unit;

public class CreateSoldier extends Move{
	public CreateSoldier(Soldier soldier, Unit unitRunning, Unit unitWaiting) {
		this.soldier = soldier;
		this.unitRunning = unitRunning;
		this.unitWaiting = unitWaiting;
	}
	Soldier soldier;
	Unit unitRunning;
	Unit unitWaiting;
	@Override
	public void influenceOtherPlayer(Player otherPlayer) {
		// TODO Auto-generated method stub
		if(unitRunning == null || unitWaiting == null)return;
		if(otherPlayer.vision[soldier.positionX][soldier.positionY] != 0)
			otherPlayer.client.writer.addUnit(soldier);
		if(otherPlayer.vision[unitRunning.positionX][unitRunning.positionY] != 0)
			otherPlayer.client.writer.removeUnit(unitRunning, unitRunning.positionX, unitRunning.positionY);
		if(otherPlayer.vision[unitWaiting.positionX][unitWaiting.positionY] != 0)
			otherPlayer.client.writer.removeUnit(unitWaiting, unitWaiting.positionX, unitWaiting.positionY);
	}
	@Override
	public void changeState(Game game) {
		// TODO Auto-generated method stub
		game.map[soldier.positionX][soldier.positionY].lastUnits = (ArrayList<Unit>) game.map[soldier.positionX][soldier.positionY].units.clone();
		game.map[unitRunning.positionX][unitRunning.positionY].lastUnits = (ArrayList<Unit>) game.map[unitRunning.positionX][unitRunning.positionY].units.clone();
	}
}

package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

import Game.Player;
import Game.Vision;
import Moves.CreateCivilian;
import Moves.CreateSoldier;
import Moves.CreateVillage;
import Moves.MoveUnit;
import Units.Civilian;
import Units.Soldier;
import Units.Unit;
import map.Village;

public class GetMessage {
	public Client client;
	public GetMessage(Socket clientSocket, Client client,  final Server server) {
		this.client = client;
		while(clientSocket.isConnected()) {
			String temp = null;
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				// starts the listening the command line
				Scanner scanner = new Scanner(in);
				
				// lets listen the message from client
				// it will stop this thread until new message will be send from client to server
				temp = scanner.nextLine(); 
			} catch (IOException e) {
				System.out.println(e + " get message");
			} catch (Exception e) {
				// TODO: handle exception
				// if new line was not found, this listener will be canceled
				
				System.out.println(e + " get message 2");
				client.disconnected = true;
				client.server.creatorOfNewGame.remove_client_form_queue(client);
				disconnect(client, clientSocket);
				break;
			}
    		if(temp != null) decomposeTheString(temp, client, clientSocket);
		}
	}
	
	// This method handles a new message from client
	public void decomposeTheString(String value, Client client, Socket clientSocket) {
		// TODO Auto-generated method stub
		System.out.println("New Message ACCEPTED ...  "  + value);
		String[] message = value.split(" ");
		switch(message[0]) {
		case "set":
			set(message);
			break;
		case "lets_play_a_game":
			if(client.actual_game == null)client.server.creatorOfNewGame.add_player_to_match_makeing(client);
			else client.reconnect();
			break;
		case "client_left_the_queue":
			client.server.creatorOfNewGame.remove_client_form_queue(client);
			break;
		case "ready":
			if(!client.disconnected)getReady(client);
			else client.reconnectSendInfoAboutAll();
			break;
		case "name":
			client.name = message[1];
			break;
		}
	}

	public static void disconnect(Client client, Socket clientSocket) {
		// TODO Auto-generated method stub
		client.server.allClients.remove(client);
		client.server.identificateTab.remove(clientSocket.getInetAddress().getHostAddress());
		client.server.IPAdresses.remove(clientSocket.getInetAddress().getHostAddress());
	}

	// sets info about that the player has loaded the game activity
	private synchronized void getReady(Client client) {
		// TODO Auto-generated method stub
		if(client.player == null || client.actual_game == null)return;
		client.player.ready = true;
		boolean isReady = true;
		for(Player player2 : client.actual_game.players)
			if(!player2.ready) {
				isReady = false;
				break;
			}
		if(isReady)client.actual_game.readyToStart = true;
	}

	private void set(String[] message) {
		if(client.actual_game == null)return;
		if(!client.actual_game.receiveMovesFromPlayers)return;
		switch(message[1]) {
			case "create_new_village": // set create_new_village 0 0
				create_new_village(message);
				break;
			case "create_new_civilian": // set create_new_civilian 0 0 Unit.Civilian
				create_new_civilian(message);
				break;
			case "move_unit": // move_unit X1 Y1 X2 Y2 id
				move_unit(message);
				break;
		}
	}
	
	public void create_new_village(String[] message) {
		if(client.player.wood >= 3) {
			int positionX = Integer.parseInt(message[2]);
			int positionY = Integer.parseInt(message[3]);
			if(client.actual_game.map[positionX][positionY] instanceof Village // denied build village on the other village
					|| client.player.vision[positionX][positionY] == 0 // denied build village where you can't see
					|| client.actual_game.map[positionX][positionY].units.size() > 1)return; 
			
			Unit unit = null;
			for(Unit unit2 : client.actual_game.map[positionX][positionY].units) 
				if(unit2.player == client.player && unit2 instanceof Civilian) {
					unit = unit2;
					break;
				}
			
			if(unit == null) return;
			
			if(!unit.getAction()) return;
			
			client.player.setWood(client.player.wood - 3);
			client.actual_game.map[positionX][positionY].removeUnit(unit);
			client.writer.removeUnit(unit, unit.positionX, unit.positionY);
			
			while(!client.player.unitsIsFreeForIterate)
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			client.player.unitsIsFreeForIterate = false;
			client.player.units.remove(unit);
			client.player.unitsIsFreeForIterate = true;
			
			Village village = new Village(client.player, positionX, positionY);
			client.player.moves.add(new CreateVillage(village, unit));
			village.setAction(false);
			client.player.addVillage(village);
			client.actual_game.map[positionX][positionY] = village;
		} else client.writer.not_enough_resources(client); // player has not enough resources
	}
	
	public void create_new_civilian(String[] message) {
		if(client.player.food >= 1) {
			int positionX2 = Integer.parseInt(message[2]);
			int positionY2 = Integer.parseInt(message[3]);
			if(client.actual_game.map[positionX2][positionY2].units.size() > 0)return;
			if(!(client.actual_game.map[positionX2][positionY2] instanceof Village))return;
			if(!client.actual_game.map[positionX2][positionY2].getAction()) return;
			if(client.player.id != ((Village)client.actual_game.map[positionX2][positionY2]).player.id)return;
			
			client.player.setFood(client.player.food - 1);
			Unit unit = new Civilian(client.player, positionX2, positionY2, client.actual_game.new_Units_id++);
			client.player.moves.add(new CreateCivilian((Civilian)unit));
			
			unit.setAction(false);
			unit.setVision();
		} else client.writer.not_enough_resources(client);
	}
	
	public void move_unit(String[] message) {
		int positionX = Integer.parseInt(message[2]);
		int positionY = Integer.parseInt(message[3]);
		int positionX2 = Integer.parseInt(message[4]);
		int positionY2 = Integer.parseInt(message[5]);
		int id = Integer.parseInt(message[6]);
		
		// cannot go to the same place
		if(positionX == positionX2 && positionY == positionY2)return;
		
		// Lets find our Unit
		Unit unit = null;
		for(Unit unit2 : client.actual_game.map[positionX][positionY].units) 
			if(unit2.player == client.player && unit2.id == id) {
				unit = unit2;
				break;
			}
		
		// If the unit is not found return
		if(unit == null) {
			System.out.println("Error, GetMessage unit was not found!!!!");
			return;
		}
		
		// If the unit want to travel to too faraway place return
		if(Math.abs(positionX - positionX2) > unit.vision || Math.abs(positionY - positionY2) > unit.vision) {
			System.out.println("Player is cheating in GetMessage move_unit");
			return;
		}
		
		// If the unit doesnt have action
		if(!unit.getAction()) {
			System.out.println("Error, GetMessage units action is false");
			return;
		}
		
		if(unit instanceof Civilian) {
			if(client.actual_game.map[positionX2][positionY2] instanceof Village) // civilan cant go to enemy Village
				if(((Village)client.actual_game.map[positionX2][positionY2]).player != unit.player)return;
			
			if(client.actual_game.map[positionX2][positionY2].units.size() > 0) {
				
				Civilian playersCivilian = null;
				for(Unit unit2 : client.actual_game.map[positionX2][positionY2].units)
					if(unit2.player.id == client.player.id && unit2 instanceof Civilian) {
						playersCivilian = (Civilian)unit2;
						break;
					}
				
				if(playersCivilian != null) {
					if(client.player.stone < 3) {
						client.writer.not_enough_resources(client);
						return;
					}
					
					// Create Soldier
					client.player.setStone(client.player.stone - 3);
					client.writer.removeUnit(unit, unit.positionX, unit.positionY);
					client.writer.removeUnit(playersCivilian, playersCivilian.positionX, playersCivilian.positionY);
					client.actual_game.map[unit.positionX][unit.positionY].units.remove(unit);
					client.actual_game.map[playersCivilian.positionX][playersCivilian.positionY].units.remove(playersCivilian);
					
					
					while(!unit.player.unitsIsFreeForIterate && !playersCivilian.player.unitsIsFreeForIterate)
						try {
							Thread.sleep(30);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					unit.player.unitsIsFreeForIterate = false;
					playersCivilian.player.unitsIsFreeForIterate = false;
					
					unit.player.units.remove(unit);
					playersCivilian.player.units.remove(playersCivilian);
					
					unit.player.unitsIsFreeForIterate = true;
					playersCivilian.player.unitsIsFreeForIterate = true;
					
					
					Vision.removeVisionAtPoint(client.player, positionX, positionY, unit.vision);
					Soldier soldier = new Soldier(client.player, positionX2, positionY2, client.actual_game.new_Units_id++);
					client.player.moves.add(new CreateSoldier(soldier, unit, playersCivilian));
					soldier.setAction(false);
					
					if(client.actual_game.map[positionX2][positionY2] instanceof Village)
						if(((Village)client.actual_game.map[positionX2][positionY2]).player == soldier.player)
							soldier.player.setForce(unit.player.force + 1);
					return;
				}
			}
			
			if(client.actual_game.map[positionX2][positionY2].lastUnits.size() > 0) {
				boolean goReturn = false;
				for(Unit lastUnit : client.actual_game.map[positionX2][positionY2].lastUnits) {
					if(lastUnit.positionX == positionX2 && lastUnit.positionY == positionY2) {
						goReturn = true;
						break;
					}
				}
				if(goReturn)return;
			}
		}
		
		if(unit instanceof Soldier) {
			Unit playersUnit = null;
			for(Unit unit2 : client.actual_game.map[positionX2][positionY2].units)
				if(unit2.player.id == client.player.id) {
					playersUnit = unit2;
					break;
				}
			if(playersUnit != null) return;
		}
			
			
		// --> finally unit can travel
		unit.setAction(false);
		client.player.moves.add(new MoveUnit(unit, unit.positionX, unit.positionY));
		client.writer.removeUnit(unit, unit.positionX, unit.positionY);
		client.actual_game.map[positionX][positionY].units.remove(unit);
		unit.positionX = positionX2;
		unit.positionY = positionY2;
		client.actual_game.map[positionX2][positionY2].units.add(unit);
		client.writer.addUnit(unit);
		Vision.addVisionToPoint(client.player, positionX2, positionY2, unit.vision);
		Vision.removeVisionAtPoint(client.player, positionX, positionY, unit.vision);
		
		
		// increase force if the unit is soldier in village and it goes into village
		if(client.actual_game.map[positionX2][positionY2] instanceof Village && unit instanceof Soldier)
			if(((Village)client.actual_game.map[positionX2][positionY2]).player == unit.player)
				unit.player.setForce(unit.player.force + 1);
		
		//decrease force if the unit is soldier and it goes out of village
		if(client.actual_game.map[positionX][positionY] instanceof Village && unit instanceof Soldier)
			if(((Village)client.actual_game.map[positionX][positionY]).player == unit.player)
				unit.player.setForce(unit.player.force - 1);
	}
}

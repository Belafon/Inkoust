package Server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import Game.Player;
import Units.Unit;
import map.Village;

/* this class contains all commands which can server send to client*/
public class SendMessage {
	private PrintWriter output;
	public Socket clientSocket;
	public SendMessage(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Send_message " + e);
			e.printStackTrace();
		}
	}
	
	public synchronized PrintWriter getOutput() {
		return output;
	}
	public synchronized void setOutput(PrintWriter output) {
		this.output = output;
	}
	
	// This method sends new message to client
	public synchronized void sendLetter(Client client, String message) {
		System.out.print("Lets send message to client -> ");
		System.out.println(message);
		output.println(message); // blank line between headers and content, very important !
		output.flush(); // flush character output stream buffer
	}

	public void addVision(Client client, String message) {
		sendLetter(client, "get addVision" + message);
	}

	public void removeVision(Client client, String message) {
		sendLetter(client, "get removeVision" + message);
	}
	
	public void startGame(Client client) {
		// TODO Auto-generated method stub
		sendLetter(client, "start_the_game");
	}

	public void addVillage(Client client, Village village) {
		// TODO Auto-generated method stub
		sendLetter(client, "get addVillage " + Integer.toString(village.positionX) + " " + Integer.toString(village.positionY) + " " + village.getClass().getName() + " " + village.player.id);
	}

	public void addUnit(Unit unit) {
		sendLetter(unit.player.client, "get addUnit " + unit.positionX + " " + unit.positionY + " " + unit.getClass().getName() + " " +  unit.id + " " +  unit.player.id);
	}

	public void not_enough_resources(Client client) {
		// TODO Auto-generated method stub
		sendLetter(client, "get not_enough_resources");
	}

	public void setId(Client client, int id) {
		// TODO Auto-generated method stub
		sendLetter(client, "get set_id " + id);
	}

	public void setWood(Client client, int wood) {
		// TODO Auto-generated method stub
		sendLetter(client, "get set_wood " + wood);
	}

	public void setStone(Client client, int stone) {
		// TODO Auto-generated method stub
		sendLetter(client, "get set_stone " + stone);
	}

	public void setFood(Client client, int food) {
		// TODO Auto-generated method stub
		sendLetter(client, "get set_food " + food);
	}

	public void setGold(Client client, int gold) {
		// TODO Auto-generated method stub
		sendLetter(client, "get set_gold " + gold);
	}
	public void setForce(Client client, int force) {
		// TODO Auto-generated method stub
		sendLetter(client, "get set_force " + force);
	}
	public void setNumberOfPlayersInQueue(Client client, int number) {
		sendLetter(client, "get number_of_players_to_wait " + number);
	}

	public void getSizeOfMap(Client client, int sizeOfMap) {
		// TODO Auto-generated method stub
		sendLetter(client, "size_of_map " + sizeOfMap);
	}

	public void startGameTimer(Client client, int durationOfRound, int numberOfRounds) {
		// TODO Auto-generated method stub
		sendLetter(client, "startGameTimer " + durationOfRound + " " + numberOfRounds);
	}

	public void removeUnit(Unit unit, int positionX, int positionY) {
			sendLetter(unit.player.client, "get removeUnit " + positionX + " " + positionY + " " + unit.id + " " + unit.player.id);
	}

	public void newRound(Client client, int actualRound) {
		// TODO Auto-generated method stub
		sendLetter(client, "new_round " + actualRound);
	}

	public void removeVillage(Client client, int positionX, int positionY) {
		// TODO Auto-generated method stub
		sendLetter(client, "get removeVillage " + positionX + " " + positionY);
	}

	public void setResultOfGame(Client client, String message) {
		// TODO Auto-generated method stub
		sendLetter(client, "get setResoult " + message);
	}
}

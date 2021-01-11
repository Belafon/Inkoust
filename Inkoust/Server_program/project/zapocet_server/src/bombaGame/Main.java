package bombaGame;

import java.util.ArrayList;
import java.util.Scanner;

import Server.Client;
import Server.Server;

/* name of program:   Inkoust (Server)
 * 
 * developer:              Ondøej Tichavský
 * 
 * year:                   2020/2021
 * 
 * subject:		           Programování 1 
 * 
 * 
 * 
 * 
 * Main class includes main method
 * Main class contains static dynamic allocated array of Server objects.
 * Also port, which will be used to create new server
 * This application is prepared to create more than one server.
 * When the main class starts, the first server will be automatically created. 
 * 
 * If you want to get online on global network, you need to do port forward in your router and disable your firewall.
 * (not sure if it is enough, I didn't try it)
 * 
 * If you want to go just on local network, you even don't need Internet connection.
 * You can run just through hotspot (without wifi and data) on mobile for example.
 * 
 * */


public class Main {
	public static ArrayList<Server> servers = new ArrayList<>();
	public static int lastPort = 25561;
	public static volatile boolean runServer = true;
	public static void main(String[] args) {
		while(runServer)createNewServer();
	}
	
	
	public static void createNewServer() {
		int sizeOfMap = 0;
		int numberOfPlayers = 0;
		
		Scanner sc = new Scanner(System.in);
		while(true) {
			try {
				System.out.print("Size of map: ");
				sizeOfMap = Integer.parseInt(sc.nextLine().toString());
				System.out.println(sizeOfMap);
				if(sizeOfMap > 1)break;
				else System.out.println("Too small map!");
			} catch(Exception e) {
				System.out.println("Wrong input!");
			}
		}
		
		while(true) {
			try {
				System.out.print("Number of players: ");
				numberOfPlayers = Integer.parseInt(sc.nextLine().toString());
				if(sizeOfMap > numberOfPlayers)break;
				else System.out.println("Too much players in too small map!");
			} catch(Exception e) {
				System.out.println("Wrong input!");
			}
		}
		Server server = new Server(lastPort, sizeOfMap, numberOfPlayers);
		servers.add(server);
		if(server.serverSocket != null)runServer = false;
		
		lastPort++;
	}
}

/*
 * commands which can you write to server console, this commands are automatically sent to all clients
 * 
 * set create_new_village 0 0
 * set create_new_civilian 0 0
 * move_unit X1 Y1 X2 Y2 id
 * set move_unit 1 4 1 5 0
 * set move_unit 1 4 2 4 0
 * set move_unit 1 4 2 4 0
 * */

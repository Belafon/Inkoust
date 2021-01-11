package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Game.CreatorOfNewGame;

/* In class server, server is created here.
 * Server uses ServerSocket library to create server.
 * Server uses Ip address of the device and
 * binds to port 25561 or higher. */

public class Server {
	// actually I thing this variable is never changed, but maybe one day I will change that
	// If this var changes to true, this server will be canceled
	public boolean stopServer = false;
	
	public final int port;
	
	public ServerSocket serverSocket;
	// The Ip addresses are store here to chack, if the client, who just has connected, is new or not
	public ArrayList<String> IPAdresses = new ArrayList<>();
	// Each client has his own object Client, which contains all info about him
	public ArrayList<Client> allClients = new ArrayList<>();
	// In dictionary, I store here Ip addresses as a keys with clients objects
	public HashMap<String, Client> identificateTab = new HashMap<String, Client>();
	
	// createorOfNewGame is object, which makes match making, it stores all clients, which are in the queue
	public volatile CreatorOfNewGame creatorOfNewGame;
	public Server(int port, int sizeOfMap, int numberOfPlayers) {
		creatorOfNewGame = new CreatorOfNewGame(this, sizeOfMap, numberOfPlayers);
		this.port = port;
		
		if(!createServerSocket())return;
		
		setListener();
		
		final Server server = this;
		
		// Here in new thread we start with catching new clients, which want to connect 
		new Thread(new Runnable() {
			@Override
			public void run() {
				chackForNewClients(server);
			}
		}).start();
	}
	
	private boolean createServerSocket() {
		try {
			this.serverSocket = new ServerSocket(port);
			try {
				System.out.println();
				System.out.println("Local ip = " + InetAddress.getLocalHost().getHostAddress());
				try {
					System.out.println("External ip = " + getExternalIp());
				}catch(Exception e) {
					System.out.println("Network exception");
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(port);
			System.out.println();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	// Start with catching new clients, which want to connect to server
	private void chackForNewClients(final Server server) {
		while(!stopServer) {
			try {
				// accept new connection with client, for each new message this connection has to be set
				Socket clientSocket = this.serverSocket.accept(); 
				
				// worker thread -> to handle the message from client
				new Thread(new Runnable() {
					public void run() {
						String hostIPAdress = clientSocket.getInetAddress().getHostAddress();
						Client client = null;
						if(IPAdresses.indexOf(hostIPAdress) == -1) client = addNewClient(clientSocket, hostIPAdress); // new client has coneccted
						else client = identificateTab.get(hostIPAdress); // old client has connected again -> he is playing a game right now
						new GetMessage(clientSocket, client, server);	 // recever of new messages
						//client.writer = new SendMessage(clientSocket); 
						//if(client.disconnected)client.reconnect();
					}					
				}).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// It creates new client object and saves info about him 
	private Client addNewClient(Socket clientSocket, String hostIPAdress) {
		System.out.println("New device connected");
		Client client = new Client(clientSocket, this);
		
		// save players ipAddress for next identification
		IPAdresses.add(hostIPAdress);
		allClients.add(client);
		identificateTab.put(hostIPAdress, client);
		return client;
	}
	
	// This is listener, which listens the text, which can you write to command line
		// It automatically sends the text to all clients of this server
	public void setListener() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					Scanner sc = new Scanner(System.in);
					String message = "";
					try {
						message = sc.nextLine().toString();
						if(message.equals("disconnect")) {
							stopServer = true;
							sc.close();
							System.out.println("Server is ending...");
							System.exit(0);
							return;
						}
						for(Client client : allClients)client.writer.sendLetter(client, message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	 public static String getExternalIp() throws Exception {
	        URL whatismyip = new URL("http://checkip.amazonaws.com");
	        BufferedReader in = null;
	        try {
	            in = new BufferedReader(new InputStreamReader(
	                    whatismyip.openStream()));
	            String ip = in.readLine();
	            return ip;
	        } finally {
	            if (in != null) {
	                try {
	                    in.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }
}

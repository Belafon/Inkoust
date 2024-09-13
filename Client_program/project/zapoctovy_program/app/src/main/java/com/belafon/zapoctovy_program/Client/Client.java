package com.belafon.zapoctovy_program.Client;


import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.belafon.zapoctovy_program.Game.Units.Civilian;
import com.belafon.zapoctovy_program.Game.Map.Forest;
import com.belafon.zapoctovy_program.Game.MainPlayer;
import com.belafon.zapoctovy_program.Game.Map.Meadow;
import com.belafon.zapoctovy_program.Game.Map.Place;
import com.belafon.zapoctovy_program.Game.Player;
import com.belafon.zapoctovy_program.Game.Units.Soldier;
import com.belafon.zapoctovy_program.Game.Timer;
import com.belafon.zapoctovy_program.Game.Units.Unit;
import com.belafon.zapoctovy_program.Game.Map.Village;
import com.belafon.zapoctovy_program.GameActivity;
import com.belafon.zapoctovy_program.MainActivity;
import com.belafon.zapoctovy_program.MenuActivity;
import com.belafon.zapoctovy_program.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    private static final String TAG = "Client";
    public static Socket clientSocket;
    public static String ip;
    public static int port = 25561;
    public static boolean flow = false; // controls the network connection
    public static String name;

    public Client(String name){
        this.name = name;
        connect();
    }

    public static void setName(String text) {
        if(!name.equals(text)){
            name = text;
            MainActivity.dataLibrary.saveDataString(MainActivity.actual_activity, name, "name");
        }

        write("name " + name);
    }

    // This method sets the connection to server
    public void connect() {
        Log.d(TAG, "run: CREATE CLIENT --- LETS CONNECT");
        boolean flow2 = true; // to gets info about flow
          this.ip = MainActivity.ip;
        try {
            try{
                this.clientSocket = new Socket(ip, port);
            }catch (UnknownHostException e){
                flow2 = false;
            } catch (IOException i) {
                MainActivity.actual_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.actual_activity, "Network exception...", Toast.LENGTH_SHORT).show();
                    }
                });
                flow2 = false;
            }
            if(clientSocket == null) flow2 = false;
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            startListener(in);
        } catch (IOException e) {
            flow2 = false;
        } catch (Exception e){
            flow2 = false;
        }
        flow = flow2;
        write_text = new Write_text();
        if(flow)MainActivity.openMenuActivity();
    }

    // sets listener of messages from server
    private void startListener(final BufferedReader in) {
        // TODO Auto-generated method stub
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while(true) {
                    String string = null;
                    try {
                        string = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        Log.d(TAG, "run: error " + e);
                        return;
                    }
                    if(string != null) {
                        makeThreadWorker(string);
                    }
                }
            }
            private void makeThreadWorker(final String string) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: NEW MESSAGE : ------->   " + string);
                        decomposeTheString(string);
                    }
                }).start();
            }
        });
        thread.start();
    }

    public static Write_text write_text;
    public static void write(String message){
        try{
            write_text.write(message);
        }catch (Exception e){
            Log.d(TAG, "write: ERROR " + e);
            return;
        }
        Log.d(TAG, "write: text was written  ->  " + message);
    }

    private static Timer timer;
    public synchronized static void decomposeTheString(String value) {
        // TODO Auto-generated method stub
        final String[] message = value.split(" ");
        String typeAction;
        typeAction = message[0];
        switch(typeAction) {
            case "get":// getter
                get(message);
                break;
            case "start_the_game":
                MainActivity.actual_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MenuActivity)MainActivity.actual_activity).goToLastActivity = true;
                        GameActivity.disconnected = false;
                        Intent menuIntent = new Intent(MainActivity.actual_activity, GameActivity.class);
                        MainActivity.actual_activity.startActivity(menuIntent);
                    }
                });
                break;
            case "startGameTimer": // startGameTimer durationOfRound rounds
                timer = new Timer(Integer.parseInt(message[1]), Integer.parseInt(message[2]));
                break;
            case "new_round":
                MainActivity.actual_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (MainActivity.actual_activity instanceof GameActivity)
                            ((TextView)MainActivity.actual_activity.findViewById(R.id.current_round)).setText("Round: " + message[1]);
                    }
                });
                timer.makeNewRound = true;
                break;
            case "size_of_map":
                GameActivity.sizeOfMap = Integer.parseInt(message[1]);
                GameActivity.map = new Place[GameActivity.sizeOfMap][GameActivity.sizeOfMap];
                for (int i = 0; i < GameActivity.sizeOfMap; i++) {
                    for (int j = 0; j < GameActivity.sizeOfMap; j++) {
                        GameActivity.map[i][j] = new Place(i, j);
                    }
                }
                break;
            default:
                Log.d(TAG, "decomposeTheString: Some wierd default!!!!");
                Log.d(TAG, "decomposeTheString: " + value);
                break;
        }

    }
    public final static Place[][] pl = GameActivity.map;

    private static void get(final String[] message) {
        switch (message[1]){
            case "number_of_players_to_wait":
                DecomposeMessage.number_of_players_to_wait(message);
                break;
            case "set_id":
                DecomposeMessage.set_id(message);
                break;
            case "set_wood":
                DecomposeMessage.set_wood(message);
                break;
            case "set_stone":
                DecomposeMessage.set_stone(message);
                break;
            case "set_food":
                DecomposeMessage.set_food(message);
                break;
            case "set_gold":
                DecomposeMessage.set_gold(message);
                break;
            case "set_force":
                DecomposeMessage.set_force(message);
                break;
            case "not_enough_resources":
                MainActivity.actual_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.actual_activity, "Not enough resources", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "addUnit":
                DecomposeMessage.addUnit(message);
                break;
            case "removeUnit":
                DecomposeMessage.remove_unit(message);
                break;
            case "addVillage":
                DecomposeMessage.addVillage(message);
                break;
            case "removeVillage":
                DecomposeMessage.remove_village(message);
                break;
            case "addVision": // get addVision  3 3 map.Meadow 3 4 map.Meadow 3 5 map.Forest
                DecomposeMessage.addVision(message);
                break;
            case "removeVision"://get removeVision
                DecomposeMessage.removeVision(message);
                break;
            case "setResoult":
                DecomposeMessage.setResoult(message);
                break;
        }
    }

}

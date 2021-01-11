package com.belafon.zapoctovy_program;

import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.belafon.zapoctovy_program.Client.Client;
import com.belafon.zapoctovy_program.Client.Write_text;
import com.belafon.zapoctovy_program.Game.Units.Civilian;
import com.belafon.zapoctovy_program.Game.MainPlayer;
import com.belafon.zapoctovy_program.Game.Map.Place;
import com.belafon.zapoctovy_program.Game.Player;
import com.belafon.zapoctovy_program.Game.Units.Unit;
import com.belafon.zapoctovy_program.Game.Map.Village;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    public static volatile MainPlayer player;
    public static int sizeOfPlace = 180;
    public static int sizeOfMap;
    public static ArrayList<Player> players = new ArrayList<>();
    public static boolean gameIsRunning = false;
    public static boolean disconnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.actual_activity = this;
        hideBars();
        setContentView(R.layout.activity_game);
        createMap();

        // if this activity is loaded, the message will be send to server
        // with info that the main activity was loaded to start the game when all clients
        // has loaded GameActivity
        if(!gameIsRunning){
            Client.write("ready");
            gameIsRunning = true;
        }

        if(disconnected){
            // device need to reconnect
            Client.write("reconnect");
            disconnected = false;
        }else{
            // device is connected

        }
    }

    // called when this activity is closed
    public void onDestroy(){
        super.onDestroy();
        // device opened activity again, we need to reconnect to server and get info about the game
        if(!disconnected){
            Client.write("disconnectGame");
            disconnected = true;
        }
    }

    // player clicked to build a village button
    public void build_village(View view) {
        moveMode = null;
        choseMode = false;
        createNewVillageMode = true;
        if(selected != null)selected.setVisibility(View.GONE);
    }

    public static Place[][] map;

    // few modes which says what will next clients touch on the screen to do.
    public volatile Unit moveMode = null;
    public volatile boolean createNewVillageMode = false;
    public volatile boolean choseMode = true;
    public static volatile ImageView selected;

    public void createMap(){
        // this sets the size of RelativeLayout, which contains all buttons of places
        //findViewById(R.id.map).setLayoutParams(new ScrollView.LayoutParams(sizeOfMap * (sizeOfPlace) + 200, sizeOfMap * (sizeOfPlace) + 200));

        ImageView background = new ImageView(this);

        // the size of background is set here, it depends on the size of screen
        if(sizeOfMap * sizeOfPlace < MainActivity.screenHeight){
            findViewById(R.id.map).setLayoutParams(new ScrollView.LayoutParams(MainActivity.screenWidth, MainActivity.screenHeight));
            background.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.screenWidth, MainActivity.screenHeight));
        } else if (sizeOfMap * sizeOfPlace < MainActivity.screenWidth){
            findViewById(R.id.map).setLayoutParams(new ScrollView.LayoutParams(MainActivity.screenWidth, sizeOfMap * sizeOfPlace + 200));
            background.setLayoutParams(new RelativeLayout.LayoutParams(MainActivity.screenWidth, sizeOfMap * sizeOfPlace + 200));
        } else {
            findViewById(R.id.map).setLayoutParams(new ScrollView.LayoutParams(sizeOfMap * sizeOfPlace + 200, sizeOfMap * sizeOfPlace + 200));
            background.setLayoutParams(new RelativeLayout.LayoutParams(sizeOfMap * sizeOfPlace + 200, sizeOfMap * sizeOfPlace + 200));
        }
        background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        background.setImageResource(R.drawable.pozadi);
        ((RelativeLayout)findViewById(R.id.map)).addView(background);

        // lets draw all places in the map
        for (int x = 0; x < sizeOfMap; x++) {
            for (int y = 0; y < sizeOfMap; y++) { // draw each place, which is visible for the player
                map[x][y].button = new Button(this);
                map[x][y].picture = new ImageView(this);
                final ScrollView.LayoutParams layoutParams = new ScrollView.LayoutParams(sizeOfPlace, sizeOfPlace);
                layoutParams.setMargins(120 + (sizeOfPlace) * x, 120 + (sizeOfPlace) * y, 0, 0);
                map[x][y].button.setLayoutParams(layoutParams);
                map[x][y].picture.setLayoutParams(layoutParams);

                map[x][y].button.setId(x * 10000 + y);

                // when this button is clicked, this method is called
                map[x][y].button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int id = view.getId();
                            final int positionX = id / 10000;
                            final int positionY = id % 10000;
                            if(moveMode != null) moveMode(positionX, positionY);
                            else if(createNewVillageMode) createNewVillageMode(positionX, positionY);
                            else if(choseMode) choseMode(positionX, positionY);
                        }
                    }).start();
                    }
                });
                map[x][y].button.setBackgroundColor(this.getResources().getColor(R.color.transparent));
                map[x][y].picture.setVisibility(View.INVISIBLE);
                ((RelativeLayout)findViewById(R.id.map)).addView(map[x][y].button);
                ((RelativeLayout)findViewById(R.id.map)).addView(map[x][y].picture);
            }
        }
    }

    private void choseMode(final int positionX, final  int positionY) {
        Unit unit = null;
        for(Unit unit2 : map[positionX][positionY].units){
            if(unit2.player == player.id) {
                unit = unit2;
                break;
            }
        }

        if(map[positionX][positionY].typeOfPlace instanceof Village && unit == null ){
            if(((Village)map[positionX][positionY].typeOfPlace).player.id != player.id){
                Log.d(TAG, "run: Not my village -> " + ((Village)map[positionX][positionY].typeOfPlace).player.id + " " + player.id);
                return;
            }
            Client.write("set create_new_civilian " + positionX  + " " + positionY + " " + "Unit.Civilian");
            return;
        }

        if(unit == null)return;

        choseMode = false;
        moveMode = unit;
        MainActivity.actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selected = new ImageView(MainActivity.actual_activity);
                selected.setImageResource(R.drawable.selected);
                selected.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(sizeOfPlace, sizeOfPlace);
                layoutParams1.setMargins(120 + (sizeOfPlace) * positionX, 120 + (sizeOfPlace) * positionY, 0, 0);
                selected.setLayoutParams(layoutParams1);
                ((RelativeLayout)findViewById(R.id.map)).addView(selected);
                for(Unit unit2 : map[positionX][positionY].units){
                    ((RelativeLayout)findViewById(R.id.map)).removeView(unit2.picture);
                    ((RelativeLayout)findViewById(R.id.map)).addView(unit2.picture);
                }
            }
        });
    }

    private void moveMode(int positionX, int positionY) {
        Unit unit = null;
        for (Unit unit2 : map[ positionX][positionY].units) // sice lineární časová složitost, ale pro velmi malý vstup
            if(unit2.player == GameActivity.player.id){
                unit = unit2;
                break;
            }
        if(unit != null && !(unit instanceof Civilian))return;
        Client.write("set move_unit " + moveMode.positionX + " " + moveMode.positionY + " " + positionX + " " + positionY + " " + moveMode.id);
        moveMode = null;
        MainActivity.actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selected.setVisibility(View.GONE);
            }
        });
        choseMode = true;
    }

    private void createNewVillageMode(int positionX, int positionY){
        Client.write("set create_new_village " + positionX + " " + positionY);
        createNewVillageMode = false;
        choseMode = true;
    }
    @Override
    public void onBackPressed(){ // When client press back button in navigation bar, do nothing
    }
    // HIDE BARS
    private int currentApiVersion;
    public void hideBars(){
        getSupportActionBar().hide(); // hide the title bar
        // HIDE NAVIGATION BAR
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            {

                @Override
                public void onSystemUiVisibilityChange(int visibility)
                {
                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) decorView.setSystemUiVisibility(flags);
                }
            });
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}

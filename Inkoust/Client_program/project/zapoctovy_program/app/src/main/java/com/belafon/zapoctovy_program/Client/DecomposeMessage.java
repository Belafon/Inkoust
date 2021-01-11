package com.belafon.zapoctovy_program.Client;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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

import com.belafon.zapoctovy_program.Game.MainPlayer;
import com.belafon.zapoctovy_program.Game.Map.Forest;
import com.belafon.zapoctovy_program.Game.Map.Meadow;
import com.belafon.zapoctovy_program.Game.Map.Place;
import com.belafon.zapoctovy_program.Game.Map.Village;
import com.belafon.zapoctovy_program.Game.Player;
import com.belafon.zapoctovy_program.Game.Units.Civilian;
import com.belafon.zapoctovy_program.Game.Units.Soldier;
import com.belafon.zapoctovy_program.Game.Units.Unit;
import com.belafon.zapoctovy_program.GameActivity;
import com.belafon.zapoctovy_program.MainActivity;
import com.belafon.zapoctovy_program.MenuActivity;
import com.belafon.zapoctovy_program.R;

import java.util.ArrayList;

public class DecomposeMessage {
    private static final String TAG = "DecomposeMessage";
    public static void number_of_players_to_wait(final String[] message){
        if(MainActivity.actual_activity.getLocalClassName().equals("MenuActivity")){
            if(Integer.parseInt(message[2]) == 0){
                MainActivity.actual_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.actual_activity.findViewById(R.id.goBack).setClickable(false);
                        ((TextView)MainActivity.actual_activity.findViewById(R.id.number_of_players_in_queue)).setText("The game is starting...");
                    }
                });
            }else{
                final String message3 = "Waiting for " + message[2] + " other players...";
                MainActivity.actual_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)MainActivity.actual_activity.findViewById(R.id.number_of_players_in_queue)).setText(message3);
                    }
                });
            }
        }
    }
    public static void set_id(final String[] message){
        GameActivity.player = new MainPlayer();
        GameActivity.player.id = Integer.parseInt(message[2]);
        GameActivity.players.add(GameActivity.player);
    }
    public static void set_wood(final String[] message){
        GameActivity.player.wood = Integer.parseInt(message[2]);
        final String message3 = "Wood: " + message[2];
        if(MainActivity.actual_activity.getLocalClassName().equals("GameActivity"))
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)MainActivity.actual_activity.findViewById(R.id.wood)).setText(message3);
                }
            });
    }
    public static void set_stone(final String[] message){
        GameActivity.player.stone = Integer.parseInt(message[2]);
        final String text = "Stone: " + message[2];
        if(MainActivity.actual_activity.getLocalClassName().equals("GameActivity"))
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)(MainActivity.actual_activity.findViewById(R.id.stone))).setText(text);
                }
            });
    }
    public static void set_food(final String[] message){
        GameActivity.player.food = Integer.parseInt(message[2]);
        final String message5 = "Food: " + message[2];
        if(MainActivity.actual_activity.getLocalClassName().equals("GameActivity"))
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)MainActivity.actual_activity.findViewById(R.id.food)).setText(message5);
                }
            });
    }
    public static void set_gold(final String[] message){
        GameActivity.player.gold = Integer.parseInt(message[2]);
        final String text = "Gold: " + message[2];
        if(MainActivity.actual_activity.getLocalClassName().equals("GameActivity"))
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)MainActivity.actual_activity.findViewById(R.id.gold)).setText(text);
                }
            });
    }
    public static void set_force(final String[] message){
        GameActivity.player.gold = Integer.parseInt(message[2]);
        final String text = "Force: " + message[2];
        if(MainActivity.actual_activity.getLocalClassName().equals("GameActivity"))
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView)MainActivity.actual_activity.findViewById(R.id.force)).setText(text);
                }
            });
    }
    public static void addUnit(final String[] message){
        int positionX = Integer.parseInt(message[2]);
        int positionY = Integer.parseInt(message[3]);
        int id = Integer.parseInt(message[5]);
        int player = Integer.parseInt(message[6]);
        Player player2 = chackNewPlayer(player);
        Unit unit8 = null;
        switch (message[4]) {
            case "Units.Civilian":
                unit8 = new Civilian(positionX, positionY, player, id, player2);
                GameActivity.map[positionX][positionY].units.add(unit8);
                break;
            case "Units.Soldier":
                unit8 = new Soldier(positionX, positionY, player, id, player2);
                GameActivity.map[positionX][positionY].units.add(unit8);
                break;
        }
        final Unit unit15 = unit8;
        setDrawImageOfUnit(unit15.positionX, unit15.positionY, unit15.positionX * GameActivity.sizeOfPlace + 120, unit15.positionY * GameActivity.sizeOfPlace + 120);
    }
    public static void remove_unit(final String[] message){
        final int positionX = Integer.parseInt(message[2]);
        final int positionY = Integer.parseInt(message[3]);
        int id2 = Integer.parseInt(message[4]);
        for(final Unit unit : GameActivity.map[positionX][positionY].units)
            if(unit.id == id2){
                MainActivity.actual_activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ViewManager) unit.
                                picture.
                                getParent()).
                                removeView(unit.picture);
                        ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).removeView(unit.label);
                    }
                });
                GameActivity.map[positionX][positionY].units.remove(unit);
                break;
            }
        setDrawImageOfUnit(positionX, positionY, positionX * GameActivity.sizeOfPlace + 120, positionY * GameActivity.sizeOfPlace + 120);
    }
    public static void addVillage(final String[] message){
        final int positionX = Integer.parseInt(message[2]);
        final int positionY = Integer.parseInt(message[3]);
        if(GameActivity.map == null || GameActivity.map[positionX][positionY].picture == null)return;
        final int playersId2 = Integer.parseInt(message[5]);
        final Player player3 = chackNewPlayer(playersId2);
        GameActivity.map[positionX][positionY].typeOfPlace = new Village(playersId2, player3);
        MainActivity.actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(GameActivity.player.id != playersId2)scrollToPoint(positionX, positionY);
                ImageView imageView = player3.createImageView();
                ((Village)GameActivity.map[positionX][positionY].typeOfPlace).label = imageView;
                imageView.setLayoutParams(GameActivity.map[positionX][positionY].picture.getLayoutParams());
                ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).addView(imageView);
                GameActivity.map[positionX][positionY].picture.setImageResource(R.drawable.village);
            }
        });
    }
    public static void remove_village(final String[] message){
        final int positionX = Integer.parseInt(message[2]);
        final int positionY = Integer.parseInt(message[3]);
        final Place place = GameActivity.map[positionX][positionY];
        MainActivity.actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).removeView(place.picture);
                place.picture.setImageDrawable(MainActivity.actual_activity.getDrawable(R.drawable.meadow));
                ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).removeView(((Village)place.typeOfPlace).label);
                ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).addView(place.picture);
                place.typeOfPlace = new Meadow();
            }
        });
    }
    public static void addVision(final String[] message){
        int pocitadlo = 2;
        while(pocitadlo < message.length) {
            final int positionX = Integer.parseInt(message[pocitadlo]);
            final int positionY = Integer.parseInt(message[pocitadlo + 1]);
            pocitadlo += 2;

            switch (message[pocitadlo]) {
                case "map.Meadow":
                    GameActivity.map[positionX][positionY].typeOfPlace = new Meadow();
                    MainActivity.actual_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GameActivity.map[positionX][positionY].picture.setImageResource(R.drawable.meadow);
                        }
                    });
                    pocitadlo += 1;
                    break;
                case "map.Forest":
                    GameActivity.map[positionX][positionY].typeOfPlace = new Forest();
                    MainActivity.actual_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GameActivity.map[positionX][positionY].picture.setImageResource(R.drawable.forest);
                        }
                    });
                    pocitadlo += 1;
                    break;
                case "map.Mountains":
                    GameActivity.map[positionX][positionY].typeOfPlace = new Forest();
                    MainActivity.actual_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GameActivity.map[positionX][positionY].picture.setImageResource(R.drawable.mountains);
                        }
                    });
                    pocitadlo += 1;
                    break;
                case "map.Village":
                    int playersIDD = Integer.parseInt(message[pocitadlo + 1]);
                    final Player playerrr = chackNewPlayer(playersIDD);
                    GameActivity.map[positionX][positionY].typeOfPlace = new Village(playersIDD, playerrr);
                    MainActivity.actual_activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollToPoint(positionX, positionY);
                            GameActivity.map[positionX][positionY].picture.setImageResource(R.drawable.village);
                            ImageView imageView = playerrr.createImageView();
                            imageView.setLayoutParams(GameActivity.map[positionX][positionY].picture.getLayoutParams());
                            ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).addView(imageView);
                            ((Village)GameActivity.map[positionX][positionY].typeOfPlace).label = imageView;
                        }
                    });
                    pocitadlo += 2;
                    break;
            }

            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GameActivity.map[positionX][positionY].picture.setVisibility(View.VISIBLE);
                }
            });
            while (!message[pocitadlo].equals(";")) {
                int uiId = Integer.parseInt(message[pocitadlo]);
                int playersId = Integer.parseInt(message[pocitadlo + 1]);
                Player player4 = chackNewPlayer(playersId);
                Unit unit9 = null;
                switch (message[pocitadlo + 2]) {
                    case "Units.Civilian":
                        unit9 = new Civilian(positionX, positionY, playersId, uiId, player4);
                        GameActivity.map[positionX][positionY].units.add(unit9);
                        break;
                    case "Units.Soldier":
                        unit9 = new Soldier(positionX, positionY, playersId, uiId, player4);
                        GameActivity.map[positionX][positionY].units.add(unit9);
                        break;
                }
                final Unit unit16 = unit9;
                setDrawImageOfUnit(unit16.positionX, unit16.positionY, unit16.positionX * GameActivity.sizeOfPlace + 120, unit16.positionY * GameActivity.sizeOfPlace + 120);
                pocitadlo += 3;
            }
            pocitadlo++;
        }
    }
    public static void removeVision(final String[] message){
        int pocitadlo2 = 2;
        while(pocitadlo2 < message.length){
            final int positionX = Integer.parseInt(message[pocitadlo2]);
            final int positionY = Integer.parseInt(message[pocitadlo2 + 1]);
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    GameActivity.map[positionX][positionY].picture.setVisibility(View.INVISIBLE);
                    for(Unit unit55 : GameActivity.map[positionX][positionY].units){
                        ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).removeView(unit55.picture);
                        ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).removeView(unit55.label);
                    }
                    if ( GameActivity.map[positionX][positionY].typeOfPlace instanceof Village){
                        ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).removeView(((Village)GameActivity.map[positionX][positionY].typeOfPlace).label);
                    }
                    GameActivity.map[positionX][positionY].units = new ArrayList<>();
                    GameActivity.map[positionX][positionY].typeOfPlace = null;
                }
            });
            pocitadlo2 += 2;
        }
    }

    public static void setResoult(String[] message) {
        Player.nextColor = 0;
        Player.next_type_of_color = 0;
        final LinearLayout table = MainActivity.actual_activity.findViewById(R.id.result_table);
        ArrayList<LinearLayout> winners = new ArrayList<>();
        int bestGold = 0;
        for (int i = 2; i < message.length; i += 3) {
            int playersId = Integer.parseInt(message[i]);
            String playersName = message[i + 1];
            final int gold = Integer.parseInt(message[i + 2]);
            final LinearLayout linearLayout = new LinearLayout(MainActivity.actual_activity);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 10, 0, 0);
            linearLayout.setLayoutParams(layoutParams);
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    table.addView(linearLayout);
                    MainActivity.actual_activity.findViewById(R.id.scrollResultTable).setVisibility(View.VISIBLE);
                }
            });

            final TextView name = new TextView(MainActivity.actual_activity);
            name.setLayoutParams(new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
            name.setTextSize(20);
            name.setGravity(View.TEXT_ALIGNMENT_CENTER);
            name.setPadding(20, 0, 20, 0);
            Player player = null;
            for(Player findPlayer : GameActivity.players)
                if(findPlayer.id == playersId){
                    player = findPlayer;
                    break;
                }
            if(player == null)player = new Player();

            if(gold == bestGold){
                winners.add(linearLayout);
            }else if(gold > bestGold){
                bestGold = gold;
                winners = new ArrayList<>();
                winners.add(linearLayout);
            }

            name.setText(playersName);
            name.setBackgroundColor(player.Color);

            final TextView goldText = new TextView(MainActivity.actual_activity);
            goldText.setLayoutParams(new LinearLayout.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
            goldText.setText("Gold :  " + gold);
            goldText.setPadding(40, 0, 0, 40);
            goldText.setTextColor(Color.YELLOW);
            goldText.setTextSize(20);
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    linearLayout.addView(name);
                    linearLayout.addView(goldText);
                }
            });
        }
        final Button goToMenu = new Button(MainActivity.actual_activity);
        goToMenu.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        goToMenu.setHint("Go back to menu");
        goToMenu.setHintTextColor(Color.BLACK);
        goToMenu.setTextSize(20);
        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.players = new ArrayList<>();
                GameActivity.player = new MainPlayer();
                GameActivity.map = null;
                GameActivity.selected = null;
                GameActivity.gameIsRunning = false;
                Intent menuIntent = new Intent(MainActivity.actual_activity, MenuActivity.class);
                MainActivity.actual_activity.startActivity(menuIntent);
            }
        });
        MainActivity.actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                table.addView(goToMenu);
            }
        });
        for(final LinearLayout winner : winners){
            final TextView win = new TextView(MainActivity.actual_activity);
            win.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            win.setText("The winner!");
            win.setTextColor(Color.RED);
            win.setTextSize(20);
            win.setPadding(10, 0, 0, 0);
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    winner.addView(win);
                }
            });
        }
    }

    public static void scrollToPoint(int positionX, int positionY) {
        HorizontalScrollView scrollhorizontal = MainActivity.actual_activity.findViewById(R.id.horizontalScrollView);
        ScrollView scrollVertical = MainActivity.actual_activity.findViewById(R.id.verticalScrollView);
        int x = (positionX * GameActivity.sizeOfPlace + 120)  - (MainActivity.screenWidth / 2) + (GameActivity.sizeOfPlace / 2);
        int y = (GameActivity.sizeOfPlace  * positionY + 120) - (MainActivity.screenHeight / 2) + (GameActivity.sizeOfPlace / 2);
        scrollhorizontal.smoothScrollTo(x, y);
        scrollVertical.smoothScrollTo(x, y);
        Log.d(TAG, "scrollToPoint: SCROLL TO " + x + " " + y);
    }

    public synchronized static Player chackNewPlayer(int id){
        Player newPlayer = null;
        for(Player player : GameActivity.players){
            if(player.id == id){
                newPlayer = player;
                break;
            }
        }

        if(newPlayer == null){
            newPlayer = new Player();
            newPlayer.id = id;
            GameActivity.players.add(newPlayer);
        }

        return newPlayer;
    }

    public static void setDrawImageOfUnit(int positionX, int positionY, int marginX, int marginY){
        int sizefOfDifferenceOfMargin = 25;
        int x = 0;
        int y = 0;
        if((GameActivity.map[positionX][positionY].units.size()) % 2 == 1){
            x = marginX - ((GameActivity.map[positionX][positionY].units.size()) / 2) * sizefOfDifferenceOfMargin;
            y = marginY - ((GameActivity.map[positionX][positionY].units.size()) / 2) * sizefOfDifferenceOfMargin;
        }else{
            x = marginX - (sizefOfDifferenceOfMargin / 2) - ((GameActivity.map[positionX][positionY].units.size()) / 2 - 1) * sizefOfDifferenceOfMargin;
            y = marginY - (sizefOfDifferenceOfMargin / 2) - ((GameActivity.map[positionX][positionY].units.size()) / 2 - 1) * sizefOfDifferenceOfMargin;
        }

        for(final Unit unit : GameActivity.map[positionX][positionY].units){
            final RelativeLayout.LayoutParams params = ((RelativeLayout.LayoutParams)unit.picture.getLayoutParams());
            params.setMargins(x, y, 0, 0);
            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    unit.picture.setLayoutParams(params);
                }
            });
            x += sizefOfDifferenceOfMargin;
            y += sizefOfDifferenceOfMargin;
        }
    }
}

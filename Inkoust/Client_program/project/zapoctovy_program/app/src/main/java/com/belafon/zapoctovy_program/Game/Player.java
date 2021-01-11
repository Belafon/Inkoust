package com.belafon.zapoctovy_program.Game;

import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.belafon.zapoctovy_program.Game.Map.Village;
import com.belafon.zapoctovy_program.Game.Units.Unit;
import com.belafon.zapoctovy_program.GameActivity;
import com.belafon.zapoctovy_program.MainActivity;

import java.util.ArrayList;

/**
 * Created by ticha on 07.12.2020.
 */

public class Player {
    public volatile int id;
    public volatile ArrayList<Village> villages = new ArrayList<>();
    public volatile ArrayList<Unit> units = new ArrayList<>();
    public volatile int Color = 0x000000;
    public Player(){
        // when new player is created, I need to choose the specific color for him
        if(this instanceof MainPlayer){
            Color = 0x44ffffff;
            return;
        }
        int r = 0;
        int g = 0;
        int b = 0;
        switch (nextColor){
            case 0: r = 200;break;
            case 1: g = 200;break;
            case 2: b = 200;break;
            case 3: r = 200;g = 200;break;
            case 4: r = 200;b = 200;break;
            case 5: g = 200;b = 200;break;
            case 6: r = 100;g = 200;break;
            case 7: r = 100;b = 200;break;
            case 8: g = 100;b = 200;break;
            case 9: r = 200;g = 100;break;
            case 10: r = 200;b = 100;break;
            case 11: g = 200;b = 100;break;
        }
        // it means max number of players in game is 12 yet, but you can add some colors
        nextColor++;
        Color = Integer.parseInt(String.format("44%02x%02x%02x", r, g, b), 16);
    }

    public static ImageView label;
    /** this method creates lable image, it is vector image, written in XML, but
     * you can do it programmatically. The image is just gradient circle. It is used under pictures of units
     * of specific player and under his villages to label it*/
    public synchronized ImageView createImageView(){
        label = new ImageView(MainActivity.actual_activity);
        label.setLayoutParams(new RelativeLayout.LayoutParams(GameActivity.sizeOfPlace, GameActivity.sizeOfPlace));

        GradientDrawable shape = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{Color, 0x00000000, 0x00000000});
        shape.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        shape.setGradientRadius(140.0f);
        label.setImageDrawable(shape);
        next_type_of_color++;
        return label;
    }
    public static int nextColor = 0;
    public static int next_type_of_color = 0;
}

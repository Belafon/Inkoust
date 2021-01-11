package com.belafon.zapoctovy_program.Game.Map;

import android.widget.Button;
import android.widget.ImageView;

import com.belafon.zapoctovy_program.Game.Map.TypeOfPlace;
import com.belafon.zapoctovy_program.Game.Units.Unit;

import java.util.ArrayList;

/**
 * Created by ticha on 07.12.2020.
 */

public class Place{
    public volatile Button button;
    public volatile ArrayList<Unit> units;
    public volatile int positionX;
    public volatile int positionY;
    public volatile TypeOfPlace typeOfPlace;
    public ImageView picture;

    public Place(int positionX, int positionY){
        this.positionX = positionX;
        this.positionY = positionY;
        units = new ArrayList<>();
    }
}

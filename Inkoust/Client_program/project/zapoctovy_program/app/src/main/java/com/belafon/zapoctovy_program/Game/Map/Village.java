package com.belafon.zapoctovy_program.Game.Map;

import android.widget.ImageView;

import com.belafon.zapoctovy_program.Game.Player;

/**
 * Created by ticha on 07.12.2020.
 */

public class Village extends TypeOfPlace {
    public volatile int playersId;
    public volatile ImageView label;
    public volatile Player player;
    public Village(int playersId, Player player){
        this.playersId = playersId;
        this.player = player;
    }
}

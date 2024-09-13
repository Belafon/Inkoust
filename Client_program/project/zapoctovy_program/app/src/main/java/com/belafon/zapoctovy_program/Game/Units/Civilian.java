package com.belafon.zapoctovy_program.Game.Units;

import com.belafon.zapoctovy_program.Game.Player;
import com.belafon.zapoctovy_program.R;

/**
 * Created by ticha on 07.12.2020.
 */

public class Civilian extends Unit {
    public Civilian(int positionX, int positionY, int player, int id, Player player1) {
        super(positionX, positionY, player, id, R.drawable.civilian, player1);
    }
}

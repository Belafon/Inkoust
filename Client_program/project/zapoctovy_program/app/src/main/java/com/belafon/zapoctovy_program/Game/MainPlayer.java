package com.belafon.zapoctovy_program.Game;

/**
 * Created by ticha on 11.12.2020.
 */

public class MainPlayer extends Player {
    public volatile int wood = 0; // for new villages
    public volatile int stone = 0; // for attac upgrade (new barracks)
    public volatile int food = 0; // for new villagers
    public volatile int gold = 0; // to win
    public volatile int force = 1;
}



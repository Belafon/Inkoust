package com.belafon.zapoctovy_program.Game;

import android.widget.TextView;

import com.belafon.zapoctovy_program.GameActivity;
import com.belafon.zapoctovy_program.MainActivity;
import com.belafon.zapoctovy_program.R;

/**
 * Created by ticha on 20.12.2020.
 */

/** Timer is counting the seconds to end of the round, when new round starts, the number of actual round is decrease */
public class Timer {
    public volatile int durationOfRound = 0;
    public volatile boolean makeNewRound = false;

    public Timer(final int durationOfRound, final int rounds){
        this.durationOfRound = durationOfRound;
        MainActivity.actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)MainActivity.actual_activity.findViewById(R.id.current_round)).setText("Round: " + rounds);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){ // the loop is started here
                    if(makeNewRound){
                        // new round has started
                        makeNewRound = false;
                        for (int i = 0; i <= durationOfRound; i++) {
                            try {
                                Thread.sleep(999,99);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            final int a = i;
                            MainActivity.actual_activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(MainActivity.actual_activity instanceof GameActivity)
                                        ((TextView)MainActivity.actual_activity.findViewById(R.id.timer)).setText((durationOfRound - a) + "");
                                }
                            });
                        }
                    }else{
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}

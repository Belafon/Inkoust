package com.belafon.zapoctovy_program.Game.Units;

import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.belafon.zapoctovy_program.Game.Player;
import com.belafon.zapoctovy_program.GameActivity;
import com.belafon.zapoctovy_program.MainActivity;
import com.belafon.zapoctovy_program.R;

/**
 * Created by ticha on 07.12.2020.
 */

public class Unit {
    public volatile int positionX;
    public volatile int positionY;
    public volatile int player;
    public volatile Player playerObj;
    public volatile ImageView picture;
    public int id;
    public ImageView label;

    boolean wait = true;
    public Unit(final int positionX, final int positionY, int playersId, int id, final int imageResources, final Player player){
        this.positionX = positionX;
        this.positionY = positionY;
        this.playerObj = player;
        this.player = playersId;
        this.id = id;

        //this sets the image of unit
        picture = new ImageView(MainActivity.actual_activity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(GameActivity.sizeOfPlace, GameActivity.sizeOfPlace);
        int marginX = positionX * GameActivity.sizeOfPlace + 120;
        int marginY = positionY * GameActivity.sizeOfPlace + 120;
        layoutParams.setMargins(marginX, marginY, 0, 0);
        picture.setLayoutParams(layoutParams);
        picture.setImageResource(imageResources);
        picture.setBackgroundResource(R.color.transparent2);
        picture.setBackgroundColor(MainActivity.actual_activity.getResources().getColor(R.color.transparent));

        // this sets the label under the image
        label = player.createImageView();
        label.setLayoutParams(layoutParams);

        // lets add the view to RelativeLayout named map
        MainActivity.actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).addView(label);
                ((RelativeLayout)MainActivity.actual_activity.findViewById(R.id.map)).addView(picture);
                wait = false;
            }
        });

        // we need to wait until the code running on UI thread is done
        while(true){
            if(!wait)break;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

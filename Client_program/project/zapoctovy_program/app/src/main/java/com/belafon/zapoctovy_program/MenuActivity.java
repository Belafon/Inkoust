package com.belafon.zapoctovy_program;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.belafon.zapoctovy_program.Client.Client;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // to know if the activity is closed because of click on the button, or just
        // because of closing the app
        goToLastActivity = false;

        MainActivity.actual_activity = this;
        hideBars();
        setContentView(R.layout.activity_menu);
        ((EditText)findViewById(R.id.edit_name_menu)).setText(MainActivity.client.name);
    }

    // this is called when client clicks to button start game
    // the client is moved to queue (match making)
    public void start_game(View view) {
        String name = ((EditText)findViewById(R.id.edit_name_menu)).getText().toString();
        if(name.length() < 3 || name.length() > 18){
            Toast.makeText(this, "Too short, or too long name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Client.name.equals(((EditText)findViewById(R.id.edit_name_menu)).getText().toString()))
            Client.setName(((EditText)findViewById(R.id.edit_name_menu)).getText().toString());
        Client.write("lets_play_a_game");
        findViewById(R.id.menu).setVisibility(View.GONE);
        findViewById(R.id.waiting_lin_lay).setVisibility(View.VISIBLE);
    }

    // go out of the queue
    public void goBack(View view) {
        findViewById(R.id.waiting_lin_lay).setVisibility(View.GONE);
        findViewById(R.id.menu).setVisibility(View.VISIBLE);
        Client.write("client_left_the_queue");
        GameActivity.player = null;
    }

    // go back to main activity
    public void goBackToMainActivity(View view) {
        goToLastActivity = true;
        Intent menuIntent = new Intent(MainActivity.actual_activity, MainActivity.class);
        MainActivity.actual_activity.startActivity(menuIntent);
        MainActivity.actual_activity.finish();
    }

    // is called, when this activity is closed
    public void onDestroy(){
        super.onDestroy();
        if(!goToLastActivity)
            Client.write("disconnect");
    }

    // to know if the activity is closed because of click on the button, or just
    // because of closing the app
    public static boolean goToLastActivity = false;

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

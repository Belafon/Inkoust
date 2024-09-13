package com.belafon.zapoctovy_program;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.belafon.zapoctovy_program.Client.Client;
import com.belafon.zapoctovy_program.Client.DataLibrary;
import com.belafon.zapoctovy_program.Client.SliderAdapter;
/*This is main Activity, when the app starts, this activity is the
* first. In res.layout.activity_main is the layout of this activity
* in XML code. */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static int durationOfTimeInFirstScreen = 500;
    public static Activity actual_activity = null;
    public static int screenWidth;
    public static int screenHeight;
    public static DataLibrary dataLibrary;

    //Values for intreduuction fragment
    private ViewPager viewPager;
    private SliderAdapter sliderAdapter;

    /* Is override when this activity is created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actual_activity = this;

        // this sets the layout and draw the UI to screen
        setContentView(R.layout.activity_main);

        // get size of screen, we needs to sum also the size of navigation bar, which is hidden
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels + getNavigationBarHeight();
        screenHeight = displaymetrics.heightPixels;

        // this will hide all unnecessary bars
        hideBars();

        // this access to few dates stored in disk of device
        dataLibrary = new DataLibrary("basic_informations");

        // It find out if the app is started first time or not
        if(dataLibrary.LoadDataBool(this, "isRunningFirstTime")){
            // If the app started first time, the tutorial will start.
            Log.d(TAG, "onCreate: Start Tutorial");
            setSliderFragment();
            viewPager = findViewById(R.id.viewPager);
            viewPager.setVisibility(View.VISIBLE);
            dataLibrary.saveDataBoolean(this, false, "isRunningFirstTime");
            return;
        } else
            findViewById(R.id.startMenu).setVisibility(View.VISIBLE);

        // lets load the name and set it to view EditText, The client can write here some text
        String name = dataLibrary.LoadDataString(this, "name");
        if(name.equals("true"))
            name = "myName";
        ((EditText)findViewById(R.id.edit_name)).setText(name);
    }

    // this method sets the tutorial into viewPager, view in layout activity_main.xml
    private void setSliderFragment(){
        sliderAdapter = new SliderAdapter(this);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(sliderAdapter);
    }

    // this method is called, when the tutorial button is clicked (in layout activity_main.xml)
    public void startTutorial(View view){
        setSliderFragment();
        viewPager = findViewById(R.id.viewPager);
        viewPager.setVisibility(View.VISIBLE);
        findViewById(R.id.startMenu).setVisibility(View.GONE);
    }

    static volatile Client client;
    public static String ip;
    public void connectToServer(String ip, String name){
        this.ip = ip;
        try{
            if(((EditText)findViewById(R.id.port)).getText().toString().length() > 0)
                Client.port = Integer.parseInt(((EditText)findViewById(R.id.port)).getText().toString());
        }catch (Exception e){
        }
        client = new Client(name);
    }

    // this method opens new activity MenuActivity, if the connection is settled
    public static void openMenuActivity(){
        actual_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataLibrary.saveDataString(actual_activity, ip, "lastIp");
                        Client.setName(Client.name);
                        Intent menuIntent = new Intent(MainActivity.actual_activity, MenuActivity.class);
                        actual_activity.startActivity(menuIntent);
                        actual_activity.finish();
                    }
                }, durationOfTimeInFirstScreen);
            }
        });
    }

    // called, when the connect button is clicked, this will try to connect to server
    // with ip set in EditText with id edit_ip
    public void connect(View view) {
        final String ip = ((EditText)findViewById(R.id.edit_ip)).getText().toString();
        if(ip.length() < 11 || ip.length() > 14){
            Toast.makeText(this, "Too short ip", Toast.LENGTH_SHORT).show();
            return;
        }
        final String name = ((EditText)findViewById(R.id.edit_name)).getText().toString();
        if(name.length() < 3 || name.length() > 18){
            Toast.makeText(this, "Too short, or too long name", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectToServer(ip, name);
            }
        }).start();
    }


    private static volatile boolean conncectToLastIpClicked = false;
    // app stores last ip, which was used
    // this method is called when connect to last ip button is clicked
    // it will try to connect to server with last ip
    public void connectToLastIp(View view) {
        if(conncectToLastIpClicked)return;
        conncectToLastIpClicked = true;
        final String ip = dataLibrary.LoadDataString(this, "lastIp");
        if(ip.equals(true))return;
        if(ip.length() < 3){
            Toast.makeText(this, "Too short ip", Toast.LENGTH_SHORT).show();
            return;
        }
        final String name = ((EditText)findViewById(R.id.edit_name)).getText().toString();
        if(name.length() < 3 || name.length() > 18){
            Toast.makeText(this, "Too short, or too long name", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectToServer(ip, name);
                conncectToLastIpClicked = false;
            }
        }).start();
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

    // GET SIZE OF NAVIGATIONBAR
    private int getNavigationBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.widthPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.widthPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }
}

package com.belafon.zapoctovy_program.Client;


import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Write_text{
    private static final String TAG = "Write_text";
    private volatile BufferedWriter out;
    public Write_text(){
        try {
            out = new BufferedWriter(new OutputStreamWriter(Client.clientSocket.getOutputStream()));
        }catch (Exception e){
            Log.d(TAG, "Write_text: " + e);
        }
    }

    protected void write(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: lets write");
                    out.write(message + "\r\n");
                    Log.d(TAG, "run: lets flush");
                    out.flush();
                    Log.d(TAG, "run: everything is alsom!!!!!!!");
                } catch (IOException e) {
                    e.printStackTrace();
                }catch(Exception e){
                    Log.d(TAG, "doInBackground: " + e);
                }
            }
        }).start();
    }
}

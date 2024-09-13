package com.belafon.zapoctovy_program.Client;

import android.content.Context;
import android.content.SharedPreferences;

public class DataLibrary {
    private volatile SharedPreferences sharedPreferences;
    private String SHARED_PREFES = "SharedPrefs";

    public DataLibrary(String nameSharedPreferences){
        this.SHARED_PREFES = nameSharedPreferences;
    }
    public void saveDataInteager(Context context, int value, String name){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, value);
        editor.commit();
    }
    public void saveDataString(Context context, String value, String name){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }
    public void saveDataBoolean(Context context, boolean value, String name){
        sharedPreferences = context.getSharedPreferences(SHARED_PREFES, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }
    public int LoadDataInteager(Context context, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFES, context.MODE_PRIVATE);
        int value = 0;
        value = sharedPreferences.getInt(name, value);
        return value;
    }
    public String LoadDataString(Context context, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFES, context.MODE_PRIVATE);
        String value  = "true";
        value = sharedPreferences.getString(name, value);
        return value;
    }
    public boolean LoadDataBool(Context context, String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFES, context.MODE_PRIVATE);
        boolean bool = true;
        bool = sharedPreferences.getBoolean(name, bool);
        return bool;
    }
}

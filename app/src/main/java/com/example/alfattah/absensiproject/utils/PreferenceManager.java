package com.example.alfattah.absensiproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;


    public PreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("prefmanager", 0);
        editor = sharedPreferences.edit();
    }
    public void simpanRole(String role){
        editor.putString("role", role);
        editor.commit();
    }
    public String ambilRole(){
        return sharedPreferences.getString("role", "");
    }

    public void setsudahChekin(boolean sudah){
        editor.putBoolean("sudahcheckin", sudah);
        editor.commit();
    }
    public  boolean getsudahcheckin(){
        return sharedPreferences.getBoolean("sudahcheckin", true);
    }

    public void setwaktunyacheckout(boolean waktucheckout){
        editor.putBoolean("waktunyachekout", waktucheckout);
        editor.commit();
    }
    public boolean iswaktucheckout(){
        return sharedPreferences.getBoolean("waktunyachekout", false);
    }


    public void setdataperusahaan(boolean sudahdiset){
        editor.putBoolean("sudahdiset", sudahdiset);
        editor.commit();
    }
    public boolean checkdataperusahaan(){
        return sharedPreferences.getBoolean("sudahdiset", false);
    }
}

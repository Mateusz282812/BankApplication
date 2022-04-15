package com.example.crustlabapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    private static final String SHARE_PREFERENCES_NAME = "CrustLabPreferences";

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context){
        return context.getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();
    }

}

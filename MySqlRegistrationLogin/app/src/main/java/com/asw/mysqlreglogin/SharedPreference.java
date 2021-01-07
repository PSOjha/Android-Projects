package com.asw.mysqlreglogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SharedPreference { private final Context context;
    private String PREF_NAME = "TEST_DB";
    private final int PRIVATE_MODE = 0;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;

    public static final String KEY_NAME = "fullname";
    public static final String KEY_MOB_NO = "mobno";
    public static final String KEY_STATE = "state";
    public static final String KEY_CITY = "city";

    public SharedPreference(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLoginSession(String name, String mobno, String state, String city) {
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_MOB_NO, mobno);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_CITY, city);
        editor.commit();
    }

    public String getEmpMob() {
        return preferences.getString(KEY_MOB_NO, "");
    }

    public String getName() {
        return preferences.getString(KEY_NAME, "");
    }

    public String getState() {
        return preferences.getString(KEY_STATE, "");
    }

    public String getCity() {
        return preferences.getString(KEY_CITY, "");
    }

    public void logoutUser() {

        editor.clear();
        editor.commit();
        context.startActivity(new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));

        ((Activity) context).finish();
    }
}

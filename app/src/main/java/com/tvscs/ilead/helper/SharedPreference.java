package com.tvscs.ilead.helper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.tvscs.ilead.activity.HomeActivity;
import com.tvscs.ilead.activity.LoginActivity;
import com.tvscs.ilead.utils.Constants;

public class SharedPreference implements Constants {
    public SharedPreferences mPreferences;
    private Context mContext;
    private Editor editor;

    public SharedPreference(Context context) {
        this.mContext = context;
        mPreferences = (SharedPreferences) context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = mPreferences.edit();
    }

    public void setUserId(String name) {
        Editor editor = mPreferences.edit();
//        editor.putString(key, value);
    }

    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setPref(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getPref(String key) {
        if (mPreferences.contains(key)) {
            String value = mPreferences.getString(key, "");
            return value;
        } else {
            return "";
        }
    }

    public String getPref(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    public void createLoginSession(String userid, String password) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USERID, userid);
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(mContext, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        } else {
            Intent intent = new Intent(mContext, HomeActivity.class);
            mContext.startActivity(intent);
        }

    }

    public void clearKeyValue(String key) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(mContext, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(i);
    }

    public boolean isLoggedIn() {
        return mPreferences.getBoolean(IS_LOGIN, false);
    }

}
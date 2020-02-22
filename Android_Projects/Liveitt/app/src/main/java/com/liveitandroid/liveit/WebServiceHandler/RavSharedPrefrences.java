package com.liveitandroid.liveit.WebServiceHandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class RavSharedPrefrences {
    static Boolean boolValue = Boolean.valueOf(false);
    static Editor editor;
    static SharedPreferences preferences;
    static String value = "";

    public static void set_authurl(Context activity, String url) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString(OnNativeInvokeListener.ARG_URL, url);
        editor.commit();
    }

    public static String get_authurl(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        value = preferences.getString(OnNativeInvokeListener.ARG_URL, "");
        return value;
    }

    public static void set_salt(Context activity, String salt) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("salt", salt);
        editor.commit();
    }

    public static String get_salt(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        value = preferences.getString("salt", "");
        return value;
    }

    public static void set_clientkey(Context activity, String key) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("LeanbackPreferenceDialogFragment.ARG_KEY", key);
        editor.commit();
    }

    public static String get_clientkey(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        value = preferences.getString("LeanbackPreferenceDialogFragment.ARG_KEY", "");
        return value;
    }

    public static void set_user_email(Context activity, String uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("user_email", uid);
        editor.commit();
    }

    public static String get_user_email(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        value = preferences.getString("user_email", "");
        return value;
    }

    public static void set_user_pass(Context activity, String uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("user_pass", uid);
        editor.commit();
    }

    public static String get_user_pass(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        value = preferences.getString("user_pass", "");
        return value;
    }

    public static void set_user_type(Context activity, String uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("user_type", uid);
        editor.commit();
    }

    public static String get_user_type(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        value = preferences.getString("user_type", "");
        return value;
    }

    public static void set_user_token(Context activity, String uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("user_token", uid);
        editor.commit();
    }

    public static String get_user_token(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        value = preferences.getString("user_token", "");
        return value;
    }

    public static void set_loginbool(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("loginbool", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_loginbool(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("loginbool", false));
        return boolValue;
    }

    public static void set_rememberbool(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("rememberbool", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_rememberbool(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("rememberbool", false));
        return boolValue;
    }

    public static void set_fbconnect(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("fbconnect", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_fbconnect(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("fbconnect", false));
        return boolValue;
    }

    public static void set_gcmid(Context activity, String uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("gcmid", uid);
        editor.commit();
    }

    public static String get_gcmid(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString("gcmid", AppConst.PASSWORD_UNSET);
    }

    public static void set_logindata(Context activity, String uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("logind", uid);
        editor.commit();
    }

    public static String get_logindata(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString("logind", "");
    }

    public static void set_notifydata(Context activity, String uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("notifydata", uid);
        editor.commit();
    }

    public static String get_notifydata(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString("notifydata", "");
    }

    public static void set_phone(Context activity, String rid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("lat", rid);
        editor.commit();
    }

    public static String get_phone(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString("lat", "");
    }

    public static void set_current_long(Context activity, String rid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putString("long", rid);
        editor.commit();
    }

    public static String get_current_long(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        return preferences.getString("long", AppConst.PASSWORD_UNSET);
    }

    public static void set_all(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("nall", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_all(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("nall", false));
        return boolValue;
    }

    public static void set_invited(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("invited", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_invited(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("invited", false));
        return boolValue;
    }

    public static void set_accepted(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("accepted", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_accepted(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("accepted", false));
        return boolValue;
    }

    public static void set_rejected(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("rejected", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_rejected(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("rejected", false));
        return boolValue;
    }

    public static void set_new_review(Context activity, Boolean uid) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        editor = preferences.edit();
        editor.putBoolean("new_review", uid.booleanValue());
        editor.commit();
    }

    public static Boolean get_new_review(Context activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolValue = Boolean.valueOf(preferences.getBoolean("new_review", false));
        return boolValue;
    }
}

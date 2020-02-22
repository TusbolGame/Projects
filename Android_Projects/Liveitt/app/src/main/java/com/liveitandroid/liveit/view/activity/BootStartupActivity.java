package com.liveitandroid.liveit.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.liveitandroid.liveit.miscelleneious.common.AppConst;

public class BootStartupActivity extends BroadcastReceiver {
    /* renamed from: a */
    private SharedPreferences loginPreferencesAfterLoginChannels;

    public void onReceive(Context context, Intent intent) {
        this.loginPreferencesAfterLoginChannels = context.getSharedPreferences(AppConst.LOGIN_AUTO_START, 0);
        String autostart = this.loginPreferencesAfterLoginChannels.getString(AppConst.LOGIN_AUTO_START, "");
        if (autostart.equals("") && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            intent = new Intent(context, SplashActivity.class);
            //intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }
}

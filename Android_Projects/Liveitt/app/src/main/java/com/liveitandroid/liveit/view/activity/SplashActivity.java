package com.liveitandroid.liveit.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.google.android.gms.cast.framework.CastContext;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.WebServiceHandler.RavSharedPrefrences;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.callback.LoginCallback;
import com.liveitandroid.liveit.view.interfaces.LoginInterface;
import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;

public class SplashActivity extends AppCompatActivity implements LoginInterface {
    private final int SPLASH_DISPLAY_LENGTH = PathInterpolatorCompat.MAX_NUM_POINTS;
    private SharedPreferences loginPreferencesAfterLogin;

    class C17271 implements Runnable {
        C17271() {
        }

        public void run() {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            SplashActivity.this.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            SplashActivity.this.finish();
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        changeStatusBarColor();
        RavSharedPrefrences.set_clientkey(this, AppConst.CLIENT_KEY);
        RavSharedPrefrences.set_salt(this, AppConst.SALT);
        AppCenter.start(getApplication(), "b22dca3f-17ac-48bc-82cb-e95a36001ea3",
                Analytics.class, Crashes.class);


//        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
//        SharedPreferences.Editor loginPrefsEditor = this.loginPreferencesAfterLogin.edit();
//        if (loginPrefsEditor != null) {
//            loginPrefsEditor.putString(AppConst.LOGIN_PREF_SELECTED_PLAYER, getString(R.string.exo_player));
//            loginPrefsEditor.commit();
//        }

        new Handler().postDelayed(new C17271(), 3000);
    }



    private void changeStatusBarColor() {
        Window window = getWindow();
        if (VERSION.SDK_INT >= 19) {
            window.clearFlags(67108864);
        }
        if (VERSION.SDK_INT >= 21) {
            window.addFlags(Integer.MIN_VALUE);
        }
        if (VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    public void atStart() {
    }

    public void onFinish() {

    }

    public void onFailed(String errorMessage) {
    }

    public void validateLogin(LoginCallback loginCallback, String validateLogin) {
    }

    public void stopLoader() {

    }
}

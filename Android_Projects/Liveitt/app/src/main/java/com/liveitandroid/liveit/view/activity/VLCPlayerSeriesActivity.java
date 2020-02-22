package com.liveitandroid.liveit.view.activity;

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;

public class VLCPlayerSeriesActivity extends AppCompatActivity {
    private static final String _VLC_PLAYER_CLASS_NAME = "org.videolan.vlc.gui.video.VideoPlayerActivity";
    private static final String _VLC_PLAYER_PACKAGE_NAME = "org.videolan.vlc";
    private Uri contentUri;
    private Context context;
    private SharedPreferences loginPreferencesSharedPref;
    private String mFilePath;

    class C17801 implements OnClickListener {
        C17801() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            try {
                VLCPlayerSeriesActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=org.videolan.vlc")));
            } catch (ActivityNotFoundException e) {
                try {
                    VLCPlayerSeriesActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=org.videolan.vlc")));
                } catch (ActivityNotFoundException exception) {
                    Utils.showToast(VLCPlayerSeriesActivity.this.context, String.valueOf(exception));
                }
            }
        }
    }

    class C17812 implements OnClickListener {
        C17812() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            VLCPlayerSeriesActivity.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        initialize();
    }

    private void initialize() {
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_stream_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        String streamType = getIntent().getStringExtra("STREAM_TYPE");
        this.mFilePath = "http://" + serverUrl + ":" + serverPort + "/series/" + username + "/" + password + "/" + opened_stream_id + "." + getIntent().getStringExtra("CONTAINER_EXTENSION");
        this.contentUri = Uri.parse(this.mFilePath);
        try {
            Uri uri = Uri.parse(this.mFilePath);
            Intent vlcIntent = new Intent("android.intent.action.VIEW");
            vlcIntent.setPackage(_VLC_PLAYER_PACKAGE_NAME);
            vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
            startActivityForResult(vlcIntent, 42);
            finish();
        } catch (ActivityNotFoundException e) {
            vlcPlayerNotFoundDialogBox();
        }
    }

    public void vlcPlayerNotFoundDialogBox() {
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.media_player));
        builder.setMessage(getResources().getString(R.string.alert_vlc_player));
        builder.setPositiveButton(getResources().getString(R.string.install_it), new C17801());
        builder.setNegativeButton(getResources().getString(R.string.cancel_small), new C17812());
        builder.setCancelable(false);
        builder.create().show();
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
}

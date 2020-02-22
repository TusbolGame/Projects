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
import android.util.Log;
import android.view.Window;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;

public class VLCPlayerExtrasActivity extends AppCompatActivity {
    private static final String _VLC_PLAYER_CLASS_NAME = "org.videolan.vlc.gui.video.VideoPlayerActivity";
    private static final String _VLC_PLAYER_PACKAGE_NAME = "org.videolan.vlc";
    private Uri contentUri;
    private Context context;
    private SharedPreferences loginPreferencesSharedPref;
    private String mFilePath;

    class C17821 implements OnClickListener {
        C17821() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            try {
                VLCPlayerExtrasActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=org.videolan.vlc")));
            } catch (ActivityNotFoundException e) {
                try {
                    VLCPlayerExtrasActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=org.videolan.vlc")));
                } catch (ActivityNotFoundException exception) {
                    Utils.showToast(VLCPlayerExtrasActivity.this.context, String.valueOf(exception));
                }
            }
        }
    }

    class C17832 implements OnClickListener {
        C17832() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            VLCPlayerExtrasActivity.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        Log.d("TmpLogs", "This is " + getLocalClassName());
        initialize();
        getWindow().setFlags(1024, 1024);
    }

    private void initialize() {
        this.context = this;
        String nameMovie = getIntent().getStringExtra("nameMovie");
        String urlMovie = getIntent().getStringExtra("urlMovie");
        String subs = getIntent().getStringExtra("subs");
        try {
            Uri uri = Uri.parse(urlMovie);
            Intent vlcIntent = new Intent("android.intent.action.VIEW");
            vlcIntent.setPackage(_VLC_PLAYER_PACKAGE_NAME);
			vlcIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
			vlcIntent.addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION);
			vlcIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			vlcIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            vlcIntent.setDataAndTypeAndNormalize(uri, "video/*");
            vlcIntent.putExtra("title", nameMovie);
            vlcIntent.putExtra("from_start", true);
            vlcIntent.putExtra("position", 90000l);
            if(!subs.equals("") && !subs.equals(null)){
                Uri urisub = Uri.parse(subs);
                vlcIntent.putExtra("subtitles", urisub);
                vlcIntent.putExtra("subtitles_location", urisub);
            }
            startActivityForResult(vlcIntent, 42);
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            finish();
        } catch (ActivityNotFoundException e) {
            vlcPlayerNotFoundDialogBox();
        }
    }

    public void vlcPlayerNotFoundDialogBox() {
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.media_player));
        builder.setMessage(getResources().getString(R.string.alert_vlc_player));
        builder.setPositiveButton(getResources().getString(R.string.install_it), new C17821());
        builder.setNegativeButton(getResources().getString(R.string.cancel_small), new C17832());
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

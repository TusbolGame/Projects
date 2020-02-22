package com.liveitandroid.liveit.view.activity;

import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.Utils;

public class XPlayerVodAppActivity extends AppCompatActivity {
    private static final String X_PLAYER_PACKAGE_NAME = "video.player.videoplayer";
    private static final String X_PLAYER_CLASS_NAME = "com.inshot.xplayer.activities.PlayerActivity";
    private Uri contentUri;
    private Context context;
    private String mFilePath;

    class C16441 implements OnClickListener {
        C16441() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            try {
                XPlayerVodAppActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=video.player.videoplayer")));
            } catch (ActivityNotFoundException e) {
                try {
                    XPlayerVodAppActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=video.player.videoplayer")));
                } catch (ActivityNotFoundException exception) {
                    Utils.showToast(XPlayerVodAppActivity.this.context, String.valueOf(exception));
                }
            }
        }
    }

    class C16452 implements OnClickListener {
        C16452() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            XPlayerVodAppActivity.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();
        Log.d("TmpLogs", "This is " + getLocalClassName());
        initialize();
        getWindow().setFlags(1024, 1024);
    }

    private static class Subtitle {
        /**
         * Subtitle URI
         */
        final Uri uri;

        /**
         * (Optional) Custom subtitle name.
         */
        String name;

        /**
         * (Optional) File name.
         */
        String filename;


        Subtitle(Uri uri) {
            if (uri.getScheme() == null)
                throw new IllegalStateException("Scheme is missed for subtitle URI " + uri);

            this.uri = uri;
        }

        Subtitle(String uriStr) {
            this(Uri.parse(uriStr));
        }
    }

    private void initialize() {
        this.context = this;
        String nameMovie = getIntent().getStringExtra("nameMovie");
        String urlMovie = getIntent().getStringExtra("urlMovie");
        String subs = getIntent().getStringExtra("subs");

        Intent intent;
        if (appInstalledOrNot(X_PLAYER_PACKAGE_NAME)) {
            intent = new Intent();
            intent.setClassName(X_PLAYER_PACKAGE_NAME, X_PLAYER_CLASS_NAME);
            intent.putExtra("package", getPackageName());
            intent.setPackage(X_PLAYER_PACKAGE_NAME);
            intent.putExtra("title", nameMovie);
            //intent.setDataAndType(Uri.parse(urlMovie), "video/*");
            intent.setData(Uri.parse(urlMovie));
            if(subs != null)
            {
                if (!subs.equals(null) && !subs.equals("")) {
                    Subtitle subtitle0_0 = new Subtitle(subs);
                    subtitle0_0.name = nameMovie;
                    subtitle0_0.filename = subs;

                    Parcelable[] parcels = new Parcelable[1];
                    parcels[0] = subtitle0_0.uri;

                    intent.putExtra("subs", parcels);
                }
            }
            startActivity(intent);
            finish();
        } else{
            mxPlayerNotFoundDialogBox();
        }
    }

    private boolean appInstalledOrNot(String uri) {
        if (this.context == null) {
            return false;
        }
        try {
            this.context.getPackageManager().getPackageInfo(uri, 1);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public void mxPlayerNotFoundDialogBox() {
        Builder builder = new Builder(this);
        builder.setTitle(getResources().getString(R.string.media_player));
        builder.setMessage(getResources().getString(R.string.alert_x_player));
        builder.setPositiveButton(getResources().getString(R.string.install_it), new C16441());
        builder.setNegativeButton(getResources().getString(R.string.cancel_small), new C16452());
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

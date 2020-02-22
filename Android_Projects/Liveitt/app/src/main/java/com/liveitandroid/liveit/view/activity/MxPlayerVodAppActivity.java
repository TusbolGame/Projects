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
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;

public class MxPlayerVodAppActivity extends AppCompatActivity {
    private static final String _MX_PLAYER_CLASS_NAME = "com.mxtech.videoplayer.ad.ActivityScreen";
    private static final String _MX_PLAYER_CLASS_NAME_PRO = "com.mxtech.videoplayer.pro.ActivityScreen";
    private static final String _MX_PLAYER_PACKAGE_NAME = "com.mxtech.videoplayer.ad";
    private static final String _MX_PLAYER_PACKAGE_NAME_PRO = "com.mxtech.videoplayer.pro";
    private Uri contentUri;
    private Context context;
    private String mFilePath;

    class C16441 implements OnClickListener {
        C16441() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            try {
                MxPlayerVodAppActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.mxtech.videoplayer.ad")));
            } catch (ActivityNotFoundException e) {
                try {
                    MxPlayerVodAppActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.mxtech.videoplayer.ad")));
                } catch (ActivityNotFoundException exception) {
                    Utils.showToast(MxPlayerVodAppActivity.this.context, String.valueOf(exception));
                }
            }
        }
    }

    class C16452 implements OnClickListener {
        C16452() {
        }

        public void onClick(DialogInterface dialogInterface, int id) {
            MxPlayerVodAppActivity.this.finish();
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
        if (!appInstalledOrNot(_MX_PLAYER_PACKAGE_NAME_PRO)) {
            try {
                intent = new Intent();
                intent.setClassName(_MX_PLAYER_PACKAGE_NAME, _MX_PLAYER_CLASS_NAME);
                intent.putExtra("package", getPackageName());
                intent.putExtra("title", nameMovie);
                intent.setDataAndType(Uri.parse(urlMovie), "video/*");

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

                intent.setPackage(_MX_PLAYER_PACKAGE_NAME);
                startActivity(intent);
                finish();
            } catch (ActivityNotFoundException e) {
                mxPlayerNotFoundDialogBox();
            }
        } else if (this.context != null) {
            try {
                intent = new Intent();
                intent.setClassName(_MX_PLAYER_PACKAGE_NAME_PRO, _MX_PLAYER_CLASS_NAME_PRO);
                intent.putExtra("package", getPackageName());
                intent.putExtra("title", nameMovie);
                intent.setDataAndType(Uri.parse(urlMovie), "video/*");

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

                intent.setPackage(_MX_PLAYER_PACKAGE_NAME_PRO);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
                finish();
            } catch (ActivityNotFoundException e2) {
                mxPlayerNotFoundDialogBox();
            }
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
        builder.setMessage(getResources().getString(R.string.alert_mx_player));
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

package com.evilkingmedia.musica;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.demand.WebViewActivity;
import com.evilkingmedia.utility.CheckXml;

public class MusicMainActivity extends AppCompatActivity {


    private LinearLayout rlListeIPTV, rlAscolta, rlShazam;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_music_main);
        } else {
            setContentView(R.layout.activity_music_main_portrait);
        }

        init();

        rlListeIPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(MusicMainActivity.this, Constant.MUSICA_LISTEIPTV_URL);
            }
        });

        rlAscolta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicMainActivity.this, MusicWebViewActivity.class);
                startActivity(intent);
            }
        });

        rlShazam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(MusicMainActivity.this)
                        .withPermissions(
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.MODIFY_AUDIO_SETTINGS
                        ).withListener(new MultiplePermissionsListener() {
                    @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Intent intent = new Intent(MusicMainActivity.this, WebViewActivity.class);
                        intent.putExtra("videoUrl", Constant.MUSICA_SHAZAM_URL);
                        startActivity(intent);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
                }).check();
            }
        });

        Constant.setFocusEvent(this, rlListeIPTV, rlAscolta, rlShazam);
        CheckXml.checkXml(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null){
            adView.resume();
        }
        CheckXml.checkXml(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null){
            adView.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null){
            adView.pause();
        }
    }

    private void init() {
        rlListeIPTV = findViewById(R.id.rlListeIPTV);
        rlAscolta = findViewById(R.id.rlAscolta);
        rlShazam = findViewById(R.id.rlShazam);
        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);
    }
}

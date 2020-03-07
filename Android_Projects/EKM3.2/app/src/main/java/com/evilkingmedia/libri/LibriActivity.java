package com.evilkingmedia.libri;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.utility.CheckXml;

public class LibriActivity extends AppCompatActivity {

    LinearLayout rlebook, rledicola;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_libri);
        } else {
            setContentView(R.layout.activity_libri_portrait);
        }

        rlebook = findViewById(R.id.rlebook);
        rledicola = findViewById(R.id.rledicola);
        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        rlebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/folders/0BzG97RrFLXbOSmh1Uk1lTS1uWFU"));
                startActivity(browserIntent);
            }
        });

        rledicola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/EdicolaWapposaOfficial"));
                startActivity(intent);
            }
        });

        Constant.setFocusEvent(this, rlebook, rledicola);
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
}
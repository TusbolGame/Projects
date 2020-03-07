package com.evilkingmedia.epg;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.utility.CheckXml;

public class EpgAndGuideActivity extends AppCompatActivity {

    LinearLayout rlepg, rlguide;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_epg_and_guide);
        } else {
            setContentView(R.layout.activity_epg_and_guide_portrait);
        }

        rlepg = findViewById(R.id.rlepg);
        rlguide = findViewById(R.id.rlguide);
        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        rlepg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EpgAndGuideActivity.this, EPGActivity.class));
            }
        });

        rlguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(EpgAndGuideActivity.this, Constant.Video_Guide_URL);
            }
        });

        Constant.setFocusEvent(this, rlepg, rlguide);
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

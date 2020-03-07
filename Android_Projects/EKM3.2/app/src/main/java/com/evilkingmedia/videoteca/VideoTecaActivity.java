package com.evilkingmedia.videoteca;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.utility.CheckXml;

public class VideoTecaActivity extends AppCompatActivity {

    LinearLayout rlDocumentariWebCaster, rlVideoVari;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_video_teca);
        } else {
            setContentView(R.layout.activity_video_teca_portrait);
        }

        rlDocumentariWebCaster = findViewById(R.id.rlDocumentariWebCaster);
        rlVideoVari = findViewById(R.id.rlVideoVari);
        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        rlDocumentariWebCaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(VideoTecaActivity.this, Constant.EVILKINGDOCUMENTARIURL);
            }
        });

        rlVideoVari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(VideoTecaActivity.this, Constant.Video_Vari_M3u_Url);
            }
        });

        Constant.setFocusEvent(this, rlDocumentariWebCaster, rlVideoVari);
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

package com.evilkingmedia.livetv;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.utility.CheckXml;

public class HaremActivity extends AppCompatActivity {

    LinearLayout listeiptv, webcaster;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_harem);
        } else {
            setContentView(R.layout.activity_harem_portrait);
        }

        listeiptv = findViewById(R.id.listeiptvLayout);
        webcaster = findViewById(R.id.webcasterLayout);
        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        listeiptv.setOnClickListener(v -> Constant.openExternalBrowser(HaremActivity.this, Constant.LIVETV_HAREM_LISTEIPTV_URL));

        webcaster.setOnClickListener(v -> Constant.openWVCapp(HaremActivity.this, Constant.LIVETV_HAREM_WEBCASTER_URL));

        Constant.setFocusEvent(this, listeiptv, webcaster);
        CheckXml.checkXml(this);
    }

    @Override
    protected void onResume(){
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

package com.evilkingmedia.demand;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmedia.share.ShareActivity;
import com.google.android.gms.ads.AdView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.utility.CheckXml;

public class FilmCategoryActivity extends AppCompatActivity {

    LinearLayout rlShare, rlMovies3,rlMovies4, r1Series4, rlSearchVideo, rlRai, rlMediaset;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_film_category);
        } else {
            setContentView(R.layout.activity_film_category_portrait);
        }

        rlShare = findViewById(R.id.rlShare);
        rlMovies3 = findViewById(R.id.rlMovies3);
        rlMovies4 = findViewById(R.id.rlMovies4);
        r1Series4 = findViewById(R.id.rlSeries4);
        rlSearchVideo = findViewById(R.id.rlSearchVideo);
        rlRai = findViewById(R.id.rlRai);
        rlMediaset = findViewById(R.id.rlMediaset);

        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        rlShare.setOnClickListener(view -> {
            Intent intent = new Intent(FilmCategoryActivity.this, ShareActivity.class);
//                Intent intent = new Intent(FilmCategoryActivity.this, ShareActivity.class);
            intent.putExtra("type", "vod");
            startActivity(intent);
        });

        rlMovies3.setOnClickListener(view -> Constant.openExternalBrowser(FilmCategoryActivity.this,Constant.EVILKINGLISTEITTVURL ));

        rlMovies4.setOnClickListener(view -> Constant.openWVCapp(FilmCategoryActivity.this,Constant.EVILKINGMOVIEURL));

        r1Series4.setOnClickListener(view -> Constant.openWVCapp(FilmCategoryActivity.this,Constant.EVILKINGSERIESURL));

        rlSearchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(FilmCategoryActivity.this, Constant.Search_Video_Url);
            }
        });

        rlRai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(FilmCategoryActivity.this, Constant.DEMAND_RAI_M3U_URL);
            }
        });

        rlMediaset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(FilmCategoryActivity.this, Constant.DEMAND_MEDIASET_M3U_URL);
            }
        });

        Constant.setFocusEvent(this, rlMovies3, rlShare, rlMovies4, r1Series4, rlSearchVideo, rlRai, rlMediaset);
        CheckXml.checkXml(this);
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        CheckXml.checkXml(this);
    }
}

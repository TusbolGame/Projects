package com.evilkingmedia.sports;

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
import com.evilkingmedia.demand.WebViewActivity;
import com.evilkingmedia.utility.CheckXml;

public class SportsCategoryActivity extends AppCompatActivity {

    LinearLayout rlSchedule, rlSportsEPG, rlCricfree, rlStreamingSports, rlEKSport, rlHighlights, rlListeIPTV, rlSportsWebCaster, rlHulkStream, rlMyP2P, rlSoccer, rlFootballOnDemand, rlUsaGoalDNS;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_sports_category);
        } else {
            setContentView(R.layout.activity_sports_category_portrait);
        }

        rlEKSport = findViewById(R.id.rlEKSport);
        rlCricfree = findViewById(R.id.rlCricfree);
        rlStreamingSports = findViewById(R.id.rlStreamingSports);
        rlHighlights = findViewById(R.id.rlHighlights);
        rlSportsEPG = findViewById(R.id.rlSportsEPG);
        rlSchedule = findViewById(R.id.rlSchedulesResults);
        rlListeIPTV = findViewById(R.id.rlListeIPTV);
        rlSportsWebCaster = findViewById(R.id.rlSportsWebCaster);
        rlHulkStream = findViewById(R.id.rlHulkStream);
        rlMyP2P = findViewById(R.id.rlMyP2P);
        rlSoccer = findViewById(R.id.rlSoccer);
        rlFootballOnDemand = findViewById(R.id.rlFootballOnDemand);
        rlUsaGoalDNS = findViewById(R.id.rlUsaGoalDNS);

        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        rlEKSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsEKSportActivity.class));
            }
        });

        rlCricfree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SportsCategoryActivity.this, SportsCricfreeActivity.class);
                startActivity(i);
            }
        });

        rlStreamingSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SportsCategoryActivity.this, SportsStreamingActivity.class);
                startActivity(i);
            }
        });

        rlSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SportsCategoryActivity.this, WebViewActivity.class);
                i.putExtra("videoUrl",Constant.SPORTSEPGURL);
                startActivity(i);
            }
        });

        rlListeIPTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.openExternalBrowser(SportsCategoryActivity.this,Constant.SPORTSBYDOCURL);
            }
        });

        rlSportsWebCaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.openWVCapp(SportsCategoryActivity.this,Constant.EVILKINGSPORTSURL);
            }
        });
        rlHighlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(SportsCategoryActivity.this,WebViewActivity.class);
//                i.putExtra("videoUrl",Constant.SPORTSURL6);
//                startActivity(i);
                Constant.openWVCapp(SportsCategoryActivity.this, Constant.SPORTSURL6);
            }
        });

        rlSportsEPG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SportsCategoryActivity.this,WebViewActivity.class);
                i.putExtra("videoUrl",Constant.SPORTSURL7);
                startActivity(i);
            }
        });

        rlHulkStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(SportsCategoryActivity.this, Constant.SPORTS_HULK_STREAM_URL);
            }
        });

        rlMyP2P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsMyp2pActivity.class));
            }
        });

        rlSoccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SportsCategoryActivity.this, SportsSoccerActivity.class));
            }
        });

        rlFootballOnDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SportsCategoryActivity.this, FootballOnDemandActivity.class));
            }
        });

        rlUsaGoalDNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openWVCapp(SportsCategoryActivity.this, Constant.SPORTS_USA_GOALS_DNS_URL);
            }
        });

        Constant.setFocusEvent(this, rlSchedule, rlSportsEPG, rlCricfree, rlStreamingSports, rlEKSport, rlHighlights, rlListeIPTV, rlSportsWebCaster, rlHulkStream, rlMyP2P, rlSoccer, rlFootballOnDemand, rlUsaGoalDNS);
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

package com.evilkingmedia.cartoon;

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

public class CartoonCategoryActivity extends AppCompatActivity {
    LinearLayout rlViewCartoons, rlCartoonWebCaster, rlEkmCartoons, rlAnimeUnity;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_cartoon_category);
        } else {
            setContentView(R.layout.activity_cartoon_category_portrait);
        }

        rlViewCartoons = findViewById(R.id.rlViewCartoons);
        rlCartoonWebCaster = findViewById(R.id.rlCartoonWebCaster);
        rlAnimeUnity = findViewById(R.id.rlAnimeUnity);
        rlEkmCartoons = findViewById(R.id.rlEkmCartoons);
        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        rlViewCartoons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(CartoonCategoryActivity.this, ViewCartoonsActivity.class);
               startActivity(i);
            }
        });
        rlCartoonWebCaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.openWVCapp(CartoonCategoryActivity.this,Constant.EVILKINGCARTOONURL);
            }
        });
        rlEkmCartoons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.openExternalBrowser(CartoonCategoryActivity.this, Constant.EKM_CARTOONS_URL);
            }
        });
        rlAnimeUnity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartoonCategoryActivity.this, AnimeUnityActivity.class));
            }
        });

        Constant.setFocusEvent(this, rlViewCartoons, rlCartoonWebCaster, rlEkmCartoons, rlAnimeUnity);
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

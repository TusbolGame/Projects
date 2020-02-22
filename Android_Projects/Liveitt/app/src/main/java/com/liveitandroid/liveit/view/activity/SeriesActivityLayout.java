package com.liveitandroid.liveit.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.liveitandroid.liveit.R;

public class SeriesActivityLayout extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_layout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}

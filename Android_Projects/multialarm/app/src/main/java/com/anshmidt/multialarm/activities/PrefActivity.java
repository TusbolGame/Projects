package com.anshmidt.multialarm.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anshmidt.multialarm.dialogs.PrefFragment;

/**
 * Created by Ilya Anshmidt on 29.09.2017.
 */

public class PrefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
    }


}

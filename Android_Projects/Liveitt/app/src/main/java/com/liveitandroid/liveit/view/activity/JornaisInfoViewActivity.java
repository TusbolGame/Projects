package com.liveitandroid.liveit.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.squareup.picasso.Picasso;
import com.liveitandroid.liveit.R;

public class JornaisInfoViewActivity extends Activity {

    ListMoviesSetterGetter movie;

    ImageView newspaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jornais_info_view);
        movie = (ListMoviesSetterGetter)getIntent().getSerializableExtra("movie");
        newspaper = (ImageView)findViewById(R.id.newspaper);

        Picasso.with(getApplicationContext())
                .load(movie.getPraias_imagem())
                .into(newspaper);
    }
}

package com.evilkingmedia.sports;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.model.MoviesModel;
import com.evilkingmedia.model.SportsModel;
import com.evilkingmedia.sports.adapter.FootballAdapter;
import com.evilkingmedia.sports.adapter.FootballDetailAdapter;
import com.evilkingmedia.sports.adapter.FootballFullMatchAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FootballFullMatchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressDialog mProgressDialog;

    FootballFullMatchAdapter detailAdapter;
    List<SportsModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_football_full_match);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getFullMatch(getIntent().getStringExtra("childNodes"));
    }

    private void getFullMatch(String childNodes){
        Document doc = Jsoup.parse(childNodes);
        Elements body = doc.select("ul").select("li");
        for (int i = 0; i < body.size(); i++){
            String title = body.get(i).select("a").select("span").text();
            String nodes = body.get(i).children().toString();
            SportsModel model = new SportsModel();
            model.setTitle(title);
            model.setLinkNodeString(nodes);
            modelList.add(model);
        }

        detailAdapter = new FootballFullMatchAdapter(modelList, this);
        recyclerView.setAdapter(detailAdapter);
        detailAdapter.notifyDataSetChanged();
    }
}

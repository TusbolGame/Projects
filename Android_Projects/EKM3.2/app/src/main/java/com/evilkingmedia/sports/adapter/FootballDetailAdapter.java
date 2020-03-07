package com.evilkingmedia.sports.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.VideoPlayerActivity;
import com.evilkingmedia.demand.WebViewActivity;
import com.evilkingmedia.model.MoviesModel;
import com.evilkingmedia.model.SportsModel;
import com.evilkingmedia.sports.FootballOnDemandDetailActivity;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FootballDetailAdapter extends RecyclerView.Adapter<FootballDetailAdapter.myview> {
    private List<SportsModel> sportsModels;
    private Context context;
    private ProgressDialog mProgressDialog;
    String videoPath;

    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView title, time;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            title = view.findViewById(R.id.sportDate);
            time = view.findViewById(R.id.sportTime);
        }
    }

    public FootballDetailAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
    }

    @NonNull
    @Override
    public FootballDetailAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new FootballDetailAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FootballDetailAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.title.setText(model.getTitle());
        holder.time.setText("");
        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new showVideo(model.getUrl()).execute();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

    private class showVideo extends AsyncTask<String, Void, Void> {

        String url;

        private showVideo(String url){
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle("");
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                Connection.Response response = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                        .referrer("http://www.google.com")
                        .method(Connection.Method.POST)//or Method.POST
                        .followRedirects(true)
                        .execute();

                String table = response.body();

                Document doc = Jsoup.parse(table);
                String str = doc.select("div[class=tab-content]").select("iframe").attr("src");
                if (str.contains("http")){
                    videoPath = str;
                } else {
                    videoPath = "http:" + str;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            Constant.openWVCapp(context, videoPath);
        }

    }

}


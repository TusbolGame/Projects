package com.liveitandroid.liveit.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.view.activity.SeasonsActivitiy;
import com.liveitandroid.liveit.view.activity.SeriesDetailActivity;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.callback.SeriesDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.activity.SeriesDetailActivity;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SeriesStreamsAdapter extends Adapter<SeriesStreamsAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private List<SeriesDBModel> completeList;
    public Context context;
    private List<SeriesDBModel> dataSet;
    private DatabaseHandler database;
    private Editor editor;
    private List<SeriesDBModel> filterList = new ArrayList();
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    MyViewHolder myViewHolder;
    private SharedPreferences pref;
    private SimpleDateFormat programTimeFormat;
    public int text_last_size;
    public int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_channel_logo)
        ImageView ivChannelLogo;
        @BindView(R.id.iv_favourite)
        ImageView ivFavourite;
        @BindView(R.id.ll_menu)
        LinearLayout llMenu;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.rl_channel_bottom)
        RelativeLayout rlChannelBottom;
        @BindView(R.id.rl_movie_image)
        RelativeLayout rlMovieImage;
        @BindView(R.id.rl_streams_layout)
        RelativeLayout rlStreamsLayout;
        @BindView(R.id.tv_movie_name)
        TextView tvChannelName;
        @BindView(R.id.tv_current_live)
        TextView tvCurrentLive;
        @BindView(R.id.tv_streamOptions)
        TextView tvStreamOptions;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setIsRecyclable(false);
        }
    }

    public SeriesStreamsAdapter(List<SeriesDBModel> liveStreamCategories, Context context) {
        this.dataSet = liveStreamCategories;
        this.context = context;
        this.filterList.addAll(liveStreamCategories);
        this.completeList = liveStreamCategories;
        this.database = new DatabaseHandler(context);
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.pref = this.context.getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG = this.pref.getInt(AppConst.LIVE_STREAM, 0);
        if (AppConst.LIVE_FLAG == 1) {
            this.myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_layout, parent, false));
            return this.myViewHolder;
        }
        this.myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.live_streams_linear_layout, parent, false));
        return this.myViewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            final String finalNum;
            final String finalName;
            final String finalStreamType;
            final int finalSeriesID;
            final String finalCover;
            final String finalPlot;
            final String finalCast;
            final String finalDirector;
            final String finalGenre;
            final String finalReleaseDate;
            final String finalLast_modified;
            final String finalRating;
            final String finalCategoryId1;
            final String finalYoutube;
            int finalSeriesID1;
            String finalCategoryId;
            ArrayList<FavouriteDBModel> checkFavourite;
            final MyViewHolder myViewHolder;
            final int i;
            final String str;
            String num = "";
            String name = "";
            String streamType = "";
            int seriesID = -1;
            String cover = "";
            String plot = "";
            String cast = "";
            String director = "";
            String genre = "";
            String releaseDate = "";
            String last_modified = "";
            String rating = "";
            String categoryId = "";
            String youtubeMov = "";
            if (this.dataSet != null) {
                SeriesDBModel seriesDBModel = (SeriesDBModel) this.dataSet.get(listPosition);
                if (seriesDBModel.getNum() != null) {
                    num = seriesDBModel.getNum();
                }
                if (seriesDBModel.getName() != null) {
                    name = seriesDBModel.getName();
                }
                if (seriesDBModel.getStreamType() != null) {
                    streamType = seriesDBModel.getStreamType();
                }
                if (seriesDBModel.getseriesID() != -1) {
                    seriesID = seriesDBModel.getseriesID();
                }
                if (seriesDBModel.getcover() != null) {
                    cover = seriesDBModel.getcover();
                }
                if (seriesDBModel.getplot() != null) {
                    plot = seriesDBModel.getplot();
                }
                if (seriesDBModel.getcast() != null) {
                    cast = seriesDBModel.getcast();
                }
                if (seriesDBModel.getdirector() != null) {
                    director = seriesDBModel.getdirector();
                }
                if (seriesDBModel.getgenre() != null) {
                    genre = seriesDBModel.getgenre();
                }
                if (seriesDBModel.getreleaseDate() != null) {
                    releaseDate = seriesDBModel.getreleaseDate();
                }
                if (seriesDBModel.getlast_modified() != null) {
                    last_modified = seriesDBModel.getlast_modified();
                }
                if (seriesDBModel.getrating() != null) {
                    rating = seriesDBModel.getrating();
                }
                if (seriesDBModel.getCategoryId() != null) {
                    categoryId = seriesDBModel.getCategoryId();
                }
                if (seriesDBModel.getCategoryId() != null) {
                    youtubeMov = seriesDBModel.getYoutube();
                }
            }
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            holder.tvTime.setText("");
            holder.progressBar.setVisibility(8);
            holder.tvCurrentLive.setText("");
            holder.tvChannelName.setText(((SeriesDBModel) this.dataSet.get(listPosition)).getName());
            if (cover != null) {
                if (!cover.equals("")) {
                    Picasso.with(this.context).load(cover).placeholder((int) R.drawable.tranparentdark).into(holder.ivChannelLogo);
                    finalNum = num;
                    finalName = name;
                    finalStreamType = streamType;
                    finalSeriesID = seriesID;
                    finalCover = cover;
                    finalPlot = plot;
                    finalCast = cast;
                    finalDirector = director;
                    finalGenre = genre;
                    finalReleaseDate = releaseDate;
                    finalLast_modified = last_modified;
                    finalRating = rating;
                    finalCategoryId1 = categoryId;
                    finalYoutube = youtubeMov;
                    holder.cardView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum, finalName, finalStreamType, finalSeriesID, finalCover, finalPlot, finalCast, finalDirector, finalGenre, finalReleaseDate, finalLast_modified, finalRating, finalCategoryId1, finalYoutube);
                        }
                    });
                    holder.rlMovieImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum, finalName, finalStreamType, finalSeriesID, finalCover, finalPlot, finalCast, finalDirector, finalGenre, finalReleaseDate, finalLast_modified, finalRating, finalCategoryId1, finalYoutube);
                        }
                    });
                    holder.rlStreamsLayout.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SeriesStreamsAdapter.this.startSeriesViewActivitit(finalNum, finalName, finalStreamType, finalSeriesID, finalCover, finalPlot, finalCast, finalDirector, finalGenre, finalReleaseDate, finalLast_modified, finalRating, finalCategoryId1, finalYoutube);
                        }
                    });
                    finalSeriesID1 = seriesID;
                    finalCategoryId = categoryId;
                    checkFavourite = this.database.checkFavourite(finalSeriesID1, finalCategoryId, AppConst.SERIES);
                    if (checkFavourite != null || checkFavourite.size() <= 0) {
                        holder.ivFavourite.setVisibility(View.GONE);
                    } else {
                        holder.ivFavourite.setVisibility(View.VISIBLE);
                    }
                    myViewHolder = holder;
                    i = finalSeriesID1;
                    str = finalCategoryId;
                    holder.rlStreamsLayout.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            SeriesStreamsAdapter.this.popmenu(myViewHolder, i, str, context, finalName, finalCover);
                            return true;
                        }
                    });
                    holder.rlMovieImage.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            SeriesStreamsAdapter.this.popmenu(myViewHolder, i, str, context, finalName, finalCover);
                            return true;
                        }
                    });
                    holder.llMenu.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SeriesStreamsAdapter.this.popmenu(myViewHolder, i, str, context, finalName, finalCover);
                        }
                    });
                }
            }
            if (VERSION.SDK_INT >= 21) {
                holder.ivChannelLogo.setImageDrawable(this.context.getResources().getDrawable(R.drawable.tranparentdark, null));
            } else {
                holder.ivChannelLogo.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.tranparentdark));
            }
        }
    }

    private void startSeriesViewActivitit(String num, String name, String streamType, int seriesID, String cover, String plot, String cast, String director, String genre, String releaseDate, String last_modified, String rating, String categoryId, String youtube) {
        if (this.context != null) {
            Intent viewDetailsActivityIntent = new Intent(this.context, SeriesDetailActivity.class);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_NUM, num);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_NAME, name);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_STREAM_TYPE, streamType);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_SERIES_ID, String.valueOf(seriesID));
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_COVER, cover);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_PLOT, plot);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_CAST, cast);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_DIRECTOR, director);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_GENERE, genre);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_RELEASE_DATE, releaseDate);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_LAST_MODIFIED, last_modified);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_RATING, rating);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_CATEGORY_ID, categoryId);
            viewDetailsActivityIntent.putExtra(AppConst.SERIES_Youtube, youtube);
            this.context.startActivity(viewDetailsActivityIntent);
        }
    }

    private void popmenu(final MyViewHolder holder, final int streamId, final String categoryId, final Context contt, final String NameSerie, final String imageSerie) {
        PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
        popup.inflate(R.menu.menu_card_series_streams);
        ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(streamId, categoryId, "series");
        if (checkFavourite == null || checkFavourite.size() <= 0) {
            popup.getMenu().getItem(1).setVisible(true);
        } else {
            popup.getMenu().getItem(2).setVisible(true);
        }
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_series_details:
                        Seriesinfo();
                        break;
                    case R.id.nav_add_to_fav:
                        addToFavourite();
                        break;
                    case R.id.nav_play:
                        playMovie();
                        break;
                    case R.id.nav_remove_from_fav:
                        removeFromFavourite();
                        break;
                }
                return false;
            }

            private void Seriesinfo() {
                holder.cardView.performClick();
            }

            private void addToFavourite() {
                FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel();
                LiveStreamsFavourite.setCategoryID(categoryId);
                LiveStreamsFavourite.setStreamID(streamId);
                SeriesStreamsAdapter.this.database.addToFavourite(LiveStreamsFavourite, AppConst.SERIES);
                holder.ivFavourite.setVisibility(View.VISIBLE);
            }

            private void removeFromFavourite() {
                SeriesStreamsAdapter.this.database.deleteFavourite(streamId, categoryId, AppConst.SERIES);
                holder.ivFavourite.setVisibility(View.GONE);
            }

            private void playMovie() {
                contt.startActivity(new Intent(contt, SeasonsActivitiy.class).putExtra(AppConst.SERIES_SERIES_ID, streamId).putExtra(AppConst.SERIES_COVER, imageSerie).putExtra(AppConst.SERIES_NAME, NameSerie));
            }
        });
        popup.show();
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C18991 implements Runnable {
                C18991() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        SeriesStreamsAdapter.this.dataSet = SeriesStreamsAdapter.this.completeList;
                    } else if (!SeriesStreamsAdapter.this.filterList.isEmpty() || SeriesStreamsAdapter.this.filterList.isEmpty()) {
                        SeriesStreamsAdapter.this.dataSet = SeriesStreamsAdapter.this.filterList;
                    }
                    if (SeriesStreamsAdapter.this.dataSet != null && SeriesStreamsAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(View.VISIBLE);
                    }
                    SeriesStreamsAdapter.this.text_last_size = SeriesStreamsAdapter.this.text_size;
                    SeriesStreamsAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                SeriesStreamsAdapter.this.filterList = new ArrayList();
                SeriesStreamsAdapter.this.text_size = text.length();
                if (SeriesStreamsAdapter.this.filterList != null) {
                    SeriesStreamsAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    SeriesStreamsAdapter.this.filterList.addAll(SeriesStreamsAdapter.this.completeList);
                } else {
                    if ((SeriesStreamsAdapter.this.dataSet != null && SeriesStreamsAdapter.this.dataSet.size() == 0) || SeriesStreamsAdapter.this.text_last_size > SeriesStreamsAdapter.this.text_size) {
                        SeriesStreamsAdapter.this.dataSet = SeriesStreamsAdapter.this.completeList;
                    }
                    if (SeriesStreamsAdapter.this.dataSet != null) {
                        for (SeriesDBModel item : SeriesStreamsAdapter.this.dataSet) {
                            if (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())) {
                                SeriesStreamsAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) SeriesStreamsAdapter.this.context).runOnUiThread(new C18991());
            }
        }).start();
    }
}

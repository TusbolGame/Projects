package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.view.activity.SeasonsActivitiy;
import com.liveitandroid.liveit.view.activity.SeriesDetailActivity;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivity;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.callback.SeriesDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.view.activity.SeriesDetailActivity;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class SeriesAdapter extends Adapter<SeriesAdapter.MyViewHolder> {
    private List<SeriesDBModel> completeList;
    private Context context;
    private List<SeriesDBModel> dataSet;
    DatabaseHandler database;
    private Editor editor;
    private List<SeriesDBModel> filterList = new ArrayList();
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences pref;
    private SharedPreferences settingsPrefs;
    public int text_last_size;
    public int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_movie)
        RelativeLayout Movie;
        @BindView(R.id.iv_movie_image)
        ImageView MovieImage;
        @BindView(R.id.tv_movie_name)
        TextView MovieName;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.iv_favourite)
        ImageView ivFavourite;
        @BindView(R.id.ll_menu)
        LinearLayout llMenu;
        @BindView(R.id.tv_movie_name1)
        TextView movieNameTV;
        @BindView(R.id.rl_movie_bottom)
        RelativeLayout rlMovieBottom;
        @BindView(R.id.tv_streamOptions)
        TextView tvStreamOptions;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setIsRecyclable(false);
        }
    }


    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        @RequiresApi(api = 21)
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 1.0f;
            if (hasFocus) {
                if (hasFocus) {
                    to = 1.1f;
                }
                performScaleXAnimation(to);
                Log.e("id is", "" + this.view.getTag());
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
            }
        }

        private void performScaleXAnimation(float to) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this.view, "scaleX", new float[]{to});
            scaleXAnimator.setDuration(150);
            scaleXAnimator.start();
        }

        private void performScaleYAnimation(float to) {
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this.view, "scaleY", new float[]{to});
            scaleYAnimator.setDuration(150);
            scaleYAnimator.start();
        }

        private void performAlphaAnimation(boolean hasFocus) {
            if (hasFocus) {
                float toAlpha = hasFocus ? 0.6f : 0.5f;
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.view, "alpha", new float[]{toAlpha});
                alphaAnimator.setDuration(150);
                alphaAnimator.start();
            }
        }
    }

    public SeriesAdapter(List<SeriesDBModel> vodCategories, Context context) {
        this.dataSet = vodCategories;
        this.context = context;
        this.filterList.addAll(vodCategories);
        this.completeList = vodCategories;
        this.database = new DatabaseHandler(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.pref = this.context.getSharedPreferences("listgridview", 0);
        this.editor = this.pref.edit();
        AppConst.LIVE_FLAG = this.pref.getInt("livestream", 0);
        MyViewHolder myViewHolder;
        if (AppConst.LIVE_FLAG == 1) {
            myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vod_layout, parent, false));
            return myViewHolder;
        }
        myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vod_layout, parent, false));
        return myViewHolder;

    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            final String finalNum;
            final String finalName;
            final String finalyoutube;
            final String finalStreamType;
            int finalSeriesID = 0;
            final String finalCover;
            final String finalPlot;
            final String finalCast;
            final String finalDirector;
            final String finalGenre;
            final String finalReleaseDate;
            final String finalLast_modified;
            final String finalRating;
            final String finalCategoryId1;
            final MyViewHolder myViewHolder;
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
            String youtube = "";
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
                if (seriesDBModel.getYoutube() != null) {
                    youtube = seriesDBModel.getYoutube();
                }

            }
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            this.pref = this.context.getSharedPreferences(AppConst.LIST_GRID_VIEW, 0);
            this.editor = this.pref.edit();
            AppConst.LIVE_FLAG_VOD = this.pref.getInt(AppConst.VOD, 0);
            if (AppConst.LIVE_FLAG_VOD == 1) {
                holder.MovieName.setText(((SeriesDBModel) this.dataSet.get(listPosition)).getName());
            } else {
                holder.movieNameTV.setText(((SeriesDBModel) this.dataSet.get(listPosition)).getName());
            }

            if (!(context == null || cover == null || cover.isEmpty())) {
                Picasso.with(context).load(cover).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
            }else{
                Picasso.with(context).load(R.drawable.tranparentdark).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
            }

            finalNum = num;
            finalName = name;
            finalStreamType = streamType;
            finalSeriesID = seriesID;
            finalCover = cover;
            finalyoutube = youtube;
            finalPlot = plot;
            finalCast = cast;
            finalDirector = director;
            finalGenre = genre;
            finalReleaseDate = releaseDate;
            finalLast_modified = last_modified;
            finalRating = rating;
            finalCategoryId1 = categoryId;
            final int finalSeriesID6 = finalSeriesID;
            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SeriesAdapter.this.startSeriesViewActivitit(finalNum, finalName, finalStreamType, finalSeriesID6, finalCover, finalPlot, finalCast, finalDirector, finalGenre, finalReleaseDate, finalLast_modified, finalRating, finalCategoryId1, finalyoutube);
                }
            });
            final int finalSeriesID3 = finalSeriesID;
            holder.MovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SeriesAdapter.this.startSeriesViewActivitit(finalNum, finalName, finalStreamType, finalSeriesID3, finalCover, finalPlot, finalCast, finalDirector, finalGenre, finalReleaseDate, finalLast_modified, finalRating, finalCategoryId1, finalyoutube);
                }
            });
            final int finalSeriesID4 = finalSeriesID;
            holder.Movie.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    SeriesAdapter.this.startSeriesViewActivitit(finalNum, finalName, finalStreamType, finalSeriesID4, finalCover, finalPlot, finalCast, finalDirector, finalGenre, finalReleaseDate, finalLast_modified, finalRating, finalCategoryId1, finalyoutube);
                }
            });
            holder.Movie.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.Movie));
            myViewHolder = holder;
            str = finalCategoryId1;
            final int finalSeriesID7 = finalSeriesID;
            holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    SeriesAdapter.this.popmenu(myViewHolder, finalSeriesID7, str, context, finalName, finalCover);
                    return true;
                }
            });
            final int finalSeriesID5 = finalSeriesID;
            holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    SeriesAdapter.this.popmenu(myViewHolder, finalSeriesID5, str, context, finalName, finalCover);
                    return true;
                }
            });
            final int finalSeriesID1 = finalSeriesID;
            holder.cardView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    SeriesAdapter.this.popmenu(myViewHolder, finalSeriesID1, str, context, finalName, finalCover);
                    return true;
                }
            });
            final int finalSeriesID2 = finalSeriesID;
            holder.llMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    SeriesAdapter.this.startSeriesViewActivitit(finalNum, finalName, finalStreamType, finalSeriesID2, finalCover, finalPlot, finalCast, finalDirector, finalGenre, finalReleaseDate, finalLast_modified, finalRating, finalCategoryId1, finalyoutube);
                }
            });

            if (this.database.checkFavourite(finalSeriesID, "-1", AppConst.SERIES).size() <= 0) {
                holder.ivFavourite.setVisibility(View.GONE);
            } else {
                holder.ivFavourite.setVisibility(View.VISIBLE);
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

        /*ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(streamId, "-1", AppConst.SERIES);
        if (checkFavourite == null || checkFavourite.size() <= 0) {
            popup.getMenu().getItem(1).setVisible(true);
        } else {
            popup.getMenu().getItem(2).setVisible(true);
        }*/
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_series_details:
                        Seriesinfo();
                        break;
                    case R.id.nav_add_to_fav:
                        addToFavourite();
                        break;
                    case R.id.nav_remove_from_fav:
                        removeFromFavourite();
                        break;
                    case R.id.nav_play:
                        playMovie();
                        break;
                }
                return false;
            }

            private void Seriesinfo() {
                holder.cardView.performClick();
            }

            private void addToFavourite() {
                FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel();
                LiveStreamsFavourite.setCategoryID("-1");
                LiveStreamsFavourite.setStreamID(streamId);
                SeriesAdapter.this.database.addToFavourite(LiveStreamsFavourite, AppConst.SERIES);
                holder.ivFavourite.setVisibility(View.VISIBLE);
            }

            private void removeFromFavourite() {
                SeriesAdapter.this.database.deleteFavourite(streamId, "-1", AppConst.SERIES);
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

            class C18871 implements Runnable {
                C18871() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        SeriesAdapter.this.dataSet = SeriesAdapter.this.completeList;
                    } else if (!SeriesAdapter.this.filterList.isEmpty() || SeriesAdapter.this.filterList.isEmpty()) {
                        SeriesAdapter.this.dataSet = SeriesAdapter.this.filterList;
                    }
                    if (SeriesAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(View.VISIBLE);
                    }
                    SeriesAdapter.this.text_last_size = SeriesAdapter.this.text_size;
                    SeriesAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                SeriesAdapter.this.filterList = new ArrayList();
                SeriesAdapter.this.text_size = text.length();
                if (SeriesAdapter.this.filterList != null) {
                    SeriesAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    SeriesAdapter.this.filterList.addAll(SeriesAdapter.this.completeList);
                } else {
                    if (SeriesAdapter.this.dataSet.size() == 0 || SeriesAdapter.this.text_last_size > SeriesAdapter.this.text_size) {
                        SeriesAdapter.this.dataSet = SeriesAdapter.this.completeList;
                    }
                    for (SeriesDBModel item : SeriesAdapter.this.dataSet) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            SeriesAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) SeriesAdapter.this.context).runOnUiThread(new C18871());
            }
        }).start();
    }
}

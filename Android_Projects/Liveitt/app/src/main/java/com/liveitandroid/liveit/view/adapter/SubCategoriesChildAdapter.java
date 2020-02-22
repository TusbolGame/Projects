package com.liveitandroid.liveit.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivity;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class SubCategoriesChildAdapter extends Adapter<SubCategoriesChildAdapter.MyViewHolder> {
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> dataSet;
    DatabaseHandler database;
    private Editor editor;
    private List<LiveStreamsDBModel> filterList = new ArrayList();
    private LiveStreamsDBModel liveStreamsDBModel;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences pref;
    private SharedPreferences settingsPrefs;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_movie)
        RelativeLayout Movie;
        @BindView(R.id.iv_movie_image)
        ImageView MovieImage;
        @BindView(R.id.tv_movie_name)
        TextView MovieName;
        @BindView(R.id.card_view)
        RelativeLayout cardView;
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

//    public class MyViewHolder_ViewBinding implements Unbinder {
//        private MyViewHolder target;
//
//        @UiThread
//        public MyViewHolder_ViewBinding(MyViewHolder target, View source) {
//            this.target = target;
//            target.MovieName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name, "field 'MovieName'", TextView.class);
//            target.Movie = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_movie, "field 'Movie'", RelativeLayout.class);
//            target.movieNameTV = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name1, "field 'movieNameTV'", TextView.class);
//            target.MovieImage = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_movie_image, "field 'MovieImage'", ImageView.class);
//            target.cardView = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.card_view, "field 'cardView'", RelativeLayout.class);
//            target.tvStreamOptions = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_streamOptions, "field 'tvStreamOptions'", TextView.class);
//            target.ivFavourite = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_favourite, "field 'ivFavourite'", ImageView.class);
//            target.rlMovieBottom = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_movie_bottom, "field 'rlMovieBottom'", RelativeLayout.class);
//            target.llMenu = (LinearLayout) Utils.findRequiredViewAsType(source, R.id.ll_menu, "field 'llMenu'", LinearLayout.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            MyViewHolder target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.MovieName = null;
//            target.Movie = null;
//            target.movieNameTV = null;
//            target.MovieImage = null;
//            target.cardView = null;
//            target.tvStreamOptions = null;
//            target.ivFavourite = null;
//            target.rlMovieBottom = null;
//            target.llMenu = null;
//        }
//    }

    public SubCategoriesChildAdapter(List<LiveStreamsDBModel> vodCategories, Context context) {
        this.dataSet = vodCategories;
        this.context = context;
        this.filterList.addAll(vodCategories);
        this.completeList = vodCategories;
        this.database = new DatabaseHandler(context);
        this.liveStreamsDBModel = this.liveStreamsDBModel;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_subcateories_cihild_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            final int i;
            final String str;
            final String str2;
            final String str3;
            final String str4;
            final String str5;
            final MyViewHolder myViewHolder;
            final int i2;
            final String str6;
            final String str7;
            final String str8;
            final String str9;
            final String str11;
            final String str12;
            Context context = this.context;
            String str10 = AppConst.LOGIN_PREF_SELECTED_PLAYER;
            Context context2 = this.context;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str10, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            final int streamId = Integer.parseInt(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId());
            final String categoryId = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getCategoryId();
            final String containerExtension = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getContaiinerExtension();
            final String streamType = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamType();
            final String num = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getNum();
            holder.MovieName.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            holder.movieNameTV.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            String StreamIcon = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamIcon();
            final String movieName = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName();
            final String movieYoutube= ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getYoutube();
            if (StreamIcon != null) {
                if (!StreamIcon.equals("")) {
                    Picasso.with(this.context).load(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamIcon()).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
                    if (this.database.checkFavourite(streamId, categoryId, AppConst.VOD).size() <= 0) {
                        holder.ivFavourite.setVisibility(View.VISIBLE);
                    } else {
                        holder.ivFavourite.setVisibility(4);
                    }
                    holder.cardView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            Utils.playWithPlayerVOD(SubCategoriesChildAdapter.this.context, selectedPlayer, streamId, streamType, containerExtension, num, movieName, "","","","");
                        }
                    });
                    i = streamId;
                    str = movieName;
                    str2 = selectedPlayer;
                    str3 = streamType;
                    str4 = containerExtension;
                    str5 = num;
                    str11 = movieYoutube;
                    holder.MovieImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SubCategoriesChildAdapter.this.startViewDeatilsActivity(i, str, str2, str3, str4, categoryId, str5, str11);
                        }
                    });
                    holder.Movie.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            SubCategoriesChildAdapter.this.startViewDeatilsActivity(i, str, str2, str3, str4, categoryId, str5, str11);
                        }
                    });
                    myViewHolder = holder;
                    i2 = streamId;
                    //str5 = movieName;
                    str6 = selectedPlayer;
                    str7 = streamType;
                    str8 = containerExtension;
                    str9 = num;
                    str12 = movieYoutube;
                    holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            SubCategoriesChildAdapter.this.popmenu(myViewHolder, i2, categoryId, str5, str6, str7, str8, str9, str12);
                            return true;
                        }
                    });
                    holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            SubCategoriesChildAdapter.this.popmenu(myViewHolder, i2, categoryId, str5, str6, str7, str8, str9, str12);
                            return true;
                        }
                    });
                    holder.llMenu.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SubCategoriesChildAdapter.this.popmenu(myViewHolder, i2, categoryId, str5, str6, str7, str8, str9, str12);
                        }
                    });
                }
            }

            if (this.database.checkFavourite(streamId, categoryId, AppConst.VOD).size() <= 0) {
                holder.ivFavourite.setVisibility(4);
            } else {
                holder.ivFavourite.setVisibility(0);
            }
//            holder.cardView.setOnClickListener(/* anonymous class already generated */);
//            i = streamId;
//            str = movieName;
//            str2 = selectedPlayer;
//            str3 = streamType;
//            str4 = containerExtension;
//            str5 = num;
//            holder.MovieImage.setOnClickListener(/* anonymous class already generated */);
//            i = streamId;
//            str = movieName;
//            str2 = selectedPlayer;
//            str3 = streamType;
//            str4 = containerExtension;
//            str5 = num;
//            holder.Movie.setOnClickListener(/* anonymous class already generated */);
//            myViewHolder = holder;
//            i2 = streamId;
//            str5 = movieName;
//            str6 = selectedPlayer;
//            str7 = streamType;
//            str8 = containerExtension;
//            str9 = num;
//            holder.Movie.setOnLongClickListener(/* anonymous class already generated */);
//            myViewHolder = holder;
//            i2 = streamId;
//            str5 = movieName;
//            str6 = selectedPlayer;
//            str7 = streamType;
//            str8 = containerExtension;
//            str9 = num;
//            holder.MovieImage.setOnLongClickListener(/* anonymous class already generated */);
//            myViewHolder = holder;
//            i2 = streamId;
//            str5 = movieName;
//            str6 = selectedPlayer;
//            str7 = streamType;
//            str8 = containerExtension;
//            str9 = num;
//            holder.llMenu.setOnClickListener(/* anonymous class already generated */);
        }
    }

    private void startViewDeatilsActivity(int streamId, String movieName, String selectedPlayer, String streamType, String containerExtension, String categoryId, String num, String youtube) {
        if (this.context != null) {
            Intent viewDetailsActivityIntent = new Intent(this.context, ViewDetailsActivity.class);
            viewDetailsActivityIntent.putExtra(AppConst.STREAM_ID, String.valueOf(streamId));
            viewDetailsActivityIntent.putExtra("movie", movieName);
            viewDetailsActivityIntent.putExtra(AppConst.LOGIN_PREF_SELECTED_PLAYER, selectedPlayer);
            viewDetailsActivityIntent.putExtra("streamType", streamType);
            viewDetailsActivityIntent.putExtra("containerExtension", containerExtension);
            viewDetailsActivityIntent.putExtra("categoryID", categoryId);
            viewDetailsActivityIntent.putExtra("num", num);
            viewDetailsActivityIntent.putExtra("youtube", youtube);
            this.context.startActivity(viewDetailsActivityIntent);
        }
    }

    private void popmenu(MyViewHolder holder, int streamId, String categoryId, String movieName, String selectedPlayer, String streamType, String containerExtension, String num, String youtube) {
        PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
        popup.inflate(R.menu.menu_card_vod);
        if (this.database.checkFavourite(streamId, categoryId, AppConst.VOD).size() > 0) {
            popup.getMenu().getItem(2).setVisible(true);
        } else {
            popup.getMenu().getItem(1).setVisible(true);
        }
        final int i = streamId;
        final String str = movieName;
        final String str2 = selectedPlayer;
        final String str3 = streamType;
        final String str4 = containerExtension;
        final String str5 = categoryId;
        final String str6 = num;
        final String str7 = youtube;
        final MyViewHolder myViewHolder = holder;
        popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_view_details:
                        startViewDeatilsActivity(i, str, str2, str3, str4, str5, str6,str7);
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

            private void startViewDeatilsActivity(int streamId, String movieName, String selectedPlayer, String streamType, String containerExtension, String categoryId, String num, String youtube) {
                if (SubCategoriesChildAdapter.this.context != null) {
                    Intent viewDetailsActivityIntent = new Intent(SubCategoriesChildAdapter.this.context, ViewDetailsActivity.class);
                    viewDetailsActivityIntent.putExtra(AppConst.STREAM_ID, String.valueOf(streamId));
                    viewDetailsActivityIntent.putExtra("movie", movieName);
                    viewDetailsActivityIntent.putExtra(AppConst.LOGIN_PREF_SELECTED_PLAYER, selectedPlayer);
                    viewDetailsActivityIntent.putExtra("streamType", streamType);
                    viewDetailsActivityIntent.putExtra("containerExtension", containerExtension);
                    viewDetailsActivityIntent.putExtra("categoryID", categoryId);
                    viewDetailsActivityIntent.putExtra("num", num);
                    viewDetailsActivityIntent.putExtra("youtube", youtube);
                    SubCategoriesChildAdapter.this.context.startActivity(viewDetailsActivityIntent);
                }
            }

            private void playMovie() {
                myViewHolder.cardView.performClick();
            }

            private void addToFavourite() {
                FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel();
                LiveStreamsFavourite.setCategoryID(str5);
                LiveStreamsFavourite.setStreamID(i);
                SubCategoriesChildAdapter.this.database.addToFavourite(LiveStreamsFavourite, AppConst.VOD);
                myViewHolder.ivFavourite.setVisibility(0);
            }

            private void removeFromFavourite() {
                SubCategoriesChildAdapter.this.database.deleteFavourite(i, str5, AppConst.VOD);
                myViewHolder.ivFavourite.setVisibility(4);
            }
        });
        popup.show();
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C19081 implements Runnable {
                C19081() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        SubCategoriesChildAdapter.this.dataSet = SubCategoriesChildAdapter.this.completeList;
                    } else if (!SubCategoriesChildAdapter.this.filterList.isEmpty() || SubCategoriesChildAdapter.this.filterList.isEmpty()) {
                        SubCategoriesChildAdapter.this.dataSet = SubCategoriesChildAdapter.this.filterList;
                    }
                    if (SubCategoriesChildAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    SubCategoriesChildAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                SubCategoriesChildAdapter.this.filterList = new ArrayList();
                if (SubCategoriesChildAdapter.this.filterList != null) {
                    SubCategoriesChildAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    SubCategoriesChildAdapter.this.filterList.addAll(SubCategoriesChildAdapter.this.completeList);
                } else {
                    for (LiveStreamsDBModel item : SubCategoriesChildAdapter.this.dataSet) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            SubCategoriesChildAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) SubCategoriesChildAdapter.this.context).runOnUiThread(new C19081());
            }
        }).start();
    }
}

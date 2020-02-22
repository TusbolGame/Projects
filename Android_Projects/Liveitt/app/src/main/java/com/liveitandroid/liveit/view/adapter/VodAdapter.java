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

public class VodAdapter extends Adapter<VodAdapter.MyViewHolder> {
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> dataSet;
    DatabaseHandler database;
    private Editor editor;
    private List<LiveStreamsDBModel> filterList = new ArrayList();
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
            setIsRecyclable(true);
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

    public VodAdapter(List<LiveStreamsDBModel> vodCategories, Context context) {
        this.dataSet = vodCategories;
        this.context = context;
        this.filterList.addAll(vodCategories);
        this.completeList = vodCategories;
        this.database = new DatabaseHandler(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        pref = context.getSharedPreferences(AppConst.LIST_GRID_VIEW, Context.MODE_PRIVATE);
        editor = pref.edit();
        AppConst.LIVE_FLAG_VOD = pref.getInt(AppConst.VOD, 0);
        if (AppConst.LIVE_FLAG_VOD == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vod_linear_layout, parent, false);

            if (view != null) {
//                view.setFocusable(true);
//                view.setFocusableInTouchMode(true);
            }

        }
        else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vod_layout, parent, false);

            if (view != null) {
//                view.setFocusable(true);
//                view.setFocusableInTouchMode(true);
            }

        }


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            final int finalStreamId2;
            final int finalStreamId1;
            final String str;
            final String str2;
            final String str3;
            final String str4;
            final String str5;
            final String str6;
            final int finalStreamId;
            final String str7;
            final String str8;
            final String str9;
            final int finalStreamId4;
            final MyViewHolder myViewHolder;
            final String str10;
            final String str11;
            final String str12;
            final String str13;
            final int finalStreamId3;
            final MyViewHolder myViewHolder2;
            final String str14;
            final String str15;
            final String str16;
            final String str17;
            final int finalStreamId5;
            final MyViewHolder myViewHolder3;
            final String str18;
            final String str19;
            final String str20;
            final String str21;
            Context context = this.context;
            String str22 = AppConst.LOGIN_PREF_SELECTED_PLAYER;
            Context context2 = this.context;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str22, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            int streamId = 0;
            if (((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId() != null) {
                streamId = Integer.parseInt(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamId());
            }
            final String categoryId = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getCategoryId();
            final String containerExtension = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getContaiinerExtension();
            final String streamType = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamType();
            final String num = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getNum();
            holder.MovieName.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            holder.movieNameTV.setText(((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName());
            String StreamIcon = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getStreamIcon();
            final String movieName = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getName();
            final String movieYoutube = ((LiveStreamsDBModel) this.dataSet.get(listPosition)).getYoutube();


            if (!(context == null || StreamIcon == null || StreamIcon.isEmpty())) {
                Picasso.with(context).load(StreamIcon).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
            }else{
                Picasso.with(context).load(R.drawable.tranparentdark).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
            }

            finalStreamId2 = streamId;
            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    VodAdapter.this.startViewDeatilsActivity(finalStreamId2, movieName, selectedPlayer, streamType, containerExtension, categoryId, num, movieYoutube);
                }
            });
            finalStreamId1 = streamId;
            str = movieName;
            str2 = selectedPlayer;
            str3 = streamType;
            str4 = containerExtension;
            str5 = categoryId;
            str6 = num;
            holder.MovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    VodAdapter.this.startViewDeatilsActivity(finalStreamId1, str, str2, str3, str4, str5, str6, movieYoutube);
                }
            });
            finalStreamId = streamId;
            str7 = containerExtension;
            str8 = categoryId;
            str9 = num;
            holder.Movie.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    VodAdapter.this.startViewDeatilsActivity(finalStreamId, str4, str5, str6, str7, str8, str9, movieYoutube);
                }
            });
            holder.Movie.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.Movie));
            finalStreamId4 = streamId;
            myViewHolder = holder;
            str10 = selectedPlayer;
            str11 = streamType;
            str12 = containerExtension;
            str13 = num;
            holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    VodAdapter.this.popmenu(myViewHolder, finalStreamId4, str8, str9, str10, str11, str12, str13, movieYoutube);
                    return true;
                }
            });
            finalStreamId3 = streamId;
            myViewHolder2 = holder;
            str14 = selectedPlayer;
            str15 = streamType;
            str16 = containerExtension;
            str17 = num;
            holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    VodAdapter.this.popmenu(myViewHolder2, finalStreamId3, str12, str13, str14, str15, str16, str17, movieYoutube);
                    return true;
                }
            });
            holder.cardView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    VodAdapter.this.popmenu(myViewHolder2, finalStreamId3, str12, str13, str14, str15, str16, str17, movieYoutube);
                    return true;
                }
            });
            finalStreamId5 = streamId;
            myViewHolder3 = holder;
//                    str16 = categoryId;
//                    str17 = movieName;
            str18 = selectedPlayer;
            str19 = streamType;
            str20 = containerExtension;
            str21 = num;

            holder.llMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    VodAdapter.this.popmenu(myViewHolder3, finalStreamId5, str16, str17, str18, str19, str20, str21, movieYoutube);
                }
            });

            if (this.database.checkFavourite(streamId, "-1", AppConst.VOD).size() <= 0) {
                holder.ivFavourite.setVisibility(View.GONE);
            } else {
                holder.ivFavourite.setVisibility(View.VISIBLE);
            }

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
            return;
        }
    }

    private void popmenu(MyViewHolder holder, int streamId, String categoryId, String movieName, String selectedPlayer, String streamType, String containerExtension, String num, final String youtube) {
        if (this.context != null) {
            PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
            popup.inflate(R.menu.menu_card_vod);
            /*ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(streamId, "-1", AppConst.VOD);
            if (checkFavourite == null || checkFavourite.size() <= 0) {
                popup.getMenu().getItem(1).setVisible(true);
            } else {
                popup.getMenu().getItem(2).setVisible(true);
            }*/
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
                            startViewDeatilsActivity(i, str, str2, str3, str4, str5, str6, str7);
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
                    if (VodAdapter.this.context != null) {
                        Intent viewDetailsActivityIntent = new Intent(VodAdapter.this.context, ViewDetailsActivity.class);
                        viewDetailsActivityIntent.putExtra(AppConst.STREAM_ID, String.valueOf(streamId));
                        viewDetailsActivityIntent.putExtra("movie", movieName);
                        viewDetailsActivityIntent.putExtra(AppConst.LOGIN_PREF_SELECTED_PLAYER, selectedPlayer);
                        viewDetailsActivityIntent.putExtra("streamType", streamType);
                        viewDetailsActivityIntent.putExtra("containerExtension", containerExtension);
                        viewDetailsActivityIntent.putExtra("categoryID", categoryId);
                        viewDetailsActivityIntent.putExtra("num", num);
                        viewDetailsActivityIntent.putExtra("youtube", youtube);
                        VodAdapter.this.context.startActivity(viewDetailsActivityIntent);
                    }
                }

                private void playMovie() {
                    Utils.playWithPlayerVOD(VodAdapter.this.context, str2, i, str3, str4, str6, str,"","","","");
                }

                private void addToFavourite() {
                    FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel();
                    LiveStreamsFavourite.setCategoryID("-1");
                    LiveStreamsFavourite.setStreamID(i);
                    VodAdapter.this.database.addToFavourite(LiveStreamsFavourite, AppConst.VOD);
                    myViewHolder.ivFavourite.setVisibility(View.VISIBLE);
                }

                private void removeFromFavourite() {
                    VodAdapter.this.database.deleteFavourite(i, "-1", AppConst.VOD);
                    myViewHolder.ivFavourite.setVisibility(4);
                }
            });
            popup.show();
        }
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C19321 implements Runnable {
                C19321() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        VodAdapter.this.dataSet = VodAdapter.this.completeList;
                    } else if (!VodAdapter.this.filterList.isEmpty() || VodAdapter.this.filterList.isEmpty()) {
                        VodAdapter.this.dataSet = VodAdapter.this.filterList;
                    }
                    if (VodAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(View.VISIBLE);
                    }
                    VodAdapter.this.text_last_size = VodAdapter.this.text_size;
                    VodAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                VodAdapter.this.filterList = new ArrayList();
                VodAdapter.this.text_size = text.length();
                if (VodAdapter.this.filterList != null) {
                    VodAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    VodAdapter.this.filterList.addAll(VodAdapter.this.completeList);
                } else {
                    if (VodAdapter.this.dataSet.size() == 0 || VodAdapter.this.text_last_size > VodAdapter.this.text_size) {
                        VodAdapter.this.dataSet = VodAdapter.this.completeList;
                    }
                    for (LiveStreamsDBModel item : VodAdapter.this.dataSet) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            VodAdapter.this.filterList.add(item);
                        }
                    }
                }
                ((Activity) VodAdapter.this.context).runOnUiThread(new C19321());
            }
        }).start();
    }
}

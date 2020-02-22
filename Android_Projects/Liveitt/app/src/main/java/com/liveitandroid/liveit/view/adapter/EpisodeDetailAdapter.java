package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.callback.GetEpisdoeDetailsCallback;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EpisodeDetailAdapter extends Adapter<EpisodeDetailAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private List<GetEpisdoeDetailsCallback> completeList;
    private Context context;
    private List<GetEpisdoeDetailsCallback> dataSet;
    private DatabaseHandler database;
    private Editor editor;
    private List<GetEpisdoeDetailsCallback> filterList = new ArrayList();
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    MyViewHolder myViewHolder;
    private SharedPreferences pref;
    private SimpleDateFormat programTimeFormat;
    private String seriesCover;
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

//    public class MyViewHolder_ViewBinding implements Unbinder {
//        private MyViewHolder target;
//
//        @UiThread
//        public MyViewHolder_ViewBinding(MyViewHolder target, View source) {
//            this.target = target;
//            target.MovieName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name, "field 'MovieName'", TextView.class);
//            target.movieNameTV = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_name1, "field 'movieNameTV'", TextView.class);
//            target.Movie = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_movie, "field 'Movie'", RelativeLayout.class);
//            target.MovieImage = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_movie_image, "field 'MovieImage'", ImageView.class);
//            target.cardView = (CardView) Utils.findRequiredViewAsType(source, R.id.card_view, "field 'cardView'", CardView.class);
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
//            target.movieNameTV = null;
//            target.Movie = null;
//            target.MovieImage = null;
//            target.cardView = null;
//            target.tvStreamOptions = null;
//            target.ivFavourite = null;
//            target.rlMovieBottom = null;
//            target.llMenu = null;
//        }
//    }

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

    public EpisodeDetailAdapter(List<GetEpisdoeDetailsCallback> liveStreamCategories, Context context, String seriesCover) {
        this.dataSet = liveStreamCategories;
        this.context = context;
        this.filterList.addAll(liveStreamCategories);
        this.completeList = liveStreamCategories;
        this.database = new DatabaseHandler(context);
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
        this.seriesCover = seriesCover;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vod_grid_layout, parent, false));
        return this.myViewHolder;
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            final String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            String trimmed = "";
            int streamId = -1;
            holder.ivFavourite.setVisibility(8);
            if (this.dataSet.get(listPosition) != null) {
                if (((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getId() != null) {
                    streamId = Integer.parseInt(((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getId().trim());
                }
                String name = "";
                if (((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getTitle() != null) {
                    holder.movieNameTV.setText(((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getTitle());
                    name = ((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getTitle();
                }
                String StreamIcon = this.seriesCover;
                String containerExtension = "";
                if (((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getContainerExtension() != null) {
                    containerExtension = ((GetEpisdoeDetailsCallback) this.dataSet.get(listPosition)).getContainerExtension();
                }
                if (StreamIcon != null && !StreamIcon.equals("")) {
                    Picasso.with(this.context).load(StreamIcon).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
                } else if (VERSION.SDK_INT >= 21) {
                    holder.MovieImage.setImageDrawable(this.context.getResources().getDrawable(R.drawable.tranparentdark, null));
                } else {
                    holder.MovieImage.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.tranparentdark));
                }
                final int finalStreamId = streamId;
                final String finalContainerExtension = containerExtension;
                final String finalName = name;
                final int i = listPosition;
                holder.cardView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        com.liveitandroid.liveit.miscelleneious.common.Utils.playSeries(EpisodeDetailAdapter.this.context, selectedPlayer, finalStreamId, null, finalContainerExtension, String.valueOf(i), finalName);
                    }
                });
                //i = listPosition;
                holder.MovieImage.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        com.liveitandroid.liveit.miscelleneious.common.Utils.playSeries(EpisodeDetailAdapter.this.context, selectedPlayer, finalStreamId, null, finalContainerExtension, String.valueOf(i), finalName);
                    }
                });
                //i = listPosition;
                holder.Movie.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        com.liveitandroid.liveit.miscelleneious.common.Utils.playSeries(EpisodeDetailAdapter.this.context, selectedPlayer, finalStreamId, null, finalContainerExtension, String.valueOf(i), finalName);
                    }
                });
                holder.Movie.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.Movie));
            }
        }
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C18431 implements Runnable {
                C18431() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        EpisodeDetailAdapter.this.dataSet = EpisodeDetailAdapter.this.completeList;
                    } else if (!EpisodeDetailAdapter.this.filterList.isEmpty() || EpisodeDetailAdapter.this.filterList.isEmpty()) {
                        EpisodeDetailAdapter.this.dataSet = EpisodeDetailAdapter.this.filterList;
                    }
                    if (EpisodeDetailAdapter.this.dataSet != null && EpisodeDetailAdapter.this.dataSet.size() == 0) {
                        tvNoRecordFound.setVisibility(View.VISIBLE);
                    }
                    EpisodeDetailAdapter.this.text_last_size = EpisodeDetailAdapter.this.text_size;
                    EpisodeDetailAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                EpisodeDetailAdapter.this.filterList = new ArrayList();
                EpisodeDetailAdapter.this.text_size = text.length();
                if (EpisodeDetailAdapter.this.filterList != null) {
                    EpisodeDetailAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    EpisodeDetailAdapter.this.filterList.addAll(EpisodeDetailAdapter.this.completeList);
                } else {
                    if ((EpisodeDetailAdapter.this.dataSet != null && EpisodeDetailAdapter.this.dataSet.size() == 0) || EpisodeDetailAdapter.this.text_last_size > EpisodeDetailAdapter.this.text_size) {
                        EpisodeDetailAdapter.this.dataSet = EpisodeDetailAdapter.this.completeList;
                    }
                    if (EpisodeDetailAdapter.this.dataSet != null) {
                        for (GetEpisdoeDetailsCallback item : EpisodeDetailAdapter.this.dataSet) {
                            if (item.getTitle() != null && item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                                EpisodeDetailAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) EpisodeDetailAdapter.this.context).runOnUiThread(new C18431());
            }
        }).start();
    }
}

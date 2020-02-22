package com.liveitandroid.liveit.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.view.nstplayer.NSTPlayerVodActivity;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerVodActivity;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MoviesOnVideoAdapter extends Adapter<MoviesOnVideoAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private int adapterPosition;
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> filterList = new ArrayList();
    private LiveStreamDBHandler liveStreamDBHandler;
    private List<LiveStreamsDBModel> moviesListl;
    private SimpleDateFormat programTimeFormat;
    private int text_last_size;
    private int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.iv_tv_icon)
        ImageView ivChannelLogo;
        @BindView(R.id.pb_paging_loader)
        ProgressBar pbPagingLoader;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.rl_list_of_categories)
        RelativeLayout rlListOfCategories;
        @BindView(R.id.rl_outer)
        RelativeLayout rlOuter;
        @BindView(R.id.testing)
        RelativeLayout testing;
        @BindView(R.id.tv_channel_id)
        TextView tvChannelId;
        @BindView(R.id.tv_current_live)
        TextView tvCurrentLive;
        @BindView(R.id.tv_movie_category_name)
        TextView tvMovieCategoryName;
        @BindView(R.id.tv_time)
        TextView tvTime;

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
//            target.tvMovieCategoryName = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_movie_category_name, "field 'tvMovieCategoryName'", TextView.class);
//            target.pbPagingLoader = (ProgressBar) Utils.findRequiredViewAsType(source, R.id.pb_paging_loader, "field 'pbPagingLoader'", ProgressBar.class);
//            target.rlOuter = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_outer, "field 'rlOuter'", RelativeLayout.class);
//            target.rlListOfCategories = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.rl_list_of_categories, "field 'rlListOfCategories'", RelativeLayout.class);
//            target.testing = (RelativeLayout) Utils.findRequiredViewAsType(source, R.id.testing, "field 'testing'", RelativeLayout.class);
//            target.tvChannelId = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_channel_id, "field 'tvChannelId'", TextView.class);
//            target.tvTime = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
//            target.progressBar = (ProgressBar) Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
//            target.tvCurrentLive = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_current_live, "field 'tvCurrentLive'", TextView.class);
//            target.ivChannelLogo = (ImageView) Utils.findRequiredViewAsType(source, R.id.iv_tv_icon, "field 'ivChannelLogo'", ImageView.class);
//        }
//
//        @CallSuper
//        public void unbind() {
//            MyViewHolder target = this.target;
//            if (target == null) {
//                throw new IllegalStateException("Bindings already cleared.");
//            }
//            this.target = null;
//            target.tvMovieCategoryName = null;
//            target.pbPagingLoader = null;
//            target.rlOuter = null;
//            target.rlListOfCategories = null;
//            target.testing = null;
//            target.tvChannelId = null;
//            target.tvTime = null;
//            target.progressBar = null;
//            target.tvCurrentLive = null;
//            target.ivChannelLogo = null;
//        }
//    }

    public MoviesOnVideoAdapter(List<LiveStreamsDBModel> movieList, Context context) {
        this.filterList.addAll(movieList);
        this.completeList = movieList;
        this.moviesListl = movieList;
        this.context = context;
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movies_on_video, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        String channelName = "";
        String channelNumber = "";
        String StreamIcon = "";
        holder.tvChannelId.setVisibility(8);
        final LiveStreamsDBModel data = (LiveStreamsDBModel) this.moviesListl.get(listPosition);
        channelName = data.getName();
        channelNumber = data.getNum();
        StreamIcon = data.getStreamIcon();
        if (!(channelName == null || channelName.equals("") || channelName.isEmpty())) {
            holder.tvMovieCategoryName.setText(channelName);
        }
        if (holder.tvChannelId != null) {
            holder.tvChannelId.setText(channelNumber);
        }
        holder.ivChannelLogo.setImageDrawable(null);
        if (StreamIcon != null && !StreamIcon.equals("")) {
            Picasso.with(this.context).load(StreamIcon).placeholder((int) R.drawable.tv_icon).into(holder.ivChannelLogo);
        } else if (VERSION.SDK_INT >= 21) {
            holder.ivChannelLogo.setImageDrawable(this.context.getResources().getDrawable(R.drawable.tv_icon, null));
        } else {
            holder.ivChannelLogo.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.tv_icon));
        }
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((NSTPlayerVodActivity) MoviesOnVideoAdapter.this.context).onClickCalled(data);
            }
        });
        holder.rlListOfCategories.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((NSTPlayerVodActivity) MoviesOnVideoAdapter.this.context).onClickCalled(data);
            }
        });
    }

    public int getItemCount() {
        return this.moviesListl.size();
    }

    public void setVisibiltygone(ProgressBar pbPagingLoader) {
        if (pbPagingLoader != null) {
            pbPagingLoader.setVisibility(8);
        }
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C18651 implements Runnable {
                C18651() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        MoviesOnVideoAdapter.this.moviesListl = MoviesOnVideoAdapter.this.completeList;
                    } else if (!MoviesOnVideoAdapter.this.filterList.isEmpty() || MoviesOnVideoAdapter.this.filterList.isEmpty()) {
                        MoviesOnVideoAdapter.this.moviesListl = MoviesOnVideoAdapter.this.filterList;
                    }
                    if (MoviesOnVideoAdapter.this.moviesListl != null && MoviesOnVideoAdapter.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    MoviesOnVideoAdapter.this.text_last_size = MoviesOnVideoAdapter.this.text_size;
                    MoviesOnVideoAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                MoviesOnVideoAdapter.this.filterList = new ArrayList();
                MoviesOnVideoAdapter.this.text_size = text.length();
                if (MoviesOnVideoAdapter.this.filterList != null) {
                    MoviesOnVideoAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    MoviesOnVideoAdapter.this.filterList.addAll(MoviesOnVideoAdapter.this.completeList);
                } else {
                    if ((MoviesOnVideoAdapter.this.moviesListl != null && MoviesOnVideoAdapter.this.moviesListl.size() == 0) || MoviesOnVideoAdapter.this.text_last_size > MoviesOnVideoAdapter.this.text_size) {
                        MoviesOnVideoAdapter.this.moviesListl = MoviesOnVideoAdapter.this.completeList;
                    }
                    if (MoviesOnVideoAdapter.this.moviesListl != null) {
                        for (LiveStreamsDBModel item : MoviesOnVideoAdapter.this.moviesListl) {
                            if (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())) {
                                MoviesOnVideoAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) MoviesOnVideoAdapter.this.context).runOnUiThread(new C18651());
            }
        }).start();
    }
}

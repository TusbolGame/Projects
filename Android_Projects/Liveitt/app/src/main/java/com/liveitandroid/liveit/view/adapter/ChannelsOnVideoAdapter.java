package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.view.nstplayer.NSTPlayerActivity;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.pojo.XMLTVProgrammePojo;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerActivity;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChannelsOnVideoAdapter extends RecyclerView.Adapter<ChannelsOnVideoAdapter.MyViewHolder> {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private int adapterPosition;
    private List<LiveStreamsDBModel> completeList;
    private Context context;
    private List<LiveStreamsDBModel> filterList = new ArrayList();
    private boolean firstTimeFlag = true;
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

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 1.09f;
            if (hasFocus) {
                if (!hasFocus) {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                Log.e("id is", "" + this.view.getTag());
                this.view.setBackgroundResource(R.drawable.shape_list_categories_focused);
            } else if (!hasFocus) {
                if (!hasFocus) {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
                this.view.setBackgroundResource(R.drawable.shape_list_categories);
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

    public ChannelsOnVideoAdapter(List<LiveStreamsDBModel> movieList, Context context) {
        this.filterList.addAll(movieList);
        this.completeList = movieList;
        this.moviesListl = movieList;
        this.context = context;
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_channels_on_video, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        String channelName = "";
        String channelId = "";
        String channelNumber = "";
        String epgChannelID = "";
        String StreamIcon = "";
        final LiveStreamsDBModel data = (LiveStreamsDBModel) this.moviesListl.get(listPosition);
        channelName = data.getName();
        channelId = data.getStreamId();
        channelNumber = data.getNum();
        epgChannelID = data.getEpgChannelId();
        StreamIcon = data.getStreamIcon();
        if (!(channelName == null || channelName.equals("") || channelName.isEmpty())) {
            holder.tvMovieCategoryName.setText(channelName);
        }
        if (holder.tvChannelId != null) {
            holder.tvChannelId.setText(channelNumber);
        }
        holder.tvTime.setText("");
        holder.progressBar.setVisibility(View.GONE);
        holder.tvCurrentLive.setText("");
        if (!(epgChannelID == null || epgChannelID.equals("") || this.liveStreamDBHandler == null)) {
            ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(epgChannelID);
            if (xmltvProgrammePojos != null) {
                for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                    String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                    String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                    String Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                    String Desc = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getDesc();
                    Long epgStartDateToTimestamp = Long.valueOf(com.liveitandroid.liveit.miscelleneious.common.Utils.epgTimeConverter(startDateTime));
                    Long epgStopDateToTimestamp = Long.valueOf(com.liveitandroid.liveit.miscelleneious.common.Utils.epgTimeConverter(stopDateTime));
                    if (com.liveitandroid.liveit.miscelleneious.common.Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context)) {
                        int epgPercentage = com.liveitandroid.liveit.miscelleneious.common.Utils.getPercentageLeft(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this.context);
                        if (epgPercentage != 0) {
                            epgPercentage = 100 - epgPercentage;
                            if (epgPercentage == 0 || Title == null || Title.equals("")) {
                                holder.tvTime.setVisibility(View.GONE);
                                holder.progressBar.setVisibility(View.GONE);
                                holder.tvCurrentLive.setVisibility(View.GONE);
                            } else {
                                if (AppConst.LIVE_FLAG == 0) {
                                    holder.tvTime.setVisibility(View.VISIBLE);
                                    loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
                                    this.programTimeFormat = new SimpleDateFormat(loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""));
                                    holder.tvTime.setText(this.programTimeFormat.format(epgStartDateToTimestamp) + " - " + this.programTimeFormat.format(epgStopDateToTimestamp));
                                }
                                holder.progressBar.setVisibility(View.VISIBLE);
                                holder.progressBar.setProgress(epgPercentage);
                                holder.tvCurrentLive.setVisibility(View.VISIBLE);
                                holder.tvCurrentLive.setText(Title);
                            }
                        }
                    }
                }
            }
            holder.ivChannelLogo.setImageDrawable(null);
            if (StreamIcon != null && !StreamIcon.equals("")) {
                Picasso.with(this.context).load(StreamIcon).placeholder((int) R.drawable.tv_icon).into(holder.ivChannelLogo);
            } else if (VERSION.SDK_INT >= 21) {
                holder.ivChannelLogo.setImageDrawable(this.context.getResources().getDrawable(R.drawable.tv_icon, null));
            } else {
                holder.ivChannelLogo.setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.tv_icon));
            }
        }
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((NSTPlayerActivity) ChannelsOnVideoAdapter.this.context).onClickCalled(data);
            }
        });
        holder.rlListOfCategories.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((NSTPlayerActivity) ChannelsOnVideoAdapter.this.context).onClickCalled(data);
                Log.e("data Value Categories", ">>>>>>>>>>>>>>" + data);
            }
        });
        holder.rlOuter.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.rlOuter));
        if (listPosition == 0 && this.firstTimeFlag) {
            holder.rlOuter.requestFocus();
            this.firstTimeFlag = false;
        }
    }

    public int getItemCount() {
        return this.moviesListl.size();
    }

    public void setVisibiltygone(ProgressBar pbPagingLoader) {
        if (pbPagingLoader != null) {
            pbPagingLoader.setVisibility(View.GONE);
        }
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C18351 implements Runnable {
                C18351() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        ChannelsOnVideoAdapter.this.moviesListl = ChannelsOnVideoAdapter.this.completeList;
                    } else if (!ChannelsOnVideoAdapter.this.filterList.isEmpty() || ChannelsOnVideoAdapter.this.filterList.isEmpty()) {
                        ChannelsOnVideoAdapter.this.moviesListl = ChannelsOnVideoAdapter.this.filterList;
                    }
                    if (ChannelsOnVideoAdapter.this.moviesListl != null && ChannelsOnVideoAdapter.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    ChannelsOnVideoAdapter.this.text_last_size = ChannelsOnVideoAdapter.this.text_size;
                    ChannelsOnVideoAdapter.this.notifyDataSetChanged();
                }
            }

            public void run() {
                ChannelsOnVideoAdapter.this.filterList = new ArrayList();
                ChannelsOnVideoAdapter.this.text_size = text.length();
                if (ChannelsOnVideoAdapter.this.filterList != null) {
                    ChannelsOnVideoAdapter.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    ChannelsOnVideoAdapter.this.filterList.addAll(ChannelsOnVideoAdapter.this.completeList);
                } else {
                    if ((ChannelsOnVideoAdapter.this.moviesListl != null && ChannelsOnVideoAdapter.this.moviesListl.size() == 0) || ChannelsOnVideoAdapter.this.text_last_size > ChannelsOnVideoAdapter.this.text_size) {
                        ChannelsOnVideoAdapter.this.moviesListl = ChannelsOnVideoAdapter.this.completeList;
                    }
                    if (ChannelsOnVideoAdapter.this.moviesListl != null) {
                        for (LiveStreamsDBModel item : ChannelsOnVideoAdapter.this.moviesListl) {
                            if (item.getName() != null && item.getName().toLowerCase().contains(text.toLowerCase())) {
                                ChannelsOnVideoAdapter.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) ChannelsOnVideoAdapter.this.context).runOnUiThread(new C18351());
            }
        }).start();
    }
}

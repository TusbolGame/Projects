package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.view.activity.LiveActivityNewFlow;
import com.liveitandroid.liveit.view.activity.TVArchiveActivityLayout;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.view.activity.LiveActivityNewFlow;
import com.liveitandroid.liveit.view.activity.TVArchiveActivityLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TVArchiveAdapterNewFlow extends Adapter<TVArchiveAdapterNewFlow.MyViewHolder> {
    private int adapterPosition;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private List<LiveStreamCategoryIdDBModel> completeList;
    private Context context;
    private DatabaseHandler dbHandeler;
    private List<LiveStreamCategoryIdDBModel> filterList = new ArrayList();
    private boolean firstTimeFlag = true;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetail;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    private List<LiveStreamCategoryIdDBModel> moviesListl;
    private int text_last_size;
    private int text_size;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.pb_paging_loader)
        ProgressBar pbPagingLoader;
        @BindView(R.id.rl_list_of_categories)
        RelativeLayout rlListOfCategories;
        @BindView(R.id.rl_outer)
        RelativeLayout rlOuter;
        @BindView(R.id.testing)
        RelativeLayout testing;
        @BindView(R.id.tv_movie_category_name)
        TextView tvMovieCategoryName;
        @BindView(R.id.tv_sub_cat_count)
        TextView tvXubCount;

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
//            target.tvXubCount = (TextView) Utils.findRequiredViewAsType(source, R.id.tv_sub_cat_count, "field 'tvXubCount'", TextView.class);
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
//            target.tvXubCount = null;
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

    public TVArchiveAdapterNewFlow(List<LiveStreamCategoryIdDBModel> movieList, Context context) {
        this.filterList.addAll(movieList);
        this.completeList = movieList;
        this.moviesListl = movieList;
        this.context = context;
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
        this.dbHandeler = new DatabaseHandler(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_live_new_flow_list_item, parent, false));
    }

    public void onBindViewHolder(final MyViewHolder holder, int listPosition) {
        String categoryName = "";
        String categoryId = "";
        LiveStreamCategoryIdDBModel data = (LiveStreamCategoryIdDBModel) this.moviesListl.get(listPosition);
        categoryName = data.getLiveStreamCategoryName();
        categoryId = data.getLiveStreamCategoryID();
        Bundle bundle = new Bundle();
        bundle.putString(AppConst.CATEGORY_ID, categoryId);
        bundle.putString(AppConst.CATEGORY_NAME, categoryName);
        if (!(categoryName == null || categoryName.equals("") || categoryName.isEmpty())) {
            holder.tvMovieCategoryName.setText(categoryName);
        }
        final String finalCategoryId = categoryId;
        final String finalCategoryName = categoryName;
        ArrayList<LiveStreamsDBModel> channelAvailable = this.liveStreamDBHandler.getAllLiveStreamsArchive(data.getLiveStreamCategoryID());
        this.liveListDetailUnlcked = new ArrayList();
        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || channelAvailable == null) {
            this.liveListDetailAvailable = channelAvailable;
        } else {
            this.listPassword = getPasswordSetCategories();
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(channelAvailable, this.listPassword);
            }
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        }
        if (this.liveListDetailAvailable != null) {
            holder.tvXubCount.setText(String.valueOf(this.liveListDetailAvailable.size()));
        } else {
            holder.tvXubCount.setText(AppConst.PASSWORD_UNSET);
        }
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new LiveActivityNewFlow().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                Intent intent = new Intent(TVArchiveAdapterNewFlow.this.context, TVArchiveActivityLayout.class);
                intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                TVArchiveAdapterNewFlow.this.context.startActivity(intent);
            }
        });
        holder.rlOuter.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.rlOuter));
        if (listPosition == 0 && this.firstTimeFlag) {
            holder.rlOuter.requestFocus();
            this.firstTimeFlag = false;
        }
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedCategories(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
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

            class C19181 implements Runnable {
                C19181() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        TVArchiveAdapterNewFlow.this.moviesListl = TVArchiveAdapterNewFlow.this.completeList;
                    } else if (!TVArchiveAdapterNewFlow.this.filterList.isEmpty() || TVArchiveAdapterNewFlow.this.filterList.isEmpty()) {
                        TVArchiveAdapterNewFlow.this.moviesListl = TVArchiveAdapterNewFlow.this.filterList;
                    }
                    if (TVArchiveAdapterNewFlow.this.moviesListl != null && TVArchiveAdapterNewFlow.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    TVArchiveAdapterNewFlow.this.text_last_size = TVArchiveAdapterNewFlow.this.text_size;
                    TVArchiveAdapterNewFlow.this.notifyDataSetChanged();
                }
            }

            public void run() {
                TVArchiveAdapterNewFlow.this.filterList = new ArrayList();
                TVArchiveAdapterNewFlow.this.text_size = text.length();
                if (TVArchiveAdapterNewFlow.this.filterList != null) {
                    TVArchiveAdapterNewFlow.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    TVArchiveAdapterNewFlow.this.filterList.addAll(TVArchiveAdapterNewFlow.this.completeList);
                } else {
                    if ((TVArchiveAdapterNewFlow.this.moviesListl != null && TVArchiveAdapterNewFlow.this.moviesListl.size() == 0) || TVArchiveAdapterNewFlow.this.text_last_size > TVArchiveAdapterNewFlow.this.text_size) {
                        TVArchiveAdapterNewFlow.this.moviesListl = TVArchiveAdapterNewFlow.this.completeList;
                    }
                    if (TVArchiveAdapterNewFlow.this.moviesListl != null) {
                        for (LiveStreamCategoryIdDBModel item : TVArchiveAdapterNewFlow.this.moviesListl) {
                            if (item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                                TVArchiveAdapterNewFlow.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) TVArchiveAdapterNewFlow.this.context).runOnUiThread(new C19181());
            }
        }).start();
    }
}

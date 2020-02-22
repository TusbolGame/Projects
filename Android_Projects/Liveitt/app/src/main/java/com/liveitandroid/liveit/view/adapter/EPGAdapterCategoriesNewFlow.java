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
import com.liveitandroid.liveit.view.activity.NewEPGActivity;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.activity.LiveActivityNewFlow;
import com.liveitandroid.liveit.view.activity.NewEPGActivity;
import java.util.ArrayList;
import java.util.List;

public class EPGAdapterCategoriesNewFlow extends Adapter<EPGAdapterCategoriesNewFlow.MyViewHolder> {
    private int adapterPosition;
    private List<LiveStreamCategoryIdDBModel> completeList;
    private Context context;
    private DatabaseHandler dbHandeler;
    private List<LiveStreamCategoryIdDBModel> filterList = new ArrayList();
    private boolean firstTimeFlag = true;
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

    public EPGAdapterCategoriesNewFlow(List<LiveStreamCategoryIdDBModel> movieList, Context context) {
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
        int countAll;
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
        int count = this.liveStreamDBHandler.getSubCatMovieCount(data.getLiveStreamCategoryID(), "live");
        if (count == 0 || count == -1) {
            holder.tvXubCount.setText("");
        } else {
            holder.tvXubCount.setText(String.valueOf(count));
        }
        if (listPosition == 0 && data.getLiveStreamCategoryID().equals(AppConst.PASSWORD_UNSET)) {
            countAll = this.liveStreamDBHandler.getStreamsCount("live");
            if (countAll == 0 || countAll == -1) {
                holder.tvXubCount.setText("");
            } else {
                holder.tvXubCount.setText(String.valueOf(countAll));
            }
        }
        if (listPosition == 1 && data.getLiveStreamCategoryID().equals("-1")) {
            countAll = this.dbHandeler.getFavouriteCount("live");
            if (countAll == 0 || countAll == -1) {
                holder.tvXubCount.setText(AppConst.PASSWORD_UNSET);
            } else {
                holder.tvXubCount.setText(String.valueOf(countAll));
            }
        }
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new LiveActivityNewFlow().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                Intent intent = new Intent(EPGAdapterCategoriesNewFlow.this.context, NewEPGActivity.class);
                intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                EPGAdapterCategoriesNewFlow.this.context.startActivity(intent);
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
            pbPagingLoader.setVisibility(8);
        }
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C18381 implements Runnable {
                C18381() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        EPGAdapterCategoriesNewFlow.this.moviesListl = EPGAdapterCategoriesNewFlow.this.completeList;
                    } else if (!EPGAdapterCategoriesNewFlow.this.filterList.isEmpty() || EPGAdapterCategoriesNewFlow.this.filterList.isEmpty()) {
                        EPGAdapterCategoriesNewFlow.this.moviesListl = EPGAdapterCategoriesNewFlow.this.filterList;
                    }
                    if (EPGAdapterCategoriesNewFlow.this.moviesListl != null && EPGAdapterCategoriesNewFlow.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    EPGAdapterCategoriesNewFlow.this.text_last_size = EPGAdapterCategoriesNewFlow.this.text_size;
                    EPGAdapterCategoriesNewFlow.this.notifyDataSetChanged();
                }
            }

            public void run() {
                EPGAdapterCategoriesNewFlow.this.filterList = new ArrayList();
                EPGAdapterCategoriesNewFlow.this.text_size = text.length();
                if (EPGAdapterCategoriesNewFlow.this.filterList != null) {
                    EPGAdapterCategoriesNewFlow.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    EPGAdapterCategoriesNewFlow.this.filterList.addAll(EPGAdapterCategoriesNewFlow.this.completeList);
                } else {
                    if ((EPGAdapterCategoriesNewFlow.this.moviesListl != null && EPGAdapterCategoriesNewFlow.this.moviesListl.size() == 0) || EPGAdapterCategoriesNewFlow.this.text_last_size > EPGAdapterCategoriesNewFlow.this.text_size) {
                        EPGAdapterCategoriesNewFlow.this.moviesListl = EPGAdapterCategoriesNewFlow.this.completeList;
                    }
                    if (EPGAdapterCategoriesNewFlow.this.moviesListl != null) {
                        for (LiveStreamCategoryIdDBModel item : EPGAdapterCategoriesNewFlow.this.moviesListl) {
                            if (item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                                EPGAdapterCategoriesNewFlow.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) EPGAdapterCategoriesNewFlow.this.context).runOnUiThread(new C18381());
            }
        }).start();
    }
}

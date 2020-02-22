package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.view.activity.VodActivityLayout;
import com.liveitandroid.liveit.view.activity.VodActivityNewFlowSecondSubCategories;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.activity.VodActivityLayout;
import com.liveitandroid.liveit.view.activity.VodActivityNewFlowSecondSubCategories;
import java.util.ArrayList;
import java.util.List;

public class VodSubCatAdpaterNew extends Adapter<VodSubCatAdpaterNew.MyViewHolder> {
    private List<LiveStreamCategoryIdDBModel> completeList = new ArrayList();
    private Context context;
    private List<LiveStreamCategoryIdDBModel> filterList = new ArrayList();
    private LiveStreamDBHandler liveStreamDBHandler;
    private List<LiveStreamCategoryIdDBModel> moviesListl = new ArrayList();
    private int text_last_size;
    private int text_size;

    public VodSubCatAdpaterNew() {

    }

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.iv_foraward_arrow)
        ImageView ivForawardArrow;
        @BindView(R.id.iv_tv_icon)
        ImageView ivTvIcon;
        @BindView(R.id.pb_paging_loader)
        ProgressBar pbPagingLoader;
        @BindView(R.id.rl_list_of_categories)
        RelativeLayout rlListOfCategories;
        @BindView(R.id.rl_outer)
        RelativeLayout rlOuter;
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

    public VodSubCatAdpaterNew(List<LiveStreamCategoryIdDBModel> movieList, Context context, LiveStreamDBHandler liveStreamDBHandler) {
        this.context = context;
        if(completeList != null)
        {
            completeList.clear();
        }

        if(filterList != null)
        {
            filterList.clear();
        }

        if(moviesListl != null)
        {
            moviesListl.clear();
        }

        if(completeList != null)
        {
            completeList.clear();
        }
        this.filterList.addAll(movieList);
        this.completeList.addAll(movieList);
        this.moviesListl.addAll(movieList);
        this.liveStreamDBHandler = liveStreamDBHandler;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vod_new_flow_list_item, parent, false));
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
        int count = this.liveStreamDBHandler.getSubCatMovieCount(data.getLiveStreamCategoryID(), "movie");
        if (count == 0 || count == -1) {
            holder.tvXubCount.setText("");
        } else {
            holder.tvXubCount.setText(String.valueOf(count));
        }
        final String finalCategoryId = categoryId;
        final String finalCategoryName = categoryName;
        holder.rlOuter.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.rlOuter));
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new VodActivityLayout().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                if (VodSubCatAdpaterNew.this.liveStreamDBHandler.getAllMovieCategoriesHavingParentIdNotZero(finalCategoryId).size() > 0) {
                    Intent intent = new Intent(VodSubCatAdpaterNew.this.context, VodActivityNewFlowSecondSubCategories.class);
                    intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                    intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                    VodSubCatAdpaterNew.this.context.startActivity(intent);
                    return;
                }
                Intent intent = new Intent(VodSubCatAdpaterNew.this.context, VodActivityLayout.class);
                intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                VodSubCatAdpaterNew.this.context.startActivity(intent);
            }
        });
        if (this.moviesListl.size() != 0) {
            holder.rlOuter.setVisibility(0);
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

            class C19381 implements Runnable {
                C19381() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        VodSubCatAdpaterNew.this.moviesListl = VodSubCatAdpaterNew.this.completeList;
                    } else if (!VodSubCatAdpaterNew.this.filterList.isEmpty() || VodSubCatAdpaterNew.this.filterList.isEmpty()) {
                        VodSubCatAdpaterNew.this.moviesListl = VodSubCatAdpaterNew.this.filterList;
                    }
                    if (VodSubCatAdpaterNew.this.moviesListl != null && VodSubCatAdpaterNew.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    VodSubCatAdpaterNew.this.text_last_size = VodSubCatAdpaterNew.this.text_size;
                    VodSubCatAdpaterNew.this.notifyDataSetChanged();
                }
            }

            public void run() {
                VodSubCatAdpaterNew.this.filterList = new ArrayList();
                VodSubCatAdpaterNew.this.text_size = text.length();
                if (VodSubCatAdpaterNew.this.filterList != null) {
                    VodSubCatAdpaterNew.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    VodSubCatAdpaterNew.this.filterList.addAll(VodSubCatAdpaterNew.this.completeList);
                } else {
                    if ((VodSubCatAdpaterNew.this.moviesListl != null && VodSubCatAdpaterNew.this.moviesListl.size() == 0) || VodSubCatAdpaterNew.this.text_last_size > VodSubCatAdpaterNew.this.text_size) {
                        VodSubCatAdpaterNew.this.moviesListl = VodSubCatAdpaterNew.this.completeList;
                    }
                    if (VodSubCatAdpaterNew.this.moviesListl != null) {
                        for (LiveStreamCategoryIdDBModel item : VodSubCatAdpaterNew.this.moviesListl) {
                            if (item.getLiveStreamCategoryName() != null && item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                                VodSubCatAdpaterNew.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) VodSubCatAdpaterNew.this.context).runOnUiThread(new C19381());
            }
        }).start();
    }
}

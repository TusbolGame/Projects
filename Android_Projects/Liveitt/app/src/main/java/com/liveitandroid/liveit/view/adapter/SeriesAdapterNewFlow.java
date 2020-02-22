package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.database.PasswordDBModel;
import com.liveitandroid.liveit.view.activity.LiveActivityNewFlow;
import com.liveitandroid.liveit.view.activity.SeriesActivitNewFlowSubCat;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.SeriesStreamsDatabaseHandler;
import com.liveitandroid.liveit.view.activity.SeriesActivitNewFlowSubCat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SeriesAdapterNewFlow extends Adapter<SeriesAdapterNewFlow.MyViewHolder> {
    private List<LiveStreamCategoryIdDBModel> completeList;
    private Context context;
    private DatabaseHandler dbHandeler;
    private List<LiveStreamCategoryIdDBModel> filterList;
    private LiveStreamDBHandler liveStreamDBHandler;
    private List<LiveStreamCategoryIdDBModel> moviesListl;
    private SeriesStreamsDatabaseHandler seriesStreamsDatabaseHandler;
    public int text_last_size;
    public int text_size;
    private SessionManager mSessionManager;

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
        @BindView(R.id.iv_block_parent)
        ImageView ImageAdults;

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

    public SeriesAdapterNewFlow(List<LiveStreamCategoryIdDBModel> movieList, Context context) {
        this.filterList = new ArrayList();
        this.filterList.addAll(movieList);
        this.completeList = movieList;
        this.moviesListl = movieList;
        this.context = context;
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
        this.dbHandeler = new DatabaseHandler(context);
        this.seriesStreamsDatabaseHandler = new SeriesStreamsDatabaseHandler(context);
        mSessionManager = new SessionManager(this.context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vod_new_flow_list_item, parent, false));
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
        final String finalAdults = data.getAdults();

        if(finalAdults.equals("0"))
        {
            holder.ImageAdults.setVisibility(View.GONE);
        }else{
            holder.ImageAdults.setVisibility(View.VISIBLE);
        }
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                /*if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }*/
                if(finalAdults.equals("1"))
                {
                    String username = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0).getString("username", "");
                    ArrayList<PasswordDBModel> list = liveStreamDBHandler.getAllPassword();
                    String usernameDB = "";
                    String userPasswordDB = "";
                    if (list != null) {
                        Iterator it = list.iterator();
                        while (it.hasNext()) {
                            PasswordDBModel listItem = (PasswordDBModel) it.next();
                            if (listItem.getUserDetail().equals(username) && !listItem.getUserPassword().isEmpty()) {
                                usernameDB = listItem.getUserDetail();
                                userPasswordDB = listItem.getUserPassword();
                            }
                        }
                    }
                    showSearchFilmTextDialog(finalCategoryName,finalCategoryId,userPasswordDB);
                }else{
                    Intent intent = new Intent(SeriesAdapterNewFlow.this.context, SeriesActivitNewFlowSubCat.class);
                    intent.putExtra(AppConst.CATEGORY_ID, finalCategoryId);
                    intent.putExtra(AppConst.CATEGORY_NAME, finalCategoryName);
                    SeriesAdapterNewFlow.this.context.startActivity(intent);
                }
            }
        });
        holder.rlOuter.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.rlOuter));
        int count = this.seriesStreamsDatabaseHandler.getSeriesCount(data.getLiveStreamCategoryID());
        if (count == 0 || count == -1) {
            holder.tvXubCount.setText("");
        } else {
            holder.tvXubCount.setText(String.valueOf(count));
        }
        if (listPosition == 0 && data.getLiveStreamCategoryID().equals(AppConst.PASSWORD_UNSET)) {
            countAll = this.seriesStreamsDatabaseHandler.getAllSeriesStreamCount();
            if (countAll == 0 || countAll == -1) {
                holder.tvXubCount.setText("");
            } else {
                holder.tvXubCount.setText(String.valueOf(countAll));
            }
        }
        if (listPosition == 1 && data.getLiveStreamCategoryID().equals("-1")) {
            countAll = this.dbHandeler.getFavouriteCount(AppConst.SERIES);
            if (countAll == 0 || countAll == -1) {
                holder.tvXubCount.setText(AppConst.PASSWORD_UNSET);
            } else {
                holder.tvXubCount.setText(String.valueOf(countAll));
            }
        }
    }


    public void showSearchFilmTextDialog(final String finalcateg, final String finalcategid, final String passwo) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.dialog_adult_password, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText serachWord_Txt = promptsView.findViewById(R.id.et_adult_password);
        final TextView txt_title = promptsView.findViewById(R.id.textView1);
        txt_title.setText("Insira a senha Adultos:");

        // set dialog message
        alertDialogBuilder
                .setCancelable(true)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String search_txt = (serachWord_Txt.getText()).toString();
                                if (!search_txt.equals("")) {
                                    if (passwo.equals(search_txt)) {
                                        mSessionManager.setCanalSele(finalcateg);
                                        Intent intent = new Intent(SeriesAdapterNewFlow.this.context, SeriesActivitNewFlowSubCat.class);
                                        intent.putExtra(AppConst.CATEGORY_ID, finalcategid);
                                        intent.putExtra(AppConst.CATEGORY_NAME, finalcateg);
                                        SeriesAdapterNewFlow.this.context.startActivity(intent);
                                    } else {
                                        String message = "A senha que inseriu não corresponde á sua!";
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle(mSessionManager.getNameAPP() + " - Erro");
                                        builder.setMessage(message);
                                        builder.setPositiveButton("Cancelar", null);
                                        builder.setNegativeButton("Tentar de novo", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                showSearchFilmTextDialog(finalcateg,finalcategid,passwo);
                                            }
                                        });
                                        builder.create().show();
                                    }

                                } else {
//                                    Log.d(user_text,"string is empty");
                                    String message = "Inserir a sua senha adultos!";
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle(mSessionManager.getNameAPP() + " - Erro");
                                    builder.setMessage(message);
                                    builder.setPositiveButton("Cancelar", null);
                                    builder.setNegativeButton("Tentar de novo", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            showSearchFilmTextDialog(finalcateg,finalcategid,passwo);
                                        }
                                    });
                                    builder.create().show();
                                }
                            }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }

                        }

                );

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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

            class C18901 implements Runnable {
                C18901() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        SeriesAdapterNewFlow.this.moviesListl = SeriesAdapterNewFlow.this.completeList;
                    } else if (!SeriesAdapterNewFlow.this.filterList.isEmpty() || SeriesAdapterNewFlow.this.filterList.isEmpty()) {
                        SeriesAdapterNewFlow.this.moviesListl = SeriesAdapterNewFlow.this.filterList;
                    }
                    if (SeriesAdapterNewFlow.this.moviesListl != null && SeriesAdapterNewFlow.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                        tvNoRecordFound.setText(SeriesAdapterNewFlow.this.context.getResources().getString(R.string.no_record_found));
                    }
                    SeriesAdapterNewFlow.this.text_last_size = SeriesAdapterNewFlow.this.text_size;
                    SeriesAdapterNewFlow.this.notifyDataSetChanged();
                }
            }

            public void run() {
                SeriesAdapterNewFlow.this.filterList = new ArrayList();
                SeriesAdapterNewFlow.this.text_size = text.length();
                if (SeriesAdapterNewFlow.this.filterList != null) {
                    SeriesAdapterNewFlow.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    SeriesAdapterNewFlow.this.filterList.addAll(SeriesAdapterNewFlow.this.completeList);
                } else {
                    if ((SeriesAdapterNewFlow.this.moviesListl != null && SeriesAdapterNewFlow.this.moviesListl.size() == 0) || SeriesAdapterNewFlow.this.text_last_size > SeriesAdapterNewFlow.this.text_size) {
                        SeriesAdapterNewFlow.this.moviesListl = SeriesAdapterNewFlow.this.completeList;
                    }
                    if (SeriesAdapterNewFlow.this.moviesListl != null) {
                        for (LiveStreamCategoryIdDBModel item : SeriesAdapterNewFlow.this.moviesListl) {
                            if (item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                                SeriesAdapterNewFlow.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) SeriesAdapterNewFlow.this.context).runOnUiThread(new C18901());
            }
        }).start();
    }
}

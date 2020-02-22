package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.view.activity.LiveActivityNewFlow;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LiveAdapterNewFlow extends Adapter<LiveAdapterNewFlow.MyViewHolder> {
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
    private SessionManager mSessionManager;

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

    public LiveAdapterNewFlow(List<LiveStreamCategoryIdDBModel> movieList, Context context) {
        this.filterList.addAll(movieList);
        this.completeList = movieList;
        this.moviesListl = movieList;
        this.context = context;
        this.liveStreamDBHandler = new LiveStreamDBHandler(context);
        this.dbHandeler = new DatabaseHandler(context);
        mSessionManager = new SessionManager(this.context);
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
        String finalCategoryName = categoryName;
        final String finalAdults = data.getAdults();

        if(finalAdults.equals("0"))
        {
            holder.ImageAdults.setVisibility(View.GONE);
        }else{
            holder.ImageAdults.setVisibility(View.VISIBLE);
        }
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
        /*holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new LiveActivityNewFlow().progressBar(holder.pbPagingLoader);
                if (!(holder == null || holder.pbPagingLoader == null)) {
                    holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                    holder.pbPagingLoader.setVisibility(0);
                }
                LiveAdapterNewFlow.this.startSkyActivity(finalCategoryId);
            }
        });*/
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
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
                    showSearchFilmTextDialog(finalCategoryId,userPasswordDB);
                }else{
                    new LiveActivityNewFlow().progressBar(holder.pbPagingLoader);
                    if (!(holder == null || holder.pbPagingLoader == null)) {
                        holder.pbPagingLoader.getIndeterminateDrawable().setColorFilter(ViewCompat.MEASURED_STATE_MASK, Mode.SRC_IN);
                        holder.pbPagingLoader.setVisibility(0);
                    }
                    LiveAdapterNewFlow.this.startSkyActivity(finalCategoryId);
                }
            }
        });
        holder.rlOuter.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.rlOuter));
        if (listPosition == 0 && this.firstTimeFlag) {
            holder.rlOuter.requestFocus();
            this.firstTimeFlag = false;
        }
    }

    public void showSearchFilmTextDialog(final String finalcateg, final String passwo) {
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
                                        LiveAdapterNewFlow.this.startSkyActivity(finalcateg);
                                    } else {
                                        String message = "A senha que inseriu não corresponde á sua!";
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle(mSessionManager.getNameAPP() + " - Erro");
                                        builder.setMessage(message);
                                        builder.setPositiveButton("Cancelar", null);
                                        builder.setNegativeButton("Tentar de novo", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                showSearchFilmTextDialog(finalcateg,passwo);
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
                                            showSearchFilmTextDialog(finalcateg,passwo);
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

    public void startSkyActivity(String catID) {
        if (this.liveStreamDBHandler != null && this.context != null) {
            this.loginPreferencesSharedPref = this.context.getSharedPreferences(AppConst.LOGIN_PREF_SELECTED_PLAYER, 0);
            String selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            ArrayList<LiveStreamsDBModel> firstChannelDetails = this.liveStreamDBHandler.getFirstchannelNum(catID);
            if (firstChannelDetails == null || firstChannelDetails.size() <= 0) {
                Utils.playWithSkyPlayer(this.context, selectedPlayer, -1, "live", AppConst.PASSWORD_UNSET, "", "", "", catID);
                return;
            }
            String streamId = ((LiveStreamsDBModel) firstChannelDetails.get(0)).getStreamId();
            String streamType = ((LiveStreamsDBModel) firstChannelDetails.get(0)).getStreamType();
            String num = ((LiveStreamsDBModel) firstChannelDetails.get(0)).getNum();
            String name = ((LiveStreamsDBModel) firstChannelDetails.get(0)).getName();
            String epgChannelId = ((LiveStreamsDBModel) firstChannelDetails.get(0)).getEpgChannelId();
            String StreamIcon = ((LiveStreamsDBModel) firstChannelDetails.get(0)).getStreamIcon();
            //String categoryId = ((LiveStreamsDBModel) firstChannelDetails.get(0)).getCategoryId();

            Utils.playWithPlayer(this.context, selectedPlayer, Integer.parseInt(streamId), streamType, num, name, epgChannelId, StreamIcon, catID);
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

            class C18461 implements Runnable {
                C18461() {
                }

                public void run() {
                    if (TextUtils.isEmpty(text)) {
                        LiveAdapterNewFlow.this.moviesListl = LiveAdapterNewFlow.this.completeList;
                    } else if (!LiveAdapterNewFlow.this.filterList.isEmpty() || LiveAdapterNewFlow.this.filterList.isEmpty()) {
                        LiveAdapterNewFlow.this.moviesListl = LiveAdapterNewFlow.this.filterList;
                    }
                    if (LiveAdapterNewFlow.this.moviesListl != null && LiveAdapterNewFlow.this.moviesListl.size() == 0) {
                        tvNoRecordFound.setVisibility(0);
                    }
                    LiveAdapterNewFlow.this.text_last_size = LiveAdapterNewFlow.this.text_size;
                    LiveAdapterNewFlow.this.notifyDataSetChanged();
                }
            }

            public void run() {
                LiveAdapterNewFlow.this.filterList = new ArrayList();
                LiveAdapterNewFlow.this.text_size = text.length();
                if (LiveAdapterNewFlow.this.filterList != null) {
                    LiveAdapterNewFlow.this.filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    LiveAdapterNewFlow.this.filterList.addAll(LiveAdapterNewFlow.this.completeList);
                } else {
                    if ((LiveAdapterNewFlow.this.moviesListl != null && LiveAdapterNewFlow.this.moviesListl.size() == 0) || LiveAdapterNewFlow.this.text_last_size > LiveAdapterNewFlow.this.text_size) {
                        LiveAdapterNewFlow.this.moviesListl = LiveAdapterNewFlow.this.completeList;
                    }
                    if (LiveAdapterNewFlow.this.moviesListl != null) {
                        for (LiveStreamCategoryIdDBModel item : LiveAdapterNewFlow.this.moviesListl) {
                            if (item.getLiveStreamCategoryName().toLowerCase().contains(text.toLowerCase())) {
                                LiveAdapterNewFlow.this.filterList.add(item);
                            }
                        }
                    }
                }
                ((Activity) LiveAdapterNewFlow.this.context).runOnUiThread(new C18461());
            }
        }).start();
    }
}

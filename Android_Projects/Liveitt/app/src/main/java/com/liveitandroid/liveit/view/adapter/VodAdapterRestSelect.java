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
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.activity.JornaisActivitySelect;
import com.liveitandroid.liveit.view.activity.ProgramasActivityInfo;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodAdapterRestSelect extends Adapter<VodAdapterRestSelect.MyViewHolder> {
    private List<ListMoviesSetterGetter> completeList;
    private Context context;
    private Editor editor;
    private List<ListMoviesSetterGetter> filterList = new ArrayList();
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences pref;
    private SharedPreferences settingsPrefs;
    public int text_last_size;
    public int text_size;
    public SessionManager mSessionManager;

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.rl_movie)
        RelativeLayout Movie;
        @BindView(R.id.iv_movie_image)
        ImageView MovieImage;
        @BindView(R.id.iv_favourite)
        ImageView iv_favourite;
        @BindView(R.id.tv_movie_name)
        TextView MovieName;
        @BindView(R.id.card_view)
        CardView cardView;
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

    public void setVisibiltygone(ProgressBar pbPagingLoader) {
        if (pbPagingLoader != null) {
            pbPagingLoader.setVisibility(View.GONE);
        }
    }

    public VodAdapterRestSelect(List<ListMoviesSetterGetter> vodCategories, Context context) {
        this.context = context;
        this.completeList = vodCategories;
        this.filterList.addAll(vodCategories);
        mSessionManager = new SessionManager(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vod_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    public String selectedPlayer;
    public void onBindViewHolder(MyViewHolder holder, int listPosition) {
        if (this.context != null) {
            final ListMoviesSetterGetter reideus;
            Context context = this.context;
            String str22 = AppConst.LOGIN_PREF_SELECTED_PLAYER;
            this.loginPreferencesSharedPref = context.getSharedPreferences(str22, 0);
            selectedPlayer = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SELECTED_PLAYER, "");
            holder.iv_favourite.setVisibility(View.GONE);
            holder.MovieName.setText(((ListMoviesSetterGetter) this.completeList.get(listPosition)).getPraias_name());
            holder.movieNameTV.setText(((ListMoviesSetterGetter) this.completeList.get(listPosition)).getPraias_name());
            String StreamIcon = ((ListMoviesSetterGetter) this.completeList.get(listPosition)).getPraias_imagem();
            reideus = ((ListMoviesSetterGetter) this.completeList.get(listPosition));
            if (StreamIcon != null) {
                if (!StreamIcon.equals("")) {
                    Picasso.with(this.context).load(StreamIcon).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
                    holder.cardView.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            playMovie(reideus);
                        }
                    });
                    holder.MovieImage.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            playMovie(reideus);
                        }
                    });
                    holder.Movie.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            playMovie(reideus);
                        }
                    });
                    holder.Movie.setOnFocusChangeListener(
                            new OnFocusChangeAccountListener(holder.Movie)
                    );
                    holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View v) {
                            playMovie(reideus);
                            return true;
                        }
                    });
                    holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            playMovie(reideus);
                            return true;
                        }
                    });
                    holder.cardView.setOnLongClickListener(new OnLongClickListener() {
                        public boolean onLongClick(View view) {
                            playMovie(reideus);
                            return true;
                        }
                    });
                    holder.llMenu.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            playMovie(reideus);
                        }
                    });
                }
            }
        }
    }

    public void playMovie(ListMoviesSetterGetter chegg)
    {
        if(chegg.getPraias_group().equals("programas"))
        {
            Intent i = new Intent(context, ProgramasActivityInfo.class);
            i.putExtra("selnome", chegg.getPraias_name());
            i.putExtra("selurl", chegg.getPraias_url());
            i.putExtra("selwebsite", chegg.getPraias_disp());
            i.putExtra("selcanal", chegg.getPraias_imageGroup());
            i.putExtra("selimagem", chegg.getPraias_imagem());
            i.putExtra("seltipo", chegg.getPraias_desc());
            context.startActivity(i);
        }
        else if(chegg.getPraias_group().equals("jornaisSelect"))
        {
            ((JornaisActivitySelect) context).SetJornal(chegg.getPraias_imagem());
        }
        else if(chegg.getPraias_group().equals("radios"))
        {
            //Utils.playWhenPlayerRadios(chegg, context);
            Utils.PlayerSetNovo(chegg, context, "Radios");
        }else if(chegg.getPraias_group().equals("praias"))
        {
            Utils.PlayerSetNovo(chegg, context, "Praias");
        }else if(chegg.getPraias_group().equals("hits"))
        {
            String film_trailer = chegg.getPraias_url();
            String nomeData = chegg.getPraias_name();
            if (chegg.getPraias_disp().equals("Youtube")) {
                String[] split_01 = film_trailer.split("v=");
                if (split_01.length > 1) {
                    film_trailer = split_01[1];
                } else {
                    split_01 = film_trailer.split(".be/");
                    if (split_01.length > 1) {
                        film_trailer = split_01[1];
                    }
                }

                boolean installed = Utils.appInstalledOrNot("com.google.android.youtube", context);

                if (installed) {
                    context.startActivity(YouTubeStandalonePlayer.createVideoIntent((Activity) context, mSessionManager.getkodiGrant(), film_trailer, 0, true, true));
                } else {
                    Utils.installPlayerfromPlaystore(context, "Youtube", "com.google.android.youtube");
                }
            } else {
                Utils.PlayerSetNovo(chegg, context, "Programas");
            }
        }
    }

    public int getItemCount() {
        return completeList.size();
    }

    public void filter(final String text, final TextView tvNoRecordFound) {
        new Thread(new Runnable() {

            class C19321 implements Runnable {
                C19321() {
                }

                public void run() {
                    if (completeList.size() == 0) {
                        tvNoRecordFound.setVisibility(View.VISIBLE);
                    }
                    VodAdapterRestSelect.this.text_last_size = VodAdapterRestSelect.this.text_size;
                    VodAdapterRestSelect.this.notifyDataSetChanged();
                }
            }

            public void run() {
                filterList = new ArrayList();
                text_size = text.length();
                if (filterList != null) {
                    filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(VodAdapterRestSelect.this.completeList);
                } else {
                    for (ListMoviesSetterGetter item : completeList) {
                        if (item.getPraias_name().toLowerCase().contains(text.toLowerCase())) {
                            filterList.add(item);
                        }
                    }
                }
                ((Activity) context).runOnUiThread(new C19321());
            }
        }).start();
    }
}

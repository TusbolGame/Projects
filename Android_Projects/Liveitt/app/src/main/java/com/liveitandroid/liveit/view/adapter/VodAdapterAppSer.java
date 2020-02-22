package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivitySer;
import com.liveitandroid.liveit.view.activity.VodMenuFilmesAPP4SerSelectActivity;
import com.liveitandroid.liveit.Movie;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.JSONPar;
import com.liveitandroid.liveit.helper.JSONParser;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivityMov;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivitySer;
import com.liveitandroid.liveit.view.activity.VodMenuFilmesAPP4MovSelectActivity;
import com.liveitandroid.liveit.view.activity.VodMenuFilmesAPP4SerSelectActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodAdapterAppSer extends Adapter<VodAdapterAppSer.MyViewHolder> {
    private List<ListMoviesSetterGetter> completeList = new ArrayList();
    private Context context;
    private List<ListMoviesSetterGetter> filterList = new ArrayList();
    private SharedPreferences loginPreferencesSharedPref;
    public int text_last_size;
    public int text_size;

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

    public SessionManager mSessionManager;
    public VodAdapterAppSer(List<ListMoviesSetterGetter> vodCategories, Context context) {
        this.context = context;
        if(completeList != null)
        {
            completeList.clear();
        }

        if(filterList != null)
        {
            filterList.clear();
        }
        this.completeList.addAll(vodCategories);
        this.filterList.addAll(vodCategories);
        notifyDataSetChanged();
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
            final MyViewHolder myViewHolder;
            final MyViewHolder myViewHolder2;
            final MyViewHolder myViewHolder3;
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

            if (!(context == null || StreamIcon == null || StreamIcon.isEmpty())) {
                Picasso.with(context).load(StreamIcon).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
            }else{
                Picasso.with(context).load(R.drawable.tranparentdark).placeholder((int) R.drawable.tranparentdark).into(holder.MovieImage);
            }

            holder.cardView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    startViewDeatilsActivity(reideus);
                }
            });
            holder.MovieImage.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    startViewDeatilsActivity(reideus);
                }
            });
            holder.Movie.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    startViewDeatilsActivity(reideus);
                }
            });
            holder.Movie.setOnFocusChangeListener(
                    new OnFocusChangeAccountListener(holder.Movie)
            );
            myViewHolder = holder;
            holder.Movie.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View v) {
                    popmenu(myViewHolder, reideus);
                    return true;
                }
            });
            myViewHolder2 = holder;
            holder.MovieImage.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    popmenu(myViewHolder2, reideus);
                    return true;
                }
            });
            holder.cardView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    popmenu(myViewHolder2, reideus);
                    return true;
                }
            });
            myViewHolder3 = holder;
            holder.llMenu.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    popmenu(myViewHolder3, reideus);
                }
            });
        }
    }

    private void startViewDeatilsActivity(ListMoviesSetterGetter esco) {
        if (this.context != null) {
            if (esco.getPraias_id() == "-1000")
            {
                String pageType = esco.getPraias_imageGroup();
                ((VodMenuFilmesAPP4SerSelectActivity) context).goPageNaviFunction(esco, pageType);
            }else
            {
                Intent viewDetailsActivityIntent = new Intent(this.context, ViewDetailsActivitySer.class);
                viewDetailsActivityIntent.putExtra("film_name", esco.getPraias_name());
                viewDetailsActivityIntent.putExtra("film_logo", esco.getPraias_imagem());
                viewDetailsActivityIntent.putExtra("film_desc", esco.getPraias_desc());
                viewDetailsActivityIntent.putExtra("film_ano", esco.getAno());
                viewDetailsActivityIntent.putExtra("film_player", selectedPlayer);
                viewDetailsActivityIntent.putExtra("film_categoria", esco.getCategoria());
                viewDetailsActivityIntent.putExtra("film_actores", esco.getAtores());
                viewDetailsActivityIntent.putExtra("film_rating", esco.getRating());
                viewDetailsActivityIntent.putExtra("film_imbd", esco.getImdb());
                viewDetailsActivityIntent.putExtra("film_director", esco.getDirector());
                viewDetailsActivityIntent.putExtra("film_trailer", esco.getTrailer());
                viewDetailsActivityIntent.putExtra("film_name_en", esco.getPraias_name_en());
                viewDetailsActivityIntent.putExtra("ficheiroMov", esco);
                viewDetailsActivityIntent.putExtra("film_temporadas", esco.getTemporadas());
                viewDetailsActivityIntent.putExtra("film_estado", esco.getEstado());
                viewDetailsActivityIntent.putExtra("film_fim", esco.getFim());
                viewDetailsActivityIntent.putExtra("film_id", esco.getPraias_id());
                viewDetailsActivityIntent.putExtra("film_favorito_id", esco.getCategoria2());
                viewDetailsActivityIntent.putExtra("favoritos", esco.getCategoria3());
                this.context.startActivity(viewDetailsActivityIntent);
            }


            return;
        }
    }

    private void popmenu(MyViewHolder holder, ListMoviesSetterGetter esco) {
        final ListMoviesSetterGetter escoo = esco;
        if (this.context != null) {
            if (esco.getPraias_id() == "-1000")
            {
                String pageType = esco.getPraias_imageGroup();
                ((VodMenuFilmesAPP4SerSelectActivity) context).goPageNaviFunction(esco, pageType);
            }else
            {
                PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
                popup.inflate(R.menu.menu_card_vod_ser);
                final MyViewHolder myViewHolder = holder;
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_view_details:
                                startViewDeatilsActivity(escoo);
                                break;
                            case R.id.nav_play:
                                playMovie(escoo);
                                break;
                        }
                        return false;
                    }

                    private void startViewDeatilsActivity(ListMoviesSetterGetter esco) {
                        if (context != null) {
                            Intent viewDetailsActivityIntent = new Intent(context, ViewDetailsActivitySer.class);
                            viewDetailsActivityIntent.putExtra("film_name", esco.getPraias_name());
                            viewDetailsActivityIntent.putExtra("film_logo", esco.getPraias_imagem());
                            viewDetailsActivityIntent.putExtra("film_desc", esco.getPraias_desc());
                            viewDetailsActivityIntent.putExtra("film_ano", esco.getAno());
                            viewDetailsActivityIntent.putExtra("film_player", selectedPlayer);
                            viewDetailsActivityIntent.putExtra("film_categoria", esco.getCategoria());
                            viewDetailsActivityIntent.putExtra("film_actores", esco.getAtores());
                            viewDetailsActivityIntent.putExtra("film_rating", esco.getRating());
                            viewDetailsActivityIntent.putExtra("film_imbd", esco.getImdb());
                            viewDetailsActivityIntent.putExtra("film_director", esco.getDirector());
                            viewDetailsActivityIntent.putExtra("film_trailer", esco.getTrailer());
                            viewDetailsActivityIntent.putExtra("film_name_en", esco.getPraias_name_en());
                            viewDetailsActivityIntent.putExtra("film_temporadas", esco.getTemporadas());
                            viewDetailsActivityIntent.putExtra("film_estado", esco.getEstado());
                            viewDetailsActivityIntent.putExtra("film_fim", esco.getFim());
                            viewDetailsActivityIntent.putExtra("ficheiroMov", escoo);

                            context.startActivity(viewDetailsActivityIntent);
                        }
                    }

                    private void playMovie(ListMoviesSetterGetter esco) {
                        ((VodMenuFilmesAPP4SerSelectActivity) context).goTemporadasPage(esco);
                    }
                });
                popup.show();
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
                    VodAdapterAppSer.this.text_last_size = VodAdapterAppSer.this.text_size;
                    VodAdapterAppSer.this.notifyDataSetChanged();
                }
            }

            public void run() {
                filterList = new ArrayList();
                text_size = text.length();
                if (filterList != null) {
                    filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(VodAdapterAppSer.this.completeList);
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

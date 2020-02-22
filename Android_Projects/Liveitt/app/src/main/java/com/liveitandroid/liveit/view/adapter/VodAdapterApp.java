package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivityMov;
import com.liveitandroid.liveit.view.activity.VodMenuFilmesAPP4MovSelectActivity;
import com.liveitandroid.liveit.view.app_shell.AppController;
import com.bumptech.glide.Glide;
import com.liveitandroid.liveit.Movie;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.Global;
import com.liveitandroid.liveit.helper.JSONPar;
import com.liveitandroid.liveit.helper.JSONParser;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivity;
import com.liveitandroid.liveit.view.activity.ViewDetailsActivityMov;
import com.liveitandroid.liveit.view.activity.VodMenuFilmesAPP4MovSelectActivity;
import com.liveitandroid.liveit.view.app_shell.AppController;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodAdapterApp extends Adapter<VodAdapterApp.MyViewHolder> {
    private List<ListMoviesSetterGetter> completeList = new ArrayList();
    private Context context;
    private Editor editor;
    private List<ListMoviesSetterGetter> filterList = new ArrayList();
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences pref;
    private SharedPreferences settingsPrefs;
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

    public VodAdapterApp(List<ListMoviesSetterGetter> vodCategories, Context context) {
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
                if (context instanceof VodMenuFilmesAPP4MovSelectActivity) {
                    ((VodMenuFilmesAPP4MovSelectActivity) context).goPageNaviFunction(esco, pageType);
                }
            }else
            {
                Intent viewDetailsActivityIntent = new Intent(this.context, ViewDetailsActivityMov.class);
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
                viewDetailsActivityIntent.putExtra("film_id", esco.getPraias_id());
                viewDetailsActivityIntent.putExtra("film_favorito_id", esco.getCategoria2());
                viewDetailsActivityIntent.putExtra("favoritos", esco.getCategoria3());
                viewDetailsActivityIntent.putExtra("ficheiroMov", esco);
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
                if (context instanceof VodMenuFilmesAPP4MovSelectActivity) {
                    ((VodMenuFilmesAPP4MovSelectActivity) context).goPageNaviFunction(esco, pageType);
                }
            }else
            {
                PopupMenu popup = new PopupMenu(this.context, holder.tvStreamOptions);
                popup.inflate(R.menu.menu_card_vod);
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
                            Intent viewDetailsActivityIntent = new Intent(context, ViewDetailsActivityMov.class);
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
                            viewDetailsActivityIntent.putExtra("ficheiroMov", escoo);

                            context.startActivity(viewDetailsActivityIntent);
                        }
                    }

                    private void playMovie(ListMoviesSetterGetter esco) {
                        FazCoisa(esco);
                    }
                });
                popup.show();
            }
        }
    }


    String urlData1 = "";
    String urlData2 = "";
    String urlData3 = "";
    String urlData4 = "";
    String urlData5 = "";
    String urlData6 = "";
    private ArrayList<ListMoviesSetterGetter> list_items;
    private SessionManager mSessionManager;
    private ListMoviesSetterGetter mFilterChannelSetterMEnu;
    private CharSequence[] popmenuList;
    String programas_url = "";
    String programas_player = "";
    String realPlaySubtitle = "";
    String realPlayURL = "";
    String realPlayIMDB = "";
    String api_type = "";
    String realPlayName = "";
    JSONPar jsonPar;
    private String stream_id, playing_url;
    String LegendaPai = "";
    JSONObject jsonURLList;
    AlertDialog alert;
    JSONObject parseURLData, objfina;
    String selOpenServer;
    AlertDialog alert2;
    private CharSequence[] popResList;
    String[] linklist;
    private String Captch;
    private String ticket;

    public void FazCoisa(ListMoviesSetterGetter reidd) {
        urlData1 = "";
        urlData2 = "";
        urlData3 = "";
        urlData4 = "";
        urlData5 = "";
        final ListMoviesSetterGetter mFilterChannelSetterGetter = reidd;
        if(!mFilterChannelSetterGetter.equals(null))
        {
            if(!mFilterChannelSetterGetter.getPraias_url().equals(null) && !mFilterChannelSetterGetter.getPraias_url().equals(""))
            {
                urlData1 = mFilterChannelSetterGetter.getPraias_url();
            }
            if(!mFilterChannelSetterGetter.getPraias_url2().equals(null) && !mFilterChannelSetterGetter.getPraias_url2().equals(""))
            {
                urlData2 = mFilterChannelSetterGetter.getPraias_url2();
            }
            if(!mFilterChannelSetterGetter.getPraias_url3().equals(null) && !mFilterChannelSetterGetter.getPraias_url3().equals(""))
            {
                urlData3 = mFilterChannelSetterGetter.getPraias_url3();
            }
            if(!mFilterChannelSetterGetter.getPraias_url4().equals(null) && !mFilterChannelSetterGetter.getPraias_url4().equals(""))
            {
                urlData4 = mFilterChannelSetterGetter.getPraias_url4();
            }
            if(!mFilterChannelSetterGetter.getPraias_url5().equals(null) && !mFilterChannelSetterGetter.getPraias_url5().equals(""))
            {
                urlData5 = mFilterChannelSetterGetter.getPraias_url5();
            }

            list_items = new ArrayList<ListMoviesSetterGetter>();
            int popInd = 0;

            if(!urlData1.equals(""))
            {
                ListMoviesSetterGetter mListPraiasSetterGetter = new ListMoviesSetterGetter();
                mListPraiasSetterGetter.setPraias_url(urlData1);

                if (urlData1.contains("openload"))
                    mListPraiasSetterGetter.setPraias_name("Open Load - "+"3ª Escolha");
                else if (urlData1.contains("oload"))
                    mListPraiasSetterGetter.setPraias_name("OLoad - "+"3ª Escolha");
                else if (urlData1.contains("cloud.mail.ru") || urlData1.contains("google") || urlData1.contains(".mp4") || urlData1.contains(".avi"))
                    mListPraiasSetterGetter.setPraias_name(mSessionManager.getNameAPP() + " - 1ª Escolha");
                else if(urlData1.contains("raptu") || urlData1.contains("rapidvideo.com"))
                    mListPraiasSetterGetter.setPraias_name("RapidVideo" + " - 8ª Escolha");
                else if(urlData1.contains("uptostream.com"))
                    mListPraiasSetterGetter.setPraias_name("Uptostream" + " - 7ª Escolha");
                else if(urlData1.contains("vidzi"))
                    mListPraiasSetterGetter.setPraias_name("Vidzi" + " - 6ª Escolha");
                else if (urlData1.contains("streamango"))
                    mListPraiasSetterGetter.setPraias_name("Streamango" + " - 4ª Escolha");
                else if (urlData1.contains("vidoza") || urlData1.contains("vidoza.net"))
                    mListPraiasSetterGetter.setPraias_name("Vidoza" + " - 2ª Escolha");
                else if (urlData1.contains("verystream"))
                    mListPraiasSetterGetter.setPraias_name("Verystream" + " - 5ª Escolha");
                else
                {
                    mListPraiasSetterGetter.setPraias_name("Outro");
                }
                if(!urlData1.equals(""))
                {
                    list_items.add(mListPraiasSetterGetter);
                    popInd++;
                }
            }
            if(!urlData2.equals(""))
            {
                ListMoviesSetterGetter mListPraiasSetterGetter = new ListMoviesSetterGetter();
                mListPraiasSetterGetter.setPraias_url(urlData2);

                if (urlData2.contains("openload"))
                    mListPraiasSetterGetter.setPraias_name("Open Load - "+"3ª Escolha");
                else if (urlData2.contains("oload"))
                    mListPraiasSetterGetter.setPraias_name("OLoad - "+"3ª Escolha");
                else if (urlData2.contains("cloud.mail.ru") || urlData2.contains("google")  || urlData2.contains(".mp4") || urlData2.contains(".avi"))
                    mListPraiasSetterGetter.setPraias_name(mSessionManager.getNameAPP() + " - 1ª Escolha");
                else if(urlData2.contains("raptu") || urlData2.contains("rapidvideo.com"))
                    mListPraiasSetterGetter.setPraias_name("RapidVideo" + " - 8ª Escolha");
                else if(urlData2.contains("uptostream.com"))
                    mListPraiasSetterGetter.setPraias_name("Uptostream" + " - 7ª Escolha");
                else if(urlData2.contains("vidzi"))
                    mListPraiasSetterGetter.setPraias_name("Vidzi" + " - 6ª Escolha");
                else if (urlData2.contains("streamango"))
                    mListPraiasSetterGetter.setPraias_name("Streamango" + " - 4ª Escolha");
                else if (urlData2.contains("vidoza") || urlData2.contains("vidoza.net"))
                    mListPraiasSetterGetter.setPraias_name("Vidoza" + " - 2ª Escolha");
                else if (urlData2.contains("verystream"))
                    mListPraiasSetterGetter.setPraias_name("Verystream" + " - 5ª Escolha");
                else
                {
                    mListPraiasSetterGetter.setPraias_name("Outro");
                }
                if(!urlData2.equals(""))
                {
                    list_items.add(mListPraiasSetterGetter);
                    popInd++;
                }
            }
            if(!urlData3.equals(""))
            {
                ListMoviesSetterGetter mListPraiasSetterGetter = new ListMoviesSetterGetter();
                mListPraiasSetterGetter.setPraias_url(urlData3);

                if (urlData3.contains("openload"))
                    mListPraiasSetterGetter.setPraias_name("Open Load - "+"3ª Escolha");
                else if (urlData3.contains("oload"))
                    mListPraiasSetterGetter.setPraias_name("OLoad - "+"3ª Escolha");
                else if (urlData3.contains("cloud.mail.ru") || urlData3.contains("google")  || urlData3.contains(".mp4") || urlData3.contains(".avi"))
                    mListPraiasSetterGetter.setPraias_name(mSessionManager.getNameAPP() + " - 1ª Escolha");
                else if(urlData3.contains("raptu") || urlData3.contains("rapidvideo.com"))
                    mListPraiasSetterGetter.setPraias_name("RapidVideo" + " - 8ª Escolha");
                else if(urlData3.contains("uptostream.com"))
                    mListPraiasSetterGetter.setPraias_name("Uptostream" + " - 7ª Escolha");
                else if(urlData3.contains("vidzi"))
                    mListPraiasSetterGetter.setPraias_name("Vidzi" + " - 6ª Escolha");
                else if (urlData3.contains("streamango"))
                    mListPraiasSetterGetter.setPraias_name("Streamango" + " - 4ª Escolha");
                else if (urlData3.contains("vidoza") || urlData3.contains("vidoza.net"))
                    mListPraiasSetterGetter.setPraias_name("Vidoza" + " - 2ª Escolha");
                else if (urlData3.contains("verystream"))
                    mListPraiasSetterGetter.setPraias_name("Verystream" + " - 5ª Escolha");
                else
                {
                    mListPraiasSetterGetter.setPraias_name("Outro");
                }
                if(!urlData3.equals(""))
                {
                    list_items.add(mListPraiasSetterGetter);
                    popInd++;
                }
            }
            if(!urlData4.equals(""))
            {
                ListMoviesSetterGetter mListPraiasSetterGetter = new ListMoviesSetterGetter();
                mListPraiasSetterGetter.setPraias_url(urlData4);

                if (urlData4.contains("openload"))
                    mListPraiasSetterGetter.setPraias_name("Open Load - "+"3ª Escolha");
                else if (urlData4.contains("oload"))
                    mListPraiasSetterGetter.setPraias_name("OLoad - "+"3ª Escolha");
                else if (urlData4.contains("cloud.mail.ru") || urlData4.contains("google") || urlData4.contains(".mp4") || urlData4.contains(".avi"))
                    mListPraiasSetterGetter.setPraias_name(mSessionManager.getNameAPP() + " - 1ª Escolha");
                else if(urlData4.contains("raptu") || urlData4.contains("rapidvideo.com"))
                    mListPraiasSetterGetter.setPraias_name("RapidVideo" + " - 8ª Escolha");
                else if(urlData4.contains("uptostream.com"))
                    mListPraiasSetterGetter.setPraias_name("Uptostream" + " - 7ª Escolha");
                else if(urlData4.contains("vidzi"))
                    mListPraiasSetterGetter.setPraias_name("Vidzi" + " - 6ª Escolha");
                else if (urlData4.contains("streamango"))
                    mListPraiasSetterGetter.setPraias_name("Streamango" + " - 4ª Escolha");
                else if (urlData4.contains("vidoza") || urlData4.contains("vidoza.net"))
                    mListPraiasSetterGetter.setPraias_name("Vidoza" + " - 2ª Escolha");
                else if (urlData4.contains("verystream"))
                    mListPraiasSetterGetter.setPraias_name("Verystream" + " - 5ª Escolha");
                else
                {
                    mListPraiasSetterGetter.setPraias_name("Outro");
                }
                if(!urlData4.equals(""))
                {
                    list_items.add(mListPraiasSetterGetter);
                    popInd++;
                }
            }
            if(!urlData5.equals(""))
            {
                ListMoviesSetterGetter mListPraiasSetterGetter = new ListMoviesSetterGetter();
                mListPraiasSetterGetter.setPraias_url(urlData5);

                if (urlData5.contains("openload"))
                    mListPraiasSetterGetter.setPraias_name("Open Load - "+"3ª Escolha");
                else if (urlData5.contains("oload"))
                    mListPraiasSetterGetter.setPraias_name("OLoad - "+"3ª Escolha");
                else if (urlData5.contains("cloud.mail.ru") || urlData5.contains("google") || urlData5.contains(".mp4") || urlData5.contains(".avi"))
                    mListPraiasSetterGetter.setPraias_name(mSessionManager.getNameAPP() + " - 1ª Escolha");
                else if(urlData5.contains("raptu") || urlData5.contains("rapidvideo.com"))
                    mListPraiasSetterGetter.setPraias_name("RapidVideo" + " - 8ª Escolha");
                else if(urlData5.contains("uptostream.com"))
                    mListPraiasSetterGetter.setPraias_name("Uptostream" + " - 7ª Escolha");
                else if(urlData5.contains("vidzi"))
                    mListPraiasSetterGetter.setPraias_name("Vidzi" + " - 6ª Escolha");
                else if (urlData5.contains("streamango"))
                    mListPraiasSetterGetter.setPraias_name("Streamango" + " - 4ª Escolha");
                else if (urlData5.contains("vidoza") || urlData5.contains("vidoza.net"))
                    mListPraiasSetterGetter.setPraias_name("Vidoza" + " - 2ª Escolha");
                else if (urlData5.contains("verystream"))
                    mListPraiasSetterGetter.setPraias_name("Verystream" + " - 5ª Escolha");
                else
                {
                    mListPraiasSetterGetter.setPraias_name("Outro");
                }
                if(!urlData5.equals(""))
                {
                    list_items.add(mListPraiasSetterGetter);
                    popInd++;
                }
            }

            popmenuList = new CharSequence[popInd];
            int selInd = 0;
            for (ListMoviesSetterGetter mProgramSetterGetter : list_items) {
                popmenuList[selInd] = mProgramSetterGetter.getPraias_name();
                selInd++;
            }

            mFilterChannelSetterMEnu = new ListMoviesSetterGetter();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.questionmark);
            builder.setTitle(mSessionManager.getNameAPP() + " - Escolha o Servidor");
            builder.setCancelable(true);
            builder.setItems(popmenuList, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    String select_menu = (String) popmenuList[item];
                    stream_id = mFilterChannelSetterGetter.getPraias_id();
                    realPlaySubtitle = mFilterChannelSetterGetter.getPraias_legenda();
                    realPlayIMDB = mFilterChannelSetterGetter.getImdb();
                    realPlayName = mFilterChannelSetterGetter.getPraias_name();

                    mFilterChannelSetterMEnu = list_items.get(item);

                    String temp_Name = mFilterChannelSetterMEnu.getPraias_name();
                    playing_url = mFilterChannelSetterMEnu.getPraias_url();

                    if (temp_Name.contains("Open Load")) {
                        api_type = "openload";
                        new ParseOpenLoadAPI_URL().execute();
                    }else if (temp_Name.contains("OLoad")) {
                        api_type = "openload2";
                        new ParseOpenLoadAPI_URL2().execute();
                    }else if(temp_Name.contains("Streamango"))
                    {
                        api_type = "streamango";
                        new ParseStreamanAPI_URL().execute();
                        //ParseStreammVideo();
                    }else if(temp_Name.contains("Vidoza"))
                    {
                        api_type = "vidoza";
                        new ParseVidozaAPI_URL().execute();
                    }else if(temp_Name.contains("Verystream"))
                    {
                        api_type = "verystream";
                        new ParseVerystreamAPI_URL().execute();
                    }else if(temp_Name.contains("Uptostream"))
                    {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                        builder3.setIcon(R.drawable.questionmark);
                        builder3.setTitle(mSessionManager.getNameAPP());
                        builder3.setMessage("Ainda não implementado. Aguarde nova atualização");
                        builder3.setPositiveButton("Fechar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder3.show();
                    }else if(temp_Name.contains("Vidzi"))
                    {
                        AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                        builder3.setIcon(R.drawable.questionmark);
                        builder3.setTitle(mSessionManager.getNameAPP());
                        builder3.setMessage("Ainda não implementado. Aguarde nova atualização");
                        builder3.setPositiveButton("Fechar",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder3.show();
                    }else if(temp_Name.contains(mSessionManager.getNameAPP()))
                    {
                        if (playing_url.contains(".mp4") || playing_url.contains(".avi"))
                        {
                            realPlayFunc(playing_url,"", realPlayName);
                        }else if (playing_url.contains("cloud.mail.ru")) {
                            api_type = "cloud";
                            new ParseCloudAPI_URL().execute();
                        }else if (playing_url.contains("google")) {
                            api_type = "google";
                            new FetchURLData().execute();
                        }else if (playing_url.contains("mystream")) {
                            api_type = "mystream";
                            new FetchURLData().execute();
                        }else{
                            api_type = "liveit";
                            new FetchURLData().execute();
                        }
                    }
                    else{
                        realPlayFunc(playing_url,"", realPlayName);
                    }
                }

            });
            builder.show();
        }
    }

    public class ParseStreamanAPI_URL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                programas_url = playing_url;
                JSONPar jsonPar = new JSONPar();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                realPlayURL = null;
                realPlayURL = jsonPar.makeHttpRequest(programas_url, "GET3", nameValuePairs);

                if (!realPlayURL.equals("")) {
                    if (!realPlayURL.equals("")) {
                        String[] split_1 = realPlayURL.split("suburl = \"");
                        if (split_1.length > 1) {
                            String tmp = split_1[1];
                            String[] split_2 = tmp.split("\";");
                            if (split_2.length > 1) {
                                String tmp1 = split_2[0];
                                if (realPlaySubtitle.equals("")) {
                                    realPlaySubtitle = tmp1.replace("\\", "");

                                    if (!LegendaPai.equals("")) {
                                        realPlaySubtitle = LegendaPai;
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return realPlayURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String realPlayURL) {
            super.onPostExecute(realPlayURL);
            ParseStreammVideo();
        }
    }

    public void ParseStreammVideo() {
        String pURL = "";
        String[] split_001 = programas_url.split("embed/");
        if (!split_001.equals(null)) {
            if (split_001.length > 1) {
                pURL = split_001[1].replaceAll("/", "");
                makeStringReqDownloadTicketStream(pURL, realPlaySubtitle, realPlayName);
            } else {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setIcon(R.drawable.questionmark);
                builder2.setTitle(mSessionManager.getNameAPP() + "Erro - Streamango");
                builder2.setMessage("Erro no Endereço do Video. Tente outro Servidor.");
                builder2.setPositiveButton("Fechar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder2.show();
            }
        } else {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
            builder2.setIcon(R.drawable.questionmark);
            builder2.setTitle(mSessionManager.getNameAPP() + "Erro - Streamango");
            builder2.setMessage("Erro no Endereço do Video. Tente outro Servidor.");
            builder2.setPositiveButton("Fechar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder2.show();
        }
    }

    public class FetchURLData extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                System.setProperty("http.keeyAlive", "true");
                programas_url = mSessionManager.getUserAcess_Token() + Urls.urlParseURL + "?type=" + api_type + "&api=" + playing_url;
                JSONParser jsonPar = new JSONParser();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                parseURLData = null;
                parseURLData = jsonPar.makeHttpRequest(programas_url, "GET", nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return parseURLData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(JSONObject parseURLData) {
            super.onPostExecute(parseURLData);
            try {
                if (!parseURLData.equals(null)) {
                    if (api_type.equals("raptu") || api_type.equals("rapid") || api_type.equals("google") || api_type.equals("uptostream")) {
                        showSelectVideoType2(parseURLData);
                    } else {
                        if(parseURLData.has("srt_url"))
                        {
                            realPlaySubtitle = parseURLData.optString("srt_url");
                        }
                        realPlayURL = parseURLData.optString("realurl");

                        if (realPlaySubtitle.equals("")) {
                            realPlayFunc(realPlayURL,"", realPlayName);
                        } else {
                            realPlayFunc(realPlayURL,realPlaySubtitle, realPlayName);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class ParseCloudAPI_URL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            programas_url = playing_url;
            JSONPar jsonPar = new JSONPar();
            String linkURL = "";
            String linkID = "";
            String linkToken = "";
            realPlayURL = "";
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                String myData = jsonPar.makeHttpRequest(programas_url, "GET3", nameValuePairs);

                if (!myData.equals("") && myData != null) {
                    String[] split_1 = myData.split("weblink_get\": \\[");
                    if (split_1 != null) {
                        if (split_1.length > 1) {
                            String tmp = split_1[1];
                            String[] split_2 = tmp.split("\\}");
                            if (split_2 != null) {
                                if (split_2.length > 1) {
                                    String tmp1 = split_2[0];
                                    String[] split_3 = tmp1.split("url\": \"");

                                    if (split_3 != null) {
                                        if (split_3.length > 1) {
                                            String tmp2 = split_3[1];
                                            linkURL = tmp2;
                                            linkURL = linkURL.replace("\"", "");
                                            linkURL = linkURL.replace("\"", "");
                                        }
                                    }
                                }
                            }
                        }
                    }


                    String[] split_01 = myData.split("state\": \\{");
                    if (split_01 != null) {
                        if (split_01.length > 1) {
                            String tmp_01 = split_01[1];
                            String[] split_02 = tmp_01.split("\",");
                            if (split_02 != null) {
                                if (split_02.length > 1) {
                                    String tmp_02 = split_02[0];
                                    linkID = tmp_02.replace("id\": \"", "");
                                    linkID = linkID.replace("\"", "");
                                    linkID = linkID.replace("\"", "");

                                }
                            }
                        }
                    }

                    String[] split_001 = myData.split("tokens\": \\{");
                    if (split_001 != null) {
                        if (split_001.length > 1) {
                            String tmp_001 = split_001[1];
                            String[] split_002 = tmp_001.split("\\},");
                            if (split_002 != null) {
                                if (split_002.length > 1) {
                                    String tmp_002 = split_002[0];
                                    linkToken = tmp_002.replace("download\": \"", "");
                                    linkToken = linkToken.replace("\"", "");
                                    linkToken = linkToken.replace("\"", "");
                                }
                            }
                        }
                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!linkURL.equals("") && !linkToken.equals("") && !linkID.equals("")) {
                realPlayURL = linkURL.trim() + "/" + linkID.trim() + "?key=" + linkToken.trim();
            }

            return realPlayURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String realPlayURL) {
            super.onPostExecute(realPlayURL);

            if (!realPlayURL.equals(null) && !realPlayURL.equals("")) {
                if (realPlaySubtitle.equals("")) {
                    realPlayFunc(realPlayURL,"", realPlayName);
                } else {
                    realPlayFunc(realPlayURL,realPlaySubtitle, realPlayName);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.drawable.questionmark);
                builder.setTitle(mSessionManager.getNameAPP() + " Erros");
                builder.setMessage("Erro ao ir buscar o Url do Servidor.");
                builder.setPositiveButton("Fechar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
            }
        }
    }

    public class ParseVerystreamAPI_URL extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            programas_url = playing_url;
            jsonArray = new JSONArray();
            jsonPar = new JSONPar();
            myDataRapid = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                System.setProperty("http.keeyAlive", "true");
                myDataRapid = jsonPar.makeHttpsRequest(programas_url, "GET", nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return myDataRapid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String myDataRapid) {
            super.onPostExecute(myDataRapid);
            String link_url = "";
            String Subtitles = "";
            try {
                if (!myDataRapid.equals("") && myDataRapid != null) {
                    String[] split_1 = myDataRapid.split("id=\"videolink\">");
                    if (!split_1.equals(null)) {
                        String[] split_2 = split_1[1].split("<\\/p>");
                        link_url = "https://verystream.com/gettoken/"+split_2[0]+"?mime=true";
                    }

                    realPlayFunc(link_url,realPlaySubtitle, realPlayName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public class ParseVidozaAPI_URL extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            programas_url = playing_url;
            jsonArray = new JSONArray();
            jsonPar = new JSONPar();
            myDataRapid = "";
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                System.setProperty("http.keeyAlive", "true");
                myDataRapid = jsonPar.makeHttpsRequest(programas_url, "GET", nameValuePairs);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return myDataRapid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String myDataRapid) {
            super.onPostExecute(myDataRapid);
            String link_url = "";
            String Subtitles = "";
            try {
                if (!myDataRapid.equals("") && myDataRapid != null) {
                    String[] split_1 = myDataRapid.split("<source src=\"");
                    if (!split_1.equals(null)) {
                        String[] split_2 = split_1[1].split("\"");
                        link_url = split_2[0];
                    }

                    String[] split_11 = myDataRapid.split("\"subtitles\" src=\"");
                    if (!split_11.equals(null)) {
                        if (split_11.length > 1) {
                            String[] split_22= split_11[1].split("\" srclang");
                            if (!split_22.equals(null)) {
                                if (split_22.length > 1) {
                                    String nova = "srclang"+split_22[1];
                                    String[] split_222 = nova.split(">");
                                    if (!split_222.equals(null)) {
                                        if (split_222.length > 1) {
                                            String[] split_33= split_222[0].split(" ");
                                            if (!split_33.equals(null)) {
                                                if (split_33.length > 1) {
                                                    for (int i = 0; i < split_33.length; i++) {
                                                        String[] split_3 = split_33[i].split("=");
                                                        if (!split_3.equals(null)) {
                                                            if (split_3.length > 1) {
                                                                String nomee = split_3[0].replace("\"", "");
                                                                nomee = nomee.replaceAll("\"", "");
                                                                nomee = nomee.replaceAll("\\{", "");
                                                                nomee = nomee.replaceAll("\\[", "");
                                                                String dataa = split_3[1].replace("\"", "");
                                                                dataa = dataa.replaceAll("\"", "");
                                                                dataa = dataa.replaceAll("\"", "");

                                                                /*if(nomee.equals("srclang") || nomee.equals("label"))
                                                                {
                                                                    if(dataa.equals("Portuguese"))
                                                                    {
                                                                        Subtitles = split_22[0];
                                                                        break;
                                                                    }
                                                                }*/
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!Subtitles.equals("")) {
                        realPlayFunc(link_url,Subtitles, realPlayName);
                    } else {
                        realPlayFunc(link_url,realPlaySubtitle, realPlayName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showSelectVideoType2(JSONObject playList) {
        if (!playList.equals(null)) {
            try {
                JSONArray items = playList.getJSONArray("url_list");
                int cnt = items.length();
                popResList = new CharSequence[cnt];
                linklist = new String[cnt];
                for (int i = 0; i < cnt; i++) {
                    JSONObject tmpObj = items.getJSONObject(i);
                    if (!tmpObj.equals(null)) {
                        String resName = tmpObj.optString("restype");
                        String urlData = tmpObj.optString("realurl");
                        popResList[i] = resName;
                        linklist[i] = urlData;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.questionmark);
        builder.setItems(popResList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                String select_url = linklist[item];

                realPlayFunc(select_url,"", realPlayName);

            }

        });
        builder.show();
    }

    public void realPlayFunc(String url, String srt_path, String nomePa) {
        ListMoviesSetterGetter qwqwe = new ListMoviesSetterGetter();
        qwqwe.setPraias_url(url);
        qwqwe.setPraias_name(nomePa);
        qwqwe.setPraias_legenda(srt_path);

        Utils.PlayerSetNovo(qwqwe, context, "FilmesAPP");
    }

    public class ParseOpenLoadAPI_URL extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                programas_url = playing_url;
                JSONPar jsonPar = new JSONPar();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                realPlayURL = null;

                realPlayURL = jsonPar.makeHttpRequest(programas_url, "GET3", nameValuePairs);

                if (!realPlayURL.equals(null)) {
                    if (!realPlayURL.equals("")) {
                        String[] split_1 = realPlayURL.split("suburl = \"");
                        if (split_1.length > 1) {
                            String tmp = split_1[1];
                            String[] split_2 = tmp.split("\";");
                            if (split_2.length > 1) {
                                String tmp1 = split_2[0];
                                if (realPlaySubtitle.equals("")) {
                                    realPlaySubtitle = tmp1.replace("\\", "");
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return realPlayURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String realPlayURL) {
            super.onPostExecute(realPlayURL);
            ParseOpenlaodVideo("openload.co");
        }
    }

    public class ParseOpenLoadAPI_URL2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                programas_url = playing_url;
                JSONPar jsonPar = new JSONPar();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                realPlayURL = null;

                realPlayURL = jsonPar.makeHttpRequest(programas_url, "GET3", nameValuePairs);

                if (!realPlayURL.equals(null)) {
                    if (!realPlayURL.equals("")) {
                        String[] split_1 = realPlayURL.split("suburl = \"");
                        if (split_1.length > 1) {
                            String tmp = split_1[1];
                            String[] split_2 = tmp.split("\";");
                            if (split_2.length > 1) {
                                String tmp1 = split_2[0];
                                if (realPlaySubtitle.equals("")) {
                                    realPlaySubtitle = tmp1.replace("\\", "");
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return realPlayURL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String realPlayURL) {
            super.onPostExecute(realPlayURL);
            ParseOpenlaodVideo("oload.tv");
        }
    }

    public void ParseOpenlaodVideo(final String filename) {
        String pURL = playing_url;
        int pos = pURL.lastIndexOf("/");
        String x = pURL.substring(pos + 1, pURL.length());
        makeStringReqDownloadLink2(x, realPlaySubtitle, realPlayName, filename);
    }

    JSONArray jsonArray;
    String myDataRapid = "";
    String openload_subtitle = "";
    String openload_name = "";
    String openload_URL = "";
    ArrayList<String> limits = new ArrayList<String>();
    public String tag_string_req = "string_req";
    private String TAG = "Liveit";

    public void makeStringReqDownloadLink2(final String filename, final String subtitle, final String name, final String tipo) {
        openload_name = name;
        openload_subtitle = subtitle;
        openload_URL = filename;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                "https://api."+tipo+"/1/streaming/get?file=" + openload_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response);
                                String statuscode = jsonObj.getString("status");
                                if (statuscode.equals("509")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setIcon(R.drawable.questionmark);
                                    builder.setTitle(mSessionManager.getNameAPP() + "- OpenLoad");
                                    builder.setMessage(" * Excedeu o limite de visualização por hoje. \n" +
                                            " * Para voltar a funcionar tem de aceder á página:\n\n   *** https://olpair.com\n\n   *** Pode ser em qualquer dispositivo ligado a mesma rede que está a tentar aceder o programa.\n\n * Depois click no visto para preencher o Captcha(controlo) e depois faça 'pair'. \n\nApós isso tente novamente ver filmes Openload.");
                                    builder.setPositiveButton("Fechar",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.show();
                                } else if (statuscode.equals("403")) {
                                    makeStringReqDownloadTicket(openload_URL, openload_subtitle, openload_name, tipo);
                                } else {
                                    JSONObject photoset = jsonObj.getJSONObject("result");
                                    String url = photoset.getString("url");
                                    if (openload_subtitle.equals("")) {
                                        realPlayFunc(url,"", openload_name);
                                    } else {
                                        realPlayFunc(url,openload_subtitle, openload_name);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error Reiaiai: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void makeStringReqDownloadTicket(final String filename, final String subtitle, final String name, final String tipo) {
        openload_name = name;
        openload_subtitle = subtitle;
        openload_URL = filename;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                "https://api."+tipo+"/1/file/dlticket?file=" + filename,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            String err_msg = "";
                            try {
                                jsonObj = new JSONObject(response);
                                err_msg = jsonObj.optString("msg");
                                String statuscode = jsonObj.getString("status");
                                if (statuscode.equals("509")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setIcon(R.drawable.questionmark);
                                    builder.setTitle(mSessionManager.getNameAPP() + "- OpenLoad");
                                    builder.setMessage(" * Excedeu o limite de visualização por hoje. \n" +
                                            " * Para voltar a funcionar tem de aceder á página:\n\n   *** https://olpair.com\n\n   *** Pode ser em qualquer dispositivo ligado a mesma rede que está a tentar aceder o programa.\n\n * Depois click no visto para preencher o Captcha(controlo) e depois faça 'pair'. \n\nApós isso tente novamente ver filmes Openload.");
                                    builder.setPositiveButton("Fechar",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.show();
                                } else {
                                    JSONObject photoset = jsonObj.getJSONObject("result");
                                    Captch = photoset.getString("captcha_url");
                                    ticket = photoset.getString("ticket");
                                    if (!Captch.equalsIgnoreCase("false")) {
                                        if (Captch != null) {
                                            final Dialog dialog = new Dialog(context);
                                            dialog.setContentView(R.layout.custom);
                                            Button dialogButton = (Button) dialog.findViewById(R.id.button_ok);
                                            ImageView captcha_image = (ImageView) dialog.findViewById(R.id.load_captcha);
                                            final EditText get_captcha = (EditText) dialog.findViewById(R.id.captcha_edit);
                                            Glide.with(context)
                                                    .load(Captch)
                                                    .into(captcha_image);
                                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    makeStringReqDownloadLink(get_captcha.getText().toString(), filename, ticket, tipo);
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialog.show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Url inválido. Tente mais tarde.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (Exception e) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                builder2 = new AlertDialog.Builder(context);
                                builder2.setTitle(mSessionManager.getNameAPP() + "-REI");

                                builder2.setMessage(err_msg);
                                builder2.show();
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void makeStringReqDownloadLink(String captch, String filename, String ticket, final String tipo) {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                "https://api."+tipo+"/1/file/dl?file=" + filename + "&ticket=" + ticket + "&captcha_response=" + captch,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response);
                                String statuscode = jsonObj.getString("status");
                                if (statuscode.equalsIgnoreCase("200")) {
                                    JSONObject photoset = jsonObj.getJSONObject("result");
                                    String url = photoset.getString("url");
                                    if (openload_subtitle.equals("")) {
                                        realPlayFunc(url,"", openload_name);
                                    } else {
                                        realPlayFunc(url,openload_subtitle, openload_name);
                                    }
                                } else {
                                    Toast.makeText(context, "Captcha inválido. Insira de novo.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void makeStringReqDownloadTicketStream(final String filename, final String subtitle, final String name) {
        //Log.e("makeStringReqDownloadTicket filename", ":=====" + filename);
        openload_name = name;
        openload_subtitle = subtitle;
        openload_URL = filename;
        //final String UrlNovo = "https://api.fruithosted.net/file/dlticket?file=" + filename + "&login=RGluEmAA3a&key=-OPHiLZK";
		final String UrlNovo = "https://api.fruithosted.net/file/dlticket?file=" + filename;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                UrlNovo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            String err_msg = "";
                            try {
                                jsonObj = new JSONObject(response);
                                err_msg = jsonObj.optString("msg");
                                String statuscode = jsonObj.getString("status");
                                if (statuscode.equals("509")) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setIcon(R.drawable.questionmark);
                                    builder.setTitle(mSessionManager.getNameAPP() + "- Streamango");
                                    builder.setMessage(" * Excedeu o limite de visualização por hoje. \n" +
                                            " * Para voltar a funcionar tem de aceder á página:\n\n   *** https://streamango.com/pair\n\n   *** Pode ser em qualquer dispositivo ligado a mesma rede que está a tentar aceder o programa.\n\n * Depois click no visto para preencher o Captcha(controlo) e depois faça 'pair'. \n\nApós isso tente novamente ver filmes Streamango.");
                                    builder.setPositiveButton("Fechar",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    builder.show();
                                } else {
                                    JSONObject photoset = jsonObj.getJSONObject("result");
                                    Captch = photoset.getString("captcha_url");
                                    ticket = photoset.getString("ticket");
                                    if (!Captch.equalsIgnoreCase("false")) {
                                        if (Captch != null) {
                                            final Dialog dialog = new Dialog(context);
                                            dialog.setContentView(R.layout.custom);
                                            Button dialogButton = (Button) dialog.findViewById(R.id.button_ok);
                                            ImageView captcha_image = (ImageView) dialog.findViewById(R.id.load_captcha);
                                            final EditText get_captcha = (EditText) dialog.findViewById(R.id.captcha_edit);
                                            Glide.with(context)
                                                    .load(Captch)
                                                    .into(captcha_image);
                                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    makeStringReqDownloadLinkStream(get_captcha.getText().toString(), filename, ticket);
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialog.show();
                                        } else {
                                            Toast.makeText(context, "Url inválido. Tente mais tarde.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        makeStringReqDownloadLinkStream(Captch, filename, ticket);
                                    }
                                }
                            } catch (Exception e) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                builder2 = new AlertDialog.Builder(context);
                                builder2.setTitle(mSessionManager.getNameAPP() + "-REI");

                                builder2.setMessage(err_msg);
                                builder2.show();
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void makeStringReqDownloadLinkStream(String captch, String filename, String ticket) {
        StringRequest strReq = new StringRequest(Request.Method.GET,
                "https://api.fruithosted.net/file/dl?file=" + filename + "&ticket=" + ticket + "&captcha_response=" + captch,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            JSONObject jsonObj = null;
                            try {
                                jsonObj = new JSONObject(response);
                                String statuscode = jsonObj.getString("status");
                                if (statuscode.equalsIgnoreCase("200")) {
                                    JSONObject photoset = jsonObj.getJSONObject("result");
                                    String url = photoset.getString("url");
                                    if (openload_subtitle.equals("")) {
                                        realPlayFunc(url,"", openload_name);
                                    } else {
                                        realPlayFunc(url,openload_subtitle, openload_name);
                                    }
                                } else {
                                    Toast.makeText(context, "Captcha inválido. Insira de novo.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
                    VodAdapterApp.this.text_last_size = VodAdapterApp.this.text_size;
                    VodAdapterApp.this.notifyDataSetChanged();
                }
            }

            public void run() {
                filterList = new ArrayList();
                text_size = text.length();
                if (filterList != null) {
                    filterList.clear();
                }
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(VodAdapterApp.this.completeList);
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

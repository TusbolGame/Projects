package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.app_shell.AppController;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.JSONPar;
import com.liveitandroid.liveit.helper.JSONParser;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.VodPresenter;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewDetailsActivityMov extends AppCompatActivity implements OnClickListener {
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    private PopupWindow changeSortPopUp;
    private TextView clientNameTv;
    Button closedBT;
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    String fullCast;
    String fullGenre;
    @BindView(R.id.iv_favourite)
    ImageView ivFavourite;
    @BindView(R.id.iv_movie_image)
    ImageView ivMovieImage;
    private LiveStreamDBHandler liveStreamDBHandler;
    @BindView(R.id.ll_cast_box)
    LinearLayout llCastBox;
    @BindView(R.id.ll_cast_box_info)
    LinearLayout llCastBoxInfo;
    @BindView(R.id.ll_director_box)
    LinearLayout llDirectorBox;
    @BindView(R.id.ll_director_box_info)
    LinearLayout llDirectorBoxInfo;
    @BindView(R.id.ll_duration_box)
    LinearLayout llDurationBox;
    @BindView(R.id.ll_duration_box_info)
    LinearLayout llDurationBoxInfo;
    @BindView(R.id.ll_genre_box)
    LinearLayout llGenreBox;
    @BindView(R.id.ll_genre_box_info)
    LinearLayout llGenreBoxInfo;
    @BindView(R.id.ll_movie_info_box)
    LinearLayout llMovieInfoBox;
    @BindView(R.id.ll_released_box)
    LinearLayout llReleasedBox;
    @BindView(R.id.ll_released_box_info)
    LinearLayout llReleasedBoxInfo;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private String film_name, film_logo, film_desc, film_ano, film_player, film_favorito_id, film_id, film_categoria, film_actores, film_rating, film_imbd, film_director, film_trailer, film_favorito, film_name_en;
    private String num = "";
    private ProgressDialog progressDialog;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.rl_account_info)
    RelativeLayout rlAccountInfo;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private String selectedPlayer = "";
    private int streamId = -1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(R.id.tv_account_info)
    TextView tvAccountInfo;
    @BindView(R.id.tv_add_to_fav)
    TextView tvAddToFav;
    @BindView(R.id.tv_add_to_fav_2)
    TextView tvAddToFavBD;
    @BindView(R.id.tv_rating_num)
    TextView tv_rating_num;
    @BindView(R.id.tv_cast)
    TextView tvCast;
    @BindView(R.id.tv_cast_info)
    TextView tvCastInfo;
    TextView tvCastInfoPopUp;
    @BindView(R.id.tv_director)
    TextView tvDirector;
    @BindView(R.id.tv_director_info)
    TextView tvDirectorInfo;
    TextView tvGenreInfoPopUp;
    @BindView(R.id.tv_movie_duration)
    TextView tvMovieDuration;
    @BindView(R.id.tv_movie_duration_info)
    TextView tvMovieDurationInfo;
    @BindView(R.id.tv_movie_genere)
    TextView tvMovieGenere;
    @BindView(R.id.tv_movie_info)
    TextView tvMovieInfo;
    @BindView(R.id.tv_movie_name)
    TextView tvMovieName;
    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.tv_readmore)
    TextView tvReadMore;
    @BindView(R.id.tv_readmore_genre)
    TextView tvReadMoreGenre;
    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.tv_release_date_info)
    TextView tvReleaseDateInfo;
    @BindView(R.id.tv_genre_info)
    TextView tv_genre_info;
    @BindView(R.id.tv_detail_back_btn)
    TextView tvdetailbackbutton;
    @BindView(R.id.tv_detail_ProgressBar)
    ProgressBar tvdetailprogressbar;
    private VodPresenter vodPresenter;
    private SessionManager mSessionManager;
    private String jsonStr;
    private JSONObject jsonFilmObject;

    class C17841 implements OnClickListener {
        C17841() {
        }

        public void onClick(View view) {
            changeSortPopUp.dismiss();
        }
    }

    class C17852 implements OnClickListener {
        C17852() {
        }

        public void onClick(View view) {
            changeSortPopUp.dismiss();
        }
    }

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 1.0f;
            if (hasFocus) {
                if (hasFocus) {
                    to = 1.05f;
                }
                if (this.view.getTag().equals("1")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.blue_btn_effect);
                } else if (this.view.getTag().equals("2")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.blue_btn_effect);
                } else if (this.view.getTag().equals("3")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.blue_btn_effect);
                } else if (this.view.getTag().equals("4")) {
                    performScaleXAnimation(to);
                    performScaleYAnimation(to);
                    this.view.setBackgroundResource(R.drawable.blue_btn_effect);
                } else {
                    performScaleXAnimation(1.15f);
                    performScaleYAnimation(1.15f);
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.back_btn_effect);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.logout_btn_effect);
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.blue_btn_effect);
                }else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.back_btn_effect);
                }
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_details_vodapp);
        ButterKnife.bind(this);

        context = this;

        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        if (this.appbarToolbar != null) {
            this.appbarToolbar.setBackground(getResources().getDrawable(R.drawable.movie_info_bg));
        }
        changeStatusBarColor();
        if (this.tvPlay != null) {
            this.tvPlay.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvPlay));
        }
        if (this.tvAddToFav != null) {
            this.tvAddToFav.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvAddToFav));
        }
        if (this.tvAddToFavBD != null) {
            this.tvAddToFavBD.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvAddToFavBD));
        }

        this.ivFavourite.setVisibility(View.GONE);
        if (this.tvdetailbackbutton != null) {
            this.tvdetailbackbutton.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvdetailbackbutton));
        }
        if (this.tvReadMore != null) {
            this.tvReadMore.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvReadMore));
        }
        if (this.tvReadMoreGenre != null) {
            this.tvReadMoreGenre.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvReadMoreGenre));
        }

        initialize();
    }

    private ListMoviesSetterGetter mSelectedMovie;

    private void showCastPopUp(ViewDetailsActivityMov context) {
        View layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_cast_details, (RelativeLayout) context.findViewById(R.id.rl_password_verification));
        this.tvCastInfoPopUp = (TextView) layout.findViewById(R.id.tv_casts_info_popup);
        this.tvCastInfoPopUp.setText(this.fullCast);
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        this.closedBT = (Button) layout.findViewById(R.id.bt_close);
        if (this.closedBT != null) {
            this.closedBT.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.closedBT));
        }
        this.closedBT.setOnClickListener(new C17841());
    }

    private void showGenrePopUp(ViewDetailsActivityMov context) {
        View layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_genre_details, (RelativeLayout) context.findViewById(R.id.rl_password_verification));
        this.tvGenreInfoPopUp = (TextView) layout.findViewById(R.id.tv_genre_info_popup);
        this.tvGenreInfoPopUp.setText(this.fullGenre);
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        this.closedBT = (Button) layout.findViewById(R.id.bt_close);
        if (this.closedBT != null) {
            this.closedBT.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.closedBT));
        }
        this.closedBT.setOnClickListener(new C17852());
    }

    private void initialize() {
        this.tvPlay.requestFocus();
        this.tvPlay.setFocusable(true);

        mSessionManager = new SessionManager(context);
        mSelectedMovie = (ListMoviesSetterGetter)getIntent().getSerializableExtra("ficheiroMov");

        startViewingDetails();
    }

    private void startViewingDetails() {
        this.film_name = getIntent().getStringExtra("film_name");
        this.film_logo = getIntent().getStringExtra("film_logo");
        this.film_desc = getIntent().getStringExtra("film_desc");
        this.film_ano = getIntent().getStringExtra("film_ano");
        this.film_player = getIntent().getStringExtra("film_player");
        this.film_categoria = getIntent().getStringExtra("film_categoria");
        this.film_actores = getIntent().getStringExtra("film_actores");
        this.film_rating = getIntent().getStringExtra("film_rating");
        this.film_imbd = getIntent().getStringExtra("film_imbd");
        this.film_director = getIntent().getStringExtra("film_director");
        this.film_trailer = getIntent().getStringExtra("film_trailer");
        this.film_name_en = getIntent().getStringExtra("film_name_en");
        this.film_favorito = getIntent().getStringExtra("film_favorito");
        this.film_id = getIntent().getStringExtra("film_id");

        this.tvPlay.setText("Assistir");
        this.tvAddToFav.setText("Trailer");

        if(film_trailer.equals("") || film_trailer.equals(null))
        {
            this.tvAddToFav.setVisibility(View.GONE);
        }else
        {
            String[] split_01 = film_trailer.split("v=");
            if (split_01.length > 1) {
                film_trailer = split_01[1];
            } else {
                split_01 = film_trailer.split(".be/");
                if (split_01.length > 1) {
                    film_trailer = split_01[1];
                }
            }
        }

        new BuscaIDFavorito().execute();
    }

    private void changeStatusBarColor() {
        Window window = getWindow();
        if (VERSION.SDK_INT >= 19) {
            window.clearFlags(67108864);
        }
        if (VERSION.SDK_INT >= 21) {
            window.addFlags(Integer.MIN_VALUE);
        }
        if (VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        getWindow().setFlags(1024, 1024);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_header_title:
                startActivity(new Intent(this, NewDashboardActivity.class));
                return;
            default:
                return;
        }
    }

    public void atStart() {
    }

    public void onFinish() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    public void onFailed(String errorMessage) {
        this.tvdetailprogressbar.setVisibility(8);
    }

    public void vodInfo() {
        this.tvdetailprogressbar.setVisibility(View.GONE);
        this.scrollView.setVisibility(0);
        String movieImage = film_logo;
        String movieDirector = film_director;
        String cast = film_actores;
        this.fullCast = cast;
        String releaseDate = film_ano;
        String rating = film_rating;
        String description = film_desc;
        String gnere = film_categoria;
        this.fullGenre = gnere;
        if (this.tvAccountInfo != null) {
            tvAccountInfo.setText(film_name);
        }
        if (this.tv_rating_num != null) {
            tv_rating_num.setText(rating);
        }
        if (!(this.context == null || movieImage == null || movieImage.isEmpty())) {
            Picasso.with(this.context).load(movieImage).placeholder((int) R.drawable.tranparentdark).into(this.ivMovieImage);
        }

            /*this.film_player = intetGetIntent.getStringExtra("film_player");
            this.film_imbd = intetGetIntent.getStringExtra("film_imbd");
            this.film_trailer = intetGetIntent.getStringExtra("film_trailer");
            this.film_name_en = intetGetIntent.getStringExtra("film_name_en");*/

        if (!(this.film_name_en == null)) {
            this.tvMovieName.setText(this.film_name_en);
        }
        if (this.llReleasedBox == null || this.llReleasedBoxInfo == null || this.tvReleaseDateInfo == null || releaseDate == null || releaseDate.isEmpty() || releaseDate.equals("n/A")) {
            if (this.llReleasedBox != null) {
                this.llReleasedBox.setVisibility(0);
            }
            if (this.llReleasedBoxInfo != null) {
                this.llReleasedBoxInfo.setVisibility(0);
            }
            if (this.tvReleaseDateInfo != null) {
                this.tvReleaseDateInfo.setText("N/A");
            }
        } else {
            this.llReleasedBox.setVisibility(0);
            this.llReleasedBoxInfo.setVisibility(0);
            this.tvReleaseDateInfo.setText(releaseDate);
        }
        if (this.tvDirectorInfo == null || this.llDirectorBoxInfo == null || this.llDirectorBox == null || movieDirector == null || movieDirector.isEmpty() || movieDirector.equals("n/A")) {
            if (this.llDirectorBox != null) {
                this.llDirectorBox.setVisibility(0);
            }
            if (this.llDirectorBoxInfo != null) {
                this.llDirectorBoxInfo.setVisibility(0);
            }
            if (this.tvDirectorInfo != null) {
                this.tvDirectorInfo.setText("N/A");
            }
        } else {
            this.llDirectorBox.setVisibility(0);
            this.llDirectorBoxInfo.setVisibility(0);
            this.tvDirectorInfo.setText(movieDirector);
        }
        if (this.llCastBox == null || this.llCastBoxInfo == null || this.tvCastInfo == null || cast == null || cast.isEmpty()) {
            if (this.llCastBox != null) {
                this.llCastBox.setVisibility(0);
            }
            if (this.llCastBoxInfo != null) {
                this.llCastBoxInfo.setVisibility(0);
            }
            if (this.tvReadMore != null) {
                this.tvReadMore.setVisibility(8);
            }
            if (this.tvCastInfo != null) {
                this.tvCastInfo.setText("N/A");
            }
        } else {
            this.llCastBox.setVisibility(0);
            this.llCastBoxInfo.setVisibility(0);
            boolean readMore = false;
            if (cast.length() > 150) {
                readMore = true;
            }
            if (readMore) {
                this.tvCastInfo.setText(cast);
                this.tvReadMore.setVisibility(0);
            } else {
                this.tvCastInfo.setText(cast);
                this.tvReadMore.setVisibility(8);
            }
        }
        if (!(this.ratingBar == null || rating == null || rating.isEmpty() || rating.equals("n/A"))) {
            this.ratingBar.setVisibility(0);
            try {
                this.ratingBar.setRating(Float.parseFloat(rating) / 2.0f);
            } catch (NumberFormatException e) {
                this.ratingBar.setRating(0.0f);
            }
        }
        if (this.tvMovieInfo != null && description != null && !description.isEmpty() && !description.equals("n/A")) {
            this.tvMovieInfo.setText(description);
        } else if (this.tvMovieInfo != null) {
            this.tvMovieInfo.setVisibility(8);
        }
        if (this.llGenreBox == null || this.llGenreBoxInfo == null || this.tv_genre_info == null || gnere == null || gnere.isEmpty()) {
            if (this.llGenreBox != null) {
                this.llGenreBox.setVisibility(0);
            }
            if (this.llGenreBoxInfo != null) {
                this.llGenreBoxInfo.setVisibility(0);
            }
            if (this.tvReadMoreGenre != null) {
                this.tvReadMoreGenre.setVisibility(8);
            }
            if (this.tv_genre_info != null) {
                this.tv_genre_info.setText("N/A");
            }
        } else {
            this.llGenreBox.setVisibility(0);
            this.llGenreBoxInfo.setVisibility(0);
            boolean readMoreGenre = false;
            if (gnere.length() > 40) {
                readMoreGenre = true;
            }
            if (readMoreGenre) {
                this.tv_genre_info.setText(gnere);
                this.tvReadMoreGenre.setVisibility(0);
            } else {
                this.tv_genre_info.setText(gnere);
                this.tvReadMoreGenre.setVisibility(8);
            }
        }
        if (this.llDurationBox == null || this.llDurationBoxInfo == null || this.tvMovieDurationInfo == null) {
            if (this.llDurationBox != null) {
                this.llDurationBox.setVisibility(0);
            }
            if (this.llDurationBoxInfo != null) {
                this.llDurationBoxInfo.setVisibility(0);
            }
            if (this.tvMovieDurationInfo != null) {
                this.tvMovieDurationInfo.setText("N/A");
                return;
            }
            return;
        }
        this.llDurationBox.setVisibility(0);
        this.llDurationBoxInfo.setVisibility(0);
        this.tvMovieDurationInfo.setText(film_imbd);
    }

    @OnClick({R.id.tv_play, R.id.tv_add_to_fav, R.id.tv_add_to_fav_2, R.id.tv_detail_back_btn, R.id.tv_readmore, R.id.tv_readmore_genre})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_to_fav:
                if (mSessionManager.getNameAPP().equals("Liveit-AndroidTV")) {
                    boolean installed = Utils.appInstalledOrNot("com.google.android.youtube.tv", context);
                    if (installed) {
                        startActivity(YouTubeStandalonePlayer.createVideoIntent(this, mSessionManager.getkodiGrant(), film_trailer, 0, true, true));
                    } else {
                        Utils.installPlayerfromPlaystore(context, "Youtube", "com.google.android.youtube.tv");
                    }
                } else {
                    boolean installed = Utils.appInstalledOrNot("com.google.android.youtube", context);

                    if (installed) {
                        startActivity(YouTubeStandalonePlayer.createVideoIntent(this, mSessionManager.getkodiGrant(), film_trailer, 0, true, true));
                    } else {
                        Utils.installPlayerfromPlaystore(context, "Youtube", "com.google.android.youtube");
                    }
                }
                return;
            case R.id.tv_add_to_fav_2:
                if(!film_favorito_id.equals("") && !film_favorito_id.equals(null))
                {
                    escolhaFazFav = "remove_fav";
                    api_type = "favorito_rem="+film_favorito_id+"&tipo=filmes&mode="+escolhaFazFav;
                    new FetchFilmList().execute();
                }else{
                    escolhaFazFav = "insert_fav";
                    api_type = "utilizador="+mSessionManager.getUserID()+"&favorito="+film_id+"&tipo=filmes&mode="+escolhaFazFav;
                    new FetchFilmList().execute();
                }
                return;
            case R.id.tv_detail_back_btn:
                finish();
                return;
            case R.id.tv_play:
                FazCoisa(mSelectedMovie);
                return;
            case R.id.tv_readmore:
                showCastPopUp(this);
                return;
            case R.id.tv_readmore_genre:
                showGenrePopUp(this);
                return;
            default:
                return;
        }
    }

    public class FetchFilmList extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                programas_url = mSessionManager.getUserAcess_Token() + "PHP/liveit/filmeapp_favorite.php?" + api_type+"&client_secret=" + mSessionManager.getClient_Secret();
                JSONPar jsonPar = new JSONPar();
                jsonStr = jsonPar.makeHttpRequest4(programas_url, "GET");
                jsonFilmObject = null;
                jsonFilmObject = XML.toJSONObject(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonFilmObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonProgramList) {
            super.onPostExecute(jsonProgramList);
            Boolean chegou = false;
            try {
                JSONObject tmpObj = jsonProgramList.getJSONObject("item");
                if(escolhaFazFav.equals("insert_fav"))
                {
                    chegou = tmpObj.optBoolean("insert");
                }else{
                    chegou = tmpObj.optBoolean("remove");
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }

            if(chegou)
            {
                if(escolhaFazFav.equals("insert_fav"))
                {
                    Toast.makeText(context, "Filme adicionado aos Favoritos. Com Sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Filme removido dos Favoritos. Com Sucesso", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(escolhaFazFav.equals("insert_fav"))
                {
                    Toast.makeText(context, "Erro ao adicionar Filme aos Favoritos.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Erro ao remover Filme dos Favoritos.", Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        }
    }


    public class BuscaIDFavorito extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                programas_url = mSessionManager.getUserAcess_Token() + "PHP/liveit/filmeapp_favorite.php?tipo=filmes&utilizador="+mSessionManager.getUserID()+"&mode=get_fav&favorito="+ film_id+"&client_secret=" + mSessionManager.getClient_Secret();
                JSONPar jsonPar = new JSONPar();
                jsonStr = jsonPar.makeHttpRequest4(programas_url, "GET");
                jsonFilmObject = null;
                jsonFilmObject = XML.toJSONObject(jsonStr);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonFilmObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonProgramList) {
            super.onPostExecute(jsonProgramList);
            String chegou = "";
            try {
                JSONObject tmpObj = jsonProgramList.getJSONObject("item");
                chegou = tmpObj.optString("ID");
            } catch (Exception ee) {
                ee.printStackTrace();
            }

            if(!chegou.equals(""))
            {
                film_favorito_id = chegou;
                tvAddToFavBD.setText("Rem Favorito");
            }else{
                tvAddToFavBD.setText("Add Favorito");
                film_favorito_id = "";
            }

            vodInfo();
        }
    }

    String urlData1 = "";
    String urlData2 = "";
    String urlData3 = "";
    String urlData4 = "";
    String urlData5 = "";
    String urlData6 = "";
    private ArrayList<ListMoviesSetterGetter> list_items;
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
    String escolhaFazFav;
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
                builder2.setMessage("Erro no Endereço do Video. Tente outro Servidor 2."+programas_url);
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
            builder2.setMessage("Erro no Endereço do Video. Tente outro Servidor. "+programas_url);
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
}

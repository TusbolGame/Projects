package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.JSONPar;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.VodPresenter;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewDetailsActivitySer extends AppCompatActivity implements OnClickListener {
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
    private String film_name, film_temporadas, film_estado, film_favorito_id, film_id, film_favorito, film_fim, film_logo, film_desc, film_ano, film_player, film_categoria, film_actores, film_rating, film_imbd, film_director, film_trailer, film_name_en;
    private String num = "";
    String escolhaFazFav;
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
    @BindView(R.id.tv_rating_num)
    TextView tv_rating_num;
    @BindView(R.id.tv_release_date_info)
    TextView tvReleaseDateInfo;
    @BindView(R.id.tv_fim_info)
    TextView tvFim;
    @BindView(R.id.tv_estado_info)
    TextView tvEstado;
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
        setContentView(R.layout.activity_view_details_serapp);
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

    private void showCastPopUp(ViewDetailsActivitySer context) {
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

    private void showGenrePopUp(ViewDetailsActivitySer context) {
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

        this.film_temporadas = getIntent().getStringExtra("film_temporadas");
        this.film_estado = getIntent().getStringExtra("film_estado");
        this.film_fim = getIntent().getStringExtra("film_fim");
        this.film_favorito = getIntent().getStringExtra("film_favorito");
        this.film_id = getIntent().getStringExtra("film_id");

        this.tvPlay.setText("Temporadas");
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
        //overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
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

        if (this.tvEstado != null) {
            if(film_estado.equals("") || film_estado.equals(null))
            {
                this.tvEstado.setText("N/A");
            }else{
                this.tvEstado.setText(film_estado);
            }
        }

        if (this.tvFim != null) {
            if(film_fim.equals("") || film_fim.equals(null))
            {
                this.tvFim.setText("N/A");
            }else{
                this.tvFim.setText(film_fim);
            }
        }

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
                    api_type = "favorito_rem="+film_favorito_id+"&tipo=series&mode="+escolhaFazFav;
                    new FetchFilmList().execute();
                }else{
                    escolhaFazFav = "insert_fav";
                    api_type = "utilizador="+mSessionManager.getUserID()+"&favorito="+film_id+"&tipo=series&mode="+escolhaFazFav;
                    new FetchFilmList().execute();
                }
                return;
            case R.id.tv_detail_back_btn:
                finish();
                return;
            case R.id.tv_play:
                goTemporadasPage(mSelectedMovie);
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
    String api_type,programas_url;

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
                    Toast.makeText(context, "Série adicionada aos Favoritos. Com Sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Série removida dos Favoritos. Com Sucesso", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(escolhaFazFav.equals("insert_fav"))
                {
                    Toast.makeText(context, "Erro ao adicionar Série aos Favoritos.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Erro ao remover Série dos Favoritos.", Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        }
    }

    public class BuscaIDFavorito extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                programas_url = mSessionManager.getUserAcess_Token() + "PHP/liveit/filmeapp_favorite.php?tipo=series&utilizador="+mSessionManager.getUserID()+"&mode=get_fav&favorito="+ film_id+"&client_secret=" + mSessionManager.getClient_Secret();
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


    String Nome_s = "";
    String Image_s = "";
    String sel_temporada;
    String sel_videoID;
    CharSequence[] selTemporadaMenu = {"Temporada"};
    public void goTemporadasPage(ListMoviesSetterGetter selItem) {

        sel_temporada = "";
        sel_videoID = "";
        if(selItem == null)
            return;
        String temporadaStr  = selItem.getTemporadas();
        String seriesID  = selItem.getPraias_id();
        Nome_s = selItem.getPraias_name();
        Image_s = selItem.getPraias_imagem();
        if(!temporadaStr.equals(""))
        {
            sel_temporada = temporadaStr;
            int cnt = Integer.parseInt(sel_temporada);

            selTemporadaMenu = new CharSequence[cnt];
            for(int i = 0 ; i < cnt  ; i++)
            {
                selTemporadaMenu[i] =  "Temporada" + Integer.toString(i+1);
            }

            sel_videoID = seriesID;


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(selTemporadaMenu, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Intent i = new Intent(context, VodMenuFilmesAPP4SerEpisodesSelectActivity.class);
                    i.putExtra("selview" , "series");
                    i.putExtra("selnome" , Nome_s);
                    i.putExtra("selvideo" , sel_videoID);
                    i.putExtra("seltemporada" , Integer.toString(item +  1));
                    startActivity(i);
                }
            });
            builder.show();
        }
    }
}

package com.liveitandroid.liveit.view.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.LayoutParams;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.presenter.VodPresenter;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SeriesDetailActivity extends AppCompatActivity implements OnClickListener {
    int actionBarHeight;
    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    private String categoryId = "";
    private PopupWindow changeSortPopUp;
    private TextView clientNameTv;
    Button closedBT;
    private String containerExtension = "";
    @BindView(R.id.content_drawer)
    RelativeLayout contentDrawer;
    private Context context;
    private DatabaseHandler database;
    String fullCast;
    String fullGenre;
    @BindView(R.id.iv_favourite)
    ImageView ivFavourite;
    @BindView(R.id.iv_movie_image)
    ImageView ivMovieImage;
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
    private String movieName = "";
    private String num = "";
    private ProgressDialog progressDialog;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.rl_account_info)
    RelativeLayout rlAccountInfo;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private String selectedPlayer = "";
    private String seriesID = "";
    int seriesID_final;
    private String series_categoryID = "";
    private String series_cover = "";
    private String series_director = "";
    private String series_genre = "";
    private String series_youtube = "";
    private String series_plot = "";
    private String series_rating = "";
    private String series_releasedate = "";
    private String series_series_name = "";
    private int streamId = -1;
    private String streamType = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private TypedValue tv;
    @BindView(R.id.tv_account_info)
    TextView tvAccountInfo;
    @BindView(R.id.tv_add_to_fav)
    TextView tvAddToFav;
    @BindView(R.id.tv_add_to_fav_2)
    TextView tvtrailer;
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
    ImageView tvHeaderTitle;
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
    @BindView(R.id.tv_play_2)
    TextView tvPlay2;
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
    TextView tv_parental_password;
    @BindView(R.id.tv_detail_back_btn)
    TextView tvdetailbackbutton;
    @BindView(R.id.tv_detail_ProgressBar)
    ProgressBar tvdetailprogressbar;
    private String userName = "";
    private String userPassword = "";
    private VodPresenter vodPresenter;

    class C16951 implements DialogInterface.OnClickListener {
        C16951() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C16962 implements DialogInterface.OnClickListener {
        C16962() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.logoutUser(SeriesDetailActivity.this.context);
        }
    }

    class C16973 implements DialogInterface.OnClickListener {
        C16973() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVod(SeriesDetailActivity.this.context);
        }
    }

    class C16984 implements DialogInterface.OnClickListener {
        C16984() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C16995 implements DialogInterface.OnClickListener {
        C16995() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuid(SeriesDetailActivity.this.context);
        }
    }

    class C17006 implements DialogInterface.OnClickListener {
        C17006() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C17017 implements OnClickListener {
        C17017() {
        }

        public void onClick(View view) {
            SeriesDetailActivity.this.changeSortPopUp.dismiss();
        }
    }

    class C17028 implements OnClickListener {
        C17028() {
        }

        public void onClick(View view) {
            SeriesDetailActivity.this.changeSortPopUp.dismiss();
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
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_02);
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_03);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_02);
                }
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                if (this.view.getTag().equals("1")) {
                    this.view.setBackgroundResource(R.drawable.livetv_focused);
                } else if (this.view.getTag().equals("2")) {
                    this.view.setBackgroundResource(R.drawable.ondemand_focused);
                } else if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.catch_up_focused);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.livetv_focused);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.ondemand_focused);
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
        setContentView(R.layout.activity_series_detail);
        ButterKnife.bind(this);
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
        if (this.tvtrailer != null) {
            this.tvtrailer.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvtrailer));
        }
        if (this.tvdetailbackbutton != null) {
            this.tvdetailbackbutton.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvdetailbackbutton));
        }
        if (this.tvReadMore != null) {
            this.tvReadMore.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvReadMore));
        }
        if (this.tvReadMoreGenre != null) {
            this.tvReadMoreGenre.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.tvReadMoreGenre));
        }
        getWindow().setFlags(1024, 1024);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        mSessionManager = new SessionManager(this);
        initialize();
    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    private void initialize() {
        this.context = this;
        this.database = new DatabaseHandler(this.context);
        this.tvPlay.requestFocus();
        this.tvPlay.setFocusable(true);
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesAfterLogin.getString("username", "");
        String password = this.loginPreferencesAfterLogin.getString("password", "");
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            return;
        }
        startViewingDetails(this.context, username, password);
    }

    private void startViewingDetails(Context context, String username, String password) {
        Intent intetGetIntent = getIntent();
        if (intetGetIntent != null) {
            this.series_series_name = intetGetIntent.getStringExtra(AppConst.SERIES_NAME);
            this.series_plot = intetGetIntent.getStringExtra(AppConst.SERIES_PLOT);
            this.series_rating = intetGetIntent.getStringExtra(AppConst.SERIES_RATING);
            this.series_director = intetGetIntent.getStringExtra(AppConst.SERIES_DIRECTOR);
            this.series_cover = intetGetIntent.getStringExtra(AppConst.SERIES_COVER);
            this.series_releasedate = intetGetIntent.getStringExtra(AppConst.SERIES_RELEASE_DATE);
            this.series_genre = intetGetIntent.getStringExtra(AppConst.SERIES_GENERE);
            this.series_youtube = intetGetIntent.getStringExtra(AppConst.SERIES_Youtube);
            this.series_categoryID = intetGetIntent.getStringExtra(AppConst.SERIES_CATEGORY_ID);
            this.seriesID = intetGetIntent.getStringExtra(AppConst.SERIES_SERIES_ID);
            this.seriesID_final = Integer.parseInt(this.seriesID);
            this.fullCast = this.series_plot;
            this.fullGenre = this.series_genre;

            if (this.database.checkFavourite(this.seriesID_final, "-1", AppConst.SERIES).size() > 0) {
                this.tvAddToFav.setText(R.string.remove_from_favourite);
                this.ivFavourite.setVisibility(View.VISIBLE);
            } else {
                this.tvAddToFav.setText(R.string.add_to_favourite);
                this.ivFavourite.setVisibility(View.GONE);
            }

            vodInfo();
        }
    }

    public void vodInfo() {
        if(this.tvdetailprogressbar != null){
            this.tvdetailprogressbar.setVisibility(View.GONE);
        }
        if(this.scrollView != null){
            this.scrollView.setVisibility(View.VISIBLE);
        }

        if (!(context == null || this.series_cover == null || this.series_cover.isEmpty())) {
            Picasso.with(context).load(this.series_cover).placeholder((int) R.drawable.tranparentdark).into(this.ivMovieImage);
        }else{
            Picasso.with(context).load(R.drawable.tranparentdark).placeholder((int) R.drawable.tranparentdark).into(this.ivMovieImage);
        }

        if(series_youtube != null){
            if(series_youtube.equals(""))
            {
                tvtrailer.setVisibility(View.GONE);
            }else{
                tvtrailer.setVisibility(View.VISIBLE);
            }
        }else{
            tvtrailer.setVisibility(View.GONE);
        }

        if (!(this.movieName == null || this.tvMovieName == null)) {
            this.tvMovieName.setText(this.series_series_name);
        }

        if (this.llReleasedBox == null || this.llReleasedBoxInfo == null || this.tvReleaseDateInfo == null || this.series_releasedate == null || this.series_releasedate.isEmpty() || this.series_releasedate.equals("n/A")) {
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
            this.tvReleaseDateInfo.setText(this.series_releasedate);
        }
        if (this.tvDirectorInfo == null || this.llDirectorBoxInfo == null || this.llDirectorBox == null || this.series_director == null || this.series_director.isEmpty() || this.series_director.equals("n/A")) {
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
            this.tvDirectorInfo.setText(this.series_director);
        }
        if (this.llCastBox == null || this.llCastBoxInfo == null || this.tvCastInfo == null || this.series_plot == null || this.series_plot.isEmpty()) {
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
            if (this.series_plot.length() > 150) {
                readMore = true;
            }
            if (readMore) {
                this.tvCastInfo.setText(this.series_plot);
                this.tvReadMore.setVisibility(0);
            } else {
                this.tvCastInfo.setText(this.series_plot);
                this.tvReadMore.setVisibility(8);
            }
        }
        if (!(this.ratingBar == null || this.series_rating == null || this.series_rating.isEmpty() || this.series_rating.equals("n/A"))) {
            this.ratingBar.setVisibility(0);
            try {
                this.ratingBar.setRating(Float.parseFloat(this.series_rating) / 2.0f);
            } catch (NumberFormatException e) {
                this.ratingBar.setRating(0.0f);
            }
        }
        if (this.llGenreBox == null || this.llGenreBoxInfo == null || this.tv_genre_info == null || this.series_genre == null || this.series_genre.isEmpty()) {
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
                return;
            }
            return;
        }
        this.llGenreBox.setVisibility(0);
        this.llGenreBoxInfo.setVisibility(0);
        boolean readMoreGenre = false;
        if (this.series_genre.length() > 40) {
            readMoreGenre = true;
        }
        if (readMoreGenre) {
            this.tv_genre_info.setText(this.series_genre);
            this.tvReadMoreGenre.setVisibility(0);
            return;
        }
        this.tv_genre_info.setText(this.series_genre);
        this.tvReadMoreGenre.setVisibility(8);
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

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getUserName() {
        if (this.context == null) {
            return this.userName;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("username", "");
    }

    public String getUserPassword() {
        if (this.context == null) {
            return this.userPassword;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("password", "");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_text_icon);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(16843499, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        for (int i = 0; i < this.toolbar.getChildCount(); i++) {
            if (this.toolbar.getChildAt(i) instanceof ActionMenuView) {
                ((LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
            }
        }
        return true;
    }
    private SessionManager mSessionManager;
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if(mSessionManager.getFilmesAPP().equals("4") || mSessionManager.getFilmesAPP().equals(4)){
                startActivity(new Intent(this.context, NewDashboardActivity.class));
            }else{
                startActivity(new Intent(this.context, NewDashboardActivity2.class));
            }
            finish();
        }
        if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }
        if (id == R.id.action_logout && this.context != null) {
            new Builder(this.context, R.style.AlertDialogCustom).setTitle(getResources().getString(R.string.logout_title)).setMessage(getResources().getString(R.string.logout_message)).setPositiveButton(17039379, new C16962()).setNegativeButton(17039369, new C16951()).show();
        }
        if (id == R.id.menu_load_channels_vod) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C16973());
            alertDialog.setNegativeButton("Não", new C16984());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide) {
            Builder alertDialog = new Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C16995());
            alertDialog.setNegativeButton("Não", new C17006());
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTvGuid() {
        if (this.context != null) {
            SharedPreferences loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            Editor loginPrefsEditor = loginPreferencesAfterLogin.edit();
            if (loginPrefsEditor != null) {
                loginPrefsEditor.putString(AppConst.SKIP_BUTTON_PREF, "autoLoad");
                loginPrefsEditor.commit();
                String skipButton = loginPreferencesAfterLogin.getString(AppConst.SKIP_BUTTON_PREF, "");
                new LiveStreamDBHandler(this.context).makeEmptyEPG();
                startActivity(new Intent(this.context, ImportEPGActivity.class));
            }
        }
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

    public void onResume() {
        super.onResume();
        this.loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        if (this.loginPreferencesAfterLogin.getString("username", "").equals("") && this.loginPreferencesAfterLogin.getString("password", "").equals("")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (this.context != null) {
            onFinish();
        }
    }

    public void onFinish() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }

    public void onFailed(String errorMessage) {
        if (this.tvdetailprogressbar != null) {
            this.tvdetailprogressbar.setVisibility(8);
        }
    }

    @OnClick({R.id.tv_play, R.id.tv_play_2, R.id.tv_add_to_fav, R.id.tv_add_to_fav_2, R.id.tv_detail_back_btn, R.id.tv_readmore, R.id.tv_readmore_genre})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add_to_fav:
                ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(seriesID_final, "-1", AppConst.SERIES);
                if (checkFavourite == null || checkFavourite.size() <= 0) {
                    addToFavourite();
                    return;
                } else {
                    removeFromFavourite();
                    return;
                }
            case R.id.tv_add_to_fav_2:
                boolean installed = Utils.appInstalledOrNot("com.google.android.youtube", context);
                if (installed) {
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(this, mSessionManager.getkodiGrant(), series_youtube, 0, true, true));
                } else {
                    Utils.installPlayerfromPlaystore(context, "Youtube", "com.google.android.youtube");
                }
                return;
            case R.id.tv_detail_back_btn:
                finish();
                return;
            case R.id.tv_play:
                startActivity(new Intent(this, EpisodeDetailActivity.class).putExtra(AppConst.SERIES_SERIES_ID, this.seriesID).putExtra(AppConst.SERIES_COVER, this.series_cover).putExtra(AppConst.SERIES_NAME, this.series_series_name));
                return;
            case R.id.tv_play_2:
                startActivity(new Intent(this, SeasonsActivitiy.class).putExtra(AppConst.SERIES_SERIES_ID, this.seriesID).putExtra(AppConst.SERIES_COVER, this.series_cover).putExtra(AppConst.SERIES_NAME, this.series_series_name));
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

    private void addToFavourite() {
        FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel();
        LiveStreamsFavourite.setCategoryID("-1");
        LiveStreamsFavourite.setStreamID(seriesID_final);
        this.database.addToFavourite(LiveStreamsFavourite, AppConst.SERIES);
        this.tvAddToFav.setText(R.string.remove_from_favourite);
        this.ivFavourite.setVisibility(View.VISIBLE);
    }

    private void removeFromFavourite() {
        this.database.deleteFavourite(seriesID_final, "-1", AppConst.SERIES);
        this.tvAddToFav.setText(R.string.add_to_favourite);
        this.ivFavourite.setVisibility(View.GONE);
    }

    private void showCastPopUp(SeriesDetailActivity context) {
        View layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_cast_details, (RelativeLayout) context.findViewById(R.id.rl_password_verification));
        this.tvCastInfoPopUp = (TextView) layout.findViewById(R.id.tv_casts_info_popup);
        this.tv_parental_password = (TextView) layout.findViewById(R.id.tv_parental_password);
        this.tv_parental_password.setText("Plot");
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
        this.closedBT.setOnClickListener(new C17017());
    }

    private void showGenrePopUp(SeriesDetailActivity context) {
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
        this.closedBT.setOnClickListener(new C17028());
    }
}

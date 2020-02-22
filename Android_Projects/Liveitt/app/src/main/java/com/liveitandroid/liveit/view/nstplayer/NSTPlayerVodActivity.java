package com.liveitandroid.liveit.view.nstplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.view.adapter.MoviesOnVideoAdapter;
import com.liveitandroid.liveit.view.adapter.SearchableAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class NSTPlayerVodActivity extends AppCompatActivity implements OnClickListener, OnNavigationItemSelectedListener {
    MoviesOnVideoAdapter MoviesOnVideoAdapter;
    String Xtream_Version;
    public Activity activity;
    SearchableAdapter adapter;
    public ArrayList<LiveStreamsDBModel> allMovies;
    private ArrayList<LiveStreamCategoryIdDBModel> allMoviesCategories;
    AppBarLayout appbarToolbar;
    public View aspectRatio;
    private AppCompatImageView btn_cat_back;
    private AppCompatImageView btn_cat_forward;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    public View channelListButton;
    public Context context;
    private int currentCategoryIndex = 0;
    public int currentWindowIndex = 0;
    public long defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    public EditText et_search;
    private ArrayList<LiveStreamsDBModel> filterList;
    public View forwardButton;
    public boolean fullScreenOnly = true;
    private GridLayoutManager gridLayoutManager;
    public GridView listChannels;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    LiveStreamDBHandler liveStreamDBHandler;
    public LinearLayout ll_categories_view;
    private SharedPreferences loginPreferencesSharedPref;
    public String mFilePath;
    SearchableAdapter mSearchableAdapter;
    private ArrayList<LiveStreamsDBModel> movieListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> movieListDetailUnlckedDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> moviesListDetailAvailable;
    RecyclerView myRecyclerView;
    public View nextButton;
    public View pauseButton;
    ProgressBar pbLoader;
    public View playButton;
    NSTPlayerVod player;
    public View prevButton;
    public View rewindButton;
    public RelativeLayout rl_middle;
    RelativeLayout rl_settings;
    public String scaleType;
    SearchView searchView;
    public boolean showNavIcon = true;
    public String title;
    Toolbar toolbar;
    TextView tvNoRecordFound;
    TextView tvNoStream;
    public TextView tv_categories_view;
    public String url;

    class C20791 implements OnQueryTextListener {
        C20791() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            NSTPlayerVodActivity.this.tvNoRecordFound.setVisibility(8);
            if (!(NSTPlayerVodActivity.this.MoviesOnVideoAdapter == null || NSTPlayerVodActivity.this.tvNoStream == null || NSTPlayerVodActivity.this.tvNoStream.getVisibility() == 0)) {
                NSTPlayerVodActivity.this.MoviesOnVideoAdapter.filter(newText, NSTPlayerVodActivity.this.tvNoRecordFound);
            }
            return false;
        }
    }

    class C20802 implements OnCloseListener {
        C20802() {
        }

        public boolean onClose() {
            NSTPlayerVodActivity.this.searchView.onActionViewCollapsed();
            NSTPlayerVodActivity.this.rl_settings.setVisibility(0);
            NSTPlayerVodActivity.this.appbarToolbar.requestFocusFromTouch();
            EditText searchEditText = (EditText) NSTPlayerVodActivity.this.searchView.findViewById(R.id.search_src_text);
            if (searchEditText != null) {
                searchEditText.setImeOptions(268435456);
            }
            return false;
        }
    }

    class C20813 implements OnClickListener {
        C20813() {
        }

        public void onClick(View view) {
            NSTPlayerVodActivity.this.hideTitleBarAndFooter();
            NSTPlayerVodActivity.this.rl_settings.setVisibility(8);
            EditText searchEditText = (EditText) NSTPlayerVodActivity.this.searchView.findViewById(R.id.search_src_text);
            if (searchEditText != null) {
                searchEditText.setImeOptions(268435456);
            }
        }
    }

    public static class Config implements Parcelable {
        public static final Creator<Config> CREATOR = new C20821();
        private static boolean debug = true;
        private Activity activity;
        private long defaultRetryTime;
        private boolean fullScreenOnly;
        private String scaleType;
        private boolean showNavIcon;
        private String title;
        private String url;

        static class C20821 implements Creator<Config> {
            C20821() {
            }

            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            public Config[] newArray(int size) {
                return new Config[size];
            }
        }

        public Config(Activity activity) {
            this.fullScreenOnly = true;
            this.defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
            this.showNavIcon = true;
            this.activity = activity;
        }

        private Config(Parcel in) {
            boolean z = true;
            this.fullScreenOnly = true;
            this.defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
            this.showNavIcon = true;
            this.scaleType = in.readString();
            this.fullScreenOnly = in.readByte() != (byte) 0;
            this.defaultRetryTime = in.readLong();
            this.title = in.readString();
            this.url = in.readString();
            if (in.readByte() == (byte) 0) {
                z = false;
            }
            this.showNavIcon = z;
        }

        public static boolean isDebug() {
            return debug;
        }

        public Config debug(boolean debug) {
            debug = debug;
            return this;
        }

        public Config setTitle(String title) {
            this.title = title;
            return this;
        }

        public Config setDefaultRetryTime(long defaultRetryTime) {
            this.defaultRetryTime = defaultRetryTime;
            return this;
        }

        public void play(String url) {
            this.url = url;
            Intent intent = new Intent(this.activity, NSTPlayerVodActivity.class);
            intent.putExtra("config", this);
            this.activity.startActivity(intent);
        }

        public Config setScaleType(String scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Config setFullScreenOnly(boolean fullScreenOnly) {
            this.fullScreenOnly = fullScreenOnly;
            return this;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i = 1;
            dest.writeString(this.scaleType);
            dest.writeByte((byte) (this.fullScreenOnly ? 1 : 0));
            dest.writeLong(this.defaultRetryTime);
            dest.writeString(this.title);
            dest.writeString(this.url);
            if (!this.showNavIcon) {
                i = 0;
            }
            dest.writeByte((byte) i);
        }
    }

    public static void play(Activity context, String... url) {
        Intent intent = new Intent(context, NSTPlayerVodActivity.class);
        intent.putExtra(OnNativeInvokeListener.ARG_URL, url[0]);
        if (url.length > 1) {
            intent.putExtra("title", url[1]);
        }
        context.startActivity(intent);
    }

    public static Config configPlayer(Activity activity) {
        return new Config(activity);
    }

    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nst_player_vod_activity);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_video_id = getIntent().getIntExtra("VIDEO_ID", 0);
        int currentWindowIndex = getIntent().getIntExtra("VIDEO_NUM", 0);
        String videoTitle = getIntent().getStringExtra("VIDEO_TITLE");
        String videoExtension = getIntent().getStringExtra("EXTENSION_TYPE");

        boolean playRecording = getIntent().getBooleanExtra("PLAY_RECORDING", false);
        if(playRecording) {
            this.mFilePath = getIntent().getStringExtra("FILE_PATH");
        }
        else
            this.mFilePath = "http://" + serverUrl + ":" + serverPort + "/movie/" + username + "/" + password + "/";

        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.categoryWithPasword = new ArrayList();
        this.movieListDetailUnlcked = new ArrayList();
        this.movieListDetailUnlckedDetail = new ArrayList();
        ArrayList<LiveStreamsDBModel> vodAvailable = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "movie");
        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || vodAvailable == null) {
            this.allMovies = vodAvailable;
        } else {
            this.listPassword = getPasswordSetCategories();
            if (this.listPassword != null) {
                this.movieListDetailUnlckedDetail = getUnlockedCategoriesAll(vodAvailable, this.listPassword);
            }
            this.allMovies = this.movieListDetailUnlckedDetail;
        }
        currentWindowIndex = getIndexOfMovies(this.allMovies, currentWindowIndex);
        getIntent().putExtra("VIDEO_NUM", currentWindowIndex);
        this.myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        this.pbLoader = (ProgressBar) findViewById(R.id.pb_loader);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.tvNoStream = (TextView) findViewById(R.id.tv_noStream);
        this.tvNoRecordFound = (TextView) findViewById(R.id.tv_no_record_found);
        this.appbarToolbar = (AppBarLayout) findViewById(R.id.appbar_toolbar);
        this.rl_settings = (RelativeLayout) findViewById(R.id.rl_settings);
        this.toolbar.inflateMenu(R.menu.menu_search_only);
        this.appbarToolbar.requestFocusFromTouch();
        this.player = new NSTPlayerVod(this);
        this.player.setCurrentWindowIndex(currentWindowIndex);
        if (videoTitle.length() > 16 && this.appbarToolbar.getVisibility() == 0) {
            videoTitle = videoTitle.substring(0, Math.min(videoTitle.length(), 16)) + "...";
        }
        this.player.setTitle(videoTitle);
        this.player.setDefaultRetryTime(this.defaultRetryTime);
        this.player.setFullScreenOnly(this.fullScreenOnly);
        this.player.setShowNavIcon(this.showNavIcon);
        this.player.showAll();

        if(playRecording)
            this.player.play(this.mFilePath);
        else
            this.player.play(this.mFilePath, opened_video_id, videoExtension);

        this.player.fullScreenRatio();
        this.player.hideSystemUi();
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        this.listChannels = (GridView) findViewById(R.id.lv_ch);
        this.et_search = (EditText) findViewById(R.id.et_search);
        this.ll_categories_view = (LinearLayout) findViewById(R.id.ll_categories_view);
        this.playButton = findViewById(R.id.exo_play);
        if (this.playButton != null) {
            this.playButton.setOnClickListener(this);
        }
        this.pauseButton = findViewById(R.id.exo_pause);
        if (this.pauseButton != null) {
            this.pauseButton.setOnClickListener(this);
        }
        this.forwardButton = findViewById(R.id.exo_ffwd);
        if (this.forwardButton != null) {
            this.forwardButton.setOnClickListener(this);
        }
        this.rewindButton = findViewById(R.id.exo_rew);
        if (this.rewindButton != null) {
            this.rewindButton.setOnClickListener(this);
        }
        this.prevButton = findViewById(R.id.exo_prev);
        if (this.prevButton != null) {
            this.prevButton.setOnClickListener(this);
        }
        this.nextButton = findViewById(R.id.exo_next);
        if (this.nextButton != null) {
            this.nextButton.setOnClickListener(this);
        }
        this.channelListButton = findViewById(R.id.btn_list);
        if (this.channelListButton != null) {
            this.channelListButton.setOnClickListener(this);
        }
        this.tv_categories_view = (TextView) findViewById(R.id.tv_categories_view);
        this.btn_cat_back = (AppCompatImageView) findViewById(R.id.btn_category_back);
        this.btn_cat_forward = (AppCompatImageView) findViewById(R.id.btn_category_fwd);
        this.btn_cat_back.setOnClickListener(this);
        this.btn_cat_forward.setOnClickListener(this);
        this.rl_middle = (RelativeLayout) findViewById(R.id.middle);
        this.aspectRatio = findViewById(R.id.btn_aspect_ratio);
        if (this.aspectRatio != null) {
            this.aspectRatio.setOnClickListener(this);
        }
        this.tv_categories_view.setText(getResources().getString(R.string.all));
        this.allMoviesCategories = this.liveStreamDBHandler.getAllMovieCategories();
        LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel();
        liveStream.setLiveStreamCategoryID(AppConst.PASSWORD_UNSET);
        liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));
        if (this.liveStreamDBHandler.getParentalStatusCount() <= 0 || this.allMoviesCategories == null) {
            this.liveListDetail.add(0, liveStream);
            this.liveListDetailAvailable = this.allMoviesCategories;
        } else {
            this.listPassword = getPasswordSetCategories();
            this.liveListDetailUnlckedDetail = getUnlockedCategories(this.allMoviesCategories, this.listPassword);
            this.liveListDetailUnlcked.add(0, liveStream);
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        }
        if (this.allMoviesCategories != null) {
            this.liveListDetailAvailable = this.liveListDetailAvailable;
        }
        if (this.allMovies != null) {
            setVodListAdapterNew(this.allMovies);
        }
    }

    private ArrayList<LiveStreamCategoryIdDBModel> getUnlockedCategories(ArrayList<LiveStreamCategoryIdDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamCategoryIdDBModel user1 = (LiveStreamCategoryIdDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getLiveStreamCategoryID().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.liveListDetailUnlcked.add(user1);
            }
        }
        return this.liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        this.categoryWithPasword = this.liveStreamDBHandler.getAllPasswordStatus();
        if (this.categoryWithPasword != null) {
            Iterator it = this.categoryWithPasword.iterator();
            while (it.hasNext()) {
                PasswordStatusDBModel listItemLocked = (PasswordStatusDBModel) it.next();
                if (listItemLocked.getPasswordStatus().equals("1")) {
                    this.listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return this.listPassword;
    }

    private ArrayList<LiveStreamsDBModel> getUnlockedCategoriesAll(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
        Iterator it = liveListDetail.iterator();
        while (it.hasNext()) {
            LiveStreamsDBModel user1 = (LiveStreamsDBModel) it.next();
            boolean flag = false;
            Iterator it2 = listPassword.iterator();
            while (it2.hasNext()) {
                if (user1.getCategoryId().equals((String) it2.next())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.movieListDetailUnlcked.add(user1);
            }
        }
        return this.movieListDetailUnlcked;
    }

    public void setVodListAdapterNew(ArrayList<LiveStreamsDBModel> allStreams) {
        int positionToSelect = getIntent().getIntExtra("VIDEO_NUM", 0);
        this.MoviesOnVideoAdapter = new MoviesOnVideoAdapter(allStreams, this.context);
        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            this.gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            this.gridLayoutManager = new GridLayoutManager(this, 2);
        }
        this.myRecyclerView.setLayoutManager(this.gridLayoutManager);
        this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.myRecyclerView.setAdapter(this.MoviesOnVideoAdapter);
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
        this.searchView = (SearchView) MenuItemCompat.getActionView(this.toolbar.getMenu().findItem(R.id.action_search));
        this.searchView.setQueryHint(getResources().getString(R.string.search_vod_category));
        EditText searchEditText = (EditText) this.searchView.findViewById(R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setImeOptions(268435456);
        }
        this.searchView.setOnQueryTextListener(new C20791());
        this.searchView.setOnCloseListener(new C20802());
        this.searchView.setOnSearchClickListener(new C20813());
    }

    public void onClickCalled(LiveStreamsDBModel data) {
        if (data != null) {
            showTitleBarAndFooter();
            int num = Integer.parseInt(data.getNum());
            String getContainerExtension = data.getContaiinerExtension();
            this.player.setTitle(data.getName());
            this.player.setCurrentWindowIndex(getIndexOfMovies(this.allMovies, num));
            this.player.play(this.mFilePath, Integer.parseInt(data.getStreamId()), getContainerExtension);
            if (this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) {
                this.appbarToolbar.setVisibility(8);
            }
            this.player.hideSystemUi();
        }
    }

    public void backbutton() {
    }

    public void nextbutton() {
    }

    public int getIndexOfMovies(ArrayList<LiveStreamsDBModel> allMovies, int num) {
        for (int i = 0; i < allMovies.size(); i++) {
            if (Integer.parseInt(((LiveStreamsDBModel) allMovies.get(i)).getNum()) == num) {
                return i;
            }
        }
        return 0;
    }

    protected void onPause() {
        super.onPause();
        if (this.player != null) {
            this.player.onPause();
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.player != null) {
            this.player.hideSystemUi();
            this.player.onResume();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.player != null) {
            this.player.onDestroy();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.player != null) {
            this.player.onConfigurationChanged(newConfig);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 19:
                if (this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) {
                    return true;
                }
                showTitleBarAndFooter();
                findViewById(R.id.exo_next).performClick();
                return true;
            case 20:
                if (this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) {
                    return true;
                }
                showTitleBarAndFooter();
                findViewById(R.id.exo_prev).performClick();
                return true;
            case 21:
            case 89:
                if ((this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) || this.player == null) {
                    return true;
                }
                this.player.forward(-0.2f);
                return true;
            case 22:
            case 90:
                if ((this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) || this.player == null) {
                    return true;
                }
                this.player.forward(0.2f);
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean uniqueDown = event.getRepeatCount() == 0;
        switch (keyCode) {
            case 23:
                if (this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) {
                    return true;
                }
                if (findViewById(R.id.app_video_top_box).getVisibility() == 0) {
                    hideTitleBarAndFooter();
                    return true;
                }
                showTitleBarAndFooter();
                if (this.player == null) {
                    return true;
                }
                this.player.showAll();
                return true;
            case 62:
            case 79:
            case 85:
                if (!uniqueDown) {
                    return true;
                }
                showTitleBarAndFooter();
                this.player.doPauseResume();
                return true;
            case 66:
                if (this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) {
                    return true;
                }
                if (findViewById(R.id.app_video_top_box).getVisibility() == 0) {
                    hideTitleBarAndFooter();
                    return true;
                }
                showTitleBarAndFooter();
                if (this.player == null) {
                    return true;
                }
                this.player.showAll();
                return true;
            case 86:
            case 127:
                if (!uniqueDown || !this.player.isPlaying()) {
                    return true;
                }
                showTitleBarAndFooter();
                this.player.pause();
                playerPauseIconsUpdate();
                return true;
            case 126:
                if (!uniqueDown || this.player.isPlaying()) {
                    return true;
                }
                showTitleBarAndFooter();
                this.player.start();
                playerStartIconsUpdate();
                return true;
            case 166:
                showTitleBarAndFooter();
                findViewById(R.id.exo_next).performClick();
                return true;
            case 167:
                showTitleBarAndFooter();
                findViewById(R.id.exo_prev).performClick();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void showTitleBarAndFooter() {
        findViewById(R.id.app_video_top_box).setVisibility(0);
        findViewById(R.id.app_video_bottom_box).setVisibility(0);
        findViewById(R.id.app_video_currentTime).setVisibility(0);
        findViewById(R.id.app_video_endTime).setVisibility(0);
        findViewById(R.id.app_video_seekBar).setVisibility(0);
        findViewById(R.id.controls).setVisibility(0);
    }

    public void hideTitleBarAndFooter() {
        findViewById(R.id.app_video_bottom_box).setVisibility(8);
        findViewById(R.id.app_video_currentTime).setVisibility(8);
        findViewById(R.id.app_video_endTime).setVisibility(8);
        findViewById(R.id.app_video_seekBar).setVisibility(8);
        findViewById(R.id.app_video_top_box).setVisibility(8);
        findViewById(R.id.controls).setVisibility(8);
    }

    private void playerStartIconsUpdate() {
        this.playButton.setVisibility(8);
        this.pauseButton.setVisibility(0);
    }

    private void playerPauseIconsUpdate() {
        this.pauseButton.setVisibility(8);
        this.playButton.setVisibility(0);
    }

    public void onClick(View view) {
        String videoTitle;
        switch (view.getId()) {
            case R.id.btn_aspect_ratio:
                this.player.toggleAspectRatio();
                return;
            case R.id.btn_list:
                if (this.appbarToolbar != null) {
                    toggleView(this.appbarToolbar);
                    this.appbarToolbar.requestFocusFromTouch();
                    return;
                }
                return;
            case R.id.exo_ffwd:
                if (this.player != null) {
                    this.player.forward(0.2f);
                    return;
                }
                return;
            case R.id.exo_next:
                if (this.player != null) {
                    next();
                    int indexNext = this.player.getCurrentWindowIndex();
                    if (indexNext <= this.allMovies.size() - 1) {
                        videoTitle = ((LiveStreamsDBModel) this.allMovies.get(indexNext)).getName();
                        if (videoTitle.length() > 16 && this.appbarToolbar.getVisibility() == 0) {
                            videoTitle = videoTitle.substring(0, Math.min(videoTitle.length(), 16)) + "...";
                        }
                        this.player.setTitle(videoTitle);
                        this.player.play(this.mFilePath, Integer.parseInt(((LiveStreamsDBModel) this.allMovies.get(indexNext)).getStreamId()), ((LiveStreamsDBModel) this.allMovies.get(indexNext)).getContaiinerExtension());
                        return;
                    }
                    return;
                }
                return;
            case R.id.exo_pause:
                if (this.player != null && this.pauseButton != null) {
                    this.player.pause();
                    playerPauseIconsUpdate();
                    return;
                }
                return;
            case R.id.exo_play:
                if (this.player != null && this.playButton != null) {
                    this.player.start();
                    playerStartIconsUpdate();
                    return;
                }
                return;
            case R.id.exo_prev:
                if (this.player != null) {
                    previous();
                    int indexPrev = this.player.getCurrentWindowIndex();
                    if (indexPrev <= this.allMovies.size() - 1) {
                        videoTitle = ((LiveStreamsDBModel) this.allMovies.get(indexPrev)).getName();
                        if (videoTitle.length() > 16 && this.appbarToolbar.getVisibility() == 0) {
                            videoTitle = videoTitle.substring(0, Math.min(videoTitle.length(), 16)) + "...";
                        }
                        this.player.setTitle(videoTitle);
                        this.player.play(this.mFilePath, Integer.parseInt(((LiveStreamsDBModel) this.allMovies.get(indexPrev)).getStreamId()), ((LiveStreamsDBModel) this.allMovies.get(indexPrev)).getContaiinerExtension());
                        return;
                    }
                    return;
                }
                return;
            case R.id.exo_rew:
                if (this.player != null) {
                    this.player.forward(-0.2f);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void toggleView(View view) {
        if (view.getVisibility() == 8) {
            view.setVisibility(0);
        } else if (view.getVisibility() == 0) {
            view.setVisibility(8);
        }
    }

    private void previous() {
        int currentWindowIndex = this.player.getCurrentWindowIndex();
        if (currentWindowIndex == 0) {
            this.player.setCurrentWindowIndex(this.allMovies.size() - 1);
        } else {
            this.player.setCurrentWindowIndex(currentWindowIndex - 1);
        }
    }

    private void next() {
        int currentWindowIndex = this.player.getCurrentWindowIndex();
        if (currentWindowIndex == this.allMovies.size() - 1) {
            this.player.setCurrentWindowIndex(0);
        } else {
            this.player.setCurrentWindowIndex(currentWindowIndex + 1);
        }
    }

    public void onBackPressed() {
        if (findViewById(R.id.app_video_top_box).getVisibility() == 0) {
            hideTitleBarAndFooter();
        } else if (this.player == null || !this.player.onBackPressed()) {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

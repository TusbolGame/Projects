package com.liveitandroid.liveit.view.nstplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Build.VERSION;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.model.pojo.XMLTVProgrammePojo;
import com.liveitandroid.liveit.view.adapter.ChannelsOnVideoAdapter;
import com.liveitandroid.liveit.view.adapter.SearchableAdapter;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class NSTPlayerActivity extends AppCompatActivity implements OnClickListener, OnNavigationItemSelectedListener {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private ArrayList<LiveStreamsDBModel> AvailableChannelsFirstOpenedCat;
    public Activity activity;
    SearchableAdapter adapter;
    private ArrayList<LiveStreamCategoryIdDBModel> allLiveCategories;
    public ArrayList<LiveStreamsDBModel> allStreams;
    public ArrayList<LiveStreamsDBModel> allStreams_with_cat;
    public String allowedFormat;
    AppBarLayout appbarToolbar;
    public View aspectRatio;
    private AppCompatImageView btn_cat_back;
    private AppCompatImageView btn_cat_forward;
    String catID = "";
    String catName = "";
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    public View channelListButton;
    public ImageView channelLogo;
    ChannelsOnVideoAdapter channelsOnVideoAdapter;
    public Context context;
    private int currentCategoryIndex = 0;
    public TextView currentProgram;
    public TextView currentProgramTime;
    public long defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    public EditText et_search;
    public View forwardButton;
    public boolean fullScreenOnly = true;
    private GridLayoutManager gridLayoutManager;
    public boolean hideEPGData = true;
    public GridView listChannels;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableChannels;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableNewChannels;
    private ArrayList<LiveStreamsDBModel> liveListDetailChannels;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedChannels;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetailChannels;
    LiveStreamDBHandler liveStreamDBHandler;
    public LinearLayout ll_categories_view;
    public LinearLayout ll_seekbar_time;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
    private SharedPreferences loginPreferencesSharedPref_opened_video;
    public String mFilePath;
    RecyclerView myRecyclerView;
    public View nextButton;
    public TextView nextProgram;
    public TextView nextProgramTime;
    public View pauseButton;
    ProgressBar pbLoader;
    public View playButton;
    NSTPlayer player;
    public View prevButton;
    private SimpleDateFormat programTimeFormat;
    ProgressBar progressBar;
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

    class C20191 implements OnQueryTextListener {
        C20191() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            NSTPlayerActivity.this.tvNoRecordFound.setVisibility(8);
            if (!(NSTPlayerActivity.this.channelsOnVideoAdapter == null || NSTPlayerActivity.this.tvNoStream == null || NSTPlayerActivity.this.tvNoStream.getVisibility() == 0)) {
                NSTPlayerActivity.this.channelsOnVideoAdapter.filter(newText, NSTPlayerActivity.this.tvNoRecordFound);
            }
            return false;
        }
    }

    class C20202 implements OnCloseListener {
        C20202() {
        }

        public boolean onClose() {
            NSTPlayerActivity.this.searchView.onActionViewCollapsed();
            NSTPlayerActivity.this.rl_settings.setVisibility(0);
            NSTPlayerActivity.this.appbarToolbar.requestFocusFromTouch();
            EditText searchEditText = (EditText) NSTPlayerActivity.this.searchView.findViewById(R.id.search_src_text);
            if (searchEditText != null) {
                searchEditText.setImeOptions(268435456);
            }
            return false;
        }
    }

    class C20213 implements OnClickListener {
        C20213() {
        }

        public void onClick(View view) {
            NSTPlayerActivity.this.hideTitleBarAndFooter();
            NSTPlayerActivity.this.rl_settings.setVisibility(8);
            EditText searchEditText = (EditText) NSTPlayerActivity.this.searchView.findViewById(R.id.search_src_text);
            if (searchEditText != null) {
                searchEditText.setImeOptions(268435456);
            }
        }
    }

    public static class Config implements Parcelable {
        public static final Creator<Config> CREATOR = new C20221();
        private static boolean debug = true;
        private Activity activity;
        private long defaultRetryTime;
        private boolean fullScreenOnly;
        private String scaleType;
        private boolean showNavIcon;
        private String title;
        private String url;

        static class C20221 implements Creator<Config> {
            C20221() {
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
            Intent intent = new Intent(this.activity, NSTPlayerActivity.class);
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
        Intent intent = new Intent(context, NSTPlayerActivity.class);
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
        setContentView(R.layout.nst_player_activity);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        Log.d("TmpLogs", "This is " + getLocalClassName());
        this.context = this;
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        this.loginPreferencesSharedPref_opened_video = this.context.getSharedPreferences(AppConst.LOGIN_PREF_OPENED_VIDEO, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        this.allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_stream_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        int currentWindowIndex = getIntent().getIntExtra("VIDEO_NUM", 0);
        int num = currentWindowIndex;
        String streamType = getIntent().getStringExtra("STREAM_TYPE");
        String videoTitle = getIntent().getStringExtra("VIDEO_TITLE");
        String epgChannelID = getIntent().getStringExtra("EPG_CHANNEL_ID");
        String epgChannelLogo = getIntent().getStringExtra("EPG_CHANNEL_LOGO");
        Editor editor = this.loginPreferencesSharedPref_opened_video.edit();
        editor.putInt(AppConst.LOGIN_PREF_OPENED_VIDEO_ID, opened_stream_id);
        editor.apply();
        loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        this.programTimeFormat = new SimpleDateFormat(loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""));
        this.mFilePath = "http://" + serverUrl + ":" + serverPort + "/live/" + username + "/" + password + "/";
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "live");
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.ll_seekbar_time = (LinearLayout) findViewById(R.id.ll_seekbar_time);
        this.channelLogo = (ImageView) findViewById(R.id.iv_channel_logo);
        this.currentProgram = (TextView) findViewById(R.id.tv_current_program);
        this.currentProgramTime = (TextView) findViewById(R.id.tv_current_time);
        this.nextProgram = (TextView) findViewById(R.id.tv_next_program);
        this.nextProgramTime = (TextView) findViewById(R.id.tv_next_program_time);
        this.btn_cat_back = (AppCompatImageView) findViewById(R.id.btn_category_back);
        this.btn_cat_forward = (AppCompatImageView) findViewById(R.id.btn_category_fwd);
        this.tv_categories_view = (TextView) findViewById(R.id.tv_categories_view);
        this.myRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        this.pbLoader = (ProgressBar) findViewById(R.id.pb_loader);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.tvNoStream = (TextView) findViewById(R.id.tv_noStream);
        this.tvNoRecordFound = (TextView) findViewById(R.id.tv_no_record_found);
        this.appbarToolbar = (AppBarLayout) findViewById(R.id.appbar_toolbar);
        this.rl_settings = (RelativeLayout) findViewById(R.id.rl_settings);
        this.toolbar.inflateMenu(R.menu.menu_search_only);
        this.appbarToolbar.requestFocusFromTouch();
        currentWindowIndex = getIndexOfStreams(this.allStreams, currentWindowIndex);
        getIntent().putExtra("VIDEO_NUM", currentWindowIndex);
        this.player = new NSTPlayer(this);
        this.player.setCurrentWindowIndex(currentWindowIndex);
        if (videoTitle.length() > 16 && this.appbarToolbar.getVisibility() == 0) {
            videoTitle = videoTitle.substring(0, Math.min(videoTitle.length(), 16)) + "...";
        }
        this.player.setTitle(num + " " + videoTitle);
        this.player.setDefaultRetryTime(this.defaultRetryTime);
        this.player.setFullScreenOnly(this.fullScreenOnly);
        this.player.showAll();
        if (this.player != null) {
            this.player.onDestroy();
        }
        this.player.play(this.mFilePath, opened_stream_id, this.allowedFormat);
        this.player.retryCount = 0;
        this.player.fullScreenRatio();
        updateEPGData(epgChannelID, epgChannelLogo);
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        this.listChannels = (GridView) findViewById(R.id.lv_ch);
        this.et_search = (EditText) findViewById(R.id.et_search);
        this.ll_categories_view = (LinearLayout) findViewById(R.id.ll_categories_view);
        this.btn_cat_back.setOnClickListener(this);
        this.btn_cat_forward.setOnClickListener(this);
        this.playButton = findViewById(R.id.exo_play);
        if (this.playButton != null) {
            this.playButton.setOnClickListener(this);
        }
        this.pauseButton = findViewById(R.id.exo_pause);
        if (this.pauseButton != null) {
            this.pauseButton.setOnClickListener(this);
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
        this.aspectRatio = findViewById(R.id.btn_aspect_ratio);
        if (this.aspectRatio != null) {
            this.aspectRatio.setOnClickListener(this);
        }
        this.rl_middle = (RelativeLayout) findViewById(R.id.middle);
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        this.liveListDetailUnlckedChannels = new ArrayList();
        this.liveListDetailUnlckedDetailChannels = new ArrayList();
        this.liveListDetailAvailableChannels = new ArrayList();
        this.AvailableChannelsFirstOpenedCat = new ArrayList();
        this.liveListDetailAvailableNewChannels = new ArrayList();
        this.liveListDetailChannels = new ArrayList();
        this.allLiveCategories = this.liveStreamDBHandler.getAllliveCategories();
        LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel();
        liveStream.setLiveStreamCategoryID(AppConst.PASSWORD_UNSET);
        liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));
        int parentalStatusCount = this.liveStreamDBHandler.getParentalStatusCount();
        this.listPassword = getPasswordSetCategories();
        if (parentalStatusCount > 0 && this.allLiveCategories != null) {
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(this.allLiveCategories, this.listPassword);
            }
            this.liveListDetailUnlcked.add(0, liveStream);
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        } else if (this.allLiveCategories != null) {
            this.allLiveCategories.add(0, liveStream);
            this.liveListDetailAvailable = this.allLiveCategories;
        }
        ArrayList<LiveStreamsDBModel> categoryDetails = this.liveStreamDBHandler.getCatDetailsWithStreamID(opened_stream_id);
        if (categoryDetails != null && categoryDetails.size() > 0) {
            this.catID = ((LiveStreamsDBModel) categoryDetails.get(0)).getCategoryId();
            this.catName = ((LiveStreamsDBModel) categoryDetails.get(0)).getCategoryName();
            if (this.liveListDetailAvailable != null) {
                for (int i = 0; i < this.liveListDetailAvailable.size(); i++) {
                    if (((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(i)).getLiveStreamCategoryID().equals(this.catID)) {
                        this.currentCategoryIndex = i;
                        break;
                    }
                }
            }
        }
        if (parentalStatusCount <= 0 || this.allStreams == null) {
            this.liveListDetailAvailableChannels = this.allStreams;
        } else {
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetailChannels = getUnlockedChannels(this.allStreams, this.listPassword);
            }
            this.liveListDetailAvailableChannels = this.liveListDetailUnlckedDetailChannels;
        }
        if (this.catID != null && !this.catID.equals("")) {
            if (this.liveStreamDBHandler != null) {
                this.AvailableChannelsFirstOpenedCat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.catID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(this.catName);
            }
            setChannelListAdapterNew(this.AvailableChannelsFirstOpenedCat);
        } else if (this.liveListDetailAvailableChannels != null) {
            this.tv_categories_view.setText(getResources().getString(R.string.all));
            setChannelListAdapterNew(this.liveListDetailAvailableChannels);
        }
    }

    public void setChannelListAdapterNew(ArrayList<LiveStreamsDBModel> allStreams) {
        int positionToSelect = getIntent().getIntExtra("VIDEO_NUM", 0);
        this.channelsOnVideoAdapter = new ChannelsOnVideoAdapter(allStreams, this.context);
        if ((getResources().getConfiguration().screenLayout & 15) == 3) {
            this.gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            this.gridLayoutManager = new GridLayoutManager(this, 1);
        }
        this.myRecyclerView.setLayoutManager(this.gridLayoutManager);
        this.myRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.myRecyclerView.setAdapter(this.channelsOnVideoAdapter);
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
        this.searchView = (SearchView) MenuItemCompat.getActionView(this.toolbar.getMenu().findItem(R.id.action_search));
        this.searchView.setQueryHint(getResources().getString(R.string.search_channel));
        EditText searchEditText = (EditText) this.searchView.findViewById(R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setImeOptions(268435456);
        }
        this.searchView.setOnQueryTextListener(new C20191());
        this.searchView.setOnCloseListener(new C20202());
        this.searchView.setOnSearchClickListener(new C20213());
    }

    public void onClickCalled(LiveStreamsDBModel data) {
        if (data != null) {
            showTitleBarAndFooter();
            int num = Integer.parseInt(data.getNum());
            String epgChannelID = data.getEpgChannelId();
            String channelLogo = data.getStreamIcon();
            this.player.setTitle(num + " " + data.getName());
            this.player.setCurrentWindowIndex(getIndexOfStreams(this.allStreams, num));
            if (this.player != null) {
                this.player.onDestroy();
            }
            this.player.play(this.mFilePath, Integer.parseInt(data.getStreamId()), this.allowedFormat);
            this.player.retryCount = 0;
            if (this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) {
                this.appbarToolbar.setVisibility(8);
            }
            this.player.hideSystemUi();
            updateEPGData(epgChannelID, channelLogo);
        }
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

    private ArrayList<LiveStreamsDBModel> getUnlockedChannels(ArrayList<LiveStreamsDBModel> liveListDetail, ArrayList<String> listPassword) {
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
                this.liveListDetailUnlckedChannels.add(user1);
            }
        }
        return this.liveListDetailUnlckedChannels;
    }

    public void backbutton() {
        if (this.currentCategoryIndex != 0) {
            this.currentCategoryIndex--;
        }
        if (this.currentCategoryIndex == 0 && this.liveListDetailAvailableChannels != null) {
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(getResources().getString(R.string.all));
            }
            setChannelListAdapterNew(this.liveListDetailAvailableChannels);
        } else if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryID();
            String currentCatName = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryName();
            if (this.liveStreamDBHandler != null) {
                this.allStreams_with_cat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
            }
            setChannelListAdapterNew(this.allStreams_with_cat);
        }
    }

    public void nextbutton() {
        if (this.currentCategoryIndex != this.liveListDetailAvailable.size() - 1) {
            this.currentCategoryIndex++;
        }
        if (this.currentCategoryIndex == 0 && this.liveListDetailAvailableChannels != null) {
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(getResources().getString(R.string.all));
            }
            setChannelListAdapterNew(this.liveListDetailAvailableChannels);
        } else if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryID();
            String currentCatName = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryName();
            if (this.liveStreamDBHandler != null) {
                this.allStreams_with_cat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
            }
            setChannelListAdapterNew(this.allStreams_with_cat);
        }
    }

    public void updateEPGData(String epgChannelID, String channel_logo) {
        this.hideEPGData = true;
        if (!(this.liveStreamDBHandler == null || this.loginPreferencesAfterLogin == null)) {
            String savedEPGShift = this.loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_SELECTED_EPG_SHIFT, "");
            if (epgChannelID == null || epgChannelID.equals("")) {
                this.hideEPGData = true;
            } else {
                ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(epgChannelID);
                String Title = "";
                if (xmltvProgrammePojos != null) {
                    for (int j = 0; j < xmltvProgrammePojos.size(); j++) {
                        String startDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStart();
                        String stopDateTime = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getStop();
                        Title = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j)).getTitle();
                        Long epgStartDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(startDateTime));
                        Long epgStopDateToTimestamp = Long.valueOf(Utils.epgTimeConverter(stopDateTime));
                        if (Utils.isEventVisible(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this)) {
                            int epgPercentage = Utils.getPercentageLeft(epgStartDateToTimestamp.longValue(), epgStopDateToTimestamp.longValue(), this);
                            if (epgPercentage != 0) {
                                epgPercentage = 100 - epgPercentage;
                                if (epgPercentage == 0 || Title == null || Title.equals("")) {
                                    this.hideEPGData = true;
                                } else {
                                    this.hideEPGData = false;
                                    this.player.hideEPGData(Boolean.valueOf(false));
                                    this.ll_seekbar_time.setVisibility(0);
                                    this.progressBar.setVisibility(0);
                                    this.progressBar.setProgress(epgPercentage);
                                    this.currentProgram.setVisibility(0);
                                    this.currentProgram.setText("Now: " + Title);
                                    this.currentProgramTime.setVisibility(0);
                                    this.currentProgramTime.setText(this.programTimeFormat.format(epgStartDateToTimestamp) + " - " + this.programTimeFormat.format(epgStopDateToTimestamp));
                                    this.channelLogo.setVisibility(0);
                                    if (channel_logo != null && !channel_logo.equals("")) {
                                        Picasso.with(this.context).load(channel_logo).placeholder((int) R.drawable.logo).into(this.channelLogo);
                                    } else if (VERSION.SDK_INT >= 21) {
                                        this.channelLogo.setImageDrawable(this.context.getResources().getDrawable(R.drawable.logo, null));
                                    }
                                    if (j + 1 < xmltvProgrammePojos.size()) {
                                        String startDateTime2 = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j + 1)).getStart();
                                        String stopDateTime2 = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j + 1)).getStop();
                                        String Title2 = ((XMLTVProgrammePojo) xmltvProgrammePojos.get(j + 1)).getTitle();
                                        this.nextProgramTime.setText(this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(startDateTime2))) + " - " + this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(stopDateTime2))));
                                        this.nextProgram.setText("Next: " + Title2);
                                        this.nextProgramTime.setVisibility(0);
                                        this.nextProgram.setVisibility(0);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    this.hideEPGData = true;
                }
            }
        }
        if (this.hideEPGData) {
            this.player.hideEPGData(Boolean.valueOf(true));
            this.ll_seekbar_time.setVisibility(8);
            this.progressBar.setVisibility(8);
            this.progressBar.setProgress(0);
            this.currentProgram.setVisibility(8);
            this.currentProgram.setText("");
            this.currentProgramTime.setVisibility(8);
            this.currentProgramTime.setText("");
            this.channelLogo.setVisibility(8);
        }
    }

    public int getIndexOfStreams(ArrayList<LiveStreamsDBModel> allStreams, int num) {
        for (int i = 0; i < allStreams.size(); i++) {
            if (Integer.parseInt(((LiveStreamsDBModel) allStreams.get(i)).getNum()) == num) {
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
            case 22:
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean uniqueDown;
        if (event.getRepeatCount() == 0) {
            uniqueDown = true;
        } else {
            uniqueDown = false;
        }
        switch (keyCode) {
            case 23:
                if (this.appbarToolbar != null && this.appbarToolbar.getVisibility() == 0) {
                    return true;
                }
                hideTitleBarAndFooter();
                if (this.appbarToolbar == null) {
                    return true;
                }
                this.appbarToolbar.setVisibility(0);
                this.appbarToolbar.requestFocusFromTouch();
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
                hideTitleBarAndFooter();
                if (this.appbarToolbar == null) {
                    return true;
                }
                this.appbarToolbar.setVisibility(0);
                this.appbarToolbar.requestFocusFromTouch();
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

    private void playerStartIconsUpdate() {
        this.playButton.setVisibility(8);
        this.pauseButton.setVisibility(0);
    }

    private void playerPauseIconsUpdate() {
        this.pauseButton.setVisibility(8);
        this.playButton.setVisibility(0);
    }

    public void showTitleBarAndFooter() {
        findViewById(R.id.app_video_top_box).setVisibility(0);
        findViewById(R.id.controls).setVisibility(0);
        if (!this.hideEPGData) {
            findViewById(R.id.ll_seekbar_time).setVisibility(0);
        }
    }

    public void hideTitleBarAndFooter() {
        findViewById(R.id.app_video_top_box).setVisibility(8);
        findViewById(R.id.controls).setVisibility(8);
        findViewById(R.id.ll_seekbar_time).setVisibility(8);
    }

    public void onClick(View view) {
        String videoTitle;
        String num;
        switch (view.getId()) {
            case R.id.btn_aspect_ratio:
                this.player.toggleAspectRatio();
                return;
            case R.id.btn_category_back:
                backbutton();
                return;
            case R.id.btn_category_fwd:
                nextbutton();
                return;
            case R.id.btn_list:
                if (this.appbarToolbar != null) {
                    toggleView(this.appbarToolbar);
                    this.appbarToolbar.requestFocusFromTouch();
                    return;
                }
                return;
            case R.id.exo_next:
                if (this.player != null) {
                    next();
                    int indexNext = this.player.getCurrentWindowIndex();
                    if (indexNext <= this.allStreams.size() - 1) {
                        videoTitle = ((LiveStreamsDBModel) this.allStreams.get(indexNext)).getName();
                        num = ((LiveStreamsDBModel) this.allStreams.get(indexNext)).getNum();
                        if (videoTitle.length() > 16 && this.appbarToolbar.getVisibility() == 0) {
                            videoTitle = videoTitle.substring(0, Math.min(videoTitle.length(), 16)) + "...";
                        }
                        this.player.setTitle(num + " " + videoTitle);
                        if (this.player != null) {
                            this.player.onDestroy();
                        }
                        this.player.play(this.mFilePath, Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(indexNext)).getStreamId()), this.allowedFormat);
                        this.player.retryCount = 0;
                        updateEPGData(((LiveStreamsDBModel) this.allStreams.get(indexNext)).getEpgChannelId(), ((LiveStreamsDBModel) this.allStreams.get(indexNext)).getStreamIcon());
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
                    if (indexPrev <= this.allStreams.size() - 1) {
                        videoTitle = ((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getName();
                        num = ((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getNum();
                        if (videoTitle.length() > 16 && this.appbarToolbar.getVisibility() == 0) {
                            videoTitle = videoTitle.substring(0, Math.min(videoTitle.length(), 16)) + "...";
                        }
                        this.player.setTitle(num + " " + videoTitle);
                        if (this.player != null) {
                            this.player.onDestroy();
                        }
                        this.player.play(this.mFilePath, Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getStreamId()), this.allowedFormat);
                        this.player.retryCount = 0;
                        updateEPGData(((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getEpgChannelId(), ((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getStreamIcon());
                        return;
                    }
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
            this.player.setCurrentWindowIndex(this.allStreams.size() - 1);
        } else {
            this.player.setCurrentWindowIndex(currentWindowIndex - 1);
        }
    }

    private void next() {
        int currentWindowIndex = this.player.getCurrentWindowIndex();
        if (currentWindowIndex == this.allStreams.size() - 1) {
            this.player.setCurrentWindowIndex(0);
        } else {
            this.player.setCurrentWindowIndex(currentWindowIndex + 1);
        }
    }

    public void onBackPressed() {
        if (!this.searchView.isIconified()) {
            this.player.hideSystemUi();
            this.searchView.onActionViewCollapsed();
            this.rl_settings.setVisibility(0);
            this.appbarToolbar.requestFocusFromTouch();
        } else if (this.appbarToolbar.getVisibility() == 0) {
            this.appbarToolbar.setVisibility(8);
            this.player.hideSystemUi();
        } else if (findViewById(R.id.app_video_top_box).getVisibility() == 0) {
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

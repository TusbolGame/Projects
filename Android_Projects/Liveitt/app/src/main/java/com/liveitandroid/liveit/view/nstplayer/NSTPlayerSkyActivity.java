package com.liveitandroid.liveit.view.nstplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.github.isabsent.filepicker.SimpleFilePickerDialog;
import com.google.android.exoplayer2.C;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.liveitandroid.liveit.CustomDialog;
import com.liveitandroid.liveit.Prefrences;
import com.liveitandroid.liveit.RecordingService;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.model.pojo.XMLTVProgrammePojo;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity;
import com.liveitandroid.liveit.view.activity.NewDashboardActivity2;
import com.liveitandroid.liveit.view.activity.SettingsActivity;
import com.liveitandroid.liveit.view.activity.VodActivityNewFlowSubCategories;
import com.liveitandroid.liveit.view.adapter.ChannelsOnVideoAdapter;
import com.liveitandroid.liveit.view.adapter.SearchableAdapter;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class NSTPlayerSkyActivity extends AppCompatActivity implements OnClickListener, SimpleFilePickerDialog.InteractionListenerInt {
    private static SharedPreferences loginPreferencesSharedPref_time_format;
    private ArrayList<LiveStreamsDBModel> AvailableChannelsFirstOpenedCat;
    public Activity activity;
    SearchableAdapter adapter;
    private ArrayList<LiveStreamCategoryIdDBModel> allLiveCategories;
    public ArrayList<LiveStreamsDBModel> allStreams;
    public ArrayList<LiveStreamsDBModel> allStreams_with_cat;
    public String allowedFormat;
    public RelativeLayout app_video_box;
    AppBarLayout appbarToolbar;
    public View aspectRatio;
    private AppCompatImageView btn_cat_back;
    private AppCompatImageView btn_cat_forward;
    String catID = "";
    String catName = "";
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    public View channelListButton;
    public ImageView channelLogo;
    public boolean channelZapped = false;

    private int currentCategoryIndex = 0;
    public TextView currentProgram;
    public String currentProgramChanneID;
    public int currentProgramStreamID;
    public TextView currentProgramTime;
    private DatabaseHandler database;
    TextView date;
    TextView time;
    public long defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    public EditText et_search;
    public View forwardButton;
    public boolean fullScreen = false;
    public boolean fullScreenOnly = true;
    private GridLayoutManager gridLayoutManager;
    public boolean hideEPGData = true;
    public ListView listChannels;
    private ArrayList<String> listPassword = new ArrayList();
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableChannels;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableChannelsSort = new ArrayList();
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableChannels_Temp;
    private ArrayList<LiveStreamsDBModel> liveListDetailAvailableNewChannels;
    private ArrayList<LiveStreamsDBModel> liveListDetailChannels;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedChannels;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private ArrayList<LiveStreamsDBModel> liveListDetailUnlckedDetailChannels;
    LiveStreamDBHandler liveStreamDBHandler;
    public LinearLayout ll_categories_view;
    LinearLayout ll_epg1_box;
    LinearLayout ll_epg2_box;
    LinearLayout ll_epg3_box;
    LinearLayout ll_epg4_box;
    LinearLayout ll_layout_to_hide1;
    LinearLayout ll_layout_to_hide2;
    LinearLayout ll_layout_to_hide3;
    LinearLayout ll_no_guide;
    public LinearLayout ll_seekbar_time;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
    private SharedPreferences loginPreferencesSharedPref_currently_playing_video;
    private Editor loginPrefsEditor;
    public boolean longKeyPressed = true;
    public String mFilePath;
    RecyclerView myRecyclerView;
    public View nextButton;
    public TextView nextProgram;
    public TextView nextProgramTime;
    public View pauseButton;
    ProgressBar pbLoader;
    public View playButton;
    NSTPlayerSky player;
    public View prevButton;
    private SimpleDateFormat programTimeFormat;
    ProgressBar progressBar;
    public View rewindButton;
    RelativeLayout rl_layout_to_hide4;
    RelativeLayout rl_layout_to_hide5;
    public RelativeLayout rl_middle;
    RelativeLayout rl_nst_player_sky_layout;
    RelativeLayout rl_settings;
    public String scaleType;
    SearchView searchView;
    public boolean showNavIcon = true;
    public String title;
    Toolbar toolbar;
    TextView tvNoRecordFound;
    TextView tvNoStream;
    public TextView tv_categories_view;
    TextView tv_epg1_date;
    TextView tv_epg1_program;
    TextView tv_epg2_date;
    TextView tv_epg2_program;
    TextView tv_epg3_date;
    TextView tv_epg3_program;
    TextView tv_epg4_date;
    TextView tv_epg4_program;
    TextView tv_video_height;
    TextView tv_video_margin_right;
    TextView tv_video_width;
    public String url;
    IjkVideoView videoView;
    Prefrences prefrences;
    TextView vodCategoryName;
    private Context context;
    private CastContext mCastContext;
    private CastSession mCastSession;
    private SessionManagerListener<CastSession> mSessionManagerListener;
    private MenuItem mediaRouteMenuItem;
    class C18285 implements DialogInterface.OnClickListener {
        C18285() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadChannelsAndVod(NSTPlayerSkyActivity.this.context);
        }
    }

    class C18296 implements DialogInterface.OnClickListener {
        C18296() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }

    class C18307 implements DialogInterface.OnClickListener {
        C18307() {
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.loadTvGuid(NSTPlayerSkyActivity.this.context);
        }
    }

    class C18318 implements DialogInterface.OnClickListener {
        C18318() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
        }
    }


    class C20686 implements TextWatcher {
        C20686() {
        }

        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            if (NSTPlayerSkyActivity.this.adapter != null) {
                NSTPlayerSkyActivity.this.adapter.getFilter().filter(cs.toString());
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    }


    class C18274 implements SearchView.OnQueryTextListener {
        C18274() {
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            if (NSTPlayerSkyActivity.this.adapter != null) {
                NSTPlayerSkyActivity.this.adapter.getFilter().filter(newText);
            }
            return false;
        }
    }

    public static class Config implements Parcelable {
        public static final Creator<Config> CREATOR = new C20691();
        private static boolean debug = true;
        private Activity activity;
        private long defaultRetryTime;
        private boolean fullScreenOnly;
        private String scaleType;
        private boolean showNavIcon;
        private String title;
        private String url;

        static class C20691 implements Creator<Config> {
            C20691() {
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
            Intent intent = new Intent(this.activity, NSTPlayerSkyActivity.class);
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

    class C18239 implements OnClickListener {
        C18239() {
        }

        public void onClick(View view) {
            changeSortPopUp.dismiss();
        }
    }

    class CountDownRunner implements Runnable {
        CountDownRunner() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    NSTPlayerSkyActivity.this.doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e2) {
                }
            }
        }
    }

    public static void play(Activity context, String... url) {
        Intent intent = new Intent(context, NSTPlayerSkyActivity.class);
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
        setContentView(R.layout.nst_player_sky_activity);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        this.context = this;
        //CastContext castContext = CastContext.getSharedInstance(this);
        mSessionManager = new SessionManager(this.context);
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        this.loginPreferencesSharedPref_currently_playing_video = getSharedPreferences(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, 0);
        this.loginPrefsEditor = this.loginPreferencesSharedPref_currently_playing_video.edit();
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        this.allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_stream_id = getIntent().getIntExtra("OPENED_STREAM_ID", 0);
        String catNovo = getIntent().getStringExtra("OPENED_CAT_ID");
        int opened_cat_id = 0;
        if(catNovo.equals("0")){
            opened_cat_id = 0;
        }else if(catNovo.equals("-1")){
            opened_cat_id = -1;
        }else{
            try {
                opened_cat_id = Integer.parseInt(catNovo);
            } catch (NumberFormatException e) {
            }
        }
        String streamType = getIntent().getStringExtra("STREAM_TYPE");
        String videoTitle = getIntent().getStringExtra("VIDEO_TITLE");
        String epgChannelID = getIntent().getStringExtra("EPG_CHANNEL_ID");
        String epgChannelLogo = getIntent().getStringExtra("EPG_CHANNEL_LOGO");
        this.mFilePath = "http://" + serverUrl + ":" + serverPort + "/live/" + username + "/" + password + "/";
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.database = new DatabaseHandler(this.context);
        this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "live");
        this.progressBar = findViewById(R.id.progressBar);
        this.ll_seekbar_time = findViewById(R.id.ll_seekbar_time);
        this.channelLogo = findViewById(R.id.iv_channel_logo);
        this.currentProgram = findViewById(R.id.tv_current_program);
        this.currentProgramTime = findViewById(R.id.tv_current_time);
        this.nextProgram = findViewById(R.id.tv_next_program);
        this.nextProgramTime = findViewById(R.id.tv_next_program_time);
        this.btn_cat_back = findViewById(R.id.btn_category_back);
        this.btn_cat_forward = findViewById(R.id.btn_category_fwd);
        this.tv_categories_view = findViewById(R.id.tv_categories_view);
        this.myRecyclerView = findViewById(R.id.my_recycler_view);
        this.pbLoader = findViewById(R.id.pb_loader);
        this.toolbar = findViewById(R.id.toolbar);
        this.tvNoStream = findViewById(R.id.tv_noStream);
        this.tvNoRecordFound = findViewById(R.id.tv_no_record_found);
        this.appbarToolbar = findViewById(R.id.appbar_toolbar);
        this.vodCategoryName = findViewById(R.id.tv_settings);
        this.rl_settings = findViewById(R.id.rl_settings);
        this.rl_nst_player_sky_layout = findViewById(R.id.rl_nst_player_sky_layout);
        this.ll_layout_to_hide1 = findViewById(R.id.ll_layout_to_hide_1);
        this.ll_layout_to_hide2 = findViewById(R.id.ll_layout_to_hide_2);
        this.ll_layout_to_hide3 = findViewById(R.id.ll_layout_to_hide_3);
        this.rl_layout_to_hide4 = findViewById(R.id.rl_layout_to_hide_4);
        this.rl_layout_to_hide5 = findViewById(R.id.rl_layout_to_hide_5);
        this.ll_epg1_box = findViewById(R.id.ll_epg1_box);
        this.ll_epg2_box = findViewById(R.id.ll_epg2_box);
        this.ll_epg3_box = findViewById(R.id.ll_epg3_box);
        this.ll_epg4_box = findViewById(R.id.ll_epg4_box);
        this.ll_no_guide = findViewById(R.id.ll_no_guide);
        this.tv_epg1_date = findViewById(R.id.tv_epg1_date);
        this.tv_epg2_date = findViewById(R.id.tv_epg2_date);
        this.tv_epg3_date = findViewById(R.id.tv_epg3_date);
        this.tv_epg4_date = findViewById(R.id.tv_epg4_date);
        this.tv_epg1_program = findViewById(R.id.tv_epg1_program);
        this.tv_epg2_program = findViewById(R.id.tv_epg2_program);
        this.tv_epg3_program = findViewById(R.id.tv_epg3_program);
        this.tv_epg4_program = findViewById(R.id.tv_epg4_program);
        this.date = (TextView) findViewById(R.id.date);
        this.time = (TextView) findViewById(R.id.time);
        this.tv_video_width = findViewById(R.id.tv_video_width);
        this.tv_video_height = findViewById(R.id.tv_video_height);
        this.tv_video_margin_right = findViewById(R.id.tv_video_margin_right);
        this.videoView = findViewById(R.id.video_view);
        //this.toolbar.inflateMenu(R.menu.menu_search_only);
        //this.toolbar.inflateMenu(R.menu.menu_search_tv);
        //this.appbarToolbar.requestFocusFromTouch();
        new Thread(new CountDownRunner()).start();
        loginPreferencesSharedPref_time_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_TIME_FORMAT, 0);
        this.programTimeFormat = new SimpleDateFormat(loginPreferencesSharedPref_time_format.getString(AppConst.LOGIN_PREF_TIME_FORMAT, ""));
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        this.fullScreen = false;
        if (!this.fullScreen) {
            findViewById(R.id.app_video_box).setOnClickListener(this);
        }
        this.listChannels = findViewById(R.id.lv_ch);
        this.et_search = findViewById(R.id.et_search);
        this.ll_categories_view = findViewById(R.id.ll_categories_view);
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
        this.rl_middle = findViewById(R.id.middle);
        this.liveListDetailUnlcked = new ArrayList();
        this.liveListDetailUnlckedDetail = new ArrayList();
        this.liveListDetailAvailable = new ArrayList();
        this.liveListDetail = new ArrayList();
        this.liveListDetailUnlckedChannels = new ArrayList();
        this.liveListDetailUnlckedDetailChannels = new ArrayList();
        this.liveListDetailAvailableChannels = new ArrayList();
        this.liveListDetailAvailableChannels_Temp = new ArrayList();
        this.AvailableChannelsFirstOpenedCat = new ArrayList();
        this.liveListDetailAvailableNewChannels = new ArrayList();
        this.liveListDetailChannels = new ArrayList();
        this.allLiveCategories = this.liveStreamDBHandler.getAllliveCategories();
        LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel();
        LiveStreamCategoryIdDBModel liveStream1 = new LiveStreamCategoryIdDBModel();
        liveStream.setLiveStreamCategoryID(AppConst.PASSWORD_UNSET);
        liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));
        liveStream1.setLiveStreamCategoryID("-1");
        liveStream1.setLiveStreamCategoryName(getResources().getString(R.string.favourites));
        int parentalStatusCount = this.liveStreamDBHandler.getParentalStatusCount();
        this.listPassword = getPasswordSetCategories();
        if (parentalStatusCount > 0 && this.allLiveCategories != null) {
            if (this.listPassword != null) {
                this.liveListDetailUnlckedDetail = getUnlockedCategories(this.allLiveCategories, this.listPassword);
            }
            this.liveListDetailUnlcked.add(0, liveStream);
            this.liveListDetailUnlcked.add(1, liveStream1);
            this.liveListDetailAvailable = this.liveListDetailUnlckedDetail;
        } else if (this.allLiveCategories != null) {
            this.allLiveCategories.add(0, liveStream);
            this.allLiveCategories.add(1, liveStream1);
            this.liveListDetailAvailable = this.allLiveCategories;
        }

        String currentCatName = "";
        if (opened_cat_id == 0) {
            this.catID = AppConst.PASSWORD_UNSET;
            this.catName = getResources().getString(R.string.all);
            this.currentCategoryIndex = 0;
        } else if (opened_cat_id == -1) {
            this.catID = "-1";
            this.catName = getResources().getString(R.string.favourites);
            this.currentCategoryIndex = -1;
        } else {
            ArrayList<LiveStreamsDBModel> categoryDetails = this.liveStreamDBHandler.getCatDetailsWithStreamID(opened_stream_id);
            if (categoryDetails != null && categoryDetails.size() > 0) {
                this.catID = categoryDetails.get(0).getCategoryId();
                this.catName = categoryDetails.get(0).getCategoryName();
                if (this.liveListDetailAvailable != null) {
                    for (int i = 0; i < this.liveListDetailAvailable.size(); i++) {
                        if (this.liveListDetailAvailable.get(i).getLiveStreamCategoryID().equals(this.catID)) {
                            this.currentCategoryIndex = i;
                            break;
                        }
                    }
                    currentCatName = this.liveListDetailAvailable.get(this.currentCategoryIndex).getLiveStreamCategoryName();
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


        this.SharedPreferencesSort = getSharedPreferences(AppConst.LOGIN_PREF_SORT_TV, 0);
        String sort = SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT_TV, "");
        if(sort.equals("") || sort.equals("1"))
        {
            mSessionManager.setOrdena("1");
        }else if(sort.equals("0"))
        {
            mSessionManager.setOrdena("0");
        }else{
            mSessionManager.setOrdena("2");
        }

        if(mSessionManager.getOrdena().equals("0")){
            Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollno);
        }else if(mSessionManager.getOrdena().equals("1"))
        {
            Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNum);
        }else{
            Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNome);
        }

        if (this.catID != null && !this.catID.equals("") && this.catID.equals(AppConst.PASSWORD_UNSET)) {
            if (this.liveListDetailAvailableChannels != null) {
                this.tv_categories_view.setText(getResources().getString(R.string.all));
                setChannelListAdapter(this.liveListDetailAvailableChannels);
            }
            if (!(this.liveListDetailAvailableChannels == null || this.liveListDetailAvailableChannels.size() == 0)) {
                if(mSessionManager.getOrdena().equals("0")){
                    Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollno);
                }else if(mSessionManager.getOrdena().equals("1"))
                {
                    Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNum);
                }else{
                    Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNome);
                }

                this.liveListDetailAvailableChannels_Temp = liveListDetailAvailableChannels;
                playFirstTime(this.liveListDetailAvailableChannels_Temp);
            }
        } else if (this.catID == null || this.catID.equals("") || !this.catID.equals("-1")) {
            if (this.liveStreamDBHandler != null) {
                this.AvailableChannelsFirstOpenedCat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.catID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
                this.tv_categories_view.setSelected(true);
            }
            setChannelListAdapter(this.AvailableChannelsFirstOpenedCat);
            if (!(this.AvailableChannelsFirstOpenedCat == null || this.AvailableChannelsFirstOpenedCat.size() == 0)) {
                if(mSessionManager.getOrdena().equals("0")){
                    Collections.sort(AvailableChannelsFirstOpenedCat, LiveStreamsDBModel.StuRollno);
                }else if(mSessionManager.getOrdena().equals("1"))
                {
                    Collections.sort(AvailableChannelsFirstOpenedCat, LiveStreamsDBModel.StuRollnoNum);
                }else{
                    Collections.sort(AvailableChannelsFirstOpenedCat, LiveStreamsDBModel.StuRollnoNome);
                }

                this.liveListDetailAvailableChannels_Temp = AvailableChannelsFirstOpenedCat;
                playFirstTime(this.liveListDetailAvailableChannels_Temp);
            }
        } else {
            this.allStreams_with_cat = new ArrayList();
            getFavourites();
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(getResources().getString(R.string.favourites));
                this.tv_categories_view.setSelected(true);
            }
            setChannelListAdapter(this.allStreams_with_cat);
            this.liveListDetailAvailableChannels_Temp = this.allStreams_with_cat;

            if(mSessionManager.getOrdena().equals("0")){
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollno);
            }else if(mSessionManager.getOrdena().equals("1"))
            {
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollnoNum);
            }else{
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollnoNome);
            }

            this.liveListDetailAvailableChannels_Temp = allStreams_with_cat;
            playFirstTime(this.liveListDetailAvailableChannels_Temp);
        }

        this.rl_nst_player_sky_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NSTPlayerSkyActivity.this.fullScreenVideoLayout();
            }
        });

        //setupCastListener();
        //mCastContext = CastContext.getSharedInstance(this);
        //mCastSession = mCastContext.getSessionManager().getCurrentCastSession();

        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        if (this.appbarToolbar != null) {
            this.appbarToolbar.setBackground(getResources().getDrawable(R.drawable.vod_backgound));
        }
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        this.vodCategoryName.setText(getResources().getString(R.string.use_long_press));
        this.vodCategoryName.setSelected(true);

        prefrences = new Prefrences(this);
    }


    private void setupCastListener() {
        mSessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                mCastSession = castSession;
                invalidateOptionsMenu();
            }

            private void onApplicationDisconnected() {
                invalidateOptionsMenu();
            }
        };
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

    public void doWork() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String dateValue = Calendar.getInstance().getTime().toString();
                String currentCurrentTime = Utils.getTime(NSTPlayerSkyActivity.this.context);
                String currentCurrentDate = Utils.getDate(dateValue);
                if (NSTPlayerSkyActivity.this.time != null) {
                    NSTPlayerSkyActivity.this.time.setText(currentCurrentTime);
                }
                if (NSTPlayerSkyActivity.this.date != null) {
                    NSTPlayerSkyActivity.this.date.setText(currentCurrentDate);
                }
            }
        });
    }

    private void playFirstTime(ArrayList<LiveStreamsDBModel> liveListDetailAvailableChannels_temp) {
        this.player = new NSTPlayerSky(this);
        if (liveListDetailAvailableChannels_temp != null && liveListDetailAvailableChannels_temp.size() > 0) {
            int num = Integer.parseInt(liveListDetailAvailableChannels_temp.get(0).getNum());
            String videoTitle = liveListDetailAvailableChannels_temp.get(0).getName();
            int opened_stream_id = Integer.parseInt(liveListDetailAvailableChannels_temp.get(0).getStreamId());
            String epgChannelID = liveListDetailAvailableChannels_temp.get(0).getEpgChannelId();
            String epgChannelLogo = liveListDetailAvailableChannels_temp.get(0).getStreamIcon();
            if (this.loginPrefsEditor != null) {
                this.loginPrefsEditor.putString(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, String.valueOf(num));
                this.loginPrefsEditor.apply();
            }
            this.currentProgramStreamID = opened_stream_id;
            this.currentProgramChanneID = epgChannelID;
            this.player.setTitle(num + " - " + videoTitle);
            this.player.setDefaultRetryTime(this.defaultRetryTime);
            this.player.setFullScreenOnly(this.fullScreenOnly);
            if (this.player != null) {
                this.player.onDestroy();
            }
            this.player.setCurrentWindowIndex(num);
            this.player.play(this.mFilePath, opened_stream_id, this.allowedFormat);
            this.player.retryCount = 0;
            this.player.fullScreenRatio();
            updateEPGData(epgChannelID, epgChannelLogo);
            showEPG(this.currentProgramChanneID);
            this.fullScreen = false;
            showTitleBar();
        }
    }

    public int getIndexOfTV(ArrayList<LiveStreamsDBModel> allStreams1, int num) {
        for (int i = 0; i < allStreams1.size(); i++) {
            if (Integer.parseInt(allStreams1.get(i).getNum()) == num) {
                return i;
            }
        }
        return 0;
    }

    private void GetCurrentDateTime() {
        String dateValue = Calendar.getInstance().getTime().toString();
        String currentCurrentTime = Utils.getTime(this.context);
        String currentCurrentDate = Utils.getDate(dateValue);
        if (this.time != null) {
            this.time.setText(currentCurrentTime);
        }
        if (this.date != null) {
            this.date.setText(currentCurrentDate);
        }
    }

    @SuppressLint({"NewApi"})
    public void setChannelListAdapter(ArrayList<LiveStreamsDBModel> allStreams) {
        if (allStreams != null) {
            this.adapter = new SearchableAdapter(this, allStreams);
            if (this.listChannels != null) {
                this.listChannels.setAdapter(this.adapter);

                this.listChannels.requestFocus();
                this.listChannels.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        ArrayList<LiveStreamsDBModel> filteredData = NSTPlayerSkyActivity.this.adapter.getFilteredData();
                        if (filteredData != null) {
                            NSTPlayerSkyActivity.this.showEPG(filteredData.get(position).getEpgChannelId());
                            NSTPlayerSkyActivity.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        NSTPlayerSkyActivity.this.showEPG(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getEpgChannelId());
                        NSTPlayerSkyActivity.this.adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                this.listChannels.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        //Log.d("TmpLogs", "loading: " + NSTPlayerSkyActivity.this.mFilePath);
                        view.setSelected(true);
                        ArrayList<LiveStreamsDBModel> filteredData = NSTPlayerSkyActivity.this.adapter.getFilteredData();
                        int num;
                        String epgChannelID;
                        String channelLogo;
                        if (filteredData != null) {
                            num = Integer.parseInt(filteredData.get(position).getNum());
//                            ((LiveStreamsDBModel) filteredData.get(position)).details();
                            epgChannelID = filteredData.get(position).getEpgChannelId();
                            channelLogo = filteredData.get(position).getStreamIcon();
                            //num = NSTPlayerSkyActivity.this.getIndexOfStreams(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels, num);
                            if (NSTPlayerSkyActivity.this.currentProgramStreamID != Integer.parseInt(filteredData.get(position).getStreamId())) {
                                NSTPlayerSkyActivity.this.player.setTitle(filteredData.get(position).getNum() + " - " + filteredData.get(position).getName());
                                if (NSTPlayerSkyActivity.this.player != null && NSTPlayerSkyActivity.this.player.isPlaying()) {
                                    NSTPlayerSkyActivity.this.player.onDestroy();
                                    NSTPlayerSkyActivity.this.player = new NSTPlayerSky(NSTPlayerSkyActivity.this);
                                }else{
                                    NSTPlayerSkyActivity.this.player = new NSTPlayerSky(NSTPlayerSkyActivity.this);
                                }
                                NSTPlayerSkyActivity.this.player.setCurrentWindowIndex(num);
                                NSTPlayerSkyActivity.this.player.play(NSTPlayerSkyActivity.this.mFilePath, Integer.parseInt(filteredData.get(position).getStreamId()), NSTPlayerSkyActivity.this.allowedFormat);
                                NSTPlayerSkyActivity.this.player.retryCount = 0;
                                NSTPlayerSkyActivity.this.currentProgramStreamID = Integer.parseInt(filteredData.get(position).getStreamId());
                                NSTPlayerSkyActivity.this.catID = filteredData.get(position).getCategoryId();
                                NSTPlayerSkyActivity.this.catName = filteredData.get(position).getCategoryName();
                                NSTPlayerSkyActivity.this.updateEPGData(epgChannelID, channelLogo);
                                NSTPlayerSkyActivity.this.showEPG(epgChannelID);
                                NSTPlayerSkyActivity.this.showTitleBar();
                                if (NSTPlayerSkyActivity.this.loginPrefsEditor != null) {
                                    NSTPlayerSkyActivity.this.loginPrefsEditor.putString(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, String.valueOf(filteredData.get(position).getNum()));
                                    NSTPlayerSkyActivity.this.loginPrefsEditor.apply();
                                }
                                NSTPlayerSkyActivity.this.adapter.notifyDataSetChanged();
                                return;
                            }
                            NSTPlayerSkyActivity.this.fullScreenVideoLayout();
                            return;
                        }
                        num = Integer.parseInt(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getNum());
                        epgChannelID = NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getEpgChannelId();
                        channelLogo = NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getStreamIcon();
                        //num = NSTPlayerSkyActivity.this.getIndexOfStreams(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels, num);
                        if (NSTPlayerSkyActivity.this.currentProgramStreamID != Integer.parseInt(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getStreamId())) {
                            NSTPlayerSkyActivity.this.player.setTitle(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getNum() + " - " + NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getName());
                            if (NSTPlayerSkyActivity.this.player != null && NSTPlayerSkyActivity.this.player.isPlaying()) {
                                NSTPlayerSkyActivity.this.player.onDestroy();
                                NSTPlayerSkyActivity.this.player = new NSTPlayerSky(NSTPlayerSkyActivity.this);
                            }else{
                                NSTPlayerSkyActivity.this.player = new NSTPlayerSky(NSTPlayerSkyActivity.this);
                            }
                            NSTPlayerSkyActivity.this.player.setCurrentWindowIndex(num);
                            NSTPlayerSkyActivity.this.player.play(NSTPlayerSkyActivity.this.mFilePath, Integer.parseInt(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getStreamId()), NSTPlayerSkyActivity.this.allowedFormat);
                            NSTPlayerSkyActivity.this.player.retryCount = 0;
                            NSTPlayerSkyActivity.this.currentProgramStreamID = Integer.parseInt(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getStreamId());
                            NSTPlayerSkyActivity.this.catID = NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getCategoryId();
                            NSTPlayerSkyActivity.this.catName = NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getCategoryName();
                            NSTPlayerSkyActivity.this.updateEPGData(epgChannelID, channelLogo);
                            NSTPlayerSkyActivity.this.showEPG(epgChannelID);
                            NSTPlayerSkyActivity.this.showTitleBar();
                            if (NSTPlayerSkyActivity.this.loginPrefsEditor != null) {
                                NSTPlayerSkyActivity.this.loginPrefsEditor.putString(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, String.valueOf(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getNum()));
                                NSTPlayerSkyActivity.this.loginPrefsEditor.apply();
                            }
                            NSTPlayerSkyActivity.this.adapter.notifyDataSetChanged();
                            return;
                        }
                        NSTPlayerSkyActivity.this.fullScreenVideoLayout();
                    }
                });
                this.listChannels.setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                        pressedOptionPosition = position;
                        showPopup(view);
                        return true;
                    }
                });
                this.et_search.addTextChangedListener(new C20686());
                return;
            }
            return;
        }
        this.allStreams_with_cat = new ArrayList();
        this.adapter = new SearchableAdapter(this, this.allStreams_with_cat);
        if (this.listChannels != null) {
            this.listChannels.setAdapter(this.adapter);


        }
    }


    private void processForFavourite(int position){
        if(position < 0){
            Toast.makeText(activity, "Error: please try again", Toast.LENGTH_LONG).show();
            return;
        }
        String categoryID;
        int streamID;
        ArrayList<LiveStreamsDBModel> filteredData = NSTPlayerSkyActivity.this.adapter.getFilteredData();
        String streamName = "";
        if (filteredData != null) {
            categoryID = filteredData.get(position).getCategoryId();
            streamID = Integer.parseInt(filteredData.get(position).getStreamId());
            streamName = filteredData.get(position).getName();
        } else {
            categoryID = NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getCategoryId();
            streamID = Integer.parseInt(NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getStreamId());
            streamName = NSTPlayerSkyActivity.this.liveListDetailAvailableChannels.get(position).getName();
        }
        if (NSTPlayerSkyActivity.this.database != null) {
            ArrayList<FavouriteDBModel> checkFavourite = NSTPlayerSkyActivity.this.database.checkFavourite(streamID, categoryID, "live");
            if (checkFavourite == null || checkFavourite.size() <= 0) {
                NSTPlayerSkyActivity.this.addToFavourite(categoryID, streamID, streamName);
            } else {
                NSTPlayerSkyActivity.this.removeFromFavourite(categoryID, streamID, streamName);
            }
        }
    }

    private void addToFavourite(String categoryID, int streamID, String streamName) {
        if (this.context != null && this.adapter != null && this.database != null) {
            FavouriteDBModel LiveStreamsFavourite = new FavouriteDBModel();
            LiveStreamsFavourite.setCategoryID(categoryID);
            LiveStreamsFavourite.setStreamID(streamID);
            this.database.addToFavourite(LiveStreamsFavourite, "live");
            this.adapter.notifyDataSetChanged();
            Utils.showToast(this.context, streamName + " : Adicionado aos Favoritos");
        }
    }

    private void removeFromFavourite(String categoryID, int streamID, String streamName) {
        if (this.context != null && this.adapter != null && this.database != null) {
            this.database.deleteFavourite(streamID, categoryID, "live");
            this.adapter.notifyDataSetChanged();
            Utils.showToast(this.context, streamName + " : Removido dos Favoritos");
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
                if (user1.getLiveStreamCategoryID().equals(it2.next())) {
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
                if (user1.getCategoryId().equals(it2.next())) {
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

            if(mSessionManager.getOrdena().equals("0")){
                Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollno);
            }else if(mSessionManager.getOrdena().equals("1"))
            {
                Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNum);
            }else{
                Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNome);
            }

            this.liveListDetailAvailableChannels_Temp = liveListDetailAvailableChannels;
            setChannelListAdapter(this.liveListDetailAvailableChannels_Temp);
        } else if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = this.liveListDetailAvailable.get(this.currentCategoryIndex).getLiveStreamCategoryID();
            String currentCatName = this.liveListDetailAvailable.get(this.currentCategoryIndex).getLiveStreamCategoryName();
            this.catID = currentCatID;
            this.catName = currentCatName;
            this.allStreams_with_cat = new ArrayList();
            if (currentCatID.equals("-1")) {
                getFavourites();
            } else if (this.liveStreamDBHandler != null) {
                this.allStreams_with_cat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
                this.tv_categories_view.setSelected(true);
            }

            if(mSessionManager.getOrdena().equals("0")){
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollno);
            }else if(mSessionManager.getOrdena().equals("1"))
            {
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollnoNum);
            }else{
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollnoNome);
            }

            this.liveListDetailAvailableChannels_Temp = allStreams_with_cat;
            setChannelListAdapter(this.liveListDetailAvailableChannels_Temp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.toolbar.inflateMenu(R.menu.menu_search_tv);
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(16843499, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        for (int i = 0; i < this.toolbar.getChildCount(); i++) {
            if (this.toolbar.getChildAt(i) instanceof ActionMenuView) {
                ((Toolbar.LayoutParams) this.toolbar.getChildAt(i).getLayoutParams()).gravity = 16;
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
        if (id == R.id.action_search) {
            this.searchView = (SearchView) MenuItemCompat.getActionView(item);
            this.searchView.setQueryHint("Digite nome...");
            this.searchView.setIconifiedByDefault(false);
            this.searchView.setOnQueryTextListener(new C18274());
            return true;
        }
        if (id == R.id.menu_load_channels_vod1) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C18285());
            alertDialog.setNegativeButton("Não", new C18296());
            alertDialog.show();
        }
        if (id == R.id.menu_load_tv_guide1) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Confirmação para Atualização...");
            alertDialog.setMessage("Confirma o Procedimento?");
            alertDialog.setIcon(R.drawable.questionmark);
            alertDialog.setPositiveButton("Sim", new C18307());
            alertDialog.setNegativeButton("Não", new C18318());
            alertDialog.show();
        }
        if (id == R.id.menu_sort) {
            showSortPopup(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private SharedPreferences SharedPreferencesSort;
    private Editor SharedPreferencesSortEditor;
    private PopupWindow changeSortPopUp;
    private void showSortPopup(Activity context) {
        final View layout = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.sort_layout_tv, (RelativeLayout) context.findViewById(R.id.rl_password_prompt));
        final Activity contt = context;
        this.changeSortPopUp = new PopupWindow(context);
        this.changeSortPopUp.setContentView(layout);
        this.changeSortPopUp.setWidth(-1);
        this.changeSortPopUp.setHeight(-1);
        this.changeSortPopUp.setFocusable(true);
        this.changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());
        this.changeSortPopUp.showAtLocation(layout, 17, 0, 0);
        Button savePasswordBT = (Button) layout.findViewById(R.id.bt_save_password);
        Button closedBT = (Button) layout.findViewById(R.id.bt_close);
        final RadioGroup rgRadio = (RadioGroup) layout.findViewById(R.id.rg_radio);
        RadioButton normal = (RadioButton) layout.findViewById(R.id.rb_normal);
        RadioButton last_added = (RadioButton) layout.findViewById(R.id.rb_byid);
        RadioButton atoz = (RadioButton) layout.findViewById(R.id.rb_atoz);

        this.SharedPreferencesSort = getSharedPreferences(AppConst.LOGIN_PREF_SORT_TV, 0);
        this.SharedPreferencesSortEditor = this.SharedPreferencesSort.edit();
        String sort = SharedPreferencesSort.getString(AppConst.LOGIN_PREF_SORT_TV, "");
        if (sort.equals("0")) {
            last_added.setChecked(true);
        } else if (sort.equals(AppConst.DB_EPG_ID)) {
            atoz.setChecked(true);
        } else {
            mSessionManager.setOrdena("1");
            normal.setChecked(true);
        }

        closedBT.setOnClickListener(new C18239());
        savePasswordBT.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                RadioButton selectedPlayer1 = (RadioButton) layout.findViewById(rgRadio.getCheckedRadioButtonId());
                if (selectedPlayer1.getText().toString().equals(getResources().getString(R.string.sort_default_id))) {
                    mSessionManager.setOrdena("1");
                    SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT_TV, "1");
                    SharedPreferencesSortEditor.commit();
                } else if (selectedPlayer1.getText().toString().equals(getResources().getString(R.string.sort_atoz))) {
                    mSessionManager.setOrdena("2");
                    SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT_TV, AppConst.DB_EPG_ID);
                    SharedPreferencesSortEditor.commit();
                } else {
                    mSessionManager.setOrdena("0");
                    SharedPreferencesSortEditor.putString(AppConst.LOGIN_PREF_SORT_TV, AppConst.PASSWORD_UNSET);
                    SharedPreferencesSortEditor.commit();
                }

                changeSortPopUp.dismiss();
                if (currentCategoryIndex == 0) {
                    setChannelListAdapter(liveListDetailAvailableChannels);
                    playFirstTime(liveListDetailAvailableChannels);
                }else if(currentCategoryIndex == -1)
                {
                    setChannelListAdapter(AvailableChannelsFirstOpenedCat);
                    playFirstTime(AvailableChannelsFirstOpenedCat);
                }else{
                    setChannelListAdapter(allStreams_with_cat);
                    playFirstTime(allStreams_with_cat);
                }
                NSTPlayerSkyActivity.this.adapter.notifyDataSetChanged();
                return;
            }
        });
    }


    public void nextbutton() {
        if (this.currentCategoryIndex != this.liveListDetailAvailable.size() - 1) {
            this.currentCategoryIndex++;
        }
        if (this.currentCategoryIndex == 0 && this.liveListDetailAvailableChannels != null) {
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(getResources().getString(R.string.all));
            }

            if(mSessionManager.getOrdena().equals("0")){
                Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollno);
            }else if(mSessionManager.getOrdena().equals("1"))
            {
                Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNum);
            }else{
                Collections.sort(liveListDetailAvailableChannels, LiveStreamsDBModel.StuRollnoNome);
            }

            this.liveListDetailAvailableChannels_Temp = liveListDetailAvailableChannels;
            setChannelListAdapter(this.liveListDetailAvailableChannels_Temp);
        } else if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = this.liveListDetailAvailable.get(this.currentCategoryIndex).getLiveStreamCategoryID();
            String currentCatName = this.liveListDetailAvailable.get(this.currentCategoryIndex).getLiveStreamCategoryName();
            this.catID = currentCatID;
            this.catName = currentCatName;
            this.allStreams_with_cat = new ArrayList();
            if (currentCatID.equals("-1")) {
                getFavourites();
            } else if (this.liveStreamDBHandler != null) {
                this.allStreams_with_cat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "live");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
                this.tv_categories_view.setSelected(true);
            }

            if(mSessionManager.getOrdena().equals("0")){
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollno);
            }else if(mSessionManager.getOrdena().equals("1"))
            {
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollnoNum);
            }else{
                Collections.sort(allStreams_with_cat, LiveStreamsDBModel.StuRollnoNome);
            }

            this.liveListDetailAvailableChannels_Temp = allStreams_with_cat;
            setChannelListAdapter(this.liveListDetailAvailableChannels_Temp);
        }
    }

    public void getFavourites() {
        if (this.database != null) {
            ArrayList<FavouriteDBModel> allFavourites = this.database.getAllFavourites("live");
            ArrayList<LiveStreamsDBModel> favouriteStreams = new ArrayList();
            Iterator it = allFavourites.iterator();
            while (it.hasNext()) {
                FavouriteDBModel favListItem = (FavouriteDBModel) it.next();
                LiveStreamsDBModel channelAvailable = new LiveStreamDBHandler(this.context).getLiveStreamFavouriteRow(favListItem.getCategoryID(), String.valueOf(favListItem.getStreamID()));
                if (channelAvailable != null) {
                    favouriteStreams.add(channelAvailable);
                }
            }
            if (favouriteStreams != null && favouriteStreams.size() != 0) {
                this.allStreams_with_cat = favouriteStreams;
            }
        }
    }

    public void testing() {
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
                        String startDateTime = xmltvProgrammePojos.get(j).getStart();
                        String stopDateTime = xmltvProgrammePojos.get(j).getStop();
                        Title = xmltvProgrammePojos.get(j).getTitle();
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
                                    this.currentProgram.setText("Agora: " + Title);
                                    this.currentProgramTime.setVisibility(0);
                                    this.currentProgramTime.setText(this.programTimeFormat.format(epgStartDateToTimestamp) + " - " + this.programTimeFormat.format(epgStopDateToTimestamp));
                                    this.channelLogo.setVisibility(0);
                                    if (channel_logo != null && !channel_logo.equals("")) {
                                        Picasso.with(this.context).load(channel_logo).placeholder(R.drawable.logo).into(this.channelLogo);
                                    } else if (VERSION.SDK_INT >= 21) {
                                        this.channelLogo.setImageDrawable(this.context.getResources().getDrawable(R.drawable.logo, null));
                                    }
                                    if (j + 1 < xmltvProgrammePojos.size()) {
                                        String startDateTime2 = xmltvProgrammePojos.get(j + 1).getStart();
                                        String stopDateTime2 = xmltvProgrammePojos.get(j + 1).getStop();
                                        String Title2 = xmltvProgrammePojos.get(j + 1).getTitle();
                                        this.nextProgramTime.setText(this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(startDateTime2))) + " - " + this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(stopDateTime2))));
                                        this.nextProgram.setText("A seguir: " + Title2);
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
            if (Integer.parseInt(allStreams.get(i).getNum()) == num) {
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
                if (!this.fullScreen) {
                    return true;
                }
                showTitleBarAndFooter();
                findViewById(R.id.exo_next).performClick();
                return true;
            case 20:
                if (!this.fullScreen) {
                    return true;
                }
                showTitleBarAndFooter();
                findViewById(R.id.exo_prev).performClick();
                return true;
            case 21:
                if (this.listChannels == null || this.listChannels.getVisibility() != 0) {
                    return true;
                }
                if (this.et_search != null) {
                    this.et_search.setText("");
                }
                backbutton();
                return true;
            case 22:
                if (this.listChannels == null || this.listChannels.getVisibility() != 0) {
                    return true;
                }
                if (this.et_search != null) {
                    this.et_search.setText("");
                }
                nextbutton();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean uniqueDown;
        uniqueDown = event.getRepeatCount() == 0;
        int action = event.getAction();
        switch (keyCode) {
            case 23:
                break;
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
                if (!this.fullScreen) {
                    return true;
                }
                showTitleBarAndFooter();
                findViewById(R.id.exo_next).performClick();
                return true;
            case 167:
                if (this.fullScreen) {
                    showTitleBarAndFooter();
                    findViewById(R.id.exo_prev).performClick();
                    return true;
                }
                break;
            default:
                return super.onKeyDown(keyCode, event);
        }
        if (!this.fullScreen) {
            return true;
        }
        if (findViewById(R.id.app_video_top_box).getVisibility() == 0) {
            hideTitleBarAndFooter();
        } else {
            showTitleBarAndFooter();
            if (this.player != null) {
                this.player.showAll();
            }
        }
        return true;
    }

    private void smallScreenVideoLayoutforphone() {
        findViewById(R.id.app_video_box).setOnClickListener(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LayoutParams params = (LayoutParams) this.rl_nst_player_sky_layout.getLayoutParams();
        params.width = (int) (450.0f * metrics.density);
        params.height = (int) (180.0f * metrics.density);
        params.rightMargin = 36;
        this.ll_layout_to_hide1.setVisibility(0);
        this.ll_layout_to_hide2.setVisibility(0);
        this.ll_layout_to_hide3.setVisibility(0);
        this.rl_layout_to_hide4.setVisibility(0);
        this.rl_layout_to_hide5.setVisibility(0);
        this.rl_settings.setVisibility(0);
        this.toolbar.setVisibility(0);
        this.rl_nst_player_sky_layout.setLayoutParams(params);
        this.fullScreen = false;
        if (this.channelZapped) {
            if (!(this.catID == null || this.catID.equals(""))) {
                int i;
                if (this.liveStreamDBHandler != null) {
                    this.allStreams_with_cat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.catID, "live");
                }
                if (this.liveListDetailAvailable != null) {
                    for (i = 0; i < this.liveListDetailAvailable.size(); i++) {
                        if (this.liveListDetailAvailable.get(i).getLiveStreamCategoryID().equals(this.catID)) {
                            this.currentCategoryIndex = i;
                            break;
                        }
                    }
                }
                if (this.tv_categories_view != null) {
                    this.tv_categories_view.setText(this.catName);
                    this.tv_categories_view.setSelected(true);
                }
                this.adapter = new SearchableAdapter(this, this.allStreams_with_cat);
                int selection = 0;
                if (this.listChannels != null) {
                    this.listChannels.setAdapter(this.adapter);
                    if (this.et_search != null) {
                        this.et_search.setText("");
                    }
                    if (this.player != null && this.allStreams_with_cat != null) {
                        for (i = 0; i < this.allStreams_with_cat.size(); i++) {
                            if (this.currentProgramStreamID == Integer.parseInt(this.allStreams_with_cat.get(i).getStreamId())) {
                                selection = i;
                                break;
                            }
                        }
                    }
                    this.listChannels.setSelection(selection);
                    this.listChannels.requestFocus();
                }
            }
            this.channelZapped = false;
        } else if (this.listChannels != null) {
            this.listChannels.requestFocus();
        }
        showTitleBar();
    }

    private void showEPG(String epgChannelID) {
        this.ll_epg1_box.setVisibility(8);
        this.ll_epg2_box.setVisibility(8);
        this.ll_epg3_box.setVisibility(8);
        this.ll_epg4_box.setVisibility(8);
        this.ll_no_guide.setVisibility(8);
        boolean epgFound = false;
        if (!(epgChannelID == null || epgChannelID.equals("") || this.liveStreamDBHandler == null)) {
            ArrayList<XMLTVProgrammePojo> xmltvProgrammePojos = this.liveStreamDBHandler.getEPG(epgChannelID);
            String Title4 = "";
            if (xmltvProgrammePojos != null) {
                int j = 0;
                while (j < xmltvProgrammePojos.size()) {
                    String startDateTime1 = xmltvProgrammePojos.get(j).getStart();
                    String stopDateTime1 = xmltvProgrammePojos.get(j).getStop();
                    String Title1 = xmltvProgrammePojos.get(j).getTitle();
                    Long epgStartDateToTimestamp1 = Long.valueOf(Utils.epgTimeConverter(startDateTime1));
                    Long epgStopDateToTimestamp1 = Long.valueOf(Utils.epgTimeConverter(stopDateTime1));
                    if (Utils.isEventVisible(epgStartDateToTimestamp1.longValue(), epgStopDateToTimestamp1.longValue(), this.context)) {
                        epgFound = true;
                        this.tv_epg1_date.setText(this.programTimeFormat.format(epgStartDateToTimestamp1) + " - " + this.programTimeFormat.format(epgStopDateToTimestamp1));
                        this.tv_epg1_program.setText(Title1);
                        this.tv_epg1_program.setSelected(true);
                        this.ll_epg1_box.setVisibility(0);
                        if (j + 1 < xmltvProgrammePojos.size()) {
                            String startDateTime2 = xmltvProgrammePojos.get(j + 1).getStart();
                            String stopDateTime2 = xmltvProgrammePojos.get(j + 1).getStop();
                            String Title2 = xmltvProgrammePojos.get(j + 1).getTitle();
                            this.tv_epg2_date.setText(this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(startDateTime2))) + " - " + this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(stopDateTime2))));
                            this.tv_epg2_program.setText(Title2);
                            this.tv_epg2_program.setSelected(true);
                            this.ll_epg2_box.setVisibility(0);
                        }
                        if (j + 2 < xmltvProgrammePojos.size()) {
                            String startDateTime3 = xmltvProgrammePojos.get(j + 2).getStart();
                            String stopDateTime3 = xmltvProgrammePojos.get(j + 2).getStop();
                            String Title3 = xmltvProgrammePojos.get(j + 2).getTitle();
                            this.tv_epg3_date.setText(this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(startDateTime3))) + " - " + this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(stopDateTime3))));
                            this.tv_epg3_program.setText(Title3);
                            this.tv_epg3_program.setSelected(true);
                            this.ll_epg3_box.setVisibility(View.VISIBLE);
                        }
                        if (j + 3 < xmltvProgrammePojos.size()) {
                            String startDateTime4 = xmltvProgrammePojos.get(j + 3).getStart();
                            String stopDateTime4 = xmltvProgrammePojos.get(j + 3).getStop();
                            Title4 = xmltvProgrammePojos.get(j + 3).getTitle();
                            this.tv_epg4_date.setText(this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(startDateTime4))) + " - " + this.programTimeFormat.format(Long.valueOf(Utils.epgTimeConverter(stopDateTime4))));
                            this.tv_epg4_program.setText(Title4);
                            this.tv_epg4_program.setSelected(true);
                            this.ll_epg4_box.setVisibility(View.VISIBLE);
                        }
                    }
                    j++;

                }
            }
        }
        if (!epgFound) {
            this.ll_no_guide.setVisibility(View.VISIBLE);
        }
    }

    private void playerStartIconsUpdate() {
        this.playButton.setVisibility(View.GONE);
        this.pauseButton.setVisibility(View.VISIBLE);
    }

    private void playerPauseIconsUpdate() {
        this.pauseButton.setVisibility(View.GONE);
        this.playButton.setVisibility(View.VISIBLE);
    }

    public void showTitleBarAndFooter() {
        findViewById(R.id.app_video_top_box).setVisibility(View.VISIBLE);
        findViewById(R.id.controls).setVisibility(View.VISIBLE);
        if (!this.hideEPGData) {
            findViewById(R.id.ll_seekbar_time).setVisibility(View.VISIBLE);
        }
    }

    public void showTitleBar() {
        findViewById(R.id.ll_seekbar_time).setVisibility(View.GONE);
        if (this.fullScreen) {
            findViewById(R.id.app_video_top_box).setVisibility(View.VISIBLE);
        }
    }

    public void hideTitleBarAndFooter() {
        findViewById(R.id.app_video_top_box).setVisibility(View.GONE);
        findViewById(R.id.controls).setVisibility(View.GONE);
        findViewById(R.id.ll_seekbar_time).setVisibility(View.GONE);
    }

    public void onClick(View view) {
        String videoTitle;
        int num;
        switch (view.getId()) {
            case R.id.app_video_box:
                fullScreenVideoLayout();
                return;
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
                    this.channelZapped = true;
                    next();
                    int indexNext = this.player.getCurrentWindowIndex();
                    indexNext = indexNext-1;
                    if (indexNext <= this.liveListDetailAvailableChannels.size() - 1) {
                        videoTitle = this.liveListDetailAvailableChannels.get(indexNext).getName();
                        num = Integer.parseInt(this.liveListDetailAvailableChannels.get(indexNext).getNum());
                        this.player.setTitle(num + " - " + videoTitle);
                        if (this.player != null) {
                            this.player.onDestroy();
                        }
                        this.player.setCurrentWindowIndex(num);
                        this.player.play(this.mFilePath, Integer.parseInt(this.liveListDetailAvailableChannels.get(indexNext).getStreamId()), this.allowedFormat);
                        this.player.retryCount = 0;
                        updateEPGData(this.liveListDetailAvailableChannels.get(indexNext).getEpgChannelId(), this.liveListDetailAvailableChannels.get(indexNext).getStreamIcon());
                        showEPG(this.liveListDetailAvailableChannels.get(indexNext).getEpgChannelId());
                        this.currentProgramStreamID = Integer.parseInt(this.liveListDetailAvailableChannels.get(indexNext).getStreamId());
                        this.catID = this.liveListDetailAvailableChannels.get(indexNext).getCategoryId();
                        this.catName = this.liveListDetailAvailableChannels.get(indexNext).getCategoryName();
                        if (this.loginPrefsEditor != null) {
                            this.loginPrefsEditor.putString(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, String.valueOf(num));
                            this.loginPrefsEditor.apply();
                            return;
                        }
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
                    indexPrev = indexPrev-1;
                    this.channelZapped = true;
                    if (indexPrev <= this.liveListDetailAvailableChannels.size() - 1) {
                        videoTitle = this.liveListDetailAvailableChannels.get(indexPrev).getName();
                        num = Integer.parseInt(this.liveListDetailAvailableChannels.get(indexPrev).getNum());
                        this.player.setTitle(num + " - " + videoTitle);
                        if (this.player != null) {
                            this.player.onDestroy();
                        }
                        this.player.setCurrentWindowIndex(num);
                        this.player.play(this.mFilePath, Integer.parseInt(this.liveListDetailAvailableChannels.get(indexPrev).getStreamId()), this.allowedFormat);
                        this.player.retryCount = 0;
                        updateEPGData(this.liveListDetailAvailableChannels.get(indexPrev).getEpgChannelId(), this.liveListDetailAvailableChannels.get(indexPrev).getStreamIcon());
                        showEPG(this.liveListDetailAvailableChannels.get(indexPrev).getEpgChannelId());
                        this.currentProgramStreamID = Integer.parseInt(this.liveListDetailAvailableChannels.get(indexPrev).getStreamId());
                        this.catID = this.liveListDetailAvailableChannels.get(indexPrev).getCategoryId();
                        this.catName = this.liveListDetailAvailableChannels.get(indexPrev).getCategoryName();
                        if (this.loginPrefsEditor != null) {
                            this.loginPrefsEditor.putString(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, String.valueOf(num));
                            this.loginPrefsEditor.apply();
                            return;
                        }
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
        if (currentWindowIndex == this.liveListDetailAvailableChannels.size() - 1) {
            this.player.setCurrentWindowIndex(0);
        } else {
            this.player.setCurrentWindowIndex(currentWindowIndex - 1);
        }
    }

    private void next() {
        int currentWindowIndex = this.player.getCurrentWindowIndex();
        if (currentWindowIndex == this.liveListDetailAvailableChannels.size() - 1) {
            this.player.setCurrentWindowIndex(0);
        } else {
            this.player.setCurrentWindowIndex(currentWindowIndex + 1);
        }
    }

    public void onBackPressed() {
        if (this.fullScreen) {
            smallScreenVideoLayoutForTv();
            hideTitleBarAndFooter();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
    }

    public void fullScreenVideoLayout() {
        findViewById(R.id.app_video_box).setOnClickListener(null);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LayoutParams params = (LayoutParams) this.rl_nst_player_sky_layout.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = metrics.heightPixels;
        params.addRule(9);
        params.addRule(11);
        params.addRule(10);
        params.addRule(12);
        params.rightMargin = 0;
        this.rl_nst_player_sky_layout.setLayoutParams(params);
        this.ll_layout_to_hide1.setVisibility(View.GONE);
        this.ll_layout_to_hide2.setVisibility(View.GONE);
        this.ll_layout_to_hide3.setVisibility(View.GONE);
        this.rl_layout_to_hide4.setVisibility(View.GONE);
        this.rl_layout_to_hide5.setVisibility(View.GONE);
        this.rl_settings.setVisibility(View.GONE);
        this.toolbar.setVisibility(View.GONE);
        this.fullScreen = true;
        showTitleBarAndFooter();
        if (this.player != null) {
            this.player.showAll();
        }
    }

    private void smallScreenVideoLayoutForTv() {
        findViewById(R.id.app_video_box).setOnClickListener(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LayoutParams params = (LayoutParams) this.rl_nst_player_sky_layout.getLayoutParams();
        int width = Integer.parseInt(this.tv_video_width.getText().toString());
        int height = Integer.parseInt(this.tv_video_height.getText().toString());
        int margin_right = Integer.parseInt(this.tv_video_margin_right.getText().toString());
        params.width = (int) (((float) width) * metrics.density);
        params.height = (int) (((float) height) * metrics.density);
        params.rightMargin = (int) (((float) margin_right) * metrics.density);
        params.removeRule(10);
        params.removeRule(12);
        this.ll_layout_to_hide1.setVisibility(0);
        this.ll_layout_to_hide2.setVisibility(0);
        this.ll_layout_to_hide3.setVisibility(0);
        this.rl_layout_to_hide4.setVisibility(0);
        this.rl_layout_to_hide5.setVisibility(0);
        this.rl_settings.setVisibility(0);
        this.toolbar.setVisibility(0);
        this.rl_nst_player_sky_layout.setLayoutParams(params);
        this.fullScreen = false;
        if (this.channelZapped) {
            if (!(this.catID == null || this.catID.equals(""))) {
                int i;
                if (this.liveStreamDBHandler != null) {
                    this.allStreams_with_cat = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(this.catID, "live");
                }
                if (this.liveListDetailAvailable != null) {
                    for (i = 0; i < this.liveListDetailAvailable.size(); i++) {
                        if (this.liveListDetailAvailable.get(i).getLiveStreamCategoryID().equals(this.catID)) {
                            this.currentCategoryIndex = i;
                            break;
                        }
                    }
                }
                if (this.tv_categories_view != null) {
                    this.tv_categories_view.setText(this.catName);
                    this.tv_categories_view.setSelected(true);
                }

                this.adapter = new SearchableAdapter(this, this.allStreams_with_cat);
                int selection = 0;
                if (this.listChannels != null) {
                    this.listChannels.setAdapter(this.adapter);
                    if (this.et_search != null) {
                        this.et_search.setText("");
                    }
                    if (this.player != null && this.allStreams_with_cat != null) {
                        for (i = 0; i < this.allStreams_with_cat.size(); i++) {
                            if (this.currentProgramStreamID == Integer.parseInt(this.allStreams_with_cat.get(i).getStreamId())) {
                                selection = i;
                                break;
                            }
                        }
                    }
                    this.listChannels.setSelection(selection);
                    this.listChannels.requestFocus();
                }
            }
            this.channelZapped = false;
        } else if (this.listChannels != null) {
            this.listChannels.requestFocus();
        }
        showTitleBar();
    }

    private String Recording = "Gravar";
    private String Favourite = "Add/Rem Favorito";

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, Recording);
        menu.add(0, v.getId(), 0, Favourite);
    }

    private int pressedOptionPosition = -1;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == Recording) {
            startRecording();
        }
        else if(item.getTitle() == Favourite) {
            processForFavourite(pressedOptionPosition);
//            return  false;
        }
        return true;
    }


    CustomDialog customDialog;
    private void startRecording(){

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        ArrayList<LiveStreamsDBModel> filteredData = NSTPlayerSkyActivity.this.adapter.getFilteredData();
                        if(filteredData == null){
                            Toast.makeText(NSTPlayerSkyActivity.this, "Error", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String streamUrl = NSTPlayerSkyActivity.this.mFilePath + Integer.parseInt(filteredData.get(pressedOptionPosition).getStreamId()) + "." + NSTPlayerSkyActivity.this.allowedFormat;
                        customDialog = new CustomDialog(NSTPlayerSkyActivity.this, streamUrl);
                        customDialog.show();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(NSTPlayerSkyActivity.this, "Não deu permissão de acesso. Desinstalar e voltar a instalar aplicação.", Toast.LENGTH_SHORT).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.channel_options, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_record){
                    startRecording();
                }
                else if(item.getItemId() == R.id.action_favourite) {
                    processForFavourite(pressedOptionPosition);
                }
                return false;
            }
        });
    }


    @Override
    public void showListItemDialog(int titleResId, String folderPath, SimpleFilePickerDialog.CompositeMode mode, String dialogTag) {
        //SimpleFilePickerDialog.build(folderPath, mode)
        //.title(titleResId)
        //  .show(Context, dialogTag);
    }

    @Override
    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
        switch (dialogTag) {
            case CustomDialog.DialogTag:
                if (extras.containsKey(SimpleFilePickerDialog.SELECTED_SINGLE_PATH)){
                    prefrences.setRecordingPath(extras.getString(SimpleFilePickerDialog.SELECTED_SINGLE_PATH));
                    try{ Thread.sleep(100); }
                    catch (Exception e){e.printStackTrace();}
                    customDialog.showRecDir();
                }
                break;
        }
        return false;
    }

}

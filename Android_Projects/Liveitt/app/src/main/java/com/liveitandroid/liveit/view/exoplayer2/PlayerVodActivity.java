package com.liveitandroid.liveit.view.exoplayer2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player.DefaultEventListener;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer.DecoderInitializationException;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.ads.AdsLoader;
import com.google.android.exoplayer2.source.ads.AdsMediaSource;
import com.google.android.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector.MappedTrackInfo;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView.VisibilityListener;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource.Factory;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.LiveStreamCategoryIdDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordStatusDBModel;
import com.liveitandroid.liveit.view.AppPref;
import com.liveitandroid.liveit.view.adapter.SearchableAdapter;
import com.liveitandroid.liveit.view.app_shell.AppController;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerVodActivity extends Activity implements OnClickListener, VisibilityListener {
    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";
    public static final String ACTION_VIEW_LIST = "com.google.android.exoplayer.demo.action.VIEW_LIST";
    public static final String AD_TAG_URI_EXTRA = "ad_tag_uri";
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER = new CookieManager();
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String DRM_LICENSE_URL = "drm_license_url";
    public static final String DRM_MULTI_SESSION = "drm_multi_session";
    public static final String DRM_SCHEME_EXTRA = "drm_scheme";
    private static final String DRM_SCHEME_UUID_EXTRA = "drm_scheme_uuid";
    public static final String EXTENSION_EXTRA = "extension";
    public static final String EXTENSION_LIST_EXTRA = "extension_list";
    public static final String PREFER_EXTENSION_DECODERS = "prefer_extension_decoders";
    private static final Map<Integer, Integer> RESIZE_MODE = Collections.unmodifiableMap(new C19481());
    public static final String URI_LIST_EXTRA = "uri_list";
    private int CURRENT_RESIZE_MODE = 0;
    private ViewGroup adUiViewGroup;
    SearchableAdapter adapter;
    private AdsLoader adsLoader;
    private ArrayList<LiveStreamCategoryIdDBModel> allMoviesCategories;
    private ArrayList<LiveStreamsDBModel> allStreams;
    private AppCompatImageView btnList;
    private AppCompatImageView btn_cat_back;
    private AppCompatImageView btn_cat_forward;
    private AppCompatImageView btn_screen;
    private AppCompatImageView btn_settings;
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    public ImageView channelLogo;
    public Context context;
    private int currentCategoryIndex = 0;
    public TextView currentProgram;
    public TextView currentProgramTime;
    private LinearLayout debugRootView;
    private TextView debugTextView;
    private DebugTextViewHelper debugViewHelper;
    public EditText et_search;
    private EventLogger eventLogger;
    private RelativeLayout exo_playback_control_view;
    String extension;
    private TrackGroupArray lastSeenTrackGroupArray;
    public ListView listChannels;
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
    private Uri loadedAdTagUri;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private Handler mainHandler;
    private Factory mediaDataSourceFactory;
    private boolean needRetrySource;
    public TextView nextProgram;
    public TextView nextProgramTime;
    private SimpleExoPlayer player;
    private SimpleDateFormat programTimeFormat = new SimpleDateFormat("HH:mm");
    ProgressBar progressBar;
    ProgressBar progressLoader;
    private long resumePosition;
    private int resumeWindow;
    private Button retryButton;
    private RelativeLayout rlChannelList;
    private boolean shouldAutoPlay;
    private SimpleExoPlayerView simpleExoPlayerView;
    String stream_type;
    private TrackSelectionHelper trackSelectionHelper;
    private DefaultTrackSelector trackSelector;
    public TextView tv_categories_view;
    private String videoTitle;
    private int video_id;
    private int video_num;
    private AppCompatTextView video_title;

    static class C19481 extends HashMap<Integer, Integer> {
        C19481() {
            put(Integer.valueOf(3), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(0), Integer.valueOf(R.drawable.ic_fullscreen_black_24dp));
            put(Integer.valueOf(1), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(2), Integer.valueOf(R.drawable.ic_zoom_out_map_black_24dp));
        }
    }

    class C19503 implements TextWatcher {
        C19503() {
        }

        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            if (PlayerVodActivity.this.adapter != null) {
                PlayerVodActivity.this.adapter.getFilter().filter(cs.toString());
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    }

    class C19514 implements MediaSourceFactory {
        C19514() {
        }

        public MediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
            return PlayerVodActivity.this.buildMediaSource(uri, null, handler, listener);
        }

        public int[] getSupportedTypes() {
            return new int[]{0, 1, 2, 3};
        }
    }

    private class PlayerEventListener extends DefaultEventListener {
        private PlayerEventListener() {
        }

        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (playbackState == 2) {
                PlayerVodActivity.this.showLoader();
            } else if (playbackState == 4) {
                PlayerVodActivity.this.showControls();
            } else if (playbackState == 3) {
                PlayerVodActivity.this.hideLoader();
            }
            PlayerVodActivity.this.updateButtonVisibilities();
        }

        public void onPositionDiscontinuity(int reason) {
            if (PlayerVodActivity.this.needRetrySource) {
                PlayerVodActivity.this.updateResumePosition();
            }
        }

        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;
            if (e.type == 1) {
                Exception cause = e.getRendererException();
                if (cause instanceof DecoderInitializationException) {
                    DecoderInitializationException decoderInitializationException = (DecoderInitializationException) cause;
                    if (decoderInitializationException.decoderName != null) {
                        errorString = PlayerVodActivity.this.getString(R.string.error_instantiating_decoder, new Object[]{decoderInitializationException.decoderName});
                    } else if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                        errorString = PlayerVodActivity.this.getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = PlayerVodActivity.this.getString(R.string.error_no_secure_decoder, new Object[]{decoderInitializationException.mimeType});
                    } else {
                        errorString = PlayerVodActivity.this.getString(R.string.error_no_decoder, new Object[]{decoderInitializationException.mimeType});
                    }
                }
            }
            if (errorString != null) {
                PlayerVodActivity.this.showToast(errorString);
            }
            PlayerVodActivity.this.needRetrySource = true;
            if (PlayerVodActivity.isBehindLiveWindow(e)) {
                PlayerVodActivity.this.clearResumePosition();
                PlayerVodActivity.this.initializePlayer();
                return;
            }
            PlayerVodActivity.this.updateResumePosition();
            PlayerVodActivity.this.updateButtonVisibilities();
            PlayerVodActivity.this.showControls();
            PlayerVodActivity.this.hideLoader();
        }

        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            PlayerVodActivity.this.updateButtonVisibilities();
            if (trackGroups != PlayerVodActivity.this.lastSeenTrackGroupArray) {
                MappedTrackInfo mappedTrackInfo = PlayerVodActivity.this.trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTrackTypeRendererSupport(2) == 1) {
                        PlayerVodActivity.this.showToast((int) R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTrackTypeRendererSupport(1) == 1) {
                        PlayerVodActivity.this.showToast((int) R.string.error_unsupported_audio);
                    }
                }
                PlayerVodActivity.this.lastSeenTrackGroupArray = trackGroups;
            }
        }
    }

    static {
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        Log.d("TmpLogs", "This is " + getLocalClassName());
        this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "movie");
        this.context = this;
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.shouldAutoPlay = true;
        clearResumePosition();
        this.mediaDataSourceFactory = buildDataSourceFactory(true);
        this.mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.player_vod_activity);
        overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        View rootView = findViewById(R.id.root);
        this.debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        this.debugTextView = (TextView) findViewById(R.id.debug_text_view);
        this.rlChannelList = (RelativeLayout) findViewById(R.id.rl_channel_list);
        this.retryButton = (Button) findViewById(R.id.retry_button);
        this.simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        this.simpleExoPlayerView.requestFocus();
        this.CURRENT_RESIZE_MODE = AppPref.getInstance(this).getResizeMode();
        this.simpleExoPlayerView.setResizeMode(((Integer) RESIZE_MODE.keySet().toArray()[this.CURRENT_RESIZE_MODE]).intValue());
        this.video_title = (AppCompatTextView) findViewById(R.id.title);
        this.btn_settings = (AppCompatImageView) findViewById(R.id.btn_settings);
        AppCompatImageView btn_back = (AppCompatImageView) findViewById(R.id.btn_back);
        AppCompatImageView btn_list = (AppCompatImageView) findViewById(R.id.btn_list);
        this.btn_screen = (AppCompatImageView) findViewById(R.id.btn_screen);
        this.btn_screen.setImageResource(((Integer) RESIZE_MODE.get(Integer.valueOf(this.CURRENT_RESIZE_MODE))).intValue());
        this.listChannels = (ListView) findViewById(R.id.lv_ch);
        this.et_search = (EditText) findViewById(R.id.et_search);
        this.ll_categories_view = (LinearLayout) findViewById(R.id.ll_categories_view);
        this.exo_playback_control_view = (RelativeLayout) findViewById(R.id.exo_playback_control_view);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressLoader = (ProgressBar) findViewById(R.id.progress_loader);
        this.ll_seekbar_time = (LinearLayout) findViewById(R.id.ll_seekbar_time);
        this.channelLogo = (ImageView) findViewById(R.id.iv_channel_logo);
        this.currentProgram = (TextView) findViewById(R.id.tv_current_program);
        this.currentProgramTime = (TextView) findViewById(R.id.tv_current_time);
        this.nextProgram = (TextView) findViewById(R.id.tv_next_program);
        this.nextProgramTime = (TextView) findViewById(R.id.tv_next_program_time);
        this.btn_cat_back = (AppCompatImageView) findViewById(R.id.btn_category_back);
        this.btn_cat_forward = (AppCompatImageView) findViewById(R.id.btn_category_forward);
        this.tv_categories_view = (TextView) findViewById(R.id.tv_categories_view);
        this.video_num = getIntent().getIntExtra("VIDEO_NUM", 0);
        getIntent().putExtra("VIDEO_NUM", getIndexOfStreams(this.allStreams, this.video_num));
        this.btn_screen.setOnClickListener(this);
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        btn_back.setOnClickListener(this);
        this.btn_settings.setOnClickListener(this);
        btn_list.setOnClickListener(this);
        this.retryButton.setOnClickListener(this);
        this.simpleExoPlayerView.setControllerVisibilityListener(this);
        rootView.setOnClickListener(this);
        this.btn_cat_back.setOnClickListener(this);
        this.btn_cat_forward.setOnClickListener(this);
        this.tv_categories_view.setText(getResources().getString(R.string.all));
        this.allMoviesCategories = this.liveStreamDBHandler.getAllMovieCategories();
        LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel();
        liveStream.setLiveStreamCategoryID(AppConst.PASSWORD_UNSET);
        liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));
        if (this.allMoviesCategories != null) {
            this.allMoviesCategories.add(0, liveStream);
            this.liveListDetailAvailable = this.allMoviesCategories;
        }
        if (this.allStreams != null) {
            setVodListAdapter(this.allStreams);
        }
    }

    public void setVodListAdapter(final ArrayList<LiveStreamsDBModel> allStreams) {
        this.video_num = getIntent().getIntExtra("VIDEO_NUM", 0);
        int positionToSelect = this.video_num;
        this.adapter = new SearchableAdapter(this, allStreams);
        if (this.listChannels != null) {
            this.listChannels.setAdapter(this.adapter);
            this.listChannels.setSelection(positionToSelect);
            this.listChannels.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    view.setSelected(true);
                    ArrayList<LiveStreamsDBModel> filteredData = PlayerVodActivity.this.adapter.getFilteredData();
                    if (filteredData != null) {
                        int num = Integer.parseInt(((LiveStreamsDBModel) filteredData.get(position)).getNum());
                        String epgChannelID = ((LiveStreamsDBModel) filteredData.get(position)).getEpgChannelId();
                        String channelLogo = ((LiveStreamsDBModel) filteredData.get(position)).getStreamIcon();
                        String title = ((LiveStreamsDBModel) filteredData.get(position)).getEpgTitle();
                        num = PlayerVodActivity.this.getIndexOfStreams(allStreams, num);
                        PlayerVodActivity.this.player.seekTo(num, C.TIME_UNSET);
                        PlayerVodActivity.this.clearResumePosition();
                        PlayerVodActivity.this.getIntent().putExtra("VIDEO_ID", ((LiveStreamsDBModel) allStreams.get(num)).getStreamId());
                        PlayerVodActivity.this.getIntent().putExtra("VIDEO_NUM", num);
                        PlayerVodActivity.this.getIntent().putExtra("VIDEO_TITLE", title);
                        PlayerVodActivity.this.initializePlayer();
                        PlayerVodActivity.this.video_title.setText(((LiveStreamsDBModel) filteredData.get(position)).getName());
                        return;
                    }
                    int num = Integer.parseInt(((LiveStreamsDBModel) allStreams.get(position)).getNum());
                    String epgChannelID = ((LiveStreamsDBModel) allStreams.get(position)).getEpgChannelId();
                    String channelLogoo = ((LiveStreamsDBModel) allStreams.get(position)).getStreamIcon();
                    String title = ((LiveStreamsDBModel) allStreams.get(position)).getEpgTitle();
                    PlayerVodActivity.this.player.seekTo(num, C.TIME_UNSET);
                    PlayerVodActivity.this.clearResumePosition();
                    PlayerVodActivity.this.getIntent().putExtra("VIDEO_ID", ((LiveStreamsDBModel) allStreams.get(num)).getStreamId());
                    PlayerVodActivity.this.getIntent().putExtra("VIDEO_NUM", num);
                    PlayerVodActivity.this.getIntent().putExtra("VIDEO_TITLE", title);
                    PlayerVodActivity.this.initializePlayer();
                    PlayerVodActivity.this.video_title.setText(((LiveStreamsDBModel) allStreams.get(position)).getName());
                }
            });
            this.et_search.addTextChangedListener(new C19503());
        }
    }

    public void onNewIntent(Intent intent) {
        releasePlayer();
        this.shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }

    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initializePlayer();
                }
            },1000);
        }
    }

    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= 23 || this.player == null) {
            initializePlayer();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initializePlayer();
                }
            },1000);
        }
    }

    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        releaseAdsLoader();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            showToast((int) R.string.storage_permission_denied);
            finish();
            return;
        }
        initializePlayer();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean z = false;
        int keyCode = event.getKeyCode();
        if (event.getAction() == 0) {
            if (keyCode == 23 || keyCode == 66) {
                if (this.listChannels.getVisibility() == 0) {
                    this.listChannels.performClick();
                } else {
                    this.listChannels.setVisibility(0);
                    this.et_search.setVisibility(0);
                    this.ll_categories_view.setVisibility(0);
                    this.listChannels.setFocusable(true);
                    this.listChannels.requestFocus();
                    return true;
                }
            }
            if (keyCode == 21 && this.listChannels.getVisibility() == 0) {
                if (this.et_search != null) {
                    this.et_search.setText("");
                }
                backbutton();
                this.listChannels.setFocusable(true);
                this.listChannels.requestFocus();
                return true;
            } else if (keyCode == 22 && this.listChannels.getVisibility() == 0) {
                if (this.et_search != null) {
                    this.et_search.setText("");
                }
                nextbutton();
                this.listChannels.setFocusable(true);
                this.listChannels.requestFocus();
                return true;
            }
        }
        if (this.simpleExoPlayerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event)) {
            z = true;
        }
        return z;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                return;
            case R.id.btn_category_back:
                backbutton();
                return;
            case R.id.btn_category_forward:
                nextbutton();
                return;
            case R.id.btn_list:
                toggleChannelList();
                return;
            case R.id.btn_screen:
                toggleResizeMode();
                return;
            case R.id.btn_settings:
                try {
                    if (this.trackSelector.getCurrentMappedTrackInfo() != null) {
                        this.trackSelectionHelper.showSelectionDialog(this, "Select", this.trackSelector.getCurrentMappedTrackInfo(), 0);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onClick: " + e.getMessage());
                    return;
                }
            case R.id.exo_next:
                if (this.player != null) {
                    next();
                    int indexNext = this.player.getCurrentWindowIndex();
                    Log.e("TAG", "indexNext: " + indexNext);
                    if (indexNext <= this.allStreams.size() - 1) {
                        clearResumePosition();
                        getIntent().putExtra("VIDEO_ID", ((LiveStreamsDBModel) this.allStreams.get(indexNext)).getStreamId());
                        initializePlayer();
                        this.video_title.setText(((LiveStreamsDBModel) this.allStreams.get(indexNext)).getName());
                        return;
                    }
                    return;
                }
                return;
            case R.id.exo_prev:
                if (this.player != null) {
                    previousLive();
                    int indexPrev = this.player.getCurrentWindowIndex();
                    Log.e("TAG", "indexPrev: " + indexPrev);
                    if (indexPrev <= this.allStreams.size() - 1) {
                        clearResumePosition();
                        getIntent().putExtra("VIDEO_ID", ((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getStreamId());
                        initializePlayer();
                        this.video_title.setText(((LiveStreamsDBModel) this.allStreams.get(indexPrev)).getName());
                        return;
                    }
                    return;
                }
                return;
            case R.id.retry_button:
                initializePlayer();
                return;
            default:
                return;
        }
    }

    public void backbutton() {
        if (this.currentCategoryIndex != 0) {
            this.currentCategoryIndex--;
        }
        if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryID();
            String currentCatName = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryName();
            if (this.liveStreamDBHandler != null) {
                this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "movie");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
            }
            setVodListAdapter(this.allStreams);
        }
    }

    public void nextbutton() {
        if (this.currentCategoryIndex != this.liveListDetailAvailable.size() - 1) {
            this.currentCategoryIndex++;
        }
        if (this.liveListDetailAvailable != null && this.liveListDetailAvailable.size() > 0 && this.currentCategoryIndex < this.liveListDetailAvailable.size()) {
            String currentCatID = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryID();
            String currentCatName = ((LiveStreamCategoryIdDBModel) this.liveListDetailAvailable.get(this.currentCategoryIndex)).getLiveStreamCategoryName();
            if (this.liveStreamDBHandler != null) {
                this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "movie");
            }
            if (this.tv_categories_view != null) {
                this.tv_categories_view.setText(currentCatName);
            }
            setVodListAdapter(this.allStreams);
        }
    }

    public void toggleChannelList() {
        if (this.listChannels != null && this.et_search != null) {
            if (this.listChannels.getVisibility() == 0) {
                this.listChannels.setVisibility(8);
                this.et_search.setVisibility(8);
                this.ll_categories_view.setVisibility(8);
                return;
            }
            this.listChannels.setVisibility(0);
            this.et_search.setVisibility(0);
            this.ll_categories_view.setVisibility(0);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 19:
                if (this.listChannels != null && this.listChannels.getVisibility() == 0) {
                    return true;
                }
                findViewById(R.id.exo_next).performClick();
                return true;
            case 20:
                if (this.listChannels != null && this.listChannels.getVisibility() == 0) {
                    return true;
                }
                findViewById(R.id.exo_prev).performClick();
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void onBackPressed() {
        if (this.listChannels != null) {
            this.listChannels.setFocusable(true);
            this.listChannels.requestFocus();
        }
        if (this.listChannels == null || this.listChannels.getVisibility() != 0) {
            super.onBackPressed();
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
            return;
        }
        this.listChannels.setVisibility(8);
        if (this.et_search != null && this.et_search.getVisibility() == 0) {
            this.et_search.setVisibility(8);
        }
        if (this.ll_categories_view != null && this.ll_categories_view.getVisibility() == 0) {
            this.ll_categories_view.setVisibility(8);
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
                this.listChannels.setVisibility(0);
                this.et_search.setVisibility(0);
                this.ll_categories_view.setVisibility(0);
                this.listChannels.setFocusable(true);
                this.listChannels.requestFocus();
                return true;
            case 62:
            case 79:
            case 85:
                return uniqueDown ? true : true;
            case 66:
                this.listChannels.setVisibility(0);
                this.et_search.setVisibility(0);
                this.ll_categories_view.setVisibility(0);
                this.listChannels.setFocusable(true);
                this.listChannels.requestFocus();
                return true;
            case 86:
            case 126:
            case 127:
                return true;
            case 166:
                findViewById(R.id.exo_next).performClick();
                return true;
            case 167:
                findViewById(R.id.exo_prev).performClick();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
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

    private void previousLive() {
        if (!this.simpleExoPlayerView.getPlayer().getCurrentTimeline().isEmpty()) {
            int currentWindowIndex = this.simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            if (currentWindowIndex == 0) {
                this.player.seekTo(this.allStreams.size() - 1, C.TIME_UNSET);
            } else {
                this.player.seekTo(currentWindowIndex - 1, C.TIME_UNSET);
            }
        }
    }

    private void toggleResizeMode() {
        this.CURRENT_RESIZE_MODE++;
        if (this.CURRENT_RESIZE_MODE >= RESIZE_MODE.size()) {
            this.CURRENT_RESIZE_MODE = 0;
        }
        AppPref.getInstance(this).setResizeMode(this.CURRENT_RESIZE_MODE);
        this.simpleExoPlayerView.setResizeMode(((Integer) RESIZE_MODE.keySet().toArray()[this.CURRENT_RESIZE_MODE]).intValue());
        this.btn_screen.setImageResource(((Integer) RESIZE_MODE.get(Integer.valueOf(this.CURRENT_RESIZE_MODE))).intValue());
    }

    private void next() {
        if (!this.simpleExoPlayerView.getPlayer().getCurrentTimeline().isEmpty()) {
            int currentWindowIndex = this.simpleExoPlayerView.getPlayer().getCurrentWindowIndex();
            if (currentWindowIndex == this.allStreams.size() - 1) {
                this.player.seekTo(0, C.TIME_UNSET);
            } else {
                this.player.seekTo(currentWindowIndex + 1, C.TIME_UNSET);
            }
        }
    }

    public void onVisibilityChange(int visibility) {
        this.debugRootView.setVisibility(visibility);
        hideSystemUi();
        if (visibility == 1 || visibility == 0) {
            if ((getResources().getConfiguration().screenLayout & 15) == 3) {
                this.rlChannelList.setPadding(0, 0, 0, PsExtractor.AUDIO_STREAM);
            } else {
                this.rlChannelList.setPadding(0, 0, 0, 245);
            }
        }
        if (visibility == 8 && this.rlChannelList != null) {
            this.rlChannelList.setPadding(0, 0, 0, 0);
        }
    }

    @SuppressLint({"InlinedApi"})
    private void hideSystemUi() {
        this.simpleExoPlayerView.setSystemUiVisibility(4871);
    }

    private void initializePlayer() {
        Intent intent = getIntent();
        this.video_id = intent.getIntExtra("VIDEO_ID", 0);
        this.stream_type = "movie";
        this.extension = getIntent().getStringExtra("EXTENSION_TYPE");
        this.video_num = getIntent().getIntExtra("VIDEO_NUM", 0);
        int currentWindowIndex = this.video_num;
        if (this.video_title != null) {
            String vtitle = getIntent().getStringExtra("VIDEO_TITLE");
            if (!Utils.isEmpty(vtitle)) {
                this.video_title.setText(vtitle);
            }
        }
        boolean needNewPlayer = this.player == null;
        if (needNewPlayer) {
            TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            this.trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
            this.trackSelectionHelper = new TrackSelectionHelper(this.trackSelector, adaptiveTrackSelectionFactory);
            this.lastSeenTrackGroupArray = null;
            this.eventLogger = new EventLogger(this.trackSelector);
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (intent.hasExtra("drm_scheme") || intent.hasExtra(DRM_SCHEME_UUID_EXTRA)) {
                String drmLicenseUrl = intent.getStringExtra("drm_license_url");
                String[] keyRequestPropertiesArray = intent.getStringArrayExtra("drm_key_request_properties");
                boolean multiSession = intent.getBooleanExtra("drm_multi_session", false);
                int errorStringId = R.string.error_drm_unknown;
                if (Util.SDK_INT < 18) {
                    errorStringId = R.string.error_drm_not_supported;
                } else {
                    try {
                        drmSessionManager = buildDrmSessionManagerV18(DemoUtil.getDrmUuid(intent.getStringExtra(intent.hasExtra("drm_scheme") ? "drm_scheme" : DRM_SCHEME_UUID_EXTRA)), drmLicenseUrl, keyRequestPropertiesArray, multiSession);
                    } catch (UnsupportedDrmException e) {
                        errorStringId = e.reason == 1 ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown;
                    }
                }
                if (drmSessionManager == null) {
                    showToast(errorStringId);
                    return;
                }
            }
            boolean preferExtensionDecoders = intent.getBooleanExtra("prefer_extension_decoders", false);
            if (AppController.getInstance().useExtensionRenderers()) {
                if (preferExtensionDecoders) {
                }
            }
            this.player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getApplicationContext(), null, 2), new DefaultTrackSelector(), new DefaultLoadControl());
            this.player.addListener(new PlayerEventListener());
            this.player.addListener(this.eventLogger);
            this.player.addMetadataOutput(this.eventLogger);
            this.player.addAudioDebugListener(this.eventLogger);
            this.player.addVideoDebugListener(this.eventLogger);
            this.simpleExoPlayerView.setPlayer(this.player);
            this.player.setPlayWhenReady(this.shouldAutoPlay);
            this.debugViewHelper = new DebugTextViewHelper(this.player, this.debugTextView);
            this.debugViewHelper.start();
        }
        if (needNewPlayer || this.needRetrySource) {
            Uri[] uris;
            String[] extensions;
            int i;
            String action = intent.getAction();
            if ("com.google.android.exoplayer.demo.action.VIEW".equals(action)) {
                Uri stream_uri = buildURI(this.stream_type, this.video_id, this.extension);
                uris = new Uri[]{stream_uri};
                extensions = new String[uris.length];
            } else if ("com.google.android.exoplayer.demo.action.VIEW_LIST".equals(action)) {
                uris = new Uri[this.allStreams.size()];
                extensions = new String[this.allStreams.size()];
                for (i = 0; i < this.allStreams.size(); i++) {
                    uris[i] = buildURI(this.stream_type, Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(i)).getStreamId()), this.extension);
                }
            } else {
                showToast(getString(R.string.unexpected_intent_action, new Object[]{action}));
                return;
            }
            if (!Util.maybeRequestReadExternalStoragePermission(this, uris)) {
                MediaSource mediaSource;
                MediaSource[] mediaSources = new MediaSource[uris.length];
                for (i = 0; i < uris.length; i++) {
                    mediaSources[i] = buildMediaSource(uris[i], extensions[i], this.mainHandler, this.eventLogger);
                }
                if (mediaSources.length == 1) {
                    mediaSource = mediaSources[0];
                } else {
                    mediaSource = new ConcatenatingMediaSource(mediaSources);
                }
                String adTagUriString = intent.getStringExtra("ad_tag_uri");
                if (adTagUriString != null) {
                    Uri adTagUri = Uri.parse(adTagUriString);
                    if (!adTagUri.equals(this.loadedAdTagUri)) {
                        releaseAdsLoader();
                        this.loadedAdTagUri = adTagUri;
                    }
                    try {
                        mediaSource = createAdsMediaSource(mediaSource, Uri.parse(adTagUriString));
                    } catch (Exception e2) {
                        showToast("Ima not loaded");
                    }
                } else {
                    releaseAdsLoader();
                }
                boolean haveResumePosition = this.resumeWindow != -1;
                if (haveResumePosition) {
                    this.player.seekTo(this.resumeWindow, this.resumePosition);
                }
                this.player.seekTo(currentWindowIndex, C.TIME_UNSET);
                this.player.prepare(mediaSource, !haveResumePosition, false);
                this.needRetrySource = false;
                updateButtonVisibilities();
            } else {
                return;
            }
        }
        this.simpleExoPlayerView.showController();
    }

    private Uri buildURI(String stream_type, int stream_id, String extension) {
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        Builder builder = new Builder();
        try {
            builder.scheme("http").encodedAuthority(serverUrl + ":" + serverPort).appendPath(stream_type).appendPath(username).appendPath(password).appendPath(Integer.toString(stream_id) + "." + extension);
            return builder.build();
        } catch (Exception e) {
            Log.e("DB", "initializePlayer: " + e.getMessage());
            return null;
        }
    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
        int type;
        if (TextUtils.isEmpty(overrideExtension)) {
            type = Util.inferContentType(uri);
        } else {
            type = Util.inferContentType("." + overrideExtension);
        }
        switch (type) {
            case 0:
                return new DashMediaSource.Factory(new DefaultDashChunkSource.Factory(this.mediaDataSourceFactory), buildDataSourceFactory(false)).createMediaSource(uri, handler, listener);
            case 1:
                return new SsMediaSource.Factory(new DefaultSsChunkSource.Factory(this.mediaDataSourceFactory), buildDataSourceFactory(false)).createMediaSource(uri, handler, listener);
            case 2:
                return new HlsMediaSource.Factory(this.mediaDataSourceFactory).createMediaSource(uri, handler, listener);
            case 3:
                return new ExtractorMediaSource.Factory(this.mediaDataSourceFactory).createMediaSource(uri, handler, listener);
            default:
                throw new IllegalStateException("Unsupported type: " + type);
        }
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManagerV18(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession) throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl, buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i], keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback, null, this.mainHandler, this.eventLogger, multiSession);
    }

    private void releasePlayer() {
        if (this.player != null) {
            this.debugViewHelper.stop();
            this.debugViewHelper = null;
            this.shouldAutoPlay = this.player.getPlayWhenReady();
            updateResumePosition();
            this.player.release();
            this.player = null;
            this.trackSelector = null;
            this.trackSelectionHelper = null;
            this.eventLogger = null;
        }
    }

    private void updateResumePosition() {
        this.resumeWindow = this.player.getCurrentWindowIndex();
        this.resumePosition = Math.max(0, this.player.getContentPosition());
    }

    private void clearResumePosition() {
        this.resumeWindow = -1;
        this.resumePosition = C.TIME_UNSET;
    }

    private Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return AppController.getInstance().buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return AppController.getInstance().buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    private MediaSource createAdsMediaSource(MediaSource mediaSource, Uri adTagUri) throws Exception {
        Class<?> loaderClass = Class.forName("com.google.android.exoplayer2.ext.ima.ImaAdsLoader");
        if (this.adsLoader == null) {
            this.adsLoader = (AdsLoader) loaderClass.getConstructor(new Class[]{Context.class, Uri.class}).newInstance(new Object[]{this, adTagUri});
            this.adUiViewGroup = new FrameLayout(this);
            this.simpleExoPlayerView.getOverlayFrameLayout().addView(this.adUiViewGroup);
        }
        return new AdsMediaSource(mediaSource, new C19514(), this.adsLoader, this.adUiViewGroup, this.mainHandler, this.eventLogger);
    }

    private void releaseAdsLoader() {
        if (this.adsLoader != null) {
            this.adsLoader.release();
            this.adsLoader = null;
            this.loadedAdTagUri = null;
            this.simpleExoPlayerView.getOverlayFrameLayout().removeAllViews();
        }
    }

    private void updateButtonVisibilities() {
        this.debugRootView.removeAllViews();
        this.retryButton.setVisibility(this.needRetrySource ? 0 : 8);
        this.debugRootView.addView(this.retryButton);
        if (this.player != null) {
            MappedTrackInfo mappedTrackInfo = this.trackSelector.getCurrentMappedTrackInfo();
            if (mappedTrackInfo != null) {
                for (int i = 0; i < mappedTrackInfo.length; i++) {
                    if (mappedTrackInfo.getTrackGroups(i).length != 0) {
                        int label = 0;
                        Button button = new Button(this);
                        switch (this.player.getRendererType(i)) {
                            case 1:
                                label = R.string.audio;
                                break;
                            case 2:
                                label = R.string.video;
                                break;
                            case 3:
                                label = R.string.text;
                                break;
                            default:
                                break;
                        }
                        button.setText(label);
                        button.setTag(Integer.valueOf(i));
                        button.setOnClickListener(this);
                    }
                }
            }
        }
    }

    private void showControls() {
        this.debugRootView.setVisibility(0);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, 1).show();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != 0) {
            return false;
        }
        for (Throwable cause = e.getSourceException(); cause != null; cause = cause.getCause()) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
        }
        return false;
    }

    private void showLoader() {
        if (this.progressLoader != null) {
            this.progressLoader.setVisibility(0);
        }
    }

    private void hideLoader() {
        if (this.progressLoader != null) {
            this.progressLoader.setVisibility(8);
        }
    }
}

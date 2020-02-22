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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
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

public class PlayerSeriesActivity extends Activity implements OnClickListener, VisibilityListener {
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
    private static final Map<Integer, Integer> RESIZE_MODE = Collections.unmodifiableMap(new C19461());
    public static final String URI_LIST_EXTRA = "uri_list";
    private int CURRENT_RESIZE_MODE = 0;
    private ViewGroup adUiViewGroup;
    SearchableAdapter adapter;
    private AdsLoader adsLoader;
    private AppCompatImageView btnList;
    private AppCompatImageView btn_screen;
    private AppCompatImageView btn_settings;
    public ImageView channelLogo;
    public Context context;
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
    LiveStreamDBHandler liveStreamDBHandler;
    public LinearLayout ll_seekbar_time;
    private Uri loadedAdTagUri;
    private SharedPreferences loginPreferencesAfterLogin;
    private SharedPreferences loginPreferencesSharedPref;
    private SharedPreferences loginPreferencesSharedPref_allowed_format;
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
    private boolean shouldAutoPlay;
    private SimpleExoPlayerView simpleExoPlayerView;
    private TrackSelectionHelper trackSelectionHelper;
    private DefaultTrackSelector trackSelector;
    private String videoTitle;
    private int video_id;
    private int video_num;
    private AppCompatTextView video_title;

    static class C19461 extends HashMap<Integer, Integer> {
        C19461() {
            put(Integer.valueOf(3), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(0), Integer.valueOf(R.drawable.ic_fullscreen_black_24dp));
            put(Integer.valueOf(1), Integer.valueOf(R.drawable.ic_center_focus_strong_black_24dp));
            put(Integer.valueOf(2), Integer.valueOf(R.drawable.ic_zoom_out_map_black_24dp));
        }
    }

    class C19472 implements MediaSourceFactory {
        C19472() {
        }

        public MediaSource createMediaSource(Uri uri, @Nullable Handler handler, @Nullable MediaSourceEventListener listener) {
            return PlayerSeriesActivity.this.buildMediaSource(uri, null, handler, listener);
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
                PlayerSeriesActivity.this.showLoader();
            } else if (playbackState == 4) {
                PlayerSeriesActivity.this.showControls();
            } else if (playbackState == 3) {
                PlayerSeriesActivity.this.hideLoader();
            }
            PlayerSeriesActivity.this.updateButtonVisibilities();
        }

        public void onPositionDiscontinuity(int reason) {
            if (PlayerSeriesActivity.this.needRetrySource) {
                PlayerSeriesActivity.this.updateResumePosition();
            }
        }

        public void onPlayerError(ExoPlaybackException e) {
            String errorString = null;
            if (e.type == 1) {
                Exception cause = e.getRendererException();
                if (cause instanceof DecoderInitializationException) {
                    DecoderInitializationException decoderInitializationException = (DecoderInitializationException) cause;
                    if (decoderInitializationException.decoderName != null) {
                        errorString = PlayerSeriesActivity.this.getString(R.string.error_instantiating_decoder, new Object[]{decoderInitializationException.decoderName});
                    } else if (decoderInitializationException.getCause() instanceof DecoderQueryException) {
                        errorString = PlayerSeriesActivity.this.getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = PlayerSeriesActivity.this.getString(R.string.error_no_secure_decoder, new Object[]{decoderInitializationException.mimeType});
                    } else {
                        errorString = PlayerSeriesActivity.this.getString(R.string.error_no_decoder, new Object[]{decoderInitializationException.mimeType});
                    }
                }
            }
            if (errorString != null) {
                PlayerSeriesActivity.this.showToast(errorString);
            }
            PlayerSeriesActivity.this.needRetrySource = true;
            if (PlayerSeriesActivity.isBehindLiveWindow(e)) {
                PlayerSeriesActivity.this.clearResumePosition();
                PlayerSeriesActivity.this.initializePlayer();
                return;
            }
            PlayerSeriesActivity.this.updateResumePosition();
            PlayerSeriesActivity.this.updateButtonVisibilities();
            PlayerSeriesActivity.this.showControls();
            PlayerSeriesActivity.this.hideLoader();
        }

        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            PlayerSeriesActivity.this.updateButtonVisibilities();
            if (trackGroups != PlayerSeriesActivity.this.lastSeenTrackGroupArray) {
                MappedTrackInfo mappedTrackInfo = PlayerSeriesActivity.this.trackSelector.getCurrentMappedTrackInfo();
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTrackTypeRendererSupport(2) == 1) {
                        PlayerSeriesActivity.this.showToast((int) R.string.error_unsupported_video);
                    }
                    if (mappedTrackInfo.getTrackTypeRendererSupport(1) == 1) {
                        PlayerSeriesActivity.this.showToast((int) R.string.error_unsupported_audio);
                    }
                }
                PlayerSeriesActivity.this.lastSeenTrackGroupArray = trackGroups;
            }
        }
    }

    static {
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }
    private ArrayList<LiveStreamsDBModel> allStreams;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.liveStreamDBHandler = new LiveStreamDBHandler(this);
        this.allStreams = this.liveStreamDBHandler.getAllLiveStreasWithCategoryId(AppConst.PASSWORD_UNSET, "series");
        this.context = this;
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.shouldAutoPlay = true;
        clearResumePosition();
        this.mediaDataSourceFactory = buildDataSourceFactory(true);
        this.mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
        setContentView(R.layout.player_archive_activity);
        View rootView = findViewById(R.id.root);
        this.debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        this.debugTextView = (TextView) findViewById(R.id.debug_text_view);
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
        this.et_search = (EditText) findViewById(R.id.et_search);
        this.exo_playback_control_view = (RelativeLayout) findViewById(R.id.exo_playback_control_view);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressLoader = (ProgressBar) findViewById(R.id.progress_loader);
        this.ll_seekbar_time = (LinearLayout) findViewById(R.id.ll_seekbar_time);
        this.channelLogo = (ImageView) findViewById(R.id.iv_channel_logo);
        this.currentProgram = (TextView) findViewById(R.id.tv_current_program);
        this.currentProgramTime = (TextView) findViewById(R.id.tv_current_time);
        this.nextProgram = (TextView) findViewById(R.id.tv_next_program);
        this.nextProgramTime = (TextView) findViewById(R.id.tv_next_program_time);
        this.btn_screen.setOnClickListener(this);
        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        btn_back.setOnClickListener(this);
        this.btn_settings.setOnClickListener(this);
        btn_list.setOnClickListener(this);
        this.retryButton.setOnClickListener(this);
        this.simpleExoPlayerView.setControllerVisibilityListener(this);
        rootView.setOnClickListener(this);
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
        }
    }

    public void onResume() {
        super.onResume();
        hideSystemUi();
        if (Util.SDK_INT <= 23 || this.player == null) {
            initializePlayer();
        }
    }

    @SuppressLint({"InlinedApi"})
    private void hideSystemUi() {
        this.simpleExoPlayerView.setSystemUiVisibility(4871);
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
        int keyCode = event.getKeyCode();
        return this.simpleExoPlayerView.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
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
            case R.id.retry_button:
                initializePlayer();
                return;
            default:
                return;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean uniqueDown = event.getRepeatCount() == 0;
        switch (keyCode) {
            case 62:
            case 79:
            case 85:
                return uniqueDown ? true : true;
            case 86:
            case 126:
            case 127:
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

    public void onVisibilityChange(int visibility) {
        this.debugRootView.setVisibility(visibility);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */

    private void initializePlayer() {
        Intent intent = getIntent();
        this.video_id = intent.getIntExtra("VIDEO_ID", 0);
        String start_time = intent.getStringExtra("STREAM_START_TIME");
        String stop_time = intent.getStringExtra("STREAM_STOP_TIME");
        String stream_type = "series";

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
            //this.player.addListener(new PlayerVodActivity.PlayerEventListener());
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
                Uri stream_uri = buildURI(this.video_id,  this.extension, stop_time, start_time, extension);
                uris = new Uri[]{stream_uri};
                extensions = new String[uris.length];
            } else if ("com.google.android.exoplayer.demo.action.VIEW_LIST".equals(action)) {
                uris = new Uri[this.allStreams.size()];
                extensions = new String[this.allStreams.size()];
                for (i = 0; i < this.allStreams.size(); i++) {
                    uris[i] = buildURI(Integer.parseInt(((LiveStreamsDBModel) this.allStreams.get(i)).getStreamId()), extension, stop_time, start_time, this.extension);
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


//    private void initializePlayer() {
//        /*
//        r38 = this;
//        r25 = r38.getIntent();
//        r4 = r38.getIntent();
//        r6 = "VIDEO_ID";
//        r36 = 0;
//        r0 = r36;
//        r5 = r4.getIntExtra(r6, r0);
//        r4 = r38.getIntent();
//        r6 = "STREAM_START_TIME";
//        r8 = r4.getStringExtra(r6);
//        r4 = r38.getIntent();
//        r6 = "STREAM_STOP_TIME";
//        r7 = r4.getStringExtra(r6);
//        r4 = r38.getIntent();
//        r6 = "EXTENSION_TYPE";
//        r9 = r4.getStringExtra(r6);
//        r0 = r38;
//        r14 = r0.video_num;
//        r0 = r38;
//        r4 = r0.video_title;
//        if (r4 == 0) goto L_0x0053;
//    L_0x003a:
//        r4 = r38.getIntent();
//        r6 = "VIDEO_TITLE";
//        r35 = r4.getStringExtra(r6);
//        r4 = com.liveitandroid.liveit.miscelleneious.common.Utils.isEmpty(r35);
//        if (r4 != 0) goto L_0x0053;
//    L_0x004a:
//        r0 = r38;
//        r4 = r0.video_title;
//        r0 = r35;
//        r4.setText(r0);
//    L_0x0053:
//        r0 = r38;
//        r4 = r0.player;
//        if (r4 != 0) goto L_0x00d1;
//    L_0x0059:
//        r30 = 1;
//    L_0x005b:
//        if (r30 == 0) goto L_0x01ad;
//    L_0x005d:
//        r13 = new com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection$Factory;
//        r4 = BANDWIDTH_METER;
//        r13.<init>(r4);
//        r4 = new com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
//        r4.<init>(r13);
//        r0 = r38;
//        r0.trackSelector = r4;
//        r4 = new com.liveitandroid.liveit.view.exoplayer2.TrackSelectionHelper;
//        r0 = r38;
//        r6 = r0.trackSelector;
//        r4.<init>(r6, r13);
//        r0 = r38;
//        r0.trackSelectionHelper = r4;
//        r4 = 0;
//        r0 = r38;
//        r0.lastSeenTrackGroupArray = r4;
//        r4 = new com.liveitandroid.liveit.view.exoplayer2.EventLogger;
//        r0 = r38;
//        r6 = r0.trackSelector;
//        r4.<init>(r6);
//        r0 = r38;
//        r0.eventLogger = r4;
//        r18 = 0;
//        r4 = "drm_scheme";
//        r0 = r25;
//        r4 = r0.hasExtra(r4);
//        if (r4 != 0) goto L_0x00a2;
//    L_0x0098:
//        r4 = "drm_scheme_uuid";
//        r0 = r25;
//        r4 = r0.hasExtra(r4);
//        if (r4 == 0) goto L_0x010c;
//    L_0x00a2:
//        r4 = "drm_license_url";
//        r0 = r25;
//        r15 = r0.getStringExtra(r4);
//        r4 = "drm_key_request_properties";
//        r0 = r25;
//        r26 = r0.getStringArrayExtra(r4);
//        r4 = "drm_multi_session";
//        r6 = 0;
//        r0 = r25;
//        r29 = r0.getBooleanExtra(r4, r6);
//        r20 = 2131820671; // 0x7f11007f float:1.9274064E38 double:1.0532593566E-314;
//        r4 = com.google.android.exoplayer2.util.Util.SDK_INT;
//        r6 = 18;
//        if (r4 >= r6) goto L_0x00d4;
//    L_0x00c4:
//        r20 = 2131820670; // 0x7f11007e float:1.9274061E38 double:1.053259356E-314;
//    L_0x00c7:
//        if (r18 != 0) goto L_0x010c;
//    L_0x00c9:
//        r0 = r38;
//        r1 = r20;
//        r0.showToast(r1);
//    L_0x00d0:
//        return;
//    L_0x00d1:
//        r30 = 0;
//        goto L_0x005b;
//    L_0x00d4:
//        r4 = "drm_scheme";
//        r0 = r25;
//        r4 = r0.hasExtra(r4);	 Catch:{ UnsupportedDrmException -> 0x00fc }
//        if (r4 == 0) goto L_0x00f9;
//    L_0x00de:
//        r16 = "drm_scheme";
//    L_0x00e0:
//        r0 = r25;
//        r1 = r16;
//        r4 = r0.getStringExtra(r1);	 Catch:{ UnsupportedDrmException -> 0x00fc }
//        r17 = com.liveitandroid.liveit.view.exoplayer2.DemoUtil.getDrmUuid(r4);	 Catch:{ UnsupportedDrmException -> 0x00fc }
//        r0 = r38;
//        r1 = r17;
//        r2 = r26;
//        r3 = r29;
//        r18 = r0.buildDrmSessionManagerV18(r1, r15, r2, r3);	 Catch:{ UnsupportedDrmException -> 0x00fc }
//        goto L_0x00c7;
//    L_0x00f9:
//        r16 = "drm_scheme_uuid";
//        goto L_0x00e0;
//    L_0x00fc:
//        r19 = move-exception;
//        r0 = r19;
//        r4 = r0.reason;
//        r6 = 1;
//        if (r4 != r6) goto L_0x0108;
//    L_0x0104:
//        r20 = 2131820672; // 0x7f110080 float:1.9274066E38 double:1.053259357E-314;
//    L_0x0107:
//        goto L_0x00c7;
//    L_0x0108:
//        r20 = 2131820671; // 0x7f11007f float:1.9274064E38 double:1.0532593566E-314;
//        goto L_0x0107;
//    L_0x010c:
//        r4 = "prefer_extension_decoders";
//        r6 = 0;
//        r0 = r25;
//        r31 = r0.getBooleanExtra(r4, r6);
//        r4 = com.liveitandroid.liveit.view.app_shell.AppController.getInstance();
//        r4 = r4.useExtensionRenderers();
//        if (r4 == 0) goto L_0x0221;
//    L_0x011f:
//        if (r31 == 0) goto L_0x021d;
//    L_0x0121:
//        r21 = 2;
//    L_0x0123:
//        r32 = new com.google.android.exoplayer2.DefaultRenderersFactory;
//        r4 = 2;
//        r0 = r32;
//        r1 = r38;
//        r2 = r18;
//        r0.<init>(r1, r2, r4);
//        r0 = r38;
//        r4 = r0.trackSelector;
//        r0 = r32;
//        r4 = com.google.android.exoplayer2.ExoPlayerFactory.newSimpleInstance(r0, r4);
//        r0 = r38;
//        r0.player = r4;
//        r0 = r38;
//        r4 = r0.player;
//        r6 = new com.liveitandroid.liveit.view.exoplayer2.PlayerSeriesActivity$PlayerEventListener;
//        r36 = 0;
//        r0 = r38;
//        r1 = r36;
//        r6.<init>();
//        r4.addListener(r6);
//        r0 = r38;
//        r4 = r0.player;
//        r0 = r38;
//        r6 = r0.eventLogger;
//        r4.addListener(r6);
//        r0 = r38;
//        r4 = r0.player;
//        r0 = r38;
//        r6 = r0.eventLogger;
//        r4.addMetadataOutput(r6);
//        r0 = r38;
//        r4 = r0.player;
//        r0 = r38;
//        r6 = r0.eventLogger;
//        r4.addAudioDebugListener(r6);
//        r0 = r38;
//        r4 = r0.player;
//        r0 = r38;
//        r6 = r0.eventLogger;
//        r4.addVideoDebugListener(r6);
//        r0 = r38;
//        r4 = r0.simpleExoPlayerView;
//        r0 = r38;
//        r6 = r0.player;
//        r4.setPlayer(r6);
//        r0 = r38;
//        r4 = r0.player;
//        r0 = r38;
//        r6 = r0.shouldAutoPlay;
//        r4.setPlayWhenReady(r6);
//        r4 = new com.google.android.exoplayer2.ui.DebugTextViewHelper;
//        r0 = r38;
//        r6 = r0.player;
//        r0 = r38;
//        r0 = r0.debugTextView;
//        r36 = r0;
//        r0 = r36;
//        r4.<init>(r6, r0);
//        r0 = r38;
//        r0.debugViewHelper = r4;
//        r0 = r38;
//        r4 = r0.debugViewHelper;
//        r4.start();
//    L_0x01ad:
//        if (r30 != 0) goto L_0x01b5;
//    L_0x01af:
//        r0 = r38;
//        r4 = r0.needRetrySource;
//        if (r4 == 0) goto L_0x02b5;
//    L_0x01b5:
//        r10 = r25.getAction();
//        r4 = 0;
//        r0 = new android.net.Uri[r4];
//        r34 = r0;
//        r4 = 0;
//        r0 = new java.lang.String[r4];
//        r22 = r0;
//        r4 = "com.google.android.exoplayer.demo.action.VIEW";
//        r4 = r4.equals(r10);
//        if (r4 == 0) goto L_0x0225;
//    L_0x01cb:
//        r0 = r38;
//        r6 = r0.extension;
//        r4 = r38;
//        r33 = r4.buildURI(r5, r6, r7, r8, r9);
//        r4 = 1;
//        r0 = new android.net.Uri[r4];
//        r34 = r0;
//        r4 = 0;
//        r34[r4] = r33;
//        r0 = r34;
//        r4 = r0.length;
//        r0 = new java.lang.String[r4];
//        r22 = r0;
//    L_0x01e4:
//        r0 = r38;
//        r1 = r34;
//        r4 = com.google.android.exoplayer2.util.Util.maybeRequestReadExternalStoragePermission(r0, r1);
//        if (r4 != 0) goto L_0x00d0;
//    L_0x01ee:
//        r0 = r34;
//        r4 = r0.length;
//        r0 = new com.google.android.exoplayer2.source.MediaSource[r4];
//        r28 = r0;
//        r24 = 0;
//    L_0x01f7:
//        r0 = r34;
//        r4 = r0.length;
//        r0 = r24;
//        if (r0 >= r4) goto L_0x0244;
//    L_0x01fe:
//        r4 = r34[r24];
//        r6 = r22[r24];
//        r0 = r38;
//        r0 = r0.mainHandler;
//        r36 = r0;
//        r0 = r38;
//        r0 = r0.eventLogger;
//        r37 = r0;
//        r0 = r38;
//        r1 = r36;
//        r2 = r37;
//        r4 = r0.buildMediaSource(r4, r6, r1, r2);
//        r28[r24] = r4;
//        r24 = r24 + 1;
//        goto L_0x01f7;
//    L_0x021d:
//        r21 = 1;
//        goto L_0x0123;
//    L_0x0221:
//        r21 = 0;
//        goto L_0x0123;
//    L_0x0225:
//        r4 = "com.google.android.exoplayer.demo.action.VIEW_LIST";
//        r4 = r4.equals(r10);
//        if (r4 != 0) goto L_0x01e4;
//    L_0x022d:
//        r4 = 2131821070; // 0x7f11020e float:1.9274873E38 double:1.053259554E-314;
//        r6 = 1;
//        r6 = new java.lang.Object[r6];
//        r36 = 0;
//        r6[r36] = r10;
//        r0 = r38;
//        r4 = r0.getString(r4, r6);
//        r0 = r38;
//        r0.showToast(r4);
//        goto L_0x00d0;
//    L_0x0244:
//        r0 = r28;
//        r4 = r0.length;
//        r6 = 1;
//        if (r4 != r6) goto L_0x02be;
//    L_0x024a:
//        r4 = 0;
//        r27 = r28[r4];
//    L_0x024d:
//        r4 = "ad_tag_uri";
//        r0 = r25;
//        r12 = r0.getStringExtra(r4);
//        if (r12 == 0) goto L_0x02cd;
//    L_0x0257:
//        r11 = android.net.Uri.parse(r12);
//        r0 = r38;
//        r4 = r0.loadedAdTagUri;
//        r4 = r11.equals(r4);
//        if (r4 != 0) goto L_0x026c;
//    L_0x0265:
//        r38.releaseAdsLoader();
//        r0 = r38;
//        r0.loadedAdTagUri = r11;
//    L_0x026c:
//        r4 = android.net.Uri.parse(r12);	 Catch:{ Exception -> 0x02c4 }
//        r0 = r38;
//        r1 = r27;
//        r27 = r0.createAdsMediaSource(r1, r4);	 Catch:{ Exception -> 0x02c4 }
//    L_0x0278:
//        r0 = r38;
//        r4 = r0.resumeWindow;
//        r6 = -1;
//        if (r4 == r6) goto L_0x02d1;
//    L_0x027f:
//        r23 = 1;
//    L_0x0281:
//        if (r23 == 0) goto L_0x0296;
//    L_0x0283:
//        r0 = r38;
//        r4 = r0.player;
//        r0 = r38;
//        r6 = r0.resumeWindow;
//        r0 = r38;
//        r0 = r0.resumePosition;
//        r36 = r0;
//        r0 = r36;
//        r4.seekTo(r6, r0);
//    L_0x0296:
//        r0 = r38;
//        r4 = r0.player;
//        r4.seekToDefaultPosition();
//        r0 = r38;
//        r6 = r0.player;
//        if (r23 != 0) goto L_0x02d4;
//    L_0x02a3:
//        r4 = 1;
//    L_0x02a4:
//        r36 = 0;
//        r0 = r27;
//        r1 = r36;
//        r6.prepare(r0, r4, r1);
//        r4 = 0;
//        r0 = r38;
//        r0.needRetrySource = r4;
//        r38.updateButtonVisibilities();
//    L_0x02b5:
//        r0 = r38;
//        r4 = r0.simpleExoPlayerView;
//        r4.showController();
//        goto L_0x00d0;
//    L_0x02be:
//        r27 = new com.google.android.exoplayer2.source.ConcatenatingMediaSource;
//        r27.<init>(r28);
//        goto L_0x024d;
//    L_0x02c4:
//        r19 = move-exception;
//        r4 = "Ima not loaded";
//        r0 = r38;
//        r0.showToast(r4);
//        goto L_0x0278;
//    L_0x02cd:
//        r38.releaseAdsLoader();
//        goto L_0x0278;
//    L_0x02d1:
//        r23 = 0;
//        goto L_0x0281;
//    L_0x02d4:
//        r4 = 0;
//        goto L_0x02a4;
//        */
//        throw new UnsupportedOperationException("Method not decompiled: com.liveitandroid.liveit.view.exoplayer2.PlayerSeriesActivity.initializePlayer():void");
//    }

    private Uri buildURI(int stream_id, String extension, String stream_stop_time, String stream_start_time, String streaExt) {
        this.loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        this.loginPreferencesSharedPref_allowed_format = this.context.getSharedPreferences(AppConst.LOGIN_PREF_ALLOWED_FORMAT, 0);
        String username = this.loginPreferencesSharedPref.getString("username", "");
        String password = this.loginPreferencesSharedPref.getString("password", "");
        String allowedFormat = this.loginPreferencesSharedPref_allowed_format.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = this.loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        Builder builder = new Builder();
        try {
            builder.scheme("http").encodedAuthority(serverUrl + ":" + serverPort).appendPath("series").appendPath(username).appendPath(password).appendPath(Integer.toString(stream_id) + "." + streaExt);
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
        return new AdsMediaSource(mediaSource, new C19472(), this.adsLoader, this.adUiViewGroup, this.mainHandler, this.eventLogger);
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
            this.progressLoader.setVisibility(View.GONE);
        }
    }
}

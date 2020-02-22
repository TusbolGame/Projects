package com.liveitandroid.liveit.view.nstplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import org.joda.time.DateTimeConstants;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaCodecInfo;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class NSTPlayerVod {
    private static final int MESSAGE_FADE_OUT = 2;
    private static final int MESSAGE_HIDE_CENTER_BOX = 4;
    private static final int MESSAGE_RESTART_PLAY = 5;
    private static final int MESSAGE_SEEK_NEW_POSITION = 3;
    private static final int MESSAGE_SHOW_PROGRESS = 1;
    public static final String SCALETYPE_16_9 = "16:9";
    public static final String SCALETYPE_4_3 = "4:3";
    public static final String SCALETYPE_FILLPARENT = "fillParent";
    public static final String SCALETYPE_FITPARENT = "fitParent";
    public static final String SCALETYPE_FITXY = "fitXY";
    public static final String SCALETYPE_WRAPCONTENT = "wrapContent";
    private Query f40$;
    private int STATUS_COMPLETED = 4;
    private int STATUS_ERROR = -1;
    private int STATUS_IDLE = 0;
    private int STATUS_LOADING = 1;
    private int STATUS_PAUSE = 3;
    private int STATUS_PLAYING = 2;
    private final Activity activity;
    private final AudioManager audioManager;
    private float brightness = -1.0f;
    private int currentPosition;
    private int currentWindowIndex = 0;
    private long defaultRetryTime = DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
    private int defaultTimeout = 7000;
    private long duration;
    private boolean fullScreenOnly;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    NSTPlayerVod.this.setProgress();
                    if (!NSTPlayerVod.this.isDragging && NSTPlayerVod.this.isShowing) {
                        sendMessageDelayed(obtainMessage(1), 1000);
                        NSTPlayerVod.this.updatePausePlay();
                        return;
                    }
                    return;
                case 2:
                    NSTPlayerVod.this.hide(false);
                    return;
                case 3:
                    if (!NSTPlayerVod.this.isLive && NSTPlayerVod.this.newPosition >= 0) {
                        NSTPlayerVod.this.videoView.seekTo((int) NSTPlayerVod.this.newPosition);
                        NSTPlayerVod.this.newPosition = -1;
                        return;
                    }
                    return;
                case 4:
                    NSTPlayerVod.this.f40$.id(R.id.app_video_volume_box).gone();
                    NSTPlayerVod.this.f40$.id(R.id.app_video_brightness_box).gone();
                    NSTPlayerVod.this.f40$.id(R.id.app_video_fastForward_box).gone();
                    return;
                default:
                    return;
            }
        }
    };
    private final int initHeight;
    private boolean instantSeeking;
    private boolean isDragging;
    private boolean isLive = false;
    private boolean isShowing;
    private final int mMaxVolume;
    private final OnSeekBarChangeListener mSeekListener = new C20756();
    private long newPosition = -1;
    private final OnClickListener onClickListener = new C20701();
    public OnControlPanelVisibilityChangeListener onControlPanelVisibilityChangeListener = new C20745();
    private OnErrorListener onErrorListener = new C20712();
    private OnInfoListener onInfoListener = new C20734();
    private Runnable oncomplete = new C20723();
    private OrientationEventListener orientationEventListener;
    private long pauseTime;
    private boolean playerSupport;
    private boolean portrait;
    private int screenWidthPixels;
    private final SeekBar seekBar;
    Editor sharedPrefEditor;
    SharedPreferences sharedPreferences;
    private int status = this.STATUS_IDLE;
    private String url;
    private final IjkVideoView videoView;
    private int volume = -1;

    class C20701 implements OnClickListener {
        C20701() {
        }

        public void onClick(View v) {
            if (v.getId() == R.id.app_video_fullscreen) {
                NSTPlayerVod.this.toggleFullScreen();
            } else if (v.getId() == R.id.app_video_play) {
                NSTPlayerVod.this.doPauseResume();
                NSTPlayerVod.this.show(NSTPlayerVod.this.defaultTimeout);
            } else if (v.getId() == R.id.app_video_replay_icon) {
                NSTPlayerVod.this.videoView.seekTo(0);
                NSTPlayerVod.this.videoView.start();
                NSTPlayerVod.this.doPauseResume();
            } else if (v.getId() != R.id.app_video_finish) {
            } else {
                if (NSTPlayerVod.this.fullScreenOnly || NSTPlayerVod.this.portrait) {
                    NSTPlayerVod.this.activity.finish();
                } else {
                    NSTPlayerVod.this.activity.setRequestedOrientation(1);
                }
            }
        }
    }

    public interface OnErrorListener {
        void onError(int i, int i2);
    }

    class C20712 implements OnErrorListener {
        C20712() {
        }

        public void onError(int what, int extra) {
        }
    }

    class C20723 implements Runnable {
        C20723() {
        }

        public void run() {
        }
    }

    public interface OnInfoListener {
        void onInfo(int i, int i2);
    }

    class C20734 implements OnInfoListener {
        C20734() {
        }

        public void onInfo(int what, int extra) {
        }
    }

    public interface OnControlPanelVisibilityChangeListener {
        void change(boolean z);
    }

    class C20745 implements OnControlPanelVisibilityChangeListener {
        C20745() {
        }

        public void change(boolean isShowing) {
        }
    }

    class C20756 implements OnSeekBarChangeListener {
        C20756() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                NSTPlayerVod.this.f40$.id(R.id.app_video_status).gone();
                int newPosition = (int) ((((double) (NSTPlayerVod.this.duration * ((long) progress))) * 1.0d) / 1000.0d);
                String time = NSTPlayerVod.this.generateTime((long) newPosition);
                if (NSTPlayerVod.this.instantSeeking) {
                    NSTPlayerVod.this.videoView.seekTo(newPosition);
                }
                NSTPlayerVod.this.f40$.id(R.id.app_video_currentTime).text(time);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            NSTPlayerVod.this.isDragging = true;
            NSTPlayerVod.this.show(3600000);
            NSTPlayerVod.this.handler.removeMessages(1);
            if (NSTPlayerVod.this.instantSeeking) {
                NSTPlayerVod.this.audioManager.setStreamMute(3, true);
            }
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            if (!NSTPlayerVod.this.instantSeeking) {
                NSTPlayerVod.this.videoView.seekTo((int) ((((double) (NSTPlayerVod.this.duration * ((long) seekBar.getProgress()))) * 1.0d) / 1000.0d));
            }
            NSTPlayerVod.this.show(NSTPlayerVod.this.defaultTimeout);
            NSTPlayerVod.this.handler.removeMessages(1);
            NSTPlayerVod.this.audioManager.setStreamMute(3, false);
            NSTPlayerVod.this.isDragging = false;
            NSTPlayerVod.this.handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    class C20778 implements IMediaPlayer.OnCompletionListener {
        C20778() {
        }

        public void onCompletion(IMediaPlayer mp) {
            NSTPlayerVod.this.statusChange(NSTPlayerVod.this.STATUS_COMPLETED);
            NSTPlayerVod.this.oncomplete.run();
        }
    }

    class C20789 implements IMediaPlayer.OnErrorListener {
        C20789() {
        }

        public boolean onError(IMediaPlayer mp, int what, int extra) {
            NSTPlayerVod.this.statusChange(NSTPlayerVod.this.STATUS_ERROR);
            NSTPlayerVod.this.onErrorListener.onError(what, extra);
            return true;
        }
    }

    public class PlayerGestureListener extends SimpleOnGestureListener {
        private boolean firstTouch;
        private boolean toSeek;
        private boolean volumeControl;

        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        public boolean onDown(MotionEvent e) {
            this.firstTouch = true;
            return super.onDown(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean z = true;
            if (!(e1 == null || e2 == null)) {
                float mOldX = e1.getX();
                float deltaY = e1.getY() - e2.getY();
                float deltaX = mOldX - e2.getX();
                if (this.firstTouch) {
                    boolean z2;
                    if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    this.toSeek = z2;
                    if (mOldX <= ((float) NSTPlayerVod.this.screenWidthPixels) * 0.5f) {
                        z = false;
                    }
                    this.volumeControl = z;
                    this.firstTouch = false;
                }
                if (!this.toSeek) {
                    float percent = deltaY / ((float) NSTPlayerVod.this.videoView.getHeight());
                    if (this.volumeControl) {
                        NSTPlayerVod.this.onVolumeSlide(percent);
                    } else {
                        NSTPlayerVod.this.onBrightnessSlide(percent);
                    }
                } else if (!NSTPlayerVod.this.isLive) {
                    NSTPlayerVod.this.onProgressSlide((-deltaX) / ((float) NSTPlayerVod.this.videoView.getWidth()));
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onSingleTapUp(MotionEvent e) {
            if (NSTPlayerVod.this.isShowing) {
                NSTPlayerVod.this.hide(false);
            } else {
                GridView channelListView = (GridView) NSTPlayerVod.this.activity.findViewById(R.id.lv_ch);
                EditText searchEditText = (EditText) NSTPlayerVod.this.activity.findViewById(R.id.et_search);
                LinearLayout ll_categories_view = (LinearLayout) NSTPlayerVod.this.activity.findViewById(R.id.ll_categories_view);
                LinearLayout topBoxLinearLayout = (LinearLayout) NSTPlayerVod.this.activity.findViewById(R.id.app_video_top_box);
                LinearLayout controlsLinearLayout = (LinearLayout) NSTPlayerVod.this.activity.findViewById(R.id.controls);
                RelativeLayout bottomBoxRelativeLayout = (RelativeLayout) NSTPlayerVod.this.activity.findViewById(R.id.app_video_bottom_box);
                if (channelListView.getVisibility() == 0) {
                    channelListView.setVisibility(8);
                    searchEditText.setVisibility(8);
                } else if (topBoxLinearLayout.getVisibility() == 0) {
                    topBoxLinearLayout.setVisibility(8);
                    controlsLinearLayout.setVisibility(8);
                    bottomBoxRelativeLayout.setVisibility(8);
                } else {
                    NSTPlayerVod.this.show(NSTPlayerVod.this.defaultTimeout);
                }
            }
            return true;
        }
    }

    class Query {
        private final Activity activity;
        private View view;

        public Query(Activity activity) {
            this.activity = activity;
        }

        public Query id(int id) {
            this.view = this.activity.findViewById(id);
            return this;
        }

        public Query image(int resId) {
            if (this.view instanceof ImageView) {
                ((ImageView) this.view).setImageResource(resId);
            }
            return this;
        }

        public Query visible() {
            if (this.view != null) {
                this.view.setVisibility(0);
            }
            return this;
        }

        public Query gone() {
            if (this.view != null) {
                this.view.setVisibility(8);
            }
            return this;
        }

        public Query invisible() {
            if (this.view != null) {
                this.view.setVisibility(4);
            }
            return this;
        }

        public Query clicked(OnClickListener handler) {
            if (this.view != null) {
                this.view.setOnClickListener(handler);
            }
            return this;
        }

        public Query text(CharSequence text) {
            if (this.view != null && (this.view instanceof TextView)) {
                ((TextView) this.view).setText(text);
            }
            return this;
        }

        public Query visibility(int visible) {
            if (this.view != null) {
                this.view.setVisibility(visible);
            }
            return this;
        }

        private void size(boolean width, int n, boolean dip) {
            if (this.view != null) {
                LayoutParams lp = this.view.getLayoutParams();
                if (n > 0 && dip) {
                    n = dip2pixel(this.activity, (float) n);
                }
                if (width) {
                    lp.width = n;
                } else {
                    lp.height = n;
                }
                this.view.setLayoutParams(lp);
            }
        }

        public void height(int height, boolean dip) {
            size(false, height, dip);
        }

        public int dip2pixel(Context context, float n) {
            return (int) TypedValue.applyDimension(1, n, context.getResources().getDisplayMetrics());
        }

        public float pixel2dip(Context context, float n) {
            return n / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
        }
    }

    public void setCurrentWindowIndex(int index) {
        this.currentWindowIndex = index;
    }

    public void setDefaultRetryTime(long defaultRetryTime) {
        this.defaultRetryTime = defaultRetryTime;
    }

    public void setTitle(CharSequence title) {
        this.f40$.id(R.id.app_video_title).text(title);
    }

    public void doPauseResume() {
        if (this.status == this.STATUS_COMPLETED) {
            this.f40$.id(R.id.app_video_replay).gone();
            this.videoView.seekTo(0);
            this.videoView.start();
        } else if (this.videoView.isPlaying()) {
            statusChange(this.STATUS_PAUSE);
            this.videoView.pause();
        } else {
            statusChange(this.STATUS_PLAYING);
            this.videoView.start();
        }
    }

    private void updatePausePlay() {
        if (this.videoView.isPlaying()) {
            this.f40$.id(R.id.exo_play).gone();
            this.f40$.id(R.id.exo_pause).visible();
            return;
        }
        this.f40$.id(R.id.exo_pause).gone();
        this.f40$.id(R.id.exo_play).visible();
    }

    @SuppressLint({"InlinedApi"})
    public void hideSystemUi() {
        this.videoView.setSystemUiVisibility(4871);
    }

    public void show(int timeout) {
        if (!this.isShowing) {
            this.f40$.id(R.id.app_video_top_box).visible();
            this.f40$.id(R.id.app_video_bottom_box).visible();
            this.f40$.id(R.id.controls).visible();
            if (!this.isLive) {
                showBottomControl(true);
            }
            if (!this.fullScreenOnly) {
                this.f40$.id(R.id.app_video_fullscreen).visible();
            }
            GridView channelListView = (GridView) this.activity.findViewById(R.id.lv_ch);
            EditText searchEditText = (EditText) this.activity.findViewById(R.id.et_search);
            LinearLayout ll_categories_view = (LinearLayout) this.activity.findViewById(R.id.ll_categories_view);
            channelListView.setVisibility(0);
            searchEditText.setVisibility(0);
            ll_categories_view.setVisibility(0);
            channelListView.setFocusable(true);
            channelListView.requestFocus();
            this.isShowing = true;
            this.onControlPanelVisibilityChangeListener.change(true);
        }
        updatePausePlay();
        this.handler.sendEmptyMessage(1);
        this.handler.removeMessages(2);
        if (timeout != 0) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(2), (long) timeout);
        }
    }

    public void showBottomControl(boolean show) {
        int i;
        int i2 = 0;
        Query id = this.f40$.id(R.id.app_video_currentTime);
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        id.visibility(i);
        id = this.f40$.id(R.id.app_video_endTime);
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        id.visibility(i);
        Query id2 = this.f40$.id(R.id.app_video_seekBar);
        if (!show) {
            i2 = 8;
        }
        id2.visibility(i2);
    }

    public NSTPlayerVod(final Activity activity) {
        boolean z;
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            this.playerSupport = true;
        } catch (Throwable e) {
            Log.e("NSTPlayerVod", "loadLibraries error", e);
        }
        this.activity = activity;
        this.screenWidthPixels = activity.getResources().getDisplayMetrics().widthPixels;
        this.f40$ = new Query(activity);
        this.videoView = (IjkVideoView) activity.findViewById(R.id.video_view);
        this.videoView.setOnCompletionListener(new C20778());
        this.videoView.setOnErrorListener(new C20789());
        this.videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                switch (what) {
                    case 3:
                        NSTPlayerVod.this.statusChange(NSTPlayerVod.this.STATUS_PLAYING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START /*701*/:
                        NSTPlayerVod.this.statusChange(NSTPlayerVod.this.STATUS_LOADING);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END /*702*/:
                        NSTPlayerVod.this.statusChange(NSTPlayerVod.this.STATUS_PLAYING);
                        break;
                }
                NSTPlayerVod.this.onInfoListener.onInfo(what, extra);
                return false;
            }
        });
        this.seekBar = (SeekBar) activity.findViewById(R.id.app_video_seekBar);
        this.seekBar.setMax(1000);
        this.seekBar.setOnSeekBarChangeListener(this.mSeekListener);
        this.f40$.id(R.id.app_video_fullscreen).clicked(this.onClickListener);
        this.f40$.id(R.id.app_video_finish).clicked(this.onClickListener);
        this.f40$.id(R.id.app_video_replay_icon).clicked(this.onClickListener);
        this.audioManager = (AudioManager) activity.getSystemService("audio");
        this.mMaxVolume = this.audioManager.getStreamMaxVolume(3);
        final GestureDetector gestureDetector = new GestureDetector(activity, new PlayerGestureListener());
        View liveBox = activity.findViewById(R.id.app_video_box);
        final GridView channelListView = (GridView) activity.findViewById(R.id.lv_ch);
        final EditText searchEditText = (EditText) activity.findViewById(R.id.et_search);
        final LinearLayout ll_categories_view = (LinearLayout) activity.findViewById(R.id.ll_categories_view);
        liveBox.setClickable(true);
        liveBox.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (channelListView.getVisibility() == 0) {
                    channelListView.setVisibility(8);
                    searchEditText.setVisibility(8);
                    ll_categories_view.setVisibility(8);
                    NSTPlayerVod.this.isShowing = true;
                    return true;
                }
                if (motionEvent != null) {
                    if (!gestureDetector.onTouchEvent(motionEvent)) {
                        switch (motionEvent.getAction() & 255) {
                            case 1:
                                NSTPlayerVod.this.endGesture();
                                break;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        this.orientationEventListener = new OrientationEventListener(activity) {
            public void onOrientationChanged(int orientation) {
                if ((orientation < 0 || orientation > 30) && orientation < 330 && (orientation < 150 || orientation > 210)) {
                    if (((orientation >= 90 && orientation <= 120) || (orientation >= PsExtractor.VIDEO_STREAM_MASK && orientation <= IjkMediaCodecInfo.RANK_SECURE)) && !NSTPlayerVod.this.portrait) {
                        activity.setRequestedOrientation(4);
                        NSTPlayerVod.this.orientationEventListener.disable();
                    }
                } else if (NSTPlayerVod.this.portrait) {
                    activity.setRequestedOrientation(4);
                    NSTPlayerVod.this.orientationEventListener.disable();
                }
            }
        };
        if (this.fullScreenOnly) {
            activity.setRequestedOrientation(0);
        }
        if (getScreenOrientation() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.portrait = z;
        this.initHeight = activity.findViewById(R.id.app_video_box).getLayoutParams().height;
        hideAll();
        if (!this.playerSupport) {
            showStatus(activity.getResources().getString(R.string.not_support));
        }
    }

    private void endGesture() {
        this.volume = -1;
        this.brightness = -1.0f;
        if (this.newPosition >= 0) {
            this.handler.removeMessages(3);
            this.handler.sendEmptyMessage(3);
        }
        this.handler.removeMessages(4);
        this.handler.sendEmptyMessageDelayed(4, 500);
    }

    private void statusChange(int newStatus) {
        this.status = newStatus;
        if (!this.isLive && newStatus == this.STATUS_COMPLETED) {
            this.handler.removeMessages(1);
            hideAll();
            this.f40$.id(R.id.app_video_replay).visible();
        } else if (newStatus == this.STATUS_ERROR) {
            this.handler.removeMessages(1);
            hideAll();
            if (this.isLive) {
                showStatus(this.activity.getResources().getString(R.string.small_problem));
                if (this.defaultRetryTime > 0) {
                    this.handler.sendEmptyMessageDelayed(5, this.defaultRetryTime);
                    return;
                }
                return;
            }
            showStatus(this.activity.getResources().getString(R.string.small_problem));
        } else if (newStatus == this.STATUS_LOADING) {
            hideAll();
            this.f40$.id(R.id.app_video_loading).visible();
        } else if (newStatus == this.STATUS_PLAYING) {
            this.f40$.id(R.id.exo_play).gone();
            this.f40$.id(R.id.exo_pause).visible();
            hideAll();
        } else if (newStatus == this.STATUS_PAUSE) {
            this.f40$.id(R.id.exo_play).visible();
            this.f40$.id(R.id.exo_pause).gone();
            show(this.defaultTimeout);
        }
    }

    private void hideAll() {
        this.f40$.id(R.id.app_video_replay).gone();
        this.f40$.id(R.id.app_video_top_box).gone();
        this.f40$.id(R.id.app_video_bottom_box).gone();
        this.f40$.id(R.id.controls).gone();
        this.f40$.id(R.id.app_video_loading).gone();
        this.f40$.id(R.id.app_video_fullscreen).invisible();
        this.f40$.id(R.id.app_video_status).gone();
        showBottomControl(false);
        this.onControlPanelVisibilityChangeListener.change(false);
    }

    public void showAll() {
        this.isShowing = true;
        this.f40$.id(R.id.app_video_top_box).visible();
        this.f40$.id(R.id.app_video_bottom_box).visible();
        this.f40$.id(R.id.controls).visible();
        showBottomControl(true);
        show(this.defaultTimeout);
    }

    public void onPause() {
        this.pauseTime = System.currentTimeMillis();
        show(0);
        if (this.status == this.STATUS_PLAYING) {
            this.videoView.pause();
            if (!this.isLive) {
                this.currentPosition = this.videoView.getCurrentPosition();
            }
        }
    }

    public void onResume() {
        this.pauseTime = 0;
        if (this.status == this.STATUS_PLAYING) {
            if (this.isLive) {
                this.videoView.seekTo(0);
            } else if (this.currentPosition > 0) {
                this.videoView.seekTo(this.currentPosition);
            }
            this.videoView.start();
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        boolean z = true;
        if (newConfig.orientation != 1) {
            z = false;
        }
        this.portrait = z;
        doOnConfigurationChanged(this.portrait);
    }

    private void doOnConfigurationChanged(final boolean portrait) {
        if (this.videoView != null && !this.fullScreenOnly) {
            this.handler.post(new Runnable() {
                public void run() {
                    NSTPlayerVod.this.tryFullScreen(!portrait);
                    if (portrait) {
                        NSTPlayerVod.this.f40$.id(R.id.app_video_box).height(NSTPlayerVod.this.initHeight, false);
                    } else {
                        NSTPlayerVod.this.f40$.id(R.id.app_video_box).height(Math.min(NSTPlayerVod.this.activity.getResources().getDisplayMetrics().heightPixels, NSTPlayerVod.this.activity.getResources().getDisplayMetrics().widthPixels), false);
                    }
                    NSTPlayerVod.this.updateFullScreenButton();
                }
            });
            this.orientationEventListener.enable();
        }
    }

    public void tryFullScreen(boolean fullScreen) {
        if (this.activity instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) this.activity).getSupportActionBar();
            if (supportActionBar != null) {
                if (fullScreen) {
                    supportActionBar.hide();
                } else {
                    supportActionBar.show();
                }
            }
        }
        setFullScreen(fullScreen);
    }

    private void setFullScreen(boolean fullScreen) {
        if (this.activity != null) {
            WindowManager.LayoutParams attrs = this.activity.getWindow().getAttributes();
            if (fullScreen) {
                attrs.flags |= 1024;
                this.activity.getWindow().setAttributes(attrs);
                this.activity.getWindow().addFlags(512);
                return;
            }
            attrs.flags &= -1025;
            this.activity.getWindow().setAttributes(attrs);
            this.activity.getWindow().clearFlags(512);
        }
    }

    public void onDestroy() {
        this.orientationEventListener.disable();
        this.handler.removeCallbacksAndMessages(null);
        this.videoView.stopPlayback();
    }

    private void showStatus(String statusText) {
        this.f40$.id(R.id.app_video_status).visible();
        this.f40$.id(R.id.app_video_status_text).text(statusText);
    }

    public void play(String url, int opened_stream_id, String videoExtension) {
        this.url = url;
        if (this.playerSupport) {
            this.f40$.id(R.id.app_video_loading).visible();
            this.videoView.setVideoPath(url + opened_stream_id + "." + videoExtension);
            this.videoView.start();
        }
    }

    public void play(String url) {
        this.url = url;
        if (this.playerSupport) {
            this.f40$.id(R.id.app_video_loading).visible();
            this.videoView.setVideoPath(url);
            this.videoView.start();
        }
    }

    private String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        if (totalSeconds / DateTimeConstants.SECONDS_PER_HOUR > 0) {
            return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(totalSeconds / DateTimeConstants.SECONDS_PER_HOUR), Integer.valueOf(minutes), Integer.valueOf(seconds)});
        }
        return String.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
    }

    private int getScreenOrientation() {
        int rotation = this.activity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        this.activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        if (((rotation == 0 || rotation == 2) && height > width) || ((rotation == 1 || rotation == 3) && width > height)) {
            switch (rotation) {
                case 0:
                    return 1;
                case 1:
                    return 0;
                case 2:
                    return 9;
                case 3:
                    return 8;
                default:
                    return 1;
            }
        }
        switch (rotation) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 8;
            case 3:
                return 9;
            default:
                return 0;
        }
    }

    private void onVolumeSlide(float percent) {
        if (this.volume == -1) {
            this.volume = this.audioManager.getStreamVolume(3);
            if (this.volume < 0) {
                this.volume = 0;
            }
        }
        hide(true);
        int index = ((int) (((float) this.mMaxVolume) * percent)) + this.volume;
        if (index > this.mMaxVolume) {
            index = this.mMaxVolume;
        } else if (index < 0) {
            index = 0;
        }
        this.audioManager.setStreamVolume(3, index, 0);
        int i = (int) (((((double) index) * 1.0d) / ((double) this.mMaxVolume)) * 100.0d);
        String s = i + "%";
        if (i == 0) {
            s = "off";
        }
        this.f40$.id(R.id.app_video_volume_icon).image(i == 0 ? R.drawable.ic_volume_off_white_36dp : R.drawable.ic_volume_up_white_36dp);
        this.f40$.id(R.id.app_video_brightness_box).gone();
        this.f40$.id(R.id.app_video_volume_box).visible();
        this.f40$.id(R.id.app_video_volume_box).visible();
        this.f40$.id(R.id.app_video_volume).text(s).visible();
    }

    private void onProgressSlide(float percent) {
        long position = (long) this.videoView.getCurrentPosition();
        long duration = (long) this.videoView.getDuration();
        long delta = (long) (((float) Math.min(100000, duration - position)) * percent);
        this.newPosition = delta + position;
        if (this.newPosition > duration) {
            this.newPosition = duration;
        } else if (this.newPosition <= 0) {
            this.newPosition = 0;
            delta = -position;
        }
        int showDelta = ((int) delta) / 1000;
        if (showDelta != 0) {
            this.f40$.id(R.id.app_video_fastForward_box).visible();
            this.f40$.id(R.id.app_video_fastForward).text((showDelta > 0 ? "+" + showDelta : "" + showDelta) + "s");
            this.f40$.id(R.id.app_video_fastForward_target).text(generateTime(this.newPosition) + "/");
            this.f40$.id(R.id.app_video_fastForward_all).text(generateTime(duration));
        }
    }

    private void onBrightnessSlide(float percent) {
        if (this.brightness < 0.0f) {
            this.brightness = this.activity.getWindow().getAttributes().screenBrightness;
            if (this.brightness <= 0.0f) {
                this.brightness = 0.5f;
            } else if (this.brightness < 0.01f) {
                this.brightness = 0.01f;
            }
        }
        Log.d(getClass().getSimpleName(), "brightness:" + this.brightness + ",percent:" + percent);
        this.f40$.id(R.id.app_video_brightness_box).visible();
        WindowManager.LayoutParams lpa = this.activity.getWindow().getAttributes();
        lpa.screenBrightness = this.brightness + percent;
        if (lpa.screenBrightness > 1.0f) {
            lpa.screenBrightness = 1.0f;
        } else if (lpa.screenBrightness < 0.01f) {
            lpa.screenBrightness = 0.01f;
        }
        this.f40$.id(R.id.app_video_brightness).text(((int) (lpa.screenBrightness * 100.0f)) + "%");
        this.activity.getWindow().setAttributes(lpa);
    }

    private long setProgress() {
        if (this.isDragging) {
            return 0;
        }
        long position = (long) this.videoView.getCurrentPosition();
        long duration = (long) this.videoView.getDuration();
        if (this.seekBar != null) {
            if (duration > 0) {
                this.seekBar.setProgress((int) ((1000 * position) / duration));
            }
            this.seekBar.setSecondaryProgress(this.videoView.getBufferPercentage() * 10);
        }
        this.duration = duration;
        this.f40$.id(R.id.app_video_currentTime).text(generateTime(position));
        this.f40$.id(R.id.app_video_endTime).text(generateTime(this.duration));
        return position;
    }

    public void hide(boolean force) {
        if (force || this.isShowing) {
            this.handler.removeMessages(1);
            showBottomControl(false);
            this.f40$.id(R.id.app_video_top_box).gone();
            this.f40$.id(R.id.app_video_bottom_box).gone();
            this.f40$.id(R.id.controls).gone();
            this.f40$.id(R.id.app_video_fullscreen).invisible();
            this.isShowing = false;
            this.onControlPanelVisibilityChangeListener.change(false);
        }
    }

    private void updateFullScreenButton() {
        if (getScreenOrientation() == 0) {
            this.f40$.id(R.id.app_video_fullscreen).image(R.drawable.ic_fullscreen_exit_white_36dp);
        } else {
            this.f40$.id(R.id.app_video_fullscreen).image(R.drawable.ic_fullscreen_white_24dp);
        }
    }

    public void setFullScreenOnly(boolean fullScreenOnly) {
        this.fullScreenOnly = fullScreenOnly;
        tryFullScreen(fullScreenOnly);
        if (fullScreenOnly) {
            this.activity.setRequestedOrientation(0);
        } else {
            this.activity.setRequestedOrientation(4);
        }
    }

    public void setScaleType(String scaleType) {
        if ("fitParent".equals(scaleType)) {
            this.videoView.setAspectRatio(0);
        } else if ("fillParent".equals(scaleType)) {
            this.videoView.setAspectRatio(1);
        } else if ("wrapContent".equals(scaleType)) {
            this.videoView.setAspectRatio(2);
        } else if ("fitXY".equals(scaleType)) {
            this.videoView.setAspectRatio(3);
        } else if ("16:9".equals(scaleType)) {
            this.videoView.setAspectRatio(4);
        } else if ("4:3".equals(scaleType)) {
            this.videoView.setAspectRatio(5);
        }
    }

    public void setShowNavIcon(boolean show) {
        this.f40$.id(R.id.app_video_finish).visibility(show ? 0 : 8);
    }

    public void start() {
        this.videoView.start();
    }

    public void pause() {
        this.videoView.pause();
    }

    public boolean onBackPressed() {
        if (this.fullScreenOnly || getScreenOrientation() != 0) {
            return false;
        }
        this.activity.setRequestedOrientation(1);
        return true;
    }

    public boolean isPlayerSupport() {
        return this.playerSupport;
    }

    public boolean isPlaying() {
        return this.videoView != null ? this.videoView.isPlaying() : false;
    }

    public void stop() {
        this.videoView.stopPlayback();
    }

    public NSTPlayerVod seekTo(int msec, boolean showControlPanle) {
        this.videoView.seekTo(msec);
        if (showControlPanle) {
            show(this.defaultTimeout);
        }
        return this;
    }

    public NSTPlayerVod forward(float percent) {
        if (!this.isLive && percent <= 1.0f && percent >= -1.0f) {
            onProgressSlide(percent);
            showBottomControl(true);
            this.handler.sendEmptyMessage(1);
            endGesture();
        }
        return this;
    }

    public int getCurrentPosition() {
        return this.videoView.getCurrentPosition();
    }

    public int getCurrentWindowIndex() {
        return this.currentWindowIndex;
    }

    public int getDuration() {
        return this.videoView.getDuration();
    }

    public NSTPlayerVod playInFullScreen(boolean fullScreen) {
        if (fullScreen) {
            this.activity.setRequestedOrientation(0);
            updateFullScreenButton();
        }
        return this;
    }

    public void toggleFullScreen() {
        if (getScreenOrientation() == 0) {
            this.activity.setRequestedOrientation(1);
        } else {
            this.activity.setRequestedOrientation(0);
        }
        updateFullScreenButton();
    }

    public NSTPlayerVod onError(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
        return this;
    }

    public NSTPlayerVod onComplete(Runnable complete) {
        this.oncomplete = complete;
        return this;
    }

    public NSTPlayerVod onInfo(OnInfoListener onInfoListener) {
        this.onInfoListener = onInfoListener;
        return this;
    }

    public NSTPlayerVod onControlPanelVisibilityChang(OnControlPanelVisibilityChangeListener listener) {
        this.onControlPanelVisibilityChangeListener = listener;
        return this;
    }

    public NSTPlayerVod live(boolean isLive) {
        this.isLive = isLive;
        return this;
    }

    public NSTPlayerVod fullScreenRatio() {
        if (this.videoView != null) {
            this.videoView.fitParentAspectRatio();
        }
        return this;
    }

    public NSTPlayerVod toggleAspectRatio() {
        if (this.videoView != null) {
            final LinearLayout ll_aspect_ratio = (LinearLayout) this.activity.findViewById(R.id.ll_aspect_ratio);
            TextView app_aspect_ratio_text = (TextView) this.activity.findViewById(R.id.app_aspect_ratio_text);
            this.sharedPreferences = this.activity.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
            this.sharedPrefEditor = this.sharedPreferences.edit();
            int togglePosition = this.videoView.toggleAspectRatio();
            if (togglePosition == 0) {
                this.sharedPrefEditor.putInt(AppConst.ASPECT_RATIO, 0);
                this.sharedPrefEditor.commit();
                app_aspect_ratio_text.setText("Fit Parent");
            } else if (togglePosition == 1) {
                this.sharedPrefEditor.putInt(AppConst.ASPECT_RATIO, 1);
                this.sharedPrefEditor.commit();
                app_aspect_ratio_text.setText("Fill Parent");
            } else if (togglePosition == 2) {
                this.sharedPrefEditor.putInt(AppConst.ASPECT_RATIO, 2);
                this.sharedPrefEditor.commit();
                app_aspect_ratio_text.setText("Wrap Content");
            } else if (togglePosition == 3) {
                this.sharedPrefEditor.putInt(AppConst.ASPECT_RATIO, 3);
                this.sharedPrefEditor.commit();
                app_aspect_ratio_text.setText("Match Parent");
            } else if (togglePosition == 4) {
                this.sharedPrefEditor.putInt(AppConst.ASPECT_RATIO, 4);
                this.sharedPrefEditor.commit();
                app_aspect_ratio_text.setText("16:9");
            } else if (togglePosition == 5) {
                this.sharedPrefEditor.putInt(AppConst.ASPECT_RATIO, 5);
                this.sharedPrefEditor.commit();
                app_aspect_ratio_text.setText("4:3");
            }
            ll_aspect_ratio.setVisibility(0);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ll_aspect_ratio.setVisibility(8);
                }
            }, 3000);
        }
        return this;
    }

    public NSTPlayerVod onControlPanelVisibilityChange(OnControlPanelVisibilityChangeListener listener) {
        this.onControlPanelVisibilityChangeListener = listener;
        return this;
    }
}

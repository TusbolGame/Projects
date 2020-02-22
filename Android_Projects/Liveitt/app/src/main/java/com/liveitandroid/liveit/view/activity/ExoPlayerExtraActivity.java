package com.liveitandroid.liveit.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.view.utility.LoadingSpinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoPlayerExtraActivity extends Activity implements OnClickListener {
    private Context context;
    private String nameMovie, urlMovie, subs;
    @BindView(R.id.pb_loader)
    ProgressBar pbLoader;
    private ProgressDialog progressDialog;
    private Button retryButton;
    VideoView videoView = null;
    public LoadingSpinner video_loader;

    class C16051 implements OnPreparedListener {

        class C16041 implements OnVideoSizeChangedListener {
            C16041() {
            }

            public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                ExoPlayerExtraActivity.this.video_loader.setVisibility(8);
                mp.start();
            }
        }

        C16051() {
        }

        public void onPrepared(MediaPlayer mp) {
            mp.start();
            mp.setOnVideoSizeChangedListener(new C16041());
        }
    }

    class C16062 implements OnErrorListener {
        C16062() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            ExoPlayerExtraActivity.this.video_loader.setVisibility(8);
            ExoPlayerExtraActivity.this.retryButton.setVisibility(0);
            return true;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_exoplayer_vod);
        ButterKnife.bind(this);
        changeStatusBarColor();
        getWindow().setFlags(1024, 1024);
        initialize();
    }

    private void initialize() {
        this.context = this;
        this.videoView = (VideoView) findViewById(R.id.videoView);
        this.retryButton = (Button) findViewById(R.id.retry_button);
        this.retryButton.setOnClickListener(this);
        this.video_loader = (LoadingSpinner) findViewById(R.id.iv_video_loader);

        this.nameMovie = getIntent().getStringExtra("nameMovie");
        this.urlMovie = getIntent().getStringExtra("urlMovie");
        this.subs = getIntent().getStringExtra("subs");

        preparePlayer();
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

    public void preparePlayer() {
        Uri uri = Uri.parse(this.urlMovie);
        MediaController mediaController = new MediaController(this);

        mediaController.setAnchorView(this.videoView);
        this.videoView.setMediaController(mediaController);
        this.videoView.setVideoURI(uri);
        this.videoView.requestFocus();
        this.retryButton.setVisibility(8);
        this.video_loader.setVisibility(0);

        if(subs.equals("") || subs.equals(null))
        {

        }else{
            videoView.addSubtitleSource(getSubtitleSource(subs),
                    MediaFormat.createSubtitleFormat("text/srt", Locale.ENGLISH.getLanguage()));
        }

        this.videoView.setOnPreparedListener(new C16051());
        this.videoView.setOnErrorListener(new C16062());
    }

    private InputStream getSubtitleSource(String filepath)
    {
        InputStream ins = null;
        String ccFileName = filepath.substring(0, filepath.lastIndexOf('.')) + ".srt";
        //String ccFileName = filepath.substring(0, filepath.lastIndexOf('.')) + ".vtt";
        File file = new File(ccFileName);
        if (file.exists() == false)
        {
            return null;
        }
        FileInputStream fins = null;
        try
        {
            fins = new FileInputStream(file);
        }
        catch (Exception e)
        {
        }
        ins = (InputStream)fins;
        return ins;
    }

    public void onClick(View view) {
        if (view == this.retryButton) {
            preparePlayer();
        }
    }

    public void onFinish() {
        if (this.pbLoader != null) {
            this.pbLoader.setVisibility(4);
        }
    }

    public void onResume() {
        super.onResume();
        onFinish();
    }
}

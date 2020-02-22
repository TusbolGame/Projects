package com.liveitandroid.liveit.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.squareup.picasso.Picasso;


public class AudioActivity extends Activity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, View.OnClickListener {

    TextView channel_name;
    ImageView imageView2;
    Button playButton;

    private ListMoviesSetterGetter mSelectedMovie;

    int count = 0;

    MediaPlayer mp;
    ProgressDialog pd;

    @Override
    public void onPrepared(MediaPlayer mp) {
        //  pd.setMessage("Playing.....");
        pd.dismiss();
        mp.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        channel_name = (TextView)findViewById(R.id.channel_name);
        ImageView ivMovieImage = (ImageView)findViewById(R.id.imageView2);
        playButton = (Button) findViewById(R.id.playbutton);
        mSelectedMovie = (ListMoviesSetterGetter) getIntent().getSerializableExtra("movie");
        channel_name.setText(mSelectedMovie.getPraias_name());
        if (ivMovieImage != null) {
            if (!mSelectedMovie.getPraias_imagem().equals("")) {
                Picasso.with(this).load(mSelectedMovie.getPraias_imagem()).placeholder((int) R.drawable.logo).into(ivMovieImage);
            }
        }
        playButton.setOnClickListener(this);

        try {
            pd = new ProgressDialog(AudioActivity.this);
            pd.setMessage("A carregar.....");
            pd.show();
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setOnPreparedListener(AudioActivity.this);
            mp.setOnErrorListener(AudioActivity.this);
            mp.setDataSource(mSelectedMovie.getPraias_url());
            mp.prepareAsync();
            mp.setOnCompletionListener(AudioActivity.this);
            playButton.setBackgroundResource(R.drawable.btn_playback_pause);
            count++;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        pd.dismiss();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        pd.dismiss();
    }

    @Override
    public void onClick(View view) {
        if(count == 0){
            try {
                pd = new ProgressDialog(AudioActivity.this);
                pd.setMessage("A carregar.....");
                pd.show();
                mp = new MediaPlayer();
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setOnPreparedListener(AudioActivity.this);
                mp.setOnErrorListener(AudioActivity.this);
                mp.setDataSource(mSelectedMovie.getPraias_url());
                mp.prepareAsync();
                mp.setOnCompletionListener(AudioActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            playButton.setBackgroundResource(R.drawable.btn_playback_pause);
            count++;
        }else{
            // stopService(musicServiceIntent);
            mp.stop();
            playButton.setBackgroundResource(R.drawable.btn_playback_play);
            count--;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mp.release();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mp.release();
    }
}

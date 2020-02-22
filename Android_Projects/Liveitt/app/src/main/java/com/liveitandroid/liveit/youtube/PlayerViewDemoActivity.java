/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liveitandroid.liveit.youtube;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.view.activity.LoginActivity;

/**
 * A simple YouTube Android API demo application which shows how to create a simple application that
 * displays a YouTube Video in a {@link YouTubePlayerView}.
 * <p>
 * Note, to use a {@link YouTubePlayerView}, your activity must extend {@link YouTubeBaseActivity}.
 */
public class PlayerViewDemoActivity extends YouTubeFailureRecoveryActivity implements
        YouTubePlayer.PlaybackEventListener {
    final Context context = this;
    String sel_youtube = "";
    AlertDialog alert;
    AlertDialog alert2;
    String[] split_01;
    YouTubePlayer player1;
    private SessionManager mSessionManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerview_demo);

        sel_youtube = getIntent().getStringExtra("youtube");
        mSessionManager = new SessionManager(PlayerViewDemoActivity.this);
        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(mSessionManager.getkodiGrant(), this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            split_01 = sel_youtube.split("v=");
            if (split_01.length > 1) {
                sel_youtube = split_01[1];
            }
            player1 = player;
            player.cueVideo(sel_youtube);
        }
        player1 = player;
        player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        player.setPlaybackEventListener(this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        player1.setFullscreen(false);
        super.onBackPressed();
        finish();
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    @Override
    public void onBuffering(boolean arg0) {
        player1.setFullscreen(true);
    }

    @Override
    public void onPaused() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlaying() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSeekTo(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopped() {
        // TODO Auto-generated method stub

    }
}

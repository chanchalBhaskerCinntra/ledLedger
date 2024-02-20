package com.cinntra.ledure.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cinntra.ledure.R;

import com.google.android.exoplayer2.ui.PlayerView;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class ExoPlayerActivity extends AppCompatActivity {

    private String YOUTUBE_VIDEO_ID = "P8wc4lFngXk";
    private String BASE_URL = "https://www.youtube.com";
    private String mYoutubeLink = BASE_URL + "/watch?v=" + YOUTUBE_VIDEO_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exoplyer_view);
        extractYoutubeUrl();
    }
    private void extractYoutubeUrl() {
        @SuppressLint("StaticFieldLeak") YouTubeExtractor mExtractor = new YouTubeExtractor(this) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> sparseArray, VideoMeta videoMeta) {
                if (sparseArray != null) {
                    playVideo(sparseArray.get(17).getUrl());
                }
            }
        };
        mExtractor.extract(mYoutubeLink, true, true);
    }
    private void playVideo(String downloadUrl) {
        PlayerView mPlayerView = findViewById(R.id.mPlayerView);
        mPlayerView.setPlayer(ExoPlayerManager.getSharedInstance(ExoPlayerActivity.this).getPlayerView().getPlayer());
        ExoPlayerManager.getSharedInstance(ExoPlayerActivity.this).playStream(downloadUrl);
    }
}
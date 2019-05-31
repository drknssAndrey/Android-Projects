package com.andreybalbino.youtube.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andreybalbino.youtube.R;
import com.andreybalbino.youtube.helper.YoutubeConfig;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubePlayerView;
    private String idvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //configurar componentes
        youTubePlayerView = findViewById(R.id.playerVideo);

        Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
            idvideo = bundle.getString("idVideo");
            youTubePlayerView.initialize(YoutubeConfig.CHAVE_YOUTUBE_API, this );
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean foiRestaurado) {
        youTubePlayer.setFullscreen(true);
        youTubePlayer.setShowFullscreenButton(false);
        youTubePlayer.loadVideo(idvideo);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}

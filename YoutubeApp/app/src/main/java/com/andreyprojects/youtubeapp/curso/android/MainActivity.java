package com.andreyprojects.youtubeapp.curso.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubePlayerView;
    private static final String GOOGLE_APY_KEY = "AIzaSyAIOKD6nSj8eTOcsHGK2_aZYvkVUTcUs5k";
    private YouTubePlayer.PlaybackEventListener playbackEventListener;
    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {
                Toast.makeText(MainActivity.this, "video carregando", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLoaded(String s) {
                Toast.makeText(MainActivity.this, "video carregado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdStarted() {
                Toast.makeText(MainActivity.this, "propaganda iniciou", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVideoStarted() {
                Toast.makeText(MainActivity.this, "video está comecando", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onVideoEnded() {
                Toast.makeText(MainActivity.this, "video chegou ao final", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {
                Toast.makeText(MainActivity.this, "erro ao recuperar eventos de carregamentoo", Toast.LENGTH_SHORT).show();

            }
        };

        playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {
                Toast.makeText(MainActivity.this, "video executado com sucesso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaused() {
                Toast.makeText(MainActivity.this, "video pausado com sucesso", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopped() {
                Toast.makeText(MainActivity.this, "video parado com sucesso", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onBuffering(boolean b) {
                Toast.makeText(MainActivity.this, "video pré carregando com sucesso", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSeekTo(int i) {
                Toast.makeText(MainActivity.this, "movimentando a seekbar", Toast.LENGTH_SHORT).show();

            }
        };

        youTubePlayerView = findViewById(R.id.viewYoutubePlay);

        youTubePlayerView.initialize(GOOGLE_APY_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean foiRestaurado) {
       // youTubePlayer.loadVideo("BhEk6DirTlY");
        Log.i("estado do player", "estado: " +  foiRestaurado);
       // youTubePlayer.setPlaybackEventListener(playbackEventListener);
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        if (!foiRestaurado){
//            youTubePlayer.cueVideo("TBZoB5Oz9ug");
            youTubePlayer.cuePlaylist("PL_C1rnvFiCqTpFUm3w4_xs4vEu5lkfIdX");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Erro ao iniciar o player " + youTubeInitializationResult.toString(), Toast.LENGTH_SHORT).show();
    }
}

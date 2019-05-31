package com.andreybalbino.youtube.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.andreybalbino.youtube.R;
import com.andreybalbino.youtube.adapter.AdapterVideo;
import com.andreybalbino.youtube.api.YoutubeService;
import com.andreybalbino.youtube.helper.RetrofitConfig;
import com.andreybalbino.youtube.helper.YoutubeConfig;
import com.andreybalbino.youtube.listerner.RecyclerItemClickListener;
import com.andreybalbino.youtube.model.Item;
import com.andreybalbino.youtube.model.Resultado;
import com.andreybalbino.youtube.model.Video;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerVideos;
    private List<Item> videos = new ArrayList<>();
    private Resultado resultado;
    private AdapterVideo adapterVideo;
    private MaterialSearchView searchView;

    //retrofit
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializar componentes
        recyclerVideos = findViewById(R.id.recyclerVideos);
        searchView = findViewById(R.id.searchView);

        //configuracoes iniciais
        retrofit = RetrofitConfig.getRetrofit();

        //configuracao da toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Youtube");
        setSupportActionBar(toolbar);
        //Recupera vídeos
        recuperarVideos("");



        //configura métodos para o searchview
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recuperarVideos(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                recuperarVideos("");
            }
        });
    }
    public void configurarRecyclerView(){
        adapterVideo = new AdapterVideo(videos, this);
        recyclerVideos.setHasFixedSize(true);
        recyclerVideos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerVideos.setAdapter(adapterVideo);

        //configurar evento de click
        recyclerVideos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerVideos, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Item video = videos.get(position);
                String idVideo = video.id.videoId;
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra("idVideo", idVideo);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    private void recuperarVideos(String pesquisa){
        String q = pesquisa.replaceAll(" ", "+");
        YoutubeService youtubeService = retrofit.create(YoutubeService.class);
       youtubeService.recuperarVideos("snippet", "date", "20", YoutubeConfig.CHAVE_YOUTUBE_API, YoutubeConfig.CANAL_ID, pesquisa).enqueue(new Callback<Resultado>() {
           @Override
           public void onResponse(Call<Resultado> call, Response<Resultado> response) {
               Log.d("resultado", "resultado: "+ response.toString());
                if (response.isSuccessful()){
                    resultado = response.body();
                    videos = resultado.items;
                    configurarRecyclerView();
                    Log.d("resultado", "resultado: "+ resultado.items.get(0).id.videoId);

                }
           }

           @Override
           public void onFailure(Call<Resultado> call, Throwable t) {

           }
       });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        searchView.setMenuItem(item);
        return true;
    }
}

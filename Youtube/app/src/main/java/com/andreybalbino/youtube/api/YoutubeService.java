package com.andreybalbino.youtube.api;

import com.andreybalbino.youtube.model.Resultado;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {
    /*
    https://www.googleapis.com/youtube/v3/
    search
    ?part=snippet
    &order=date
    &maxResults=20
    &key=AIzaSyBTss7fptX5bGtryfqJGk275njL8Tz2zKk
    &channelId=UCU5JicSrEM5A63jkJ2QvGYw
    &q=teste +
     */
    /*
    https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResults=20&key=AIzaSyBTss7fptX5bGtryfqJGk275njL8Tz2zKk&channelId=UCU5JicSrEM5A63jkJ2QvGYw
     */

    @GET("search")
    Call<Resultado> recuperarVideos(@Query("part") String part, @Query("order") String order, @Query("maxResult") String maxResult, @Query("key") String key, @Query("channelId") String channelId,@Query("q") String q );
}

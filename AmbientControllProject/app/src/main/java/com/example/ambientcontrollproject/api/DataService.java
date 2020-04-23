package com.example.ambientcontrollproject.api;

import com.example.ambientcontrollproject.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

    @GET("users")
    Call<List<Users>> getUsers();

    @POST("/users")
    Call<Users> setUsers(@Body Users users);
}

package com.example.ambientcontrollproject.util;

import android.content.Intent;

import com.example.ambientcontrollproject.MainActivity;
import com.example.ambientcontrollproject.api.DataService;
import com.example.ambientcontrollproject.model.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestAPI {
    private static Retrofit retrofit;
    private static List<Users> listUsers = new ArrayList<>();


    public static void initAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/drknssAndrey/github-course/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DataService dataService = retrofit.create(DataService.class);
        Call<List<Users>> call = dataService.getUsers();
        call.enqueue(new Callback<List<Users>>() {
            @Override
            public void onResponse(Call<List<Users>> call, Response<List<Users>> response) {
                if (response.isSuccessful()) {
                    List<Users> usersList = response.body();
                    for (Users user : usersList) {
                        listUsers.add(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Users>> call, Throwable t) {

            }
        });

    }

    public static boolean queryUser(Users us) {
        for (Users user : listUsers) {
            if (user.getUsername().equals(us.getUsername()) && user.getPassword().equals(us.getPassword())) {
                return true;
            }
        }
        return false;
    }
}

package com.example.ambientcontrollproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ambientcontrollproject.api.DataService;
import com.example.ambientcontrollproject.model.Users;
import com.example.ambientcontrollproject.util.RequestAPI;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText textName, textPassword, textRepeatPassword;
    private Retrofit retrofit;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textName = findViewById(R.id.textInputUserRegister);
        textPassword = findViewById(R.id.textInputPasswordRegister);
        textRepeatPassword = findViewById(R.id.textInputRepeatPassword);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public void registerUser(View view) {

        if (checkFields()) {
            String userName = textName.getText().toString();
            String password = textPassword.getText().toString();
            user = new Users();
            user.setUsername(userName);
            user.setPassword(password);
            if (!RequestAPI.queryUser(user)){
                saveUser(user);
            }
        }
    }

    private void saveUser(Users user) {
        DataService service = retrofit.create(DataService.class);
        Call<Users> call = service.setUsers(user);
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuario cadastrado "+ response.code(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this, "ERRO  "+response.code(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });
    }


    private boolean checkFields() {
        if (!textName.getText().toString().isEmpty()) {
            if (textName.getText().toString().length() >= 6) {
                if (!textPassword.getText().toString().isEmpty()) {
                    if (textPassword.getText().toString().length() >= 6) {
                        if (textPassword.getText().toString().equals(textRepeatPassword.getText().toString())) {
                            return true;
                        } else {
                            Toast.makeText(this, "As senhas não correspondem!", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        Toast.makeText(this, "A senha não pode ter menos que 6 dígitos", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "Preencha o campo Senha", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "O nome de usuário não pode ter menos que 6 caracteres", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Preencha o campo Usuário", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


}



package com.example.ambientcontrollproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText textInputUser, textInputPassword;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private Users user;
    private List<Users> listUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textInputUser = findViewById(R.id.textInputUserLogin);
        textInputPassword = findViewById(R.id.textInputPasswordLogin);
        progressBar = findViewById(R.id.progressBarLogin);


    }

    public void loginUser(View view) {
        progressBar.setVisibility(View.VISIBLE);
        if (checkFields()) {
            String username = textInputUser.getText().toString();
            String password = textInputPassword.getText().toString();
            user = new Users();
            user.setUsername(username);
            user.setPassword(password);
            if (RequestAPI.queryUser(user)) {
//                Toast.makeText(this, "Sucesso ao fazer login", Toast.LENGTH_SHORT).show();
                openDisplayMain();
                finish();
            } else {
                Toast.makeText(this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }

    }

    private boolean checkFields() {
        if (!textInputUser.getText().toString().isEmpty()) {
            if (textInputUser.getText().toString().length() >= 6) {
                if (!textInputPassword.getText().toString().isEmpty()) {
                    return true;
                } else {
                    Toast.makeText(this, "Preencha o campo Senha", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            } else {
                Toast.makeText(this, "Um nome de usuário não possui menos que 6 caracteres", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return false;
            }
        } else {
            Toast.makeText(this, "Preencha o campo Usuário", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return false;
        }
    }

    private void openDisplayMain() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        RequestAPI.initAPI();
    }
    public void registerScreen(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}

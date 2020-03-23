package com.example.ambientcontrollproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText textInputEmail, textInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textInputEmail = findViewById(R.id.textInputEmailLogin);
        textInputPassword = findViewById(R.id.textInputPasswordLogin);
    }

    public void loginUser(View view) {
        if (checkFields()) {
            String myApi = "https://my-json-server.typicode.com/drknssAndrey/github-course/db";
            MyAsyncTask task = new MyAsyncTask();
            task.execute(myApi);

        }

    }

    private boolean checkFields() {
        if (!textInputEmail.getText().toString().isEmpty()) {
            if (!textInputPassword.getText().toString().isEmpty()) {
                return true;
            } else {
                Toast.makeText(this, "Preencha o campo Senha", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Preencha o campo Email", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String strUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;
            try {
                //recebe a url no formato de string e converte para url propriamente dita
                URL url = new URL(strUrl);
                // inicia a conexao o servidor
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //recupera os dados em bytes
                inputStream = connection.getInputStream();
                //lê os dados em bytes decodifica em caracteres
                inputStreamReader = new InputStreamReader(inputStream);
                // faz a leitura dos caracteres do inputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String linha = "";
                buffer = new StringBuffer();

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String usuario = null;
            String email = null;
            String senha = null;

            try {
                JSONObject object = new JSONObject(s);
                String users = object.getString("usuarios");
                JSONObject usersObject = new JSONObject(users);
                String id = usersObject.getString("001");
                JSONObject user = new JSONObject(id);
                usuario = user.getString("usuario");
                email = user.getString("email");
                senha = user.getString("senha");
                Toast.makeText(LoginActivity.this, "usuario: "+usuario+"\nemail: "+email+"\nsenha: "+senha, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
}

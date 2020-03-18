package com.example.requesthttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private Button botaoRecuperar;
    private TextView textResultado;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textResultado = findViewById(R.id.resultado);
        editText = findViewById(R.id.editCep);

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAsyncTask task = new MyAsyncTask();
                String urlApi = " https://blockchain.info/tobtc?currency=USD&value=500";
                String cep = editText.getText().toString();
                String urlCep = " https://viacep.com.br/ws/" + cep + "/json/";
                task.execute(urlCep);
            }
        });
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                HttpsURLConnection conexao = (HttpsURLConnection) url.openConnection();
                //recupera os dados em bytes
                inputStream = conexao.getInputStream();
                //lê os dados em bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);
                //faz a leitura dos caracteres do inputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha = "";
                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (buffer!=null){
                return buffer.toString();
            }
            return "ERRO";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("ERRO")){
                textResultado.setText(s);
            }else{
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

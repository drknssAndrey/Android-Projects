package com.andreybalbino.olx.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.andreybalbino.olx.R;
import com.blackcat.currencyedittext.CurrencyEditText;

import java.util.Locale;

public class CadastrarAnuncioActivity extends AppCompatActivity {

    private EditText campoTitulo, campoDescricao;
    private CurrencyEditText campoValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        inicializarComponentes();
    }
    public void salvarAnuncio(View view){
        String valor = campoValor.getText().toString();
        Log.d("salvar", "salvar anuncio: " + valor);
    }
    private void inicializarComponentes(){
        campoTitulo = findViewById(R.id.editTitulo);
        campoValor = findViewById(R.id.editValor);
        campoDescricao = findViewById(R.id.editDescricao);
        //configura localidade para pt -> portugues br -> brasil
        campoValor.setLocale(new Locale("pt", "BR"));
    }
}

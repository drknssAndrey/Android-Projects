package com.andreybalbino.meuolx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.andreybalbino.meuolx.R;
import com.andreybalbino.meuolx.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configuracoes iniciais
        auth = ConfiguracaoFirebase.getFirebaseAuth();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (auth.getCurrentUser() != null) {
            menu.setGroupVisible(R.id.logado, true);
        } else {
            menu.setGroupVisible(R.id.deslogado, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.entrar:
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
                break;
            case R.id.meus_anuncios:
                startActivity(new Intent(MainActivity.this, MeusAnunciosActivity.class));
                break;
            case R.id.sair:
                auth.signOut();
                invalidateOptionsMenu();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

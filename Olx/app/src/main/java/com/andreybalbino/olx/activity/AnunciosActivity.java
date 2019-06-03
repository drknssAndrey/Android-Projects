package com.andreybalbino.olx.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.andreybalbino.olx.R;
import com.andreybalbino.olx.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class AnunciosActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncios);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (firebaseAuth.getCurrentUser() == null){
            menu.setGroupVisible(R.id.group_deslogado, true);
        }else{
            menu.setGroupVisible(R.id.group_logado, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cadastrar:
                startActivity(new Intent(AnunciosActivity.this, CadastroActivity.class));
                break;
            case R.id.menu_sair:
                firebaseAuth.signOut();
                invalidateOptionsMenu();
                break;
            case R.id.menu_anuncios:
                startActivity(new Intent(AnunciosActivity.this, MeusAnunciosActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

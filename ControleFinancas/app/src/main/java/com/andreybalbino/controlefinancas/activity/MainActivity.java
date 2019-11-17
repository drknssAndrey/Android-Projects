package com.andreybalbino.controlefinancas.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.andreybalbino.controlefinancas.R;
import com.andreybalbino.controlefinancas.activity.CadastroActivity;
import com.andreybalbino.controlefinancas.activity.LoginActivity;
import com.andreybalbino.controlefinancas.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)

                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)

                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)

                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());

    }

    public void botaoEntrar(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
    public void botaoCadastrar(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
    private void verificaUsuarioLogado(){
        auth = ConfiguracaoFirebase.getFirebaseAuth();
//        auth.signOut();
        if(auth.getCurrentUser()!=null){
            abrirTelaPrincipal();
        }
    }
    private void abrirTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaUsuarioLogado();
    }
}

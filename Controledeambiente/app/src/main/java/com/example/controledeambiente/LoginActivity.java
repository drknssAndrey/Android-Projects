package com.example.controledeambiente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.controledeambiente.config.ConfiguracaoFirebase;
import com.example.controledeambiente.helper.Base64Custom;
import com.example.controledeambiente.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;
    private Button botaoLogar;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        campoEmail = findViewById(R.id.InputEmailLogin);
        campoSenha = findViewById(R.id.inputSenhaLogin);
        botaoLogar = findViewById(R.id.btLogar);
        progressBar = findViewById(R.id.progressBarLogin);

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarCampos()) {
                    progressBar.setVisibility(View.VISIBLE);
                    autenticarUsuario(usuario);
                }
            }
        });
    }

    public void abrirTelaCadastro(View view) {
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
    }

    private boolean verificarCampos() {
        if (!campoEmail.getText().toString().isEmpty()) {
            if (!campoSenha.getText().toString().isEmpty()) {
                usuario = new Usuario();
                usuario.setEmail(campoEmail.getText().toString());
                usuario.setSenha(campoSenha.getText().toString());
                return true;
            } else {
                Toast.makeText(LoginActivity.this, "Preencha o campo senha", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(LoginActivity.this, "Preencha o campo email", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void autenticarUsuario(Usuario usuario) {
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    progressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Email ou senha incorretos";
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não cadastrado";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
}

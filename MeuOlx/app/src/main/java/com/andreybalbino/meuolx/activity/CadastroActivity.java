package com.andreybalbino.meuolx.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.andreybalbino.meuolx.R;
import com.andreybalbino.meuolx.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha;
    private Switch switctAcesso;
    private Button btAcesso;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        //inicializar componentes
        campoEmail = findViewById(R.id.editTextEmail);
        campoSenha = findViewById(R.id.editTextSenha);
        switctAcesso = findViewById(R.id.switchAcesso);
        btAcesso = findViewById(R.id.buttonAcessar);

        //configuracoes iniciais
        auth = ConfiguracaoFirebase.getFirebaseAuth();

    }

    public void modoAcesso(View view) {
        if (switctAcesso.isChecked()) {
            cadastrarUsuario();
        } else {
            autenticarUsuario();
        }
    }

    private void cadastrarUsuario() {
        if (verificarCampos()) {
            auth.createUserWithEmailAndPassword(campoEmail.getText().toString(), campoSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        String excecao = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            excecao = "Por favor, digite uma senha com no minimo 6 dígitos";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Digite um e-mail válido";
                        } catch (FirebaseAuthUserCollisionException e) {
                            excecao = "Esse email já está sendo usado";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void autenticarUsuario() {
        if (verificarCampos()) {
            auth.signInWithEmailAndPassword(campoEmail.getText().toString(), campoSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(CadastroActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String excecao = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            excecao = "Usuário não encontrado";
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            excecao = "Dados incorretos";
                        } catch (FirebaseAuthUserCollisionException e) {
                            excecao = "Esse email já está sendo usado";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private boolean verificarCampos(){
        if (!campoEmail.getText().toString().isEmpty()){
            if (!campoSenha.getText().toString().isEmpty()){
                return true;
            }else {
                Toast.makeText(this, "Preencha o campo Senha", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "Preencha o campo E-mail", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser()!=null){
            startActivity(new Intent(CadastroActivity.this, MainActivity.class));
            finish();
        }
    }
}

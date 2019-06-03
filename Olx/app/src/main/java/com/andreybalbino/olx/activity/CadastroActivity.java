package com.andreybalbino.olx.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.andreybalbino.olx.R;
import com.andreybalbino.olx.helper.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        inicializarComponentes();

        //configuracoes iniciais
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();
                if (!email.isEmpty()){
                    if (!senha.isEmpty()){
                        //verifica o estado do switch
                        if (tipoAcesso.isChecked()){//cadastro

                             firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (task.isSuccessful()){
                                         Toast.makeText(CadastroActivity.this, "Cadastro realizado com sucesso",Toast.LENGTH_SHORT).show();
                                     }else{
                                         String excecao = "";
                                         try {
                                             throw task.getException();
                                         } catch (FirebaseAuthWeakPasswordException e){
                                             excecao = "Digite uma senha com 6 dígitos, no mínimo";
                                         } catch (FirebaseAuthInvalidCredentialsException e){
                                             excecao = "Digite um email válido";
                                         } catch (FirebaseAuthUserCollisionException e){
                                             excecao = "Esse email já está cadastrado";
                                         }catch (Exception e){
                                             excecao = "Erro ao cadastrar o usuario";
                                             e.printStackTrace();
                                         }
                                         Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             });

                        }else{//login
                            firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(CadastroActivity.this, "Logado com sucesso",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(CadastroActivity.this, AnunciosActivity.class));
                                    }else{
                                        String excecao = "";
                                        try {
                                            throw task.getException();
                                        } catch (FirebaseAuthInvalidUserException e){
                                            excecao = "Usuário não encontrado";
                                        } catch (FirebaseAuthInvalidCredentialsException e){
                                            excecao = "Dados inválidos";
                                        }catch (Exception e){
                                            excecao = "Erro ao cadastrar o usuario";
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }else{
                        Toast.makeText(CadastroActivity.this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha o email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void inicializarComponentes(){
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
        tipoAcesso = findViewById(R.id.switchAcesso);
        botaoAcessar = findViewById(R.id.buttonAcesso);

    }
}

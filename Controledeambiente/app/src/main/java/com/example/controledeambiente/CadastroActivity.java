package com.example.controledeambiente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.controledeambiente.config.ConfiguracaoFirebase;
import com.example.controledeambiente.helper.Base64Custom;
import com.example.controledeambiente.helper.UsuarioFirebase;
import com.example.controledeambiente.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    private TextInputEditText campoNome, campoEmail, campoSenha;
    private ProgressBar progressBar;
    private Button botaoCadastrar;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.inputNomeCad);
        campoEmail = findViewById(R.id.inputEmailCad);
        campoSenha = findViewById(R.id.inputSenhaCad);
        progressBar = findViewById(R.id.progressBar);
        botaoCadastrar = findViewById(R.id.btCadastrar);

        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verificarCampos()) {
                    progressBar.setVisibility(View.VISIBLE);
                    cadastrarUsuario(usuario);
                }
            }
        });
    }

    private boolean verificarCampos() {
        if (!campoNome.getText().toString().isEmpty()) {
            if (!campoEmail.getText().toString().isEmpty()) {
                if (!campoSenha.getText().toString().isEmpty()) {
                    usuario = new Usuario();
                    String idUsuario = Base64Custom.codificarBase64(campoEmail.getText().toString());
                    usuario.setId(idUsuario);
                    usuario.setNome(campoNome.getText().toString());
                    usuario.setEmail(campoEmail.getText().toString());
                    usuario.setSenha(campoSenha.getText().toString());
                    return true;
                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha o campo senha", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(CadastroActivity.this, "Preencha o campo email", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(CadastroActivity.this, "Preencha o campo nome", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void cadastrarUsuario(final Usuario usuario) {
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    usuario.salvar();
                    Toast.makeText(CadastroActivity.this, "Usuário cadastrado", Toast.LENGTH_SHORT).show();
                    UsuarioFirebase.atualizaNomeUsuario(usuario.getNome());
                    progressBar.setVisibility(View.GONE);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um email válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Essa conta já foi cadastrada";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar o usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}

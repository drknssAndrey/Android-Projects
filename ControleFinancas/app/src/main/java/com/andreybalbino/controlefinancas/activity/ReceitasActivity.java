package com.andreybalbino.controlefinancas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andreybalbino.controlefinancas.R;
import com.andreybalbino.controlefinancas.config.ConfiguracaoFirebase;
import com.andreybalbino.controlefinancas.helper.Base64Custom;
import com.andreybalbino.controlefinancas.helper.DateCustom;
import com.andreybalbino.controlefinancas.model.Movimentacao;
import com.andreybalbino.controlefinancas.model.Usuario;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {
    private EditText valorReceita;
    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private Movimentacao movimentacao;
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        valorReceita = findViewById(R.id.editValorReceita);
        campoData = findViewById(R.id.editDataReceita);
        campoCategoria = findViewById(R.id.editCategoriaReceita);
        campoDescricao = findViewById(R.id.editDescricaoReceita);

        //preenche o campo com a data atual
        campoData.setText(DateCustom.dataAtual());

        //recupera o total de despesas
        recuperarReceitaTotal();
    }

    public void salvarReceita(View view) {
        if (validarCamposReceita()) {
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valor = Double.parseDouble(valorReceita.getText().toString());
            movimentacao.setValor(valor);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("R");

            double receitaGerada = valor;
            double despesaAtualizada = receitaTotal + receitaGerada;
            atualizarReceita(despesaAtualizada);

            movimentacao.salvar(data);
            finish();
        }
    }

    public boolean validarCamposReceita() {
        if (!valorReceita.getText().toString().isEmpty()) {
            if (!campoData.getText().toString().isEmpty()) {
                if (!campoCategoria.getText().toString().isEmpty()) {
                    if (!campoDescricao.getText().toString().isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ReceitasActivity.this, "Por favor, preencha descrição.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ReceitasActivity.this, "Por favor, preencha a categoria.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ReceitasActivity.this, "Por favor, insira a data.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ReceitasActivity.this, "Por favor, insira o valor da receita.", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void recuperarReceitaTotal() {
        String idUser = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = databaseReference.child("usuarios").child(idUser);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizarReceita(double receita) {
        String idUser = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = databaseReference.child("usuarios").child(idUser);
        usuarioRef.child("receitaTotal").setValue(receita);
    }
}

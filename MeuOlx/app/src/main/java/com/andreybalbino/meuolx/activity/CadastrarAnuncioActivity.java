package com.andreybalbino.meuolx.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreybalbino.meuolx.R;
import com.andreybalbino.meuolx.helper.ConfiguracaoFirebase;
import com.andreybalbino.meuolx.helper.Permissoes;
import com.andreybalbino.meuolx.model.Anuncio;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class CadastrarAnuncioActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imagem1, imagem2, imagem3;
    private EditText campoTitulo, campoDescricao;
    private CurrencyEditText campoPreco;
    private MaskEditText campoTelefone;
    private Spinner spinnerEstado, spinnerCategoria;
    private List<String> listFotos = new ArrayList<>();
    private List<String> urlFotosFirebase = new ArrayList<>();
    private Button botaoCadastrar;
    private String[] permissoes = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
    private Anuncio anuncio;
    private StorageReference storageReference;
    private String idUsuarioLogado = ConfiguracaoFirebase.getIdUsuarioLogado();
    private int cont;
    private android.app.AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        inicializarComponentes();
        configurarSpinner();

        storageReference = ConfiguracaoFirebase.getStorageReference();

        Permissoes.validarPermissoes(permissoes, CadastrarAnuncioActivity.this, 1);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verificarCampos()) {
                    dialog = new SpotsDialog.Builder().setContext(CadastrarAnuncioActivity.this).setMessage("Salvando anuncio").setCancelable(false).build();
                    dialog.show();
                    configurarAnuncio();
                    salvarAnuncioStorage();
                }
            }
        });
    }

    private void inicializarComponentes() {
        imagem1 = findViewById(R.id.imageCadastro1);
        imagem2 = findViewById(R.id.imageCadastro2);
        imagem3 = findViewById(R.id.imageCadastro3);
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);
        campoDescricao = findViewById(R.id.editTextDecricao);
        campoTitulo = findViewById(R.id.editTitulo);
        campoPreco = findViewById(R.id.editTextPreco);
        campoTelefone = findViewById(R.id.editTextTelefone);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);
    }

    private boolean verificarCampos() {
        if (listFotos.size() != 0) {
            if (!campoTitulo.getText().toString().isEmpty()) {
                if (!(campoPreco.getRawValue() == 0)) {
                    if (!(campoTelefone.getText().toString().length() < 12)) {
                        if (!campoDescricao.getText().toString().isEmpty()) {
                            return true;
                        } else {
                            Toast.makeText(this, "Informe a descrição do produto", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } else {
                        Toast.makeText(this, "Preencha o campo telefone", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "Insira o preço da venda", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(this, "Informe o o título da venda", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Insira ao menos uma foto!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int permissao : grantResults) {
            if (permissao == getPackageManager().PERMISSION_DENIED) {
                alertMensagem();
            }
        }
    }

    private void alertMensagem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissão negada.");
        builder.setMessage("É necessário aceitar a permissão para continuar.");
        builder.setCancelable(false);
        builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri urlFoto = data.getData();
            String caminhoFoto = urlFoto.toString();

            if (requestCode == 1) {
                imagem1.setImageURI(urlFoto);
            } else if (requestCode == 2) {
                imagem2.setImageURI(urlFoto);
            } else {
                imagem3.setImageURI(urlFoto);
            }
            listFotos.add(caminhoFoto);
        }
    }

    private void configurarImagem(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageCadastro1:
                configurarImagem(1);
                break;
            case R.id.imageCadastro2:
                configurarImagem(2);
                break;
            case R.id.imageCadastro3:
                configurarImagem(3);
                break;
        }
    }

    private void configurarAnuncio() {
        anuncio = new Anuncio();
        anuncio.setEstado(spinnerEstado.getSelectedItem().toString());
        anuncio.setCategoria(spinnerCategoria.getSelectedItem().toString());
        anuncio.setTitulo(campoTitulo.getText().toString());
        anuncio.setValor(campoPreco.getText().toString());
        anuncio.setTelefone(campoTelefone.getText().toString());
        anuncio.setDescricao(campoDescricao.getText().toString());
    }

    private void configurarSpinner() {
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        String[] categorias = getResources().getStringArray(R.array.categorias);
        ArrayAdapter adapterCategorias = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapterCategorias);
    }

    private void salvarAnuncioStorage() {

        StorageReference anunciosStorage = null;
        Uri urlFoto = null;
        UploadTask uploadTask = null;
        for (cont = 0; cont < listFotos.size(); cont++) {
            anunciosStorage = storageReference.child("imagens").child("anuncios").child(idUsuarioLogado).child(anuncio.getId()).child("imagem" + cont + ".jpeg");
            urlFoto = Uri.parse(listFotos.get(cont));
            uploadTask = anunciosStorage.putFile(urlFoto);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (cont == listFotos.size()) {
                        Uri url = taskSnapshot.getDownloadUrl();
                        String caminhoFoto = url.toString();
                        urlFotosFirebase.add(caminhoFoto);
                        anuncio.setImagens(urlFotosFirebase);
                        anuncio.salvar();
                        dialog.dismiss();
                        finish();
                    }
                }
            });
        }
    }
}

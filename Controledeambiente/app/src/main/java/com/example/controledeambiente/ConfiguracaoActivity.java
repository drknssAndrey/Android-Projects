package com.example.controledeambiente;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.controledeambiente.config.ConfiguracaoFirebase;
import com.example.controledeambiente.helper.Permissao;
import com.example.controledeambiente.helper.UsuarioFirebase;
import com.example.controledeambiente.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracaoActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA
    };
    private ImageButton botaoCamera, botaoGaleria;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALERIA = 200;
    private CircleImageView circleImageViewPerfil;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private AlertDialog dialog;
    private EditText editNome;
    private ImageView botaoAlterarNome;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        Toolbar toolbar = findViewById(R.id.toolbarID);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Configuracoes iniciais
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //validar permissoes
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        botaoCamera = findViewById(R.id.imageButtonCamera);
        botaoGaleria = findViewById(R.id.imageButtonGaleria);
        circleImageViewPerfil = findViewById(R.id.circleImageViewFotoPerfil);
        editNome = findViewById(R.id.editPerfilNome);
        botaoAlterarNome = findViewById(R.id.btAlterarNome);

        //recuperar dados do usuário
        final FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        Uri foto = usuario.getPhotoUrl();
        if (foto != null) {
            Glide.with(ConfiguracaoActivity.this).load(foto).into(circleImageViewPerfil);
        } else {
            circleImageViewPerfil.setImageResource(R.drawable.padrao);
        }
        editNome.setText(usuario.getDisplayName());
        botaoCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_CAMERA);
                }
            }
        });

        botaoGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, SELECAO_GALERIA);
                }
            }
        });
        botaoAlterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editNome.getText().toString().isEmpty()) {
                    if (!editNome.getText().toString().equals(usuario.getDisplayName())) {
                        String nomeUsuario = editNome.getText().toString();
                        boolean retorno = UsuarioFirebase.atualizaNomeUsuario(nomeUsuario);
                        if (retorno) {
                            usuarioLogado.setNome(nomeUsuario);
                            usuarioLogado.atualizar();
                            Toast.makeText(ConfiguracaoActivity.this, "Nome de usuário alterado", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ConfiguracaoActivity.this, "Esse nome já está configurado", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(ConfiguracaoActivity.this, "O campo nome não pode estar vazio", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap image = null;

            try {
                switch (requestCode) {
                    case SELECAO_CAMERA:
                        image = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (image != null) {
                    telaCarregamento();
                    circleImageViewPerfil.setImageBitmap(image);

                    //recuperar os dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //salvar imagem no firebase
                    StorageReference imagemRef = storageReference.child("imagens").child("perfil").child(identificadorUsuario + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.cancel();
                            Toast.makeText(ConfiguracaoActivity.this, "Erro ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.cancel();
                            Toast.makeText(ConfiguracaoActivity.this, "Sucesso ao fazer upload da imagem", Toast.LENGTH_SHORT).show();
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri url = uri;
                                    atualizarFotoUsuario(url);

                                }
                            });
                        }
                    });


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void telaCarregamento() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Configurando foto, aguarde");
        alert.setView(R.layout.carregamento);
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.show();
    }

    private void atualizarFotoUsuario(Uri url) {
        boolean retorno = UsuarioFirebase.atualizaFotoUsuario(url);
        if (retorno){
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();
            Toast.makeText(ConfiguracaoActivity.this, "Foto alterada com sucesso", Toast.LENGTH_SHORT).show();
        }
    }
}

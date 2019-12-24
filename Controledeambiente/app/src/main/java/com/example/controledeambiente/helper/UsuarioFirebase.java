package com.example.controledeambiente.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.controledeambiente.R;
import com.example.controledeambiente.config.ConfiguracaoFirebase;
import com.example.controledeambiente.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

    public static String getIdentificadorUsuario() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        String email = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(email);

        return idUsuario;
    }

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        return auth.getCurrentUser();
    }
    public static boolean atualizaNomeUsuario(String nome) {
        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(nome).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    }

                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizaFotoUsuario(Uri url) {
        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(url).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar foto de perfil");
                    }

                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Usuario getDadosUsuarioLogado(){
        FirebaseUser firebaseAuth = getUsuarioAtual();
        Usuario usuario = new Usuario();
        usuario.setEmail(firebaseAuth.getEmail());
        usuario.setNome(firebaseAuth.getDisplayName());
        if (firebaseAuth.getPhotoUrl()==null){
            usuario.setFoto("");
        }else{
            usuario.setFoto(firebaseAuth.getPhotoUrl().toString());
        }
        return usuario;
    }
}

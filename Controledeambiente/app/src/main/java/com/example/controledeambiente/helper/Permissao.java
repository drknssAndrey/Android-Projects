package com.example.controledeambiente.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {
    public static Boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode ){
        if (Build.VERSION.SDK_INT>=23){
            List<String> listaPermissoes = new ArrayList<>();

            //Percorre as permissoes passadas, verificando uma a uma
            //se ja tem a permissao liberada
            for (String permissao: permissoes){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!temPermissao) {
                    listaPermissoes.add(permissao);
                }
            }
            //caso a lista esteja vazia, nao é necessario solicitar permissao
            if (listaPermissoes.isEmpty()){
                return true;
            }
            String [] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);
            //solicita permissao
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);

        }

        return true;
    }
}

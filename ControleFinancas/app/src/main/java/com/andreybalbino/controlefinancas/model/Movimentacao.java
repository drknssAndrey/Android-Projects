package com.andreybalbino.controlefinancas.model;

import com.andreybalbino.controlefinancas.config.ConfiguracaoFirebase;
import com.andreybalbino.controlefinancas.helper.Base64Custom;
import com.andreybalbino.controlefinancas.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {
    private String data;
    private String categoria;
    private String descricao;
    private String tipo;
    private double valor;
    private String key;

    public Movimentacao() {
    }
    public void salvar(String dataEscolhida){
        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        String idUser = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());

        String mesAno = DateCustom.mesAnoDataEscolhida(dataEscolhida);

        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        databaseReference.child("movimentacao").child(idUser).child(mesAno).push().setValue(this);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
